package app.keyboardly.dev.keyboard.keypad

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.os.IBinder
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.WindowManager
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import androidx.core.view.isVisible
import app.keyboardly.dev.R
import app.keyboardly.dev.databinding.DefaultKeyboardBinding
import app.keyboardly.dev.databinding.QwertyKeypadLowercaseBinding
import app.keyboardly.dev.databinding.QwertyKeypadUppercaseBinding
import app.keyboardly.dev.databinding.QwertyKeypadWithNumBinding
import app.keyboardly.dev.keyboard.di.BaseComponent
import app.keyboardly.dev.keyboard.di.DaggerBaseComponent
import app.keyboardly.dev.keyboard.layouts.KeyboardLayout
import app.keyboardly.dev.keyboard.manager.KeyboardManager
import app.keyboardly.dev.keyboard.manager.KeyboardManager.KEYCODE_BACKSPACE
import app.keyboardly.dev.keyboard.manager.KeyboardManager.KeyboardListener
import app.keyboardly.dev.keyboard.utils.DynamicModuleHelper
import app.keyboardly.dev.keyboard.utils.InstallFeatureCallback
import app.keyboardly.lib.*
import app.keyboardly.lib.helper.*
import app.keyboardly.lib.navigation.NavigationCallback
import app.keyboardly.lib.navigation.NavigationMenuModel
import com.google.android.material.chip.Chip
import timber.log.Timber

/**
 * KokokeyboardView
 * original source :
 * https://github.com/RowlandOti/KokoKeyboard
 */
open class KokoKeyboardView : ExpandableLayout {
    private var defaultHeight: Int = 0
    private lateinit var mOptionsDialog: AlertDialog
    private var currentInputConnection: InputConnection? = null
    private lateinit var field: EditText
    private lateinit var keyboard: KeyboardLayout
    private var frameKeyboard: FrameLayout? = null
    private lateinit var keyboardManager: KeyboardManager
    private lateinit var container: KeyboardActionContainer
    private lateinit var currentEditorInfo: EditorInfo

    private var activeEditField: EditText? = null
    private val keyboards = HashMap<EditText?, KeyboardLayout>()
    private var keyboardListener: KeyboardListener? = null

    private var moduleHelper: DynamicModuleHelper =
        DynamicModuleHelper(context, object : InstallFeatureCallback {
            override fun onError(message: String?) {
//                toastLong("E: $message")
                Timber.e("message=$message")
            }

            override fun onSuccess(modules: List<String>) {
                Timber.i("success load=${modules.joinToString("/")}")
            }

            override fun onLoading(loading: Boolean) {}
        })

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init() {
        keyboardListener = object : KeyboardListener {
            override fun characterClicked(c: Char) {}
            override fun specialKeyClicked(keyCode: Int) {
                when (keyCode) {
                    KeyboardManager.KEYCODE_BACKSPACE -> {}
                    KeyboardManager.KEYCODE_SPACE -> {}
                    KeyboardManager.KEYCODE_ENTER -> {}
                    KeyboardManager.KEYCODE_SHIFT -> shiftKeyboard()
                    KeyboardManager.KEYCODE_DONE -> collapse()
                    KeyboardManager.KEYCODE_SYMBOL -> setKeypadSymbolNumber()
                    KeyboardManager.KEYCODE_ALPHABET -> setKeypadAlphabet()
                }
            }

            override fun onButtonClicked(id: Int) {
                Timber.i("clicked id=$id")
                when (id) {
                    R.id.logoButton -> {
                        container.onLogoButtonClicked()
                    }
                    R.id.backButton -> {
                        container.onBackButtonClicked()
                    }
                    else -> {
                        Timber.w("unhandle clicked id=$id")
                    }
                }
            }
        }

        initDependency()
    }

    private var isLowerCase = true

