package app.keyboardly.dev.keyboard.keys;

import static app.keyboardly.dev.keyboard.keys.RectangularKeyView.DEFAULT_HEIGHT_ROW;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import com.google.android.material.button.MaterialButton;

import app.keyboardly.dev.R;


public class SquareKeyView extends MaterialButton {

    public static float screenWidthPercentage = 0.20f;
    public static float widthHeightRatio = 1.0f;

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
            setAllCaps(false);
            screenWidthPercentage = a.getFloat(R.styleable.KeyPadView_screen_width_percent, 0.20f);
            widthHeightRatio = a.getFloat(R.styleable.KeyPadView_width_height_ratio, 1.0f);
        } finally {
            a.recycle();
        }
    }


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


    public float getScreenWidthPercentage() {
        return this.screenWidthPercentage;
    }
}
