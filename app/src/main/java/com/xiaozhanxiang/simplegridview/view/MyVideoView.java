package com.xiaozhanxiang.simplegridview.view;

import android.content.Context;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * author: dai
 * date:2019/3/16
 */
public class MyVideoView extends SurfaceView {

    static {
        System.loadLibrary("native");
        System.loadLibrary("avcodec");
        System.loadLibrary("avdevice");
        System.loadLibrary("avfilter");
        System.loadLibrary("avformat");
        System.loadLibrary("avutil");
        System.loadLibrary("postproc");
        System.loadLibrary("swresample");
        System.loadLibrary("swscale");

    }
    public MyVideoView(Context context) {
        this(context,null);
    }

    public MyVideoView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyVideoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        SurfaceHolder holder = getHolder();

        holder.setFormat(PixelFormat.RGBA_8888);
    }

    public void play(final String path){

        new Thread(new Runnable() {
            @Override
            public void run() {
                renderRe(path,getHolder().getSurface());
            }
        }).start();
    }


    public native void render(String path, Surface surface);

    public native void renderRe(String path,Surface surface);
}
