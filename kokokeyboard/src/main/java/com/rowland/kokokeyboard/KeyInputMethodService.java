package com.rowland.kokokeyboard;

import android.inputmethodservice.InputMethodService;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;

//ToDo: allow  integration of custom keyboard as a systme wide keyboard
public class KeyInputMethodService extends InputMethodService implements KeyboardView.OnKeyboardActionListener {

    private Keyboard mKeyboard;
    private Keyboard mKeyboardWithNum;
    private Keyboard mActiveKeyboard;

    private StringBuilder mComposing = new StringBuilder();
    private KeyboardView mKeyboardInputView;

    @Override
    public void onCreate() {
        super.onCreate();

    }

    @Override
    public void onInitializeInterface() {
        mKeyboard = new Keyboard(this, R.xml.keyboard_pad);
        mKeyboardWithNum = new Keyboard(this, R.xml.keyboard_with_num);
    }

    @Override
    public void onStartInput(EditorInfo attribute, boolean restarting) {
        super.onStartInput(attribute, restarting);

        switch (attribute.inputType & EditorInfo.TYPE_MASK_CLASS) {
            case EditorInfo.TYPE_CLASS_NUMBER:
                mActiveKeyboard = mKeyboardWithNum;
                break;
            case EditorInfo.TYPE_TEXT_VARIATION_PERSON_NAME:
                mActiveKeyboard = mKeyboard;
                break;
            case EditorInfo.TYPE_CLASS_TEXT:
                mActiveKeyboard = mKeyboardWithNum;
                break;

            default:
                mActiveKeyboard = mKeyboardWithNum;

        }
    }

    @Override
    public void onStartInputView(EditorInfo attribute, boolean restarting) {
        super.onStartInputView(attribute, restarting);

        mKeyboardInputView.closing();
        mKeyboardInputView.setKeyboard(mActiveKeyboard);
        mComposing.setLength(0);
    }


    @Override
    public void onFinishInput() {
        super.onFinishInput();

        if (mKeyboardInputView != null) {
            mKeyboardInputView.closing();
        }
    }


    @Override
    public View onCreateInputView() {
        mKeyboardInputView = (KeyboardView) getLayoutInflater().inflate(R.layout.qwerty_keypad, null);
        mKeyboardInputView.setOnKeyboardActionListener(this);
        mKeyboardInputView.setKeyboard(mActiveKeyboard);
        mKeyboardInputView.setPreviewEnabled(false);
        return mKeyboardInputView;
    }

    @Override
    public void onPress(int primaryCode) {

    }

    @Override
    public void onRelease(int primaryCode) {

    }

    @Override
    public void onKey(int primaryCode, int[] keyCodes) {

        switch (primaryCode) {
            case Keyboard.KEYCODE_DELETE:
                CharSequence selectedText = getCurrentInputConnection().getSelectedText(0);
                if (TextUtils.isEmpty(selectedText)) {
                    getCurrentInputConnection().deleteSurroundingText(1, 0);
                } else {
                    getCurrentInputConnection().commitText("", 1);
                }
                break;

            case Keyboard.KEYCODE_DONE:
                getCurrentInputConnection().sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_ENTER));
            case Keyboard.KEYCODE_MODE_CHANGE:
                if (mKeyboardInputView != null) {
                    Keyboard current = mKeyboardInputView.getKeyboard();
                    if (current == mKeyboard) {
                        mKeyboardInputView.setKeyboard(mKeyboardWithNum);
                    } else {
                        mKeyboardInputView.setKeyboard(mKeyboard);
                    }
                }
                break;
            default:
                getCurrentInputConnection().commitText(String.valueOf((char) primaryCode), 1);
        }
    }

    @Override
    public void onText(CharSequence text) {

    }

    @Override
    public void swipeLeft() {

    }

    @Override
    public void swipeRight() {

    }

    @Override
    public void swipeDown() {

    }

    @Override
    public void swipeUp() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (event.getRepeatCount() == 0 && mKeyboardInputView != null) {
                    if (mKeyboardInputView.handleBack()) {
                        return true;
                    }
                }
                break;

            case KeyEvent.KEYCODE_DEL:
                if (mComposing.length() > 0) {
                    onKey(Keyboard.KEYCODE_DELETE, null);
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_SPACE:
                return false;
            default:
        }
        return super.onKeyDown(keyCode, event);
    }
}
