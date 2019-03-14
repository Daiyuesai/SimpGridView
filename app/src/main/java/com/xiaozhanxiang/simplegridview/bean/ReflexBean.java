package com.xiaozhanxiang.simplegridview.bean;

import android.util.Log;

import com.xiaozhanxiang.simplegridview.utils.ReflexUtils;

import java.lang.reflect.Method;
import java.net.HttpCookie;

/**
 * author: dai
 * date:2019/3/5
 */
public class ReflexBean extends ReflexSuperBean {
    private static final String TAG = "ReflexBean";


    public void ReflexSuper() {
        ReflexUtils.invokeMethod(this,"eat",new Class[]{String.class},new Object[]{"我的啊"});
        String name = (String) ReflexUtils.getFieldValue(this,"name");
        Log.e(TAG, "ReflexSuper: "+ name);
    }




}
