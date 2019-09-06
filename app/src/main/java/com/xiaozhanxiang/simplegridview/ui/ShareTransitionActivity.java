package com.xiaozhanxiang.simplegridview.ui;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;

import android.support.v4.app.ActivityOptionsCompat;
import android.transition.Explode;
import android.transition.Slide;
import android.view.Gravity;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xiaozhanxiang.simplegridview.R;
import com.xiaozhanxiang.simplegridview.utils.GlideApp;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * author: dai
 * date:2019/8/3
 */
public class ShareTransitionActivity extends BaseActivity {
    @BindView(R.id.iv_one)
    ImageView ivOne;
    //https://cdn.dribbble.com/users/239075/screenshots/4797890/chat-by-iftikharshaikh_1x.jpg

    public static void getInstance(Context context, ActivityOptionsCompat options) {
        Intent intent = new Intent(context, ShareTransitionActivity.class);
        context.startActivity(intent,options.toBundle());
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share_transition);
        ButterKnife.bind(this);



         setEnterAnmi();
        GlideApp.with(this)
                .load("https://cdn.dribbble.com/users/239075/screenshots/4797890/chat-by-iftikharshaikh_1x.jpg")
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .override(400,300)
                .into(ivOne);

    }

    private void  setEnterAnmi() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            Explode explode = new Explode();
            explode.setDuration(500);
            Slide slide = new Slide();
            slide.setDuration(500);
            slide.setSlideEdge(Gravity.TOP);

            getWindow().setEnterTransition(slide);
            getWindow().setReturnTransition(explode);
//
//            getWindow().setAllowEnterTransitionOverlap(false);
//            getWindow().setAllowReturnTransitionOverlap(false);
        }
    }
}
