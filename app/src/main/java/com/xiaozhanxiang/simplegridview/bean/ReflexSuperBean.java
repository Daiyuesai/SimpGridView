package com.xiaozhanxiang.simplegridview.bean;


import android.util.Log;

/**
 * author: dai
 * date:2019/3/5
 */
public class ReflexSuperBean {
    private static final String TAG = "ReflexSuperBean";
    private String name;

    private void eat(String type){
        name = type;
        Log.i(TAG, "eat: " + name + type);
    }

    public String getName(){
        return name;
    }
}
