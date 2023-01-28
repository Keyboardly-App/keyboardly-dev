package app.keyboardly.lib

import android.content.Context
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import app.keyboardly.lib.navigation.NavigationCallback
import app.keyboardly.lib.navigation.NavigationMenuModel

/**
 * Created by zainal on 6/27/22 - 4:09 PM
 */
interface KeyboardActionDependency {

    /**
     * for get context
     */
    fun getContext(): Context

    /**
     * for getting main EditText while on input mode
     * */
    fun getEditTextInput(): EditText

    /**
     * get current input connection
     * */
    fun getCurrentInputConnection(): InputConnection

    /**
     * get current editor info from IME service
     * */
    fun getCurrentEditorInfo(): EditorInfo

    /**
     * get recyclerView from layout if appeared
     */
    fun getRecyclerView(): RecyclerView

    /**
     * view default keyboard navigation
     */
    fun viewKeyboardNavigation()

    /**
     * get keyboard height
     */
    fun getKeyboardHeight() : Int

    /**
     * view default keyboard navigation
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
     * view to input mode
     */
    fun viewInputMode(active: Boolean)

    /**
     * commit string data to editor outside keyboard through IME service
     *
     * @param text : string data to commit
     */
    fun commitText(text: String)

    /**
     * add text watcher to main input edittext
     */
    fun setTextWatcher(textWatcher: TextWatcher)

    /**
     * set view on keyboard layout.
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
    fun showChipOptions(list: MutableList<Chip>, callback: ChipGroupCallback, editText: EditText?=null )

    /**
     * show date picker from keyboard
     */
    fun showDatePicker(editText: EditText?=null, inputPresenter: InputPresenter?, readableMode:Boolean?=true)

    /**
     * loading view on submit icon default input
     */
    fun loadingOnInput(loading: Boolean)

    /**
     * show title above default Recycler view
     */
    fun showTitleAboveList(show: Boolean, title:String?=null)

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
     */
    fun requestInput (
        editTextTarget: EditText?=null,
        inputPresenter: InputPresenter?=null,
        enableInput: Boolean?=true,
        longInput: Boolean?=false,
        hint: Int?=null,
        inputType: Int?=null,
        textWatcher: TextWatcher?=null,
        onCloseSearch: ()->Unit?={}
    )

    /**
     * show recyclerview options
     * @param onViewReady : to handle callback when recyclerview ready
     */
    fun showRecyclerViewOptions(onViewReady: OnViewReady)

    /**
     * show floating recyclerview options
     * @param onViewReady : to handle callback when recyclerview ready
     */
    fun showFloatingRecyclerView(onViewReady: OnViewReady)

    /**
     * show message to keyboard
     */
    fun showMessageView(onViewMessageReady: OnViewMessageReady)

    /**
     * to set callback navigation when submenu add on clicked.
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
    fun isDarkMode() : Boolean

    /**
     *  for check current keyboard border theme, is with border or not
     */
    fun isBorderMode() : Boolean

    /**
     * interface class for handle callback recycler view
     * */
    interface OnViewReady{
        fun onRecyclerViewReady(recyclerView: RecyclerView)
    }

    interface OnViewMessageReady{
        fun onTextViewReady(textView: TextView)
    }
}