package com.xiaozhanxiang.simplegridview.utils;

import android.util.Log;

import java.util.Locale;

/**
 * author: dai
 * date:2019/8/14
 */
public final class Utils {

    private Utils(){};

    /**
     * 打印堆栈信息
     */
    public static void logStackInfo(String tag,String prefix) {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : elements) {
            Log.i(tag, prefix + "-- " + String.format(Locale.getDefault(), "%s----->%s\tline: %d",
                    element.getClassName(), element.getMethodName(), element.getLineNumber()));
        }
    }

}
