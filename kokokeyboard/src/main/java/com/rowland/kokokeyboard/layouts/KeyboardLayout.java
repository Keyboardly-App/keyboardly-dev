package com.rowland.kokokeyboard.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.rowland.kokokeyboard.keys.RectangularKeyView;
import com.rowland.kokokeyboard.keys.SquareKeyView;
import com.rowland.kokokeyboard.manager.KeyboardManager;

import androidx.annotation.Nullable;

public class KeyboardLayout extends LinearLayout {

    private KeyboardManager keyboardManager;

    public KeyboardLayout(Context context, KeyboardManager keyboardManager) {
        super(context);
        this.keyboardManager = keyboardManager;
        init();
    }

    public KeyboardLayout(Context context, KeyboardManager keyboardManager, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.keyboardManager = keyboardManager;
        init();
    }

    private void init() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);
        setOrientation(LinearLayout.VERTICAL);
    }

    public void registerListener(KeyboardManager.KeyboardListener listener) {
        keyboardManager.registerListener(listener);
        setKeypadClickListener(this);
    }


    public void setKeypadClickListener(ViewGroup parent) {
        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);
            if (child instanceof ViewGroup) {
                setKeypadClickListener((ViewGroup) child);
            } else {
                if (child != null) {
                    child.setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            if (view instanceof SquareKeyView) {
                                keyboardManager.onKeyStroke(((Button) view).getText().charAt(0));
                            }
                            if (view instanceof RectangularKeyView) {
                                if (((RectangularKeyView) view).isSpecialKey()) {
                                    keyboardManager.onKeyStroke(((RectangularKeyView) view).getSpecialKeyCode(), false);
                                } else {
                                    keyboardManager.onKeyStroke(((Button) view).getText().charAt(0));
                                }
                            }
                        }
                    });

                    if (child instanceof RectangularKeyView) {
                        if (((RectangularKeyView) child).isSpecialKey()) {
                            child.setLongClickable(true);
                            child.setOnLongClickListener(new OnLongClickListener() {
                                @Override
                                public boolean onLongClick(View v) {
                                    keyboardManager.onKeyStroke(((RectangularKeyView) child).getSpecialKeyCode(), true);
                                    return true;
                                }
                            });

                        }
                    }
                }
            }
        }
    }
}
