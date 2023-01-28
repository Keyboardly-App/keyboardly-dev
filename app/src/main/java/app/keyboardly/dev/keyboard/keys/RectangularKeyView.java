package app.keyboardly.dev.keyboard.keys;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.google.android.material.button.MaterialButton;

import app.keyboardly.dev.R;


public class RectangularKeyView extends MaterialButton {

    public static final int DEFAULT_HEIGHT_ROW = 140;
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
            widthHeightRatio = a.getFloat(R.styleable.KeyPadView_width_height_ratio, 1.0f);
        } finally {
            a.recycle();
        }
    }

    public static float widthHeightRatio = 1.0f;

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int minW = getSuggestedMinimumWidth();
        int measuredWidth = getDefaultSize(minW, widthMeasureSpec);
        int finalHeight = (int) (measuredWidth / widthHeightRatio);

        setMeasuredDimension(measuredWidth, finalHeight);
        if (widthHeightRatio > 0.0) {
            // set the image views size
            int width = MeasureSpec.getSize(widthMeasureSpec);
            setMeasuredDimension(width, DEFAULT_HEIGHT_ROW);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
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
