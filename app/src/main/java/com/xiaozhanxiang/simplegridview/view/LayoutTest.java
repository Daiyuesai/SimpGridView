package com.xiaozhanxiang.simplegridview.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

/**
 * author: dai
 * date:2019/8/12
 */
public class LayoutTest extends FrameLayout {
    public LayoutTest( @NonNull Context context) {
        super(context);
    }

    public LayoutTest(@NonNull Context context,  AttributeSet attrs) {
        super(context, attrs);
    }

    public LayoutTest( Context context,  @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int childCount = getChildCount();
        for (int i = childCount -1; i >= 0; i--) {
            View childView = getChildAt(i);

            if (childView != null) {
                int measuredWidth = childView.getMeasuredWidth();
                int measuredHeight = childView.getMeasuredHeight();
                childView.layout(0,0,measuredWidth,measuredHeight);
            }

        }
    }
}
