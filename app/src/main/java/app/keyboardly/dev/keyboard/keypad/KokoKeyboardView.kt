package app.keyboardly.dev.keyboard.keypad

import android.app.Activity
import android.content.Context
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.FrameLayout
import androidx.recyclerview.widget.RecyclerView
import app.keyboardly.dev.R
import app.keyboardly.dev.keyboard.di.BaseComponent
import app.keyboardly.dev.keyboard.di.DaggerBaseComponent
import app.keyboardly.dev.keyboard.layouts.KeyboardLayout
import app.keyboardly.dev.keyboard.manager.KeyboardManager
import app.keyboardly.dev.keyboard.manager.KeyboardManager.KeyboardListener
import app.keyboardly.dev.keyboard.utils.DynamicModuleHelper
import app.keyboardly.dev.keyboard.utils.InstallFeatureCallback
import app.keyboardly.lib.ChipGroupCallback
import app.keyboardly.lib.InputPresenter
import app.keyboardly.lib.KeyboardActionDependency
import app.keyboardly.lib.KeyboardActionView
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

    var TAG = KeyboardManager::class.java.simpleName
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
        field.setRawInputType(InputType.TYPE_CLASS_TEXT)
        field.isSoundEffectsEnabled = false
        field.isLongClickable = false
        field.showSoftInputOnFocus = false
        val editorInfo = EditorInfo()
        currentEditorInfo = editorInfo
        currentInputConnection = field.onCreateInputConnection(editorInfo)
        keyboard = generateCorrectKeyboard(type, currentInputConnection!!)
        keyboards[field] = keyboard
        keyboards[field]?.registerListener(keyboardListener)
        field.onFocusChangeListener = OnFocusChangeListener { _: View?, hasFocus: Boolean ->
            Timber.i("has focus=$hasFocus")
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
        field.setOnClickListener {
            if (!isExpanded) {
                expand()
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

    fun hideSoftKeyboard(field: EditText) {
        val imm = context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(field.windowToken, 0)
    }

    fun unregisterEditText(field: EditText?) {
        keyboards.remove(field)
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

            override fun getRecyclerView(): RecyclerView {
                return container.recyclerView
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

            override fun viewInputMode(active: Boolean) {
                container.viewInputMode(active)
            }

            override fun commitText(text: String) {
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

            override fun showChipOptions(
                list: MutableList<Chip>,
                callback: ChipGroupCallback,
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

            override fun showTitleAboveList(show: Boolean, title: String?) {
                container.showTitle(show, title)
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
                onCloseSearch: () -> Unit?
            ) {
                if (longInput!=null && longInput){
                    container.requestInputLong(editTextTarget,inputPresenter,hint)
                } else {
                    container.requestInput(editTextTarget,enableInput,inputPresenter,hint, inputType, textWatcher, onCloseSearch)
                }
            }

            override fun showRecyclerViewOptions(onViewReady: KeyboardActionDependency.OnViewReady) {
                container.viewList(onViewReady)
            }

            override fun showFloatingRecyclerView(onViewReady: KeyboardActionDependency.OnViewReady) {
                container.viewFloatingRv(onViewReady)
            }

            override fun showMessageView(onViewMessageReady: KeyboardActionDependency.OnViewMessageReady) {
                container.showMessageView(onViewMessageReady)
            }

            override fun setNavigationCallback(navigationCallback: NavigationCallback) {
                container.updateNavigationCallBack(navigationCallback)
            }

            override fun setNavigationMenu(list: MutableList<NavigationMenuModel>) {
                container.switchAddOnNavigationView(list)
            }

        }
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
                view = LayoutInflater.from(context)
                    .inflate(R.layout.qwerty_keypad_with_num, null)
                keypad.addView(view)
                keypad
            }
            else -> {
                view = LayoutInflater.from(context)
                    .inflate(R.layout.qwerty_keypad_with_num, null)
                keypad.addView(view)
                keypad
            }
        }
    }

    private fun initView(isLowerCase: Boolean?=true): KeyboardLayout{
        val keypad = KeyboardLayout(context, keyboardManager)

        val view = LayoutInflater.from(context).inflate(R.layout.default_keyboard, null)
        container = KeyboardActionContainer(view, moduleHelper, this)
        val keyboardView = if (isLowerCase!!){
            LayoutInflater.from(context).inflate(R.layout.qwerty_keypad_lowercase, null)
        } else {
            LayoutInflater.from(context).inflate(R.layout.qwerty_keypad_uppercase, null)
        }
        frameKeyboard = view.findViewById(R.id.keyboardView)
        frameKeyboard?.addView(keyboardView)

        keypad.addView(view)
        return keypad
    }



    companion object {
        var dependency: KeyboardActionDependency? = null
        const val INPUT_TYPE_QWERTY_NUM = -10
        const val INPUT_TYPE_QWERTY = -20

        lateinit var component: BaseComponent


        fun rebuildBaseComponent(featureNameId: String){
            if (dependency == null){
                Timber.e("instance null")
            }
            dependency?.let {
                component = DaggerBaseComponent.builder()
                    .keyboardActionDependency(it)
                    .featureNameId(featureNameId)
                    .build()
            }
        }

    }
}