    private fun shiftKeyboard() {
        Timber.i("islower case=$isLowerCase")
        isLowerCase = !isLowerCase

        val keyboardViewResId = if (isLowerCase) R.layout.qwerty_keypad_lowercase
            else R.layout.qwerty_keypad_uppercase
        updateKeypad(keyboardViewResId)
        Timber.i("updated=$isLowerCase")
    }

    fun setKeypadNumber(){
        updateKeypad(R.layout.keypad_num)
    }

    fun setKeypadSymbolNumber(){
        updateKeypad(R.layout.keypad_num_with_symbol)
    }

    fun setKeypadAlphabet(){
        updateKeypad(R.layout.qwerty_keypad_lowercase)
    }

    private fun updateKeypad(keyboardViewResId: Int) {
        val keyboardView = LayoutInflater.from(context).inflate(keyboardViewResId, null)
        frameKeyboard?.removeAllViews()
        frameKeyboard?.addView(keyboardView)
        keyboards[field]?.setKeypadClickListener(frameKeyboard)
    }

    fun registerEditText(type: Int, field: EditText) {
        if (!field.isEnabled) {
            return
        }
        this.field = field
        if (type==InputType.TYPE_CLASS_TEXT) {
            field.setRawInputType(InputType.TYPE_CLASS_TEXT)
        }
        field.isSoundEffectsEnabled = false
        field.isLongClickable = false
        field.showSoftInputOnFocus = false
        val editorInfo = EditorInfo()
        currentEditorInfo = editorInfo
        currentInputConnection = field.onCreateInputConnection(editorInfo)
        loadKeyboard(type, field)
        field.setOnClickListener {
            if (!isExpanded) {
                expand()
            }
        }
    }

    private fun loadKeyboard(type: Int, field: EditText) {
        keyboard = generateCorrectKeyboard(type, currentInputConnection!!)
        keyboards[field] = keyboard
        keyboards[field]?.registerListener(keyboardListener)
        val focused = field.isVisible
//        Timber.d("focused="+focused)
        if (focused) {
            hideSoftKeyboard(field)
            activeEditField = field
            updateKeyboard()
        }
        field.onFocusChangeListener = OnFocusChangeListener { _: View?, hasFocus: Boolean ->
            Timber.i("field=${field.id} | has focus=$hasFocus")
            if (hasFocus) {
                hideSoftKeyboard(field)
                activeEditField = field
                updateKeyboard()
            } else {
                if (isExpanded) {
                    for (editText in keyboards.keys) {
                        if (editText!!.hasFocus()) {
                            return@OnFocusChangeListener
                        }
                    }
    //                    collapse()
                }
            }
        }

    }

    private fun updateKeyboard() {
        removeAllViews()
        val keyboard1 = keyboards[activeEditField]
        addView(keyboard1)
        if (!isExpanded) {
            expand()
        }
    }

