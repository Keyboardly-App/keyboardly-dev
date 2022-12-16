package com.rowland.kokokeyboard.keypad;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.rowland.kokokeyboard.R;
import com.rowland.kokokeyboard.layouts.KeyboardLayout;
import com.rowland.kokokeyboard.manager.KeyboardManager;

import java.util.HashMap;

public class KokoKeyboardView extends ExpandableLayout {

    public static final int INPUT_TYPE_QWERTY_NUM = -10;
    public static final int INPUT_TYPE_QWERTY = -20;

    private EditText activeEditField;
    private HashMap<EditText, KeyboardLayout> keyboards = new HashMap<>();
    private KeyboardManager.KeyboardListener keyboardListener;

    public KokoKeyboardView(Context context) {
        super(context);
        init();
    }

    public KokoKeyboardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    protected void init() {
        keyboardListener = new KeyboardManager.KeyboardListener() {
            @Override
            public void characterClicked(char c) {

            }

            @Override
            public void specialKeyClicked(int keyCode) {
                switch (keyCode) {
                    case KeyboardManager.KEYCODE_BACKSPACE:
                        // Handle Backspace key
                        break;
                    case KeyboardManager.KEYCODE_SPACE:
                        // ToDo: Handle Space key
                        break;
                    case KeyboardManager.KEYCODE_DONE:
                        collapse();
                        break;
                }
            }
        };
    }

    public void registerEditText(int type, final EditText field) {
        if (!field.isEnabled()) {
            return;
        }
        field.setRawInputType(InputType.TYPE_CLASS_TEXT);
        field.setSoundEffectsEnabled(false);
        field.setLongClickable(false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            field.setShowSoftInputOnFocus(false);
        } else {
            field.setTextIsSelectable(true);
        }

        InputConnection inputConnection = field.onCreateInputConnection(new EditorInfo());
        KeyboardLayout keyboard = generateCorrectKeyboard(type, inputConnection);

        keyboards.put(field, keyboard);
        keyboards.get(field).registerListener(keyboardListener);

        field.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(field.getWindowToken(), 0);

                activeEditField = field;
                removeAllViews();

                KeyboardLayout keyboard1 = keyboards.get(activeEditField);
                addView(keyboard1);

                if (!isExpanded()) {
                    expand();
                }
            } else {
                if (isExpanded()) {
                    for (EditText editText : keyboards.keySet()) {
                        if (editText.hasFocus()) {
                            return;
                        }
                    }
                    collapse();
                }
            }
        });

        field.setOnClickListener(v -> {
            if (!isExpanded()) {
                expand();
            }
        });
    }

    public void unregisterEditText(EditText field) {
        keyboards.remove(field);
    }

    private KeyboardLayout generateCorrectKeyboard(int type, InputConnection ic) {
        KeyboardLayout keypad = new KeyboardLayout(getContext(), new KeyboardManager(ic));
        View view;

        switch (type) {
            case INPUT_TYPE_QWERTY:
                view = LayoutInflater.from(getContext()).inflate(R.layout.qwerty_keypad, null);
                keypad.addView(view);
                return keypad;
            case INPUT_TYPE_QWERTY_NUM:
            default:
                view = LayoutInflater.from(getContext()).inflate(R.layout.qwerty_keypad_with_num, null);
                keypad.addView(view);
                return keypad;
        }
    }
}
