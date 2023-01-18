package app.keyboardly.dev.keyboard.keypad.keyboardaction

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputConnection
import android.widget.EditText
import app.keyboardly.dev.R
import app.keyboardly.dev.keyboard.keypad.KokoKeyboardView
import app.keyboardly.dev.keyboard.utils.DynamicModuleHelper
import app.keyboardly.dev.keyboard.utils.gone
import app.keyboardly.dev.keyboard.utils.invisible
import app.keyboardly.dev.keyboard.utils.visible
import app.keyboardly.lib.InputPresenter
import timber.log.Timber

/**
 * Created by Zainal on 09/01/2023 - 19:18
 */
open class KeyboardInputManager(
    view: View,
    moduleHelper: DynamicModuleHelper,
    private val kokoKeyboardView: KokoKeyboardView
) : KeyboardNavigation(view, moduleHelper){

    private var mPresenter: InputPresenter? = null
    private var reInputFlag = false
    private var customEditorInfo: EditorInfo? = null

    init {
        /*mEditField.onFocusChangeListener = View.OnFocusChangeListener { _: View?, hasFocus: Boolean ->
            if (hasFocus) {
                kokoKeyboardView.hideSoftKeyboard(mEditField)
            }
        }
        mEditFieldLong.onFocusChangeListener = View.OnFocusChangeListener { _: View?, hasFocus: Boolean ->
            if (hasFocus) {
                kokoKeyboardView.hideSoftKeyboard(mEditFieldLong)
            }
        }*/
    }
    /**
     * default request default input text / number / email
     *
     * @param editTextTarget : target that inputted text will be placed
     * @param enableInput : sometime edittext input just for preview from options
     * @param presenter : for handle callback onDone button after input
     * @param hint : hint resource id for hint edit text, if null get hint from @param editTextTarget
     * @param inputType : input type for input edit text, if null get hint from @param editTextTarget
     */
    fun requestInput(
        editTextTarget: EditText?,
        enableInput: Boolean? = true,
        presenter: InputPresenter? = null,
        hint: Int? = null,
        inputType: Int? = null,
        textWatcher: TextWatcher? = null,
        onClose: () -> Unit? = { }
    ) {

        mPresenter = presenter
        enableInput?.apply {
            enableEditText(this)
        }
        requestInput(editTextTarget, hint, inputType, textWatcher, onClose)
    }

    /**
     * for requesting input long text, text that may required than 1 line
     */
    fun requestInputLong(
        editTextTarget: EditText?,
        presenter: InputPresenter?,
        hintResId: Int?
    ) {
        mPresenter = presenter
        viewInputMode(true)
        customEditorInfo = EditorInfo()
        parentEditMain.visibility = View.GONE
        parentEditMainLong.visibility = View.VISIBLE
        val customIC: InputConnection = mEditFieldLong.onCreateInputConnection(customEditorInfo)
        setCustomInput(customIC)

        val currentValue = editTextTarget?.text.toString()
        if (currentValue.isNotEmpty()) {
            mEditFieldLong.setText(currentValue)
            mEditFieldLong.setSelection(currentValue.length)
        }
        val inputType = editTextTarget?.inputType
        inputType?.apply {
            setKeyboardViewAsRequired(this, textWatcher)
        }
        if (hintResId != null) {
            setHint(hintResId)
        } else {
            val hintEdittext = editTextTarget?.hint
            if (hintEdittext != null) {
                setHint(hintEdittext)
            }
        }
        mEditFieldLong.isLongClickable = true
        mEditFieldLong.setTextIsSelectable(true)
        mEditFieldLong.requestFocus()
        doneEditButton.setImageResource(app.keyboardly.style.R.drawable.ic_done)
        doneEditButton.setOnClickListener {
            parentEditMain.visibility = View.VISIBLE
            parentEditMainLong.visibility = View.GONE
            if (mEditFieldLong.text != null) {
                val textInputed: String = mEditFieldLong.text.toString()
                if (mPresenter != null) {
                    mPresenter?.onDone(textInputed, editTextTarget)
                } else {
                    editTextTarget?.setText(textInputed)
                    viewLayoutAction()
                }
                mEditFieldLong.setText("")
            } else {
                viewLayoutAction()
            }
        }
        mEditFieldLong.setOnClickListener {
            if (recyclerView.isShown
                || messageOnFrame.isShown
            ) {
                viewInputMode(true)
                inputType?.apply {
                    setKeyboardViewAsRequired(this, textWatcher)
                }
            }
        }
    }

    private fun setKeyboardViewAsRequired(inputType: Int, textWatcher: TextWatcher?) {
        when(inputType){
            InputType.TYPE_CLASS_NUMBER -> kokoKeyboardView.setKeypadNumber()
            else -> kokoKeyboardView.setKeypadAlphabet()
        }

        Timber.i("added text watcher=$textWatcher")
        if (textWatcher!=null) {
            mEditField.addTextChangedListener(textWatcher)
        } else {
            mEditField.removeTextChangedListener(this@KeyboardInputManager.textWatcher)
            mEditFieldLong.removeTextChangedListener(this@KeyboardInputManager.textWatcher)
        }
        this.textWatcher = textWatcher
    }

    /**
     * newest model request input
     * @param editTextTarget : target EditText, also get data :
     * + hint from edittext target
     * + input type from edittext target
     */
    private fun requestInput(
        editTextTarget: EditText?, hintResId: Int?, inputType: Int?,
        textWatcher: TextWatcher? = null,
        onClose: () -> Unit?
    ) {
//        Timber.e("request input//%s", editTextTarget.getResources().getResourceName(editTextTarget.getId()));
        Timber.d("textwatcher=$textWatcher")

        if (editTextTarget == null && mPresenter == null) {
            throw IllegalAccessException("Edittext or InputPresenter can't be null")
        }

        viewInputMode(true)
        if (hintResId != null) {
            setHint(hintResId)
        } else {
            val hintFromEditTextTarget = editTextTarget?.hint
            Timber.d("hint edittext=$hintFromEditTextTarget")
            if (hintFromEditTextTarget != null) {
                setHint(hintFromEditTextTarget)
            }
        }

        val finalInputType = inputType ?: editTextTarget?.inputType
        customEditorInfo = EditorInfo()
        if (finalInputType != null) {
            customEditorInfo?.inputType = finalInputType
            setKeyboardViewAsRequired(finalInputType, textWatcher)
        } else {
            setKeyboardViewAsRequired(InputType.TYPE_CLASS_TEXT, textWatcher)
        }

        val customIC: InputConnection = mEditField.onCreateInputConnection(customEditorInfo)
        setCustomInput(customIC)

        if (editTextTarget != null) {
            val currentValue = editTextTarget.text.toString()
            if (currentValue.isNotEmpty() && !reInputFlag) {
                mEditField.setText(currentValue)
                mEditField.setSelection(currentValue.length)
            }
        }
        mEditField.isLongClickable = true
        mEditField.setTextIsSelectable(true)

        if (textWatcher != null) {
            doneEditButton.setImageResource(R.drawable.ic_round_close_24)
        } else {
            doneEditButton.setImageResource(app.keyboardly.style.R.drawable.ic_done)
        }

        doneEditButton.setOnClickListener {
            val text: Editable = mEditField.text
            Timber.i("==$text")
            if (text.isNotEmpty()) {
                if (textWatcher != null) {
                    mEditField.setText("")
                } else {
                    val string: String = mEditField.text.toString()
                    Timber.d("presenter=%s", mPresenter)
                    if (mPresenter != null) {
                        mPresenter?.onDone(string, editTextTarget)
                    } else {
                        editTextTarget?.apply {
                            clearFocus()
                            error = null
                            setText(string)
                        }
                        viewLayoutAction()
                    }
                }
            } else {
                if (textWatcher != null) {
                    Timber.d("on close")
                    mEditField.removeTextChangedListener(textWatcher)
                    onClose()
                } else {
                    editTextTarget?.setText("")
                    mPresenter?.onDone("", editTextTarget)
                }
                viewLayoutAction()
            }
        }
        mEditField.setOnClickListener {
            if (recyclerView.isShown
                || messageOnFrame.isShown
                || floatingRecyclerView.isShown
                || !keyboardWrapper.isShown
            ) {
                reInputFlag = true
                viewInputMode(true)
                if (finalInputType != null) {
                    setKeyboardViewAsRequired(finalInputType, textWatcher)
                }
            }
        }
    }

    private fun setCustomInput(customIC: InputConnection) {
        kokoKeyboardView.setCustomInputConnection(customIC)
    }

    private fun setHint(resId: Int) {
        mEditFieldTIL.setText(resId)
        mEditFieldLongTIL.setText(resId)

        Timber.d("hint=" + mEditFieldTIL.hint)
    }

    fun setHint(resId: CharSequence?) {
        mEditFieldTIL.text = resId
        mEditFieldLongTIL.text = resId

        Timber.d("hint=" + mEditFieldTIL.hint)
    }

    /**
     *  input mode */
    fun viewInputMode(active: Boolean) {
        if (active) {
            goneOptionsView()
            if (!getKeyboardViewWrapper().isShown) {
                getKeyboardViewWrapper().visible()
            }
            keyboardView.visible()
            keyboardWrapper.visible()
            keyboardActionWrapper.gone()
            viewBaseInput()

            if (!reInputFlag) {
                mEditField.text = null
            }
            mEditField.requestFocus()
            mEditField.setTextIsSelectable(true)
        } else {
            reInputFlag = false
            headerWrapper.invisible()
            headerShadowAction.gone()
            mLayoutEdit.gone()
            defaultInputLayout.gone()
            mEditField.clearFocus()
        }
    }

    fun viewBaseInput() {
        frame.gone()
        navigationParentLayout.invisible()
        parentEditMainLong.gone()

        headerWrapper.visible()
        mLayoutEdit.visible()
        defaultInputLayout.visible()
    }



    fun enableEditText(enable: Boolean) {
        mEditField.isFocusable = enable
        mEditField.isLongClickable = enable
        mEditField.isClickable = enable
        mEditField.setTextIsSelectable(enable)
    }
}