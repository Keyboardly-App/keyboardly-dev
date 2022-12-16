package com.rowland.kokokeyboard.layouts;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

public class KeyBoardRowLayout extends LinearLayout {

    public KeyBoardRowLayout(Context context) {
        super(context);
        init();
    }

    public KeyBoardRowLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        setLayoutParams(layoutParams);
        setOrientation(LinearLayout.HORIZONTAL);
        setGravity(Gravity.CENTER);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

       /* for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            if (child.getVisibility() == GONE)
                return;
            LinearLayout.LayoutParams childLayoutParams = (LayoutParams) child.getLayoutParams();
            int heightSquareKey = 0;
            if ((child instanceof SquareKeyView)) {
                childLayoutParams.width = (int) (w * ((SquareKeyView) child).getScreenWidthPercentage());
                heightSquareKey = childLayoutParams.width;
                child.setLayoutParams(childLayoutParams);
            } else {
                if(heightSquareKey > 0) {
                    childLayoutParams.width = (int) (w * ((RectangularKeyView) child).getScreenWidthPercentage());
                    childLayoutParams.height = heightSquareKey;
                }
                child.setLayoutParams(childLayoutParams);
            }
        }*/


    }
}