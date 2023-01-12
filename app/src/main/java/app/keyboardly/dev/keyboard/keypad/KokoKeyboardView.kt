package app.keyboardly.dev.keyboard.keypad

import android.app.Activity
import android.content.Context
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnFocusChangeListener
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
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
                    KeyboardManager.KEYCODE_ADDON -> gotoAddOn()
                    KeyboardManager.KEYCODE_DONE -> collapse()
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
    private fun gotoAddOn() {
        Log.i(TAG, "key add on triggered")
    }

    fun registerEditText(type: Int, field: EditText) {
        if (!field.isEnabled) {
            return
        }
        field.setRawInputType(InputType.TYPE_CLASS_TEXT)
        field.isSoundEffectsEnabled = false
        field.isLongClickable = false
        field.showSoftInputOnFocus = false
        val editorInfo = EditorInfo()
        currentEditorInfo = editorInfo
        val inputConnection = field.onCreateInputConnection(editorInfo)
        val keyboard = generateCorrectKeyboard(type, inputConnection)
        keyboards[field] = keyboard
        keyboards[field]!!.registerListener(keyboardListener)
        field.onFocusChangeListener = OnFocusChangeListener { _: View?, hasFocus: Boolean ->
            if (hasFocus) {
                hideSoftKeyboard(field)
                activeEditField = field
                removeAllViews()
                val keyboard1 = keyboards[activeEditField]
                addView(keyboard1)
                if (!isExpanded) {
                    expand()
                }
            } else {
                if (isExpanded) {
                    for (editText in keyboards.keys) {
                        if (editText!!.hasFocus()) {
                            return@OnFocusChangeListener
                        }
                    }
                    collapse()
                }
            }
        }
        field.setOnClickListener {
            if (!isExpanded) {
                expand()
            }
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

    open fun getInputConnection(): InputConnection? {
        return keyboardManager.inputConnection
    }

    private fun initDependency(){
        dependency = object: KeyboardActionDependency {
            override fun getContext(): Context = context

            override fun getEditTextInput(): EditText {
                // TODO: add edittext
                return EditText(context)
            }

            override fun getCurrentInputConnection(): InputConnection {
                return keyboardManager.inputConnection
            }

            override fun getCurrentEditorInfo(): EditorInfo {
                return this@KokoKeyboardView.currentEditorInfo
            }

            override fun getRecyclerView(): RecyclerView {
                return container.recyclerView
            }

            override fun viewKeyboardNavigation() {
                keyboardManager.resetInputConnection()
                container.viewNavigation()
            }

            override fun getKeyboardHeight(): Int {
                return 60
            }

            override fun viewDefaultKeyboard() {
                container.viewDefault()
            }

            override fun viewAddOnNavigation() {
                keyboardManager.resetInputConnection()
                container.viewAddOnNavigation()
            }

            override fun viewLayoutAction() {
                container.viewLayoutAction()
            }

            override fun viewInputMode(active: Boolean) {
                container.viewInputMode(active)
            }

            override fun commitText(text: String) {
                getInputConnection()?.commitText(text, text.length)
            }

            override fun setTextWatcher(textWatcher: TextWatcher) {

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

            }

            override fun showDatePicker(editText: EditText?, inputPresenter: InputPresenter?) {

            }

            override fun loadingOnInput(loading: Boolean) {

            }

            override fun showTitleAboveList(loading: Boolean, title: String?) {

            }

            override fun loadingMain(loading: Boolean) {

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

            }

            override fun showFloatingRecyclerView(onViewReady: KeyboardActionDependency.OnViewReady) {

            }

            override fun showMessageView(onViewMessageReady: KeyboardActionDependency.OnViewMessageReady) {

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
        val keypad = KeyboardLayout(context, keyboardManager)
        val view: View
        return when (type) {
            INPUT_TYPE_QWERTY -> {
                view = LayoutInflater.from(context)
                    .inflate(R.layout.qwerty_keypad, null)
                keypad.addView(view)
                initView(view)
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

    private fun initView(view: View) {
        container = KeyboardActionContainer(view, moduleHelper, this)
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

