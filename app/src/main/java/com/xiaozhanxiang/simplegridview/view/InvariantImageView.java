package com.xiaozhanxiang.simplegridview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.xiaozhanxiang.simplegridview.R;
import com.xiaozhanxiang.simplegridview.utils.ReflexUtils;

/**
 * author: dai
 * date:2019/3/6
 * 保证图片不变形，需要固定宽或高的其中一个，并以其作为标准
 */
public class InvariantImageView extends AppCompatImageView {
    private static final int LIMIT_MODE_WIDTH = 1;
    private static final int LIMIT_MODE_HIEGHT = 2;
    private int limitMode;
    private Drawable mDrawable;

    public InvariantImageView(Context context) {
        this(context,null);
    }

    public InvariantImageView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public InvariantImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.InvariantImageView);
            limitMode = typedArray.getInt(R.styleable.InvariantImageView_limitMode,LIMIT_MODE_WIDTH);
            typedArray.recycle();
        }
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        /*================================用反射实现=================================*/
        ReflexUtils.invokeMethod(this,"resolveUri",new Class[]{},new Object[]{});
        mDrawable = getDrawable();
        int measuredWidth ;
        int measuredHeight ;
        if (mDrawable != null) {
            measuredWidth = mDrawable.getIntrinsicWidth();
            measuredHeight = mDrawable.getIntrinsicHeight();
        }else {
            measuredWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
            measuredHeight = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        }
        /*================================用反射实现=================================*/

        /*================================不用反射实现=================================*/
        //第一次测量，让控件完全是包裹内容
//        super.onMeasure(MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED), MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED));
//        int measuredWidth = getMeasuredWidth();
//        int measuredHeight = getMeasuredHeight();
        /*================================不用反射实现=================================*/
        int expectHeigth; //预期的图片高度
        int expecWidth;  // 预期的图片宽度
        if (limitMode == LIMIT_MODE_WIDTH) { //以宽作为标准
            if (widthMode == MeasureSpec.EXACTLY) {
                expecWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
                float scale = expecWidth*1f/measuredWidth;
                expectHeigth = (int) (measuredHeight * scale);
            }else {
                expecWidth = measuredWidth;
                expectHeigth = measuredHeight;
            }
            if (heightMode == MeasureSpec.EXACTLY) {
                expectHeigth = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
            }
        }else { //以高作为标准
            if (heightMode == MeasureSpec.EXACTLY) {
                expectHeigth = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
                float scale = expectHeigth*1f/measuredHeight;
                expecWidth = (int) (widthMeasureSpec * scale);
            }else {
                expecWidth = measuredWidth;
                expectHeigth = measuredHeight;
            }
            if (widthMode == MeasureSpec.EXACTLY) {
                expecWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
            }
        }
        expecWidth = expecWidth + getPaddingLeft() + getPaddingRight();
        expectHeigth = expectHeigth + getPaddingTop() + getPaddingBottom();
        super.onMeasure(MeasureSpec.makeMeasureSpec(expecWidth,MeasureSpec.EXACTLY),MeasureSpec.makeMeasureSpec(expectHeigth,MeasureSpec.EXACTLY));
    }
}
