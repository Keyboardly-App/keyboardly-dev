package app.keyboardly.dev.keyboard.keypad

import android.app.Activity
import android.content.Context
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import app.keyboardly.dev.keyboard.layouts.KeyboardLayout
import app.keyboardly.dev.keyboard.manager.KeyboardManager.KeyboardListener
import androidx.recyclerview.widget.RecyclerView
import app.keyboardly.lib.ChipGroupCallback
import app.keyboardly.lib.InputPresenter
import app.keyboardly.lib.KeyboardActionDependency
import app.keyboardly.lib.KeyboardActionView
import app.keyboardly.lib.navigation.NavigationCallback
import app.keyboardly.lib.navigation.NavigationMenuAdapter
import app.keyboardly.lib.navigation.NavigationMenuModel
import app.keyboardly.lib.reflector.DynamicFeature
import com.google.android.material.chip.Chip
import app.keyboardly.dev.keyboard.utils.DynamicModuleHelper
import app.keyboardly.dev.R
import app.keyboardly.dev.keyboard.di.BaseComponent
import app.keyboardly.dev.keyboard.di.DaggerBaseComponent
import app.keyboardly.dev.keyboard.manager.KeyboardManager
import app.keyboardly.dev.keyboard.utils.InstallFeatureCallback
import timber.log.Timber
import java.util.HashMap
open class KokoKeyboardView : ExpandableLayout {
    private lateinit var backButton: ImageButton
    private var subMenuAddOnActive: Boolean = false
    private lateinit var adapterNavigation: NavigationMenuAdapter
    private lateinit var currentInputConnection: InputConnection
    private lateinit var currentEditorInfo: EditorInfo
    private lateinit var navigationView: RecyclerView

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
        currentInputConnection = inputConnection
        val keyboard = generateCorrectKeyboard(type, inputConnection)
        keyboards[field] = keyboard
        keyboards[field]!!.registerListener(keyboardListener)
        field.onFocusChangeListener = OnFocusChangeListener { v: View?, hasFocus: Boolean ->
            if (hasFocus) {
                val imm =
                    context.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(field.windowToken, 0)
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
        field.setOnClickListener { v: View? ->
            if (!isExpanded) {
                expand()
            }
        }
    }

    fun unregisterEditText(field: EditText?) {
        keyboards.remove(field)
    }

    private fun initDependency(){
        dependency = object: KeyboardActionDependency {
            override fun getContext(): Context = context

            override fun getEditTextInput(): EditText {
                // TODO: add edittext
                return EditText(context)
            }

            override fun getCurrentInputConnection(): InputConnection {
                return this@KokoKeyboardView.currentInputConnection
            }

            override fun getCurrentEditorInfo(): EditorInfo {
                return this@KokoKeyboardView.currentEditorInfo
            }

            override fun getRecyclerView(): RecyclerView {
                // TODO: fix this
                return RecyclerView(context)
            }

            override fun viewKeyboardNavigation() {

            }

            override fun getKeyboardHeight(): Int {
                return 60
            }

            override fun viewDefaultKeyboard() {

            }

            override fun viewAddOnNavigation() {

            }

            override fun viewLayoutAction() {

            }

            override fun viewInputMode(active: Boolean) {

            }

            override fun commitText(text: String) {

            }

            override fun setTextWatcher(textWatcher: TextWatcher) {

            }

            override fun setActionView(view: KeyboardActionView) {

            }

            override fun setActionView(view: View?) {

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

            }

            override fun showRecyclerViewOptions(onViewReady: KeyboardActionDependency.OnViewReady) {

            }

            override fun showFloatingRecyclerView(onViewReady: KeyboardActionDependency.OnViewReady) {

            }

            override fun showMessageView(onViewMessageReady: KeyboardActionDependency.OnViewMessageReady) {

            }

            override fun setNavigationCallback(navigationCallback: NavigationCallback) {

            }

            override fun setNavigationMenu(list: MutableList<NavigationMenuModel>) {

            }

        }
    }

    private fun generateCorrectKeyboard(type: Int, ic: InputConnection): KeyboardLayout {
        val keypad = KeyboardLayout(context, KeyboardManager(ic))
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
        navigationView = view.findViewById(R.id.navigation)
        backButton = view.findViewById(R.id.backButton)
        val defaultMenuList = defaultNavigation()

        backButton.setOnClickListener {
            Timber.i("submenu = $subMenuAddOnActive")
            if (subMenuAddOnActive){
                viewDefaultNavigation(defaultMenuList)
            }
        }

        adapterNavigation = NavigationMenuAdapter(defaultMenuList,object: NavigationCallback{
            override fun onClickMenu(data: NavigationMenuModel) {
                navigationOnClick(data)
                Log.d(TAG, "onClickMenu: data=${data.name}")
            }
        })
        navigationView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL,
                false)
            adapter = adapterNavigation
        }
    }

    private fun viewDefaultNavigation(defaultMenuList: MutableList<NavigationMenuModel>) {
        adapterNavigation.updateList(defaultMenuList)
        backButton.visibility = GONE
        subMenuAddOnActive = false
    }

    private fun defaultNavigation(): MutableList<NavigationMenuModel> {
        val model = NavigationMenuModel(
            1,
            R.string.nav_sample,
            R.drawable.ic_round_local_activity_24,
            true,
            featurePackageId = "app.keyboardly.sample",
            featureNameId = "sample",
            nameString = "Sample"
        )

        val mutableList = mutableListOf<NavigationMenuModel>()
        mutableList.add(model)
        return mutableList
    }

    private fun navigationOnClick(data: NavigationMenuModel) {
        val featureName = data.featureNameId
        if (featureName != null) {
            // check is feature installed
            if (moduleHelper.isFeatureInstalled(featureName)) {
                data.featurePackageId?.let {
                    // open sub menu
                    openSubMenuFeature(it)
                }
            } else {
                toast("Feature ${data.nameString} not installed yet.")
            }
        } else {
            Timber.e("Error. Something wrong on the feature.")
        }
    }

    private fun toast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }

    private fun openSubMenuFeature(featureNameId: String) {
        try {
            // load add on
            val dynamicModule: DynamicFeature? = moduleHelper.initialize(featureNameId)
            if (dynamicModule != null) {
                // get submenu is exist
                val subsMenu = dynamicModule.getSubMenus()
                if (subsMenu.isNotEmpty()) {
                    // show submenu on keyboard
                    switchAddOnNavigationView(subsMenu)
                } else {
                    // if submenu empty, load the view
                    // get view from add on
                    val view = dynamicModule.getView()
                    Timber.d("view=$view")
                    if (view != null) {
                        dependency?.setActionView(view)
                    } else {
                        Timber.e("nothing to do.")
                    }
                }
            } else {
                Timber.e("dynamic module null")
                toast("Failed open feature, something wrong.")
            }
        } catch (e: Exception) {
            e.printStackTrace()
            toast("Failed open feature.\ne:${e.message}")
        }
    }

    private fun switchAddOnNavigationView(list: MutableList<NavigationMenuModel>) {
        subMenuAddOnActive = true
        backButton.visibility = View.VISIBLE
        adapterNavigation.updateList(list)
        navigationView.scrollToPosition(0)
        // set visibility gone for button more key when submenu active.
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

