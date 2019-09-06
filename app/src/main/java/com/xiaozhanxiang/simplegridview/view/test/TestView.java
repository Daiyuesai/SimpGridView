package com.xiaozhanxiang.simplegridview.view.test;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.TextView;

import com.xiaozhanxiang.simplegridview.utils.Utils;

import java.util.Locale;

/**
 * author: dai
 * date:2019/8/14
 */
public class TestView extends android.support.v7.widget.AppCompatTextView {
    private static final String TAG = "TestView";
    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TestView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        String text = getText().toString().trim();
        if (TextUtils.isEmpty(text)) {
            text = "TestView";
        }
        Utils.logStackInfo(TAG,text);
    }



}
