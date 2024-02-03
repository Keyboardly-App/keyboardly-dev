package app.keyboardly.lib

import android.app.AlertDialog
import android.content.Context
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.EditText
import app.keyboardly.lib.helper.*
import com.google.android.material.chip.Chip
import app.keyboardly.lib.navigation.NavigationCallback
import app.keyboardly.lib.navigation.NavigationMenuModel

/**
 * Created by zainal on 6/27/22 - 4:09 PM
 */
interface KeyboardActionDependency {

    /**
     * for get context of keyboard service / theme context
     */
    fun getContext(): Context

    /**
     * for getting current editText while on input mode (inside keyboard).
     * there is two type EditText, default and long,
     * default  : for short input and single line
     * long     : for long input and multiline
     * */
    fun getEditTextInput(): EditText

    /**
     * get current input connection
     * input connection is a way to communicate the keyboard with the input from other app
     * for example: when the keyboard is active on the chat app, the add on possible to send some
     * words in one tap rather than a single letter.
     * */
    fun getCurrentInputConnection(): InputConnection

    /**
     * get current editor info from IME service
     * */
    fun getCurrentEditorInfo(): EditorInfo

    /**
     * get keyboard height
     */
    fun getKeyboardHeight(): Int

    /**
     * commit string data to editor outside keyboard through IME service
     *
     * @param text : string data to commit
     */
    fun commitText(text: String)

    fun commitTextToApp(text: String)

    /**
     * loading view on submit icon default input
     */
    fun loadingOnInput(loading: Boolean)

    /**
     * view default keyboard navigation
     */
      fun viewKeyboardNavigation()

    /**
     * view default keyboard view
     */
    fun viewDefaultKeyboard()

    /**
     * view addon's default submenu navigation
     */
    fun viewAddOnNavigation()

    /**
     * move to view current KeyboardActionView
     */
    fun viewLayoutAction()

    /**
     * add text watcher to main input edittext
     */
    fun setTextWatcher(textWatcher: TextWatcher)

    /**
     * set view on keyboard layout like add on menu.
     * @param view : should have parent KeyboardActionView
     */
    fun setActionView(view: KeyboardActionView)

    /**
     * set view on keyboard layout.
     * @param view : should have parent KeyboardActionView
     */
    fun setActionView(view: View?)

    /**
     * show chip options from keyboard
     */
    fun showChipOptions(
        list: MutableList<Chip>,
        callback: ChipGroupCallBack,
        editText: EditText? = null
    )

    /**
     * show date picker from keyboard
     */
    fun showDatePicker(
        editText: EditText? = null,
        inputPresenter: InputPresenter?,
        readableMode: Boolean? = true
    )

    /**
     * show title above default Recycler view
     * @param show : for show the title or hide it
     * @param title : text for the title
     * @param asFooter : if true, this mean the title layout on bottom as footer and
     * on the above title is list from showTopRecyclerView()
     * default value is false
     */
    fun showTitleAboveList(show: Boolean, title: String? = null, asFooter: Boolean?=false)

    /**
     * loading view on main keyboard
     */
    fun loadingMain(loading: Boolean)

    /**
     * request input from keyboard inside a KeyboardActionView
     *
     * @param editTextTarget : edittext as target text after input or source hint, inputType
     * @param inputPresenter : class for handle done button event
     * @param enableInput : enabling or not main edittext
     * @param longInput : is long input or not. like write long address or not, something required
     * multiline input
     * @param hint : hint res Id for hint default edit text. if null, default hint from editTextTarget
     * @param inputType : input type to default edit text. if null input type from editTextTarget
     * @param textWatcher : for add listen text watcher
     * @param onCloseSearch : method that called when close on edit text main
     * @param inputOnTopActionView : flag for input on view above keyboard
     * @param isCurrency : flag for currency input
     */
    fun requestInput(
        editTextTarget: EditText? = null,
        inputPresenter: InputPresenter? = null,
        enableInput: Boolean? = true,
        longInput: Boolean? = false,
        hint: Int? = null,
        inputType: Int? = null,
        textWatcher: TextWatcher? = null,
        onCloseSearch: () -> Unit? = {},
        inputOnTopActionView: Boolean? = false,
        isCurrency: Boolean? = false
    )

    /**
     * show recyclerview options
     * @param onViewReady : to handle callback when recyclerview ready
     */
    fun showRecyclerViewOptions(onViewReady: OnViewReady)

    /**
     * show top recyclerview options
     * the position is above keyboard navigation, usually use when on input mode.
     * @param onViewReady : to handle callback when recyclerview ready
     *
     * @param inputMode : for flag is input mode or not, default value : true.
     * if true the keyboard layout will keep appear,
     * if not keyboard layout will hide.
     * For input mode false, it's possible combine the view with function showTitleAboveList()
     */
    fun showTopRecyclerView(onViewReady: OnViewReady, inputMode: Boolean?=true)


    /**
     * show message on main keyboard layout, it's relate with showRecyclerViewOptions for example
     * if search on the list, then the result is not found the message can be show up here
     */
    fun showMessageView(onViewMessageReady: OnViewMessage)

    /**
     * set callback navigation when navigation keyboard change to be submenu add on
     */
    fun setNavigationCallback(navigationCallback: NavigationCallback)

    /**
     * used for set list menu to keyboard navigation.
     * for example: when the add on installed & clicked, it should do login first before can access
     * all the menu of add on.
     */
    fun setNavigationMenu(list: MutableList<NavigationMenuModel>)

    /**
     *  for check current keyboard theme, is dark mode or not
     */
    fun isDarkMode(): Boolean

    /**
     *  for check current keyboard border theme, is with border or not
     */
    fun isBorderMode(): Boolean


    /**
     * function to show an alert dialog from keyboard
     */
    fun showDialog(
        dialog: AlertDialog
    )

    /**
     * get context for dialog theme
     */
    fun getDialogTheme() : Context

    /**
     * hide top view, view above navigation keyboard like top recyclerview, top actionview
     */
    fun hideTopView()

    fun resetInputConnection()
    /**
     * set top view on keyboard layout like add on menu.
     * @param view : should have parent KeyboardActionView
     */
    fun setTopActionView(view: KeyboardActionView)

    /**
     * set top view on keyboard layout like add on menu.
     * @param view : should have parent KeyboardActionView
     */
    fun setTopActionView(view: View)

    /**
     * Set height keyboard runtime.
     * default height keyboard is on setting menu will be default if on default menu / regular use
     * @param percent : value for the height. for example 150 to 150% height keyboard.
     * range the value should between : 50 - 300
     * see sample here:
     * https://github.com/Keyboardly-App/keyboardly-dev/blob/main/addon/sample/src/main/java/app/keyboardly/addon/sample/action/register/RegisterActionView.kt#L34]
     * always call at onCreate and OnResume function of actionView class add-on
     */
    fun setKeyboardHeight(percent: Int)

    /**
     * function for backspace on current active editor
     * @param all : if true, will delete all text on current editor
     * see sample here:
     * https://github.com/Keyboardly-App/keyboardly-dev/blob/main/addon/sample/src/main/java/app/keyboardly/addon/sample/action/register/RegisterActionView.kt#L88
     */
    fun doBackSpace(all: Boolean?=false)
}