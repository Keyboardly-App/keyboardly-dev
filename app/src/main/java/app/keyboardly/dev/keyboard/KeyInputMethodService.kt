package app.keyboardly.dev.keyboard

import android.annotation.SuppressLint
import android.inputmethodservice.InputMethodService
import android.inputmethodservice.Keyboard
import android.inputmethodservice.KeyboardView
import android.text.TextUtils
import android.view.KeyEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import app.keyboardly.dev.R

//ToDo: allow  integration of custom keyboard as a system wide keyboard
class KeyInputMethodService : InputMethodService(), KeyboardView.OnKeyboardActionListener {
    private var mKeyboard: Keyboard? = null
    private var mKeyboardWithNum: Keyboard? = null
    private var mActiveKeyboard: Keyboard? = null
    private val mComposing = StringBuilder()
    private var mKeyboardInputView: KeyboardView? = null

    override fun onInitializeInterface() {
        mKeyboard = Keyboard(this, R.xml.keyboard_pad)
        mKeyboardWithNum = Keyboard(this, R.xml.keyboard_with_num)
        instance = this
    }

    override fun onStartInput(attribute: EditorInfo, restarting: Boolean) {
        super.onStartInput(attribute, restarting)
        mActiveKeyboard =
            when (attribute.inputType and EditorInfo.TYPE_MASK_CLASS) {
                EditorInfo.TYPE_CLASS_NUMBER -> mKeyboardWithNum
                EditorInfo.TYPE_TEXT_VARIATION_PERSON_NAME -> mKeyboard
                EditorInfo.TYPE_CLASS_TEXT -> mKeyboardWithNum
                else -> mKeyboardWithNum
            }
    }

    override fun onStartInputView(attribute: EditorInfo, restarting: Boolean) {
        super.onStartInputView(attribute, restarting)
        mKeyboardInputView!!.closing()
        mKeyboardInputView!!.keyboard = mActiveKeyboard
        mComposing.setLength(0)
    }

    override fun onFinishInput() {
        super.onFinishInput()
        if (mKeyboardInputView != null) {
            mKeyboardInputView!!.closing()
        }
    }

    override fun onCreateInputView(): View {
        mKeyboardInputView = layoutInflater.inflate(R.layout.default_keyboard, null) as KeyboardView
        mKeyboardInputView!!.setOnKeyboardActionListener(this)
        mKeyboardInputView!!.keyboard = mActiveKeyboard
        mKeyboardInputView!!.isPreviewEnabled = false
        return mKeyboardInputView!!
    }

    override fun onPress(primaryCode: Int) {}
    override fun onRelease(primaryCode: Int) {}
    override fun onKey(primaryCode: Int, keyCodes: IntArray?) {
        when (primaryCode) {
            Keyboard.KEYCODE_DELETE -> {
                val selectedText = currentInputConnection.getSelectedText(0)
                if (TextUtils.isEmpty(selectedText)) {
                    currentInputConnection.deleteSurroundingText(1, 0)
                } else {
                    currentInputConnection.commitText("", 1)
                }
            }
            Keyboard.KEYCODE_DONE -> {
                currentInputConnection.sendKeyEvent(
                    KeyEvent(
                        KeyEvent.ACTION_DOWN,
                        KeyEvent.KEYCODE_ENTER
                    )
                )
                if (mKeyboardInputView != null) {
                    val current = mKeyboardInputView!!.keyboard
                    if (current === mKeyboard) {
                        mKeyboardInputView!!.keyboard = mKeyboardWithNum
                    } else {
                        mKeyboardInputView!!.keyboard = mKeyboard
                    }
                }
            }
            Keyboard.KEYCODE_MODE_CHANGE -> if (mKeyboardInputView != null) {
                val current = mKeyboardInputView!!.keyboard
                if (current === mKeyboard) {
                    mKeyboardInputView!!.keyboard = mKeyboardWithNum
                } else {
                    mKeyboardInputView!!.keyboard = mKeyboard
                }
            }
            else -> currentInputConnection.commitText(primaryCode.toChar().toString(), 1)
        }
    }

    override fun onText(text: CharSequence) {}
    override fun swipeLeft() {}
    override fun swipeRight() {}
    override fun swipeDown() {}
    override fun swipeUp() {}
    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        when (keyCode) {
            KeyEvent.KEYCODE_BACK -> if (event.repeatCount == 0 && mKeyboardInputView != null) {
                if (mKeyboardInputView!!.handleBack()) {
                    return true
                }
            }
            KeyEvent.KEYCODE_DEL -> if (mComposing.isNotEmpty()) {
                onKey(Keyboard.KEYCODE_DELETE, null)
                return true
            }
            KeyEvent.KEYCODE_SPACE -> return false
            else -> {}
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: KeyInputMethodService? = null


    }
}