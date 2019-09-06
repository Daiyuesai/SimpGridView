package com.xiaozhanxiang.simplegridview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.xiaozhanxiang.simplegridview.R;


/**
 * author: dai
 * date:2018/12/20
 * 可以设置最大高度的FrameLayout
 */
public class MaxHeightFrameLayout extends FrameLayout {

    private int mMaxHeight;

    public MaxHeightFrameLayout(@NonNull Context context) {
        super(context);
        init(context,null);
    }

    public MaxHeightFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context,attrs);
    }


    public MaxHeightFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MaxHeightFrameLayout);
            mMaxHeight = typedArray.getDimensionPixelOffset(R.styleable.MaxHeightFrameLayout_maxHeight,-1);
        }
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mMaxHeight != -1) {
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            int heightSize = MeasureSpec.getSize(heightMeasureSpec);
            if (heightMode == MeasureSpec.AT_MOST) {
                heightSize = Math.min(mMaxHeight,heightSize);
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, heightMode);
                super.onMeasure(widthMeasureSpec,heightMeasureSpec);
            }else {
                super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            }
        }else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }
}
