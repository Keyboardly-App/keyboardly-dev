package com.rowland.kokokeyboard.layouts;

import android.content.Context;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class KeyboardWrapperLayout extends LinearLayout {

    public KeyboardWrapperLayout(Context context) {
        super(context);
        init();
    }

    public KeyboardWrapperLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        layoutParams.topMargin = (int) (8 / Resources.getSystem().getDisplayMetrics().density);
        layoutParams.bottomMargin = (int) (8 / Resources.getSystem().getDisplayMetrics().density);

        setLayoutParams(layoutParams);
        setOrientation(LinearLayout.VERTICAL);
    }
}
