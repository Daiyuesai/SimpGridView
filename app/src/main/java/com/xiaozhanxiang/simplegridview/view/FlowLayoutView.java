package com.xiaozhanxiang.simplegridview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import com.xiaozhanxiang.simplegridview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author: dai
 * date:2019/2/15
 * 自定义流式布局
 */
public class FlowLayoutView extends ViewGroup {
    private int childDefultGravity = Gravity.TOP;
    private int layoutGravity ;
    private  List<View> lineView = new ArrayList<>(); //layout子view 时使用，每次布局一行
    public FlowLayoutView(Context context) {
        super(context);
        init(context,null);
    }

    public FlowLayoutView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public FlowLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs) ;
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FlowLayoutView);
            childDefultGravity = array.getInt(R.styleable.FlowLayoutView_childDefultGravity,Gravity.TOP);
            layoutGravity = array.getInt(R.styleable.FlowLayoutView_layoutGravity,Gravity.LEFT);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int childCount = getChildCount();
        int maxWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int totalheight = getPaddingTop() + getPaddingBottom();
        int tempHeight = 0; //用来记录每一行的最大高度
        int lineWidth = getPaddingLeft() + getPaddingRight(); //用来记录测量行的当前高度
        boolean isMultipleRow = false;
        if (childCount == 0) {
            setMeasuredDimension(resolveSize(0,widthMeasureSpec),resolveSize(0,heightMeasureSpec));
        }else {
            for (int i = 0; i < childCount; i++) {
                View childView = getChildAt(i);
                if (childView != null && childView.getVisibility() != GONE){ //为gone 时不占空间
                    MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();

                    int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec, getPaddingLeft() + getPaddingRight() + layoutParams.leftMargin + layoutParams.rightMargin, layoutParams.width);
                    int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec, getPaddingTop() + getPaddingBottom() + layoutParams.topMargin + layoutParams.bottomMargin, layoutParams.height);

                    childView.measure(childWidthMeasureSpec,childHeightMeasureSpec);
                    int chideMeasuredWidth = childView.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
                    int chideMeasuredHeight = childView.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
                    lineWidth = lineWidth + chideMeasuredWidth;
                    if (lineWidth > maxWidthSize) {
                        isMultipleRow = true;
                        totalheight = totalheight + tempHeight;
                        lineWidth = getPaddingLeft() + getPaddingRight() + chideMeasuredWidth;
                        tempHeight = chideMeasuredHeight;
                    }else {
                        tempHeight = Math.max(tempHeight,chideMeasuredHeight);
                    }
                }
            }
            if (isMultipleRow) {
                setMeasuredDimension(maxWidthSize,resolveSize(totalheight,heightMeasureSpec));
            }else {
                setMeasuredDimension(resolveSize(lineWidth,widthMeasureSpec),resolveSize(totalheight,heightMeasureSpec));
            }
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int top = getPaddingTop();
        int maxWidthSize = getMeasuredWidth();
        int rowWidth = getPaddingLeft() + getPaddingRight(); //用来记录测量行的当前高度
        int tempHeight = 0; //用来记录每一行的最大高度
        for (int i = 0; i < childCount; i++) {
            View childView = getChildAt(i);
            if (childView != null && childView.getVisibility() != GONE) {
                LayoutParams layoutParams = (LayoutParams) childView.getLayoutParams();
                int childMeasuredWidth = childView.getMeasuredWidth() + layoutParams.leftMargin + layoutParams.rightMargin;
                int chideMeasuredHeight = childView.getMeasuredHeight() + layoutParams.topMargin + layoutParams.bottomMargin;
                rowWidth = rowWidth + childMeasuredWidth;
                if (rowWidth > maxWidthSize) {
                    layoutRow(top,tempHeight,maxWidthSize,rowWidth - childMeasuredWidth);
                    top = top + tempHeight;
                    rowWidth = childMeasuredWidth + getPaddingLeft() + getPaddingRight();
                    tempHeight = chideMeasuredHeight;

                }else {
                   tempHeight = Math.max(tempHeight,chideMeasuredHeight);
                }
                lineView.add(childView);
            }
        }
        layoutRow(top,tempHeight,maxWidthSize,rowWidth);
    }

    /**
     * 对一行进行布局
     * @param top
     * @param maxChildHeight
     * @param maxWidth
     * @param width
     */
    private void layoutRow(int top,int maxChildHeight,int maxWidth,int width){
        //对这一行进行布局
        int left;
        switch(layoutGravity){
            case Gravity.CENTER:
                left = maxWidth/2 - width/2;
                break;
            case Gravity.RIGHT:
                left = maxWidth - getPaddingRight() - width;
                break;
            default:
                left = getPaddingLeft();
                break;
        }
        for (View view : lineView) {
            LayoutParams params = (LayoutParams) view.getLayoutParams();
            int gravity = params.gravity == -1 ? childDefultGravity : params.gravity;
            switch(gravity){
                case Gravity.BOTTOM:
                    view.layout(left + params.leftMargin,
                            top + maxChildHeight - params.bottomMargin - view.getMeasuredHeight(),
                            left + params.leftMargin + view.getMeasuredWidth(),
                            top+ maxChildHeight - params.bottomMargin);
                    break;
                case Gravity.CENTER:
                    view.layout(left + params.leftMargin ,
                            top + maxChildHeight/2 - view.getMeasuredHeight()/2,
                            left + params.leftMargin + view.getMeasuredWidth(),
                            top + maxChildHeight/2 + view.getMeasuredHeight()/2);
                    break;
                default:
                    view.layout(left + params.leftMargin,
                            top + params.topMargin ,
                            left + params.leftMargin + view.getMeasuredWidth(),
                            top + params.topMargin + view.getMeasuredHeight());
                    break;
            }
            left = left + params.leftMargin + params.rightMargin + view.getMeasuredWidth();
        }
        lineView.clear();
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) { //支持marginlayoutParams

        return new LayoutParams(getContext(),attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        //return super.checkLayoutParams(p);
        return p instanceof LayoutParams;
    }




    public static class LayoutParams extends ViewGroup.MarginLayoutParams{
        public int gravity =-1;
        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
            if (attrs != null) {
                TypedArray typedArray = c.obtainStyledAttributes(attrs, R.styleable.FlowLayoutView_Layout);
                gravity = typedArray.getInt(R.styleable.FlowLayoutView_Layout_flow_gravity,-1);
                typedArray.recycle();
            }
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }





}
