package com.xiaozhanxiang.simplegridview.utils;

import android.content.Context;
import android.util.Log;

import com.xiaozhanxiang.simplegridview.bean.ReflexBean;

/**
 * author: dai
 * date:2019/3/3
 */
public class JNIUtils {
    private static final String TAG = "JNIUtils";
    static {

        System.loadLibrary("native");
        System.loadLibrary("avcodec");
        System.loadLibrary("avdevice");
    }

    public native String getNativeValue(String key);

    public native int nativeInt(int i,int j);

    public native boolean nativeArrayJava(int[] array);

    public native void callJavaVoid();
    public native void callJavaString(String key);
    public native void callJavaArray(int[] array);

    public void nativeCallJavaVoid(){
        ReflexBean bean = new ReflexBean();
        bean.ReflexSuper();
    }

    public void nativeCallJavaString(String s){
        Log.i(TAG, "nativeCallJavaString: " + s);
    }

    public void nativeCallJavaArray(int [] array) {
        StringBuffer stringBuffer = new StringBuffer();

        for (int i = 0; i < array.length; i++) {
            stringBuffer.append(i).append("=").append(array[i]).append("  ");
        }
        Log.i(TAG, "nativeCallJavaArray: " + stringBuffer.toString());
    }

}
