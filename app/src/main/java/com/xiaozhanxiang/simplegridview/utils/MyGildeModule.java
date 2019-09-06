package com.xiaozhanxiang.simplegridview.utils;

import android.content.Context;
import android.support.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;

import com.bumptech.glide.module.AppGlideModule;


import java.io.InputStream;



/**
 * author: dai
 * date:2018/11/30
 */
@GlideModule
public class MyGildeModule extends AppGlideModule {


    @Override
    public void applyOptions(@NonNull Context context, @NonNull GlideBuilder builder) {




    }

    @Override
    public void registerComponents(@NonNull Context context, @NonNull Glide glide, @NonNull Registry registry) {

    }



}
