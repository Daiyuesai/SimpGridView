package com.xiaozhanxiang.simplegridview.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.transition.AutoTransition;
import android.transition.Explode;
import android.transition.Slide;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.xiaozhanxiang.simplegridview.R;
import com.xiaozhanxiang.simplegridview.utils.GlideApp;

import butterknife.BindView;
import butterknife.ButterKnife;



/**
 * author: dai
 * date:2019/8/3
 */
public class TransitionActivity extends BaseActivity {


    @BindView(R.id.tv_one)
    TextView tvOne;
    @BindView(R.id.bnt_one)
    Button bntOne;
    @BindView(R.id.ll_root)
    LinearLayout llRoot;
    @BindView(R.id.bnt_two)
    Button bntTwo;
    @BindView(R.id.iv_one)
    ImageView ivOne;

    public static void getInstance(Context context) {
        Intent intent = new Intent(context, TransitionActivity.class);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_transition);
        ButterKnife.bind(this);
        bntOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AutoTransition transition = new AutoTransition();
                // transition.setDuration(3000);
                TransitionManager.beginDelayedTransition(llRoot, transition);


                if (tvOne.getVisibility() == View.VISIBLE) {
                    tvOne.setVisibility(View.GONE);
                } else {
                    tvOne.setVisibility(View.VISIBLE);
                }
            }
        });


        GlideApp.with(this)
                .load("https://cdn.dribbble.com/users/239075/screenshots/4797890/chat-by-iftikharshaikh_1x.jpg")
                .diskCacheStrategy(DiskCacheStrategy.DATA)
                .override(400,300)
                .into(ivOne);



        setEnterAnmi();
        bntTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Pair[] pairs = new Pair[]{Pair.create(ivOne,"image1")};
                ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(TransitionActivity.this,pairs);
                ShareTransitionActivity.getInstance(TransitionActivity.this,activityOptionsCompat);


            }
        });



    }

    private void setEnterAnmi() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            Slide slide = new Slide(Gravity.END);
            slide.setDuration(500);

            Explode explode = new Explode();
            explode.setDuration(500);
            getWindow().setExitTransition(slide);
            getWindow().setReenterTransition(explode);
//            getWindow().setAllowEnterTransitionOverlap(false);
//            getWindow().setAllowReturnTransitionOverlap(false);

        }
    }


    private void  hideViewAnim(final View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int cx = view.getWidth() / 2;
            int cy = view.getHeight() / 2;

            int initialRadius = Math.max(view.getWidth(),view.getHeight());

            Animator circularReveal = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);

            circularReveal.setDuration(2000);
            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    view.setVisibility(View.GONE);
                }
            });
            circularReveal.start();
        }else {
            view.setVisibility(View.GONE);
        }
    }
    private void  showViewAnim(final View view) {


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            int cx = view.getWidth() / 2;
            int cy = view.getHeight() / 2;

            int initialRadius = Math.max(view.getWidth(),view.getHeight());
            view.setVisibility(View.VISIBLE);
            Animator circularReveal = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, initialRadius);
            circularReveal.setDuration(2000);
            circularReveal.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                   // view.setVisibility(View.GONE);
                }
            });
            circularReveal.start();
        }else {
            view.setVisibility(View.VISIBLE);
        }
    }
}
