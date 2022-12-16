package com.rowland.kokokeyboard.utils;

import android.content.Context;
import android.util.TypedValue;

public class Utilities {

    //http://stackoverflow.com/questions/4605527/converting-pixels-to-dp
    //The above method results accurate method compared to below methods
    //http://stackoverflow.com/questions/8309354/formula-px-to-dp-dp-to-px-android
    //http://stackoverflow.com/questions/13751080/converting-pixels-to-dpi-for-mdpi-and-hdpi-screens

    public static int pxFromDp(Context context, int dp) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (px / density);
    }

    private static int dpFromPx(Context context, int px) {
        float dp = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, px, context.getResources().getDisplayMetrics());
        float density = context.getResources().getDisplayMetrics().density;
        return (int) (dp / density);
    }
}
