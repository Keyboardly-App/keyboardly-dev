package com.rowland.kokokeyboard.keys;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.google.android.material.button.MaterialButton;
import com.rowland.kokokeyboard.R;


public class SquareKeyView extends MaterialButton {

    private float screenWidthPercentage = 0.20f;
    private float widthHeightRatio = 1.0f;

    public SquareKeyView(Context context) {
        super(context);
        init(context, null, 0);
    }

    public SquareKeyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0);
    }

    public SquareKeyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.KeyPadView);
        try {
            this.screenWidthPercentage = a.getFloat(R.styleable.KeyPadView_screen_width_percent, 0.20f);
            this.widthHeightRatio = a.getFloat(R.styleable.KeyPadView_width_height_ratio, 1.0f);
        } finally {
            a.recycle();
        }
    }


    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

       /*int minW = getSuggestedMinimumWidth();
        int measuredWidth = getDefaultSize(minW, widthMeasureSpec);

        int minH =  getSuggestedMinimumHeight();
        int measuredHeight = getDefaultSize(minH, heightMeasureSpec);

        int finalWidth = measuredWidth;
        int finalHeight = (int) (measuredWidth / widthHeightRatio);

        setMeasuredDimension(finalWidth, finalHeight);
*/
        /*if (widthHeightRatio > 0.0) {
            // set the image views size
            int width = MeasureSpec.getSize(widthMeasureSpec);
            int height = (int) (width * widthHeightRatio);
            setMeasuredDimension(width, height);
        }
        else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }*/
    }


    public float getScreenWidthPercentage() {
        return this.screenWidthPercentage;
    }
}
