package com.rowland.kokokeyboard.keys;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.google.android.material.button.MaterialButton;
import com.rowland.kokokeyboard.R;


public class RectangularKeyView extends MaterialButton {

    private boolean isSpecialKey = true;
    private int specialKeyCode = -1;
    private float screenWidthPercentage = 0.3f;

    public RectangularKeyView(Context context) {
        super(context);
    }

    public RectangularKeyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.KeyPadView);
        try {
            this.screenWidthPercentage = a.getFloat(R.styleable.KeyPadView_screen_width_percent, 0.30f);
            this.isSpecialKey = a.getBoolean(R.styleable.KeyPadView_is_special_key, true);
            this.specialKeyCode = a.getInt(R.styleable.KeyPadView_special_key_code, -1);
        } finally {
            a.recycle();
        }
    }

    public boolean isSpecialKey() {
        return isSpecialKey;
    }

    public int getSpecialKeyCode() {
        return specialKeyCode;
    }

    public float getScreenWidthPercentage() {
        return this.screenWidthPercentage;
    }
}