    private fun hideSoftKeyboard(field: EditText) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(field.windowToken, 0)
    }


    open fun setCustomInputConnection(inputConnection: InputConnection) {
        keyboardManager.setCustomInputConnection(inputConnection)
    }

    private fun initDependency(){
        dependency = object: KeyboardActionDependency {
            override fun getContext(): Context = context

            override fun getEditTextInput(): EditText {
                return container.mEditField
            }

            override fun getCurrentInputConnection(): InputConnection {
                return keyboardManager.inputConnection
            }

            override fun getCurrentEditorInfo(): EditorInfo {
                return currentEditorInfo
            }

            override fun viewKeyboardNavigation() {
                keyboardManager.resetInputConnection()
                container.viewNavigation()
            }

            override fun getKeyboardHeight(): Int {
                return 0 // not used on here
            }

            override fun viewDefaultKeyboard() {
                container.viewDefault()
            }

            override fun viewAddOnNavigation() {
                keyboardManager.resetInputConnection()
                setKeypadAlphabet()
                container.viewAddOnNavigation()
            }

            override fun viewLayoutAction() {
                container.viewLayoutAction()
            }

            override fun commitText(text: String) {
                currentInputConnection?.commitText(text, text.length)
            }

            override fun commitTextToApp(text: String) {
                currentInputConnection?.commitText(text, text.length)
            }

            override fun setTextWatcher(textWatcher: TextWatcher) {
                container.textWatcher = textWatcher
            }

            override fun setActionView(view: KeyboardActionView) {
                container.setActionView(view.getView())
            }

            override fun setActionView(view: View?) {
                container.setActionView(view)
            }

            override fun setTopActionView(view: KeyboardActionView) {
                container.setTopActionView(view.getView())
            }

            override fun setTopActionView(view: View) {
                container.setTopActionView(view)
            }

            override fun setKeyboardHeight(percent: Int) {
                updateKeyboardViewRealtime(percent)
            }

            override fun doBackSpace(all: Boolean?) {
                if (all!=null && all){
                    keyboardManager.clearAll()
                } else {
                    keyboardManager.onKeyStroke(KEYCODE_BACKSPACE, false)
                }
            }

            override fun showChipOptions(
                list: MutableList<Chip>,
                callback: ChipGroupCallBack,
                editText: EditText?
            ) {
                container.showChipOptions(list, callback, editText)
            }

            override fun showDatePicker(
                editText: EditText?,
                inputPresenter: InputPresenter?,
                readableMode: Boolean?
            ) {
                container.showDatePicker(editText, inputPresenter, readableMode)
            }

            override fun loadingOnInput(loading: Boolean) {
                container.loadingOnInput(loading)
            }

            override fun showTitleAboveList(show: Boolean, title: String?, asFooter: Boolean?) {
                container.showTitle(show, title, asFooter)
            }

            override fun loadingMain(loading: Boolean) {
                container.loadingMain(loading)
            }

            override fun requestInput(
                editTextTarget: EditText?,
                inputPresenter: InputPresenter?,
                enableInput: Boolean?,
                longInput: Boolean?,
                hint: Int?,
                inputType: Int?,
                textWatcher: TextWatcher?,
                onCloseSearch: () -> Unit?,
                inputOnTopActionView: Boolean?,
                isCurrency: Boolean?
            ) {
                if (longInput!=null && longInput){
                    container.requestInputLong(editTextTarget,inputPresenter,hint, isCurrency)
                } else {
                    container.requestInput(editTextTarget,enableInput,inputPresenter,hint,
                        inputType, textWatcher, onCloseSearch, inputOnTopActionView, isCurrency)
                }
            }

            override fun showRecyclerViewOptions(onViewReady: OnViewReady) {
                container.viewList(onViewReady)
            }

            override fun showTopRecyclerView(onViewReady: OnViewReady, inputMode: Boolean?) {
                container.viewFloatingRv(onViewReady, inputMode)
            }

            override fun showMessageView(onViewMessageReady: OnViewMessage) {
                container.showMessageView(onViewMessageReady)
            }

            override fun setNavigationCallback(navigationCallback: NavigationCallback) {
                container.updateNavigationCallBack(navigationCallback)
            }

            override fun setNavigationMenu(list: MutableList<NavigationMenuModel>) {
                container.switchAddOnNavigationView(list)
            }

            override fun isDarkMode(): Boolean {
                val searchAttr = intArrayOf(app.keyboardly.style.R.attr.darkMode)
                @SuppressLint("ResourceType") val attrs =
                    getContext().obtainStyledAttributes(null, searchAttr)
                val isDarkTheme = attrs.getBoolean(0, false)
                attrs.recycle()
                return isDarkTheme
            }

            override fun isBorderMode(): Boolean {
                val searchAttr = intArrayOf(app.keyboardly.style.R.attr.borderMode)
                @SuppressLint("ResourceType") val attrs =
                    getContext().obtainStyledAttributes(null, searchAttr)
                val isBorderMode = attrs.getBoolean(0, false)
                attrs.recycle()
                return isBorderMode
            }

            override fun showDialog(dialog: AlertDialog) {
                showOptionDialog(dialog)
            }

            override fun getDialogTheme(): Context {
                return getContext()
            }

            override fun hideTopView() {
                container.hideTopView()
            }

            override fun resetInputConnection() {
                keyboardManager.resetInputConnection()
            }

        }
    }

    open fun showOptionDialog(dialog: AlertDialog) {
        val windowToken: IBinder? = frameKeyboard?.rootView?.windowToken
        if (windowToken == null) {
            Timber.e("window token null")
            return
        }
        val window = dialog.window
        val lp = window!!.attributes
        lp.token = windowToken
        lp.type = WindowManager.LayoutParams.TYPE_APPLICATION_ATTACHED_DIALOG
        window.attributes = lp
        window.addFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM)
        mOptionsDialog = dialog
        dialog.show()
    }
    private fun generateCorrectKeyboard(type: Int, ic: InputConnection): KeyboardLayout {
        keyboardManager = KeyboardManager(ic)
        var keypad = KeyboardLayout(context, keyboardManager)
        val view: View
        return when (type) {
            INPUT_TYPE_QWERTY -> {
                keypad = initView()
                keypad
            }
            INPUT_TYPE_QWERTY_NUM -> {
                view = QwertyKeypadWithNumBinding.inflate(LayoutInflater.from(context)).root
                keypad.addView(view)
                keypad
            }
            else -> {
                view = QwertyKeypadLowercaseBinding.inflate(LayoutInflater.from(context)).root
                keypad.addView(view)
                keypad
            }
        }
    }

    private fun initView(isLowerCase: Boolean?=true): KeyboardLayout{
        val keypad = KeyboardLayout(context, keyboardManager)

        val layoutInflater = LayoutInflater.from(context)
        val view = DefaultKeyboardBinding.inflate(layoutInflater).root
        container = KeyboardActionContainer(view, moduleHelper, this)

        val binding = if (isLowerCase!!){
            QwertyKeypadLowercaseBinding.inflate(layoutInflater)
        } else QwertyKeypadUppercaseBinding.inflate(layoutInflater)


        frameKeyboard = view.findViewById(R.id.keyboardView)
        frameKeyboard?.addView(binding.root)

        keypad.addView(view)
        defaultHeight = height
        return keypad
    }

    fun resetHeight(){
        Timber.d("default=$defaultHeight")
        if (defaultHeight>0){
            updateKeyboardViewRealtime(100,defaultHeight)
        }
    }

    fun updateKeyboardViewRealtime(percent: Int, heightParam:Int?=null) {
        val headerHeight = container.headerShadowAction.height
        if (defaultHeight==0){
            defaultHeight = height
        }
        val constraintLayoutParams = frameKeyboard?.layoutParams
        val actionParams = container.frame.layoutParams

        Timber.d("height=$defaultHeight | $height | param=$heightParam")
        if (percent > 0 && constraintLayoutParams!=null) {
            val percentValue = percent.toDouble() / 100.0
            val heightWithPercent = percentValue * (heightParam?:defaultHeight)
            Timber.d("height=$heightWithPercent")
            constraintLayoutParams.height = heightWithPercent.toInt() - headerHeight
            actionParams.height = (heightWithPercent.toInt()+ headerHeight)
            frameKeyboard?.layoutParams = constraintLayoutParams
            container.frame.layoutParams = actionParams
        } else {
            Timber.e("height total invalid")
        }
    }

    companion object {
        var dependency: KeyboardActionDependency? = null
        const val INPUT_TYPE_QWERTY_NUM = -10
        const val INPUT_TYPE_QWERTY = -20

        lateinit var component: BaseComponent


        fun rebuildBaseComponent(featureNameId: String){
            dependency?.let {
                component = DaggerBaseComponent.builder()
                    .keyboardActionDependency(it)
                    .featureNameId(featureNameId)
                    .build()
            }
        }

    }
}

