package com.xiaozhanxiang.simplegridview.utils;

import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;

/**
 * author: dai
 * date:2018/11/6
 */
public class PaintUtils {

    /**
     * 获得文字垂直居中的基线
     * @param bounds
     * @param fontMetrics
     * @return
     */
    public static float getTextVerticalCenter(RectF bounds ,Paint.FontMetrics fontMetrics) {
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        float textBaseY = bounds.height() - (bounds.height() - fontHeight) / 2 - fontMetrics.bottom;
        return textBaseY;
    }    /**
     * 获得文字垂直居中的基线
     * @param fontMetrics
     * @return
     */
    public static float getTextVerticalCenter(float height ,Paint.FontMetrics fontMetrics) {
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        float textBaseY = height - (height - fontHeight) / 2 - fontMetrics.bottom;
        return textBaseY;
    }

    /**
     * @param paint paint 对象
     * @param s 绘制的文字
     * @return  返回绘制的宽度
     */
    public static float  getTextWidth(Paint paint , String s) {
        if (TextUtils.isEmpty(s)) {
            return 0;
        }
       return paint.measureText(s);
    }

    public static Rect getTextBounds(Paint paint,String s) {
        Rect bounds = new Rect();
        paint.getTextBounds(s,0,s.length(),bounds);
        return bounds;
    }

    public static float getTextheight(Paint paint,String s) {
        return getTextBounds( paint, s).height();
    }

    /**
     * @param paint
     * @param s
     * @param maxWidth
     * @return  返回能绘制的字数
     */
    public static  int getMaxCount(Paint paint , String s,float maxWidth) {
        if (TextUtils.isEmpty(s)) {
            return 0;
        }
        return paint.breakText(s,true,maxWidth,null);
    }
}
