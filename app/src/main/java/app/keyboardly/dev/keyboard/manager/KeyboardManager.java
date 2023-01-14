package app.keyboardly.dev.keyboard.manager;

import android.view.inputmethod.InputConnection;

import java.util.ArrayList;
import java.util.List;

import timber.log.Timber;

public class KeyboardManager {

    public static final int KEYCODE_BACKSPACE = 1;
    public static final int KEYCODE_SPACE = 2;
    public static final int KEYCODE_DONE = 3;
    public static final int KEYCODE_MODE_CHANGE = 4;
    public static final int KEYCODE_SHIFT = 5;
    public static final int KEYCODE_SYMBOL = 6;
    public static final int KEYCODE_ALPHABET = 7;
    public static final int KEYCODE_ENTER = 8;

    private int cursorPosition = 0;
    private String inputText = "";

    private List<KeyboardListener> listeners = new ArrayList<>();
    private InputConnection defaultInputConnection;
    private InputConnection customConnection;

    public interface KeyboardListener {
        void characterClicked(char c);
        void specialKeyClicked(int keyCode);
        void onButtonClicked(int id);
    }

    public KeyboardManager(InputConnection inputConnection) {
        this.defaultInputConnection = inputConnection;
    }

    public void setCustomInputConnection(InputConnection inputConnection) {
        customConnection = inputConnection;
    }

    public void resetInputConnection() {
        customConnection = null;
        Timber.i("custom connection reset. customConnection="+customConnection);
    }

    protected void handleKeyStroke(int key, boolean isLongPress) {
        switch (key) {
            case KEYCODE_BACKSPACE:
                if (cursorPosition == 0)
                    return;
                if (isLongPress) {
                    clearAll();
                } else {
                    getInputConnection().deleteSurroundingText(1, 0);
                    StringBuilder builder = new StringBuilder(this.inputText);
                    inputText = builder.deleteCharAt(--cursorPosition).toString();
                }
                break;
            case KEYCODE_ENTER:
                this.handleKeyStroke('\n');
                break;
            case KEYCODE_SPACE :
                this.handleKeyStroke(' ');
                break;
            case KEYCODE_DONE:
                // ToDo: Do nothing for now - view will close keyboard
            default:
                break;
        }
    }

    public InputConnection getInputConnection() {
        InputConnection inputConnection;
        if (customConnection!=null){
            inputConnection = customConnection;
        } else {
            inputConnection = defaultInputConnection;
        }
        return inputConnection;
    }

    protected void handleKeyStroke(char c) {
        getInputConnection().commitText(Character.toString(c), 1);

        if (cursorPosition++ >= inputText.length()) {
            inputText = inputText + c;
        } else {
            inputText = inputText.substring(0, cursorPosition - 1) + c + inputText.substring(cursorPosition - 1);
        }
    }

    public void onKeyStroke(char c) {
        CharSequence beforeText = getInputConnection().getTextBeforeCursor(100, 0).toString();
        CharSequence afterText = getInputConnection().getTextAfterCursor(100, 0).toString();
        cursorPosition = beforeText.length();
        inputText = beforeText.toString() + afterText.toString();

        handleKeyStroke(c);
        for (KeyboardListener listener : listeners) {
            listener.characterClicked(c);
        }
    }

    public void onKeyStroke(int keyCode, boolean isLongPress) {
        CharSequence beforeText = getInputConnection().getTextBeforeCursor(100, 0).toString();
        CharSequence afterText = getInputConnection().getTextAfterCursor(100, 0).toString();
        cursorPosition = beforeText.length();
        inputText = beforeText.toString() + afterText.toString();

        handleKeyStroke(keyCode, isLongPress);
        for (KeyboardListener listener : listeners) {
            listener.specialKeyClicked(keyCode);
        }
    }

    public void onButtonClick(int resId){
        for (KeyboardListener listener : listeners) {
            listener.onButtonClicked(resId);
        }
    }

    public void clearAll() {
        while (cursorPosition > 0) {
            getInputConnection().deleteSurroundingText(1, 0);
            StringBuilder builder = new StringBuilder(this.inputText);
            inputText = builder.deleteCharAt(--cursorPosition).toString();
        }
    }

    public void registerListener(KeyboardListener listener) {
        listeners.clear();
        listeners.add(listener);
    }
}
