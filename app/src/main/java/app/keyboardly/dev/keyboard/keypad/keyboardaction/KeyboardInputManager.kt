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
import app.keyboardly.style.helper.gone
import app.keyboardly.style.helper.invisible
import app.keyboardly.style.helper.visible
import app.keyboardly.lib.helper.InputPresenter
import app.keyboardly.style.helper.currencyTextWatcher
import app.keyboardly.style.helper.decimalTextWatcher
import timber.log.Timber

/**
 * Created by Zainal on 09/01/2023 - 19:18
 */
open class KeyboardInputManager(
    view: View,
    moduleHelper: DynamicModuleHelper,
    private val kokoKeyboardView: KokoKeyboardView
) : KeyboardNavigation(view, moduleHelper, kokoKeyboardView){

    private var mPresenter: InputPresenter? = null
    private var reInputFlag = false
    private var customEditorInfo: EditorInfo? = null

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
        onClose: () -> Unit? = { },
        inputFloating: Boolean? = false,
        isCurrency: Boolean? = false
    ) {

        mPresenter = presenter
        if (inputFloating == true) {
                val finalInputType = inputType ?: editTextTarget?.inputType
                customEditorInfo = EditorInfo()
                if (finalInputType != null) {
                    customEditorInfo = EditorInfo()
                    if (editTextTarget != null) {
                        customEditorInfo?.inputType = editTextTarget.inputType
                        customEditorInfo?.imeOptions = editTextTarget.imeOptions
                        customEditorInfo?.hintText = editTextTarget.hint
                        customEditorInfo?.actionId = editTextTarget.imeActionId
                    }
                    Timber.d("inputType=$inputType")
//                    kokoKeyboardView.loadKeyboard(
//                        inputType?: KokoKeyboardView.INPUT_TYPE_QWERTY, editTextTarget!!)
                }
                // check this for direct input
            val customIC = editTextTarget?.onCreateInputConnection(customEditorInfo)
            customIC?.apply {
                setCustomInput(this)
            }

            setKeyboardViewAsRequired(inputType?:InputType.TYPE_CLASS_TEXT, null, isCurrency)

        } else {
            enableInput?.apply {
                enableEditText(this)
            }
            requestInput(editTextTarget, hint, inputType, textWatcher, onClose, isCurrency)
        }
    }

    /**
     * for requesting input long text, text that may required than 1 line
     */
    fun requestInputLong(
        editTextTarget: EditText?,
        presenter: InputPresenter?,
        hintResId: Int?,
        isCurrency : Boolean? = false
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
            setKeyboardViewAsRequired(this, textWatcher, isCurrency)
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
                    setKeyboardViewAsRequired(this, textWatcher, isCurrency)
                }
            }
        }
    }

    private fun setKeyboardViewAsRequired(
        inputType: Int,
        textWatcher: TextWatcher?,
        isCurrency: Boolean? = false
    ) {

        when(inputType){
            InputType.TYPE_CLASS_NUMBER -> kokoKeyboardView.setKeypadNumber()
            InputType.TYPE_NUMBER_FLAG_DECIMAL -> kokoKeyboardView.setKeypadNumber()
            else -> {
                if(isCurrency == true){
                    kokoKeyboardView.setKeypadNumber()
                } else {
                    kokoKeyboardView.setKeypadAlphabet()
                }
            }
        }
        Timber.d("added text watcher=$textWatcher || currency=$isCurrency")

        if (isCurrency == true){
            val currencyTextWatcher = currencyTextWatcher(mEditField)
            mEditField.addTextChangedListener(currencyTextWatcher)
            this.textWatcher = currencyTextWatcher
        } else if (inputType == InputType.TYPE_NUMBER_FLAG_DECIMAL){
            this.textWatcher = decimalTextWatcher(mEditField)
            mEditField.addTextChangedListener(this.textWatcher)
        } else {
            if (textWatcher != null) {
                mEditField.addTextChangedListener(textWatcher)
            } else {
                mEditField.removeTextChangedListener(this@KeyboardInputManager.textWatcher)
                mEditFieldLong.removeTextChangedListener(this@KeyboardInputManager.textWatcher)
            }
            this.textWatcher = textWatcher
        }
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
        onClose: () -> Unit?,
        isCurrency:Boolean?=false
    ) {
//        Timber.e("request input//%s", editTextTarget.getResources().getResourceName(editTextTarget.getId()));
        Timber.d("textwatcher=$textWatcher")

        if (editTextTarget == null && mPresenter == null && textWatcher==null) {
            throw IllegalAccessException("Edittext, TextWatcher and InputPresenter can't be all null")
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
            setKeyboardViewAsRequired(finalInputType, textWatcher, isCurrency)
        } else {
            setKeyboardViewAsRequired(InputType.TYPE_CLASS_TEXT, textWatcher, isCurrency)
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

        if (textWatcher!=null) {
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
                    setKeyboardViewAsRequired(finalInputType, textWatcher, isCurrency)
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