package com.zw.filterrv.utils;

import android.content.Context;

public class DensityUtil {

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = getScale(context);
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = getScale(context);
        return (int) (pxValue / scale + 0.5f);
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = getScale(context);
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = getScale(context);
        return (int) (spValue * fontScale + 0.5f);
    }

    private static float getScale(Context context) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return findScale(fontScale);
    }
    private static float findScale(float scale){
        if(scale<=1){
            scale=1;
        }else if(scale<=1.5){
            scale=1.5f;
        }else if(scale<=2){
            scale=2f;
        }else if(scale<=3){
            scale=3f;
        }
        return scale;
    }
}
