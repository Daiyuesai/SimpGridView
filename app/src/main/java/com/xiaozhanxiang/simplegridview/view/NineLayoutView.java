package com.xiaozhanxiang.simplegridview.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.xiaozhanxiang.simplegridview.R;

import java.util.ArrayList;
import java.util.List;

/**
 * author: dai
 * date:2019/2/27
 */
public class NineLayoutView extends ViewGroup implements InAdapter.OnDataChangeListener {

    private int mMiddleSpace;
    private int mChildWidth;
    private int mSingleMaxSize = -1;

    private InAdapter mAdapter;

    public List<InViewHodler> mViewHodlers = new ArrayList<>();
    public List<InViewHodler> recyleViewHodlers = new ArrayList<>();


    public NineLayoutView(Context context) {
        this(context,null);
    }

    public NineLayoutView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public NineLayoutView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.NineLayoutView);
            mMiddleSpace = array.getDimensionPixelOffset(R.styleable.NineLayoutView_nine_middleSpace,0);
            mSingleMaxSize = array.getDimensionPixelSize(R.styleable.NineLayoutView_singleMaxSize,-1);
            array.recycle();
        }
    }

    /** 可以不考虑子控件的margin 和自身的padding
     * @param widthMeasureSpec
     * @param heightMeasureSpec //默认高位包裹内容 ,MeasureSpec.UNSPECIFIED模式
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int childCount = getChildCount();
        if (childCount == 0) {
            setMeasuredDimension(resolveSize(getPaddingLeft()+getPaddingRight(),widthMeasureSpec),resolveSize(getPaddingBottom()+getPaddingTop(),heightMeasureSpec));
        }else if (childCount == 1){ //只有一张图片，

            View childView = getChildAt(0);
            if (childView != null) {
                //这里要保证图片不变形 ，并且可以设置宽高的最大值
                if (mSingleMaxSize == -1) {
                    mSingleMaxSize = widthSize;
                }
                MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
                //考虑padding 和 子控件的margin

                int maxWidth = Math.min(mSingleMaxSize,widthSize - getPaddingLeft() - getPaddingRight() - layoutParams.leftMargin - layoutParams.rightMargin);


                //第一次测量，完全包裹内容
                childView.measure(MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED),MeasureSpec.makeMeasureSpec(0,MeasureSpec.UNSPECIFIED));
                int childMeasuredWidth = childView.getMeasuredWidth();
                int childMeasuredHeight = childView.getMeasuredHeight();
                float scale = Math.min(maxWidth*1f/childMeasuredWidth,mSingleMaxSize*1f/childMeasuredHeight);
                if (scale < 1f){
                    //小于1 需要缩放，重新测量子view 的宽高
                    childView.measure(MeasureSpec.makeMeasureSpec((int) (childMeasuredWidth *scale),MeasureSpec.EXACTLY),MeasureSpec.makeMeasureSpec((int) (heightMeasureSpec * scale),MeasureSpec.EXACTLY));
                    childMeasuredWidth = childView.getMeasuredWidth();
                    childMeasuredHeight = childView.getMeasuredHeight();
                }
                setMeasuredDimension(resolveSize(childMeasuredWidth + getPaddingLeft() + getPaddingRight() + layoutParams.leftMargin + layoutParams.rightMargin,widthMeasureSpec),
                        resolveSize(childMeasuredHeight + getPaddingTop()+getPaddingBottom() + layoutParams.topMargin+ layoutParams.bottomMargin,heightMeasureSpec));
            }

        }else if (childCount == 4) {
            mChildWidth = (widthSize - 2*mMiddleSpace)/3;
            int resultHeight = 0;
            for (int i = 0; i < 2; i++) {
                int childHeight = 0;
                for (int j = 0; j < 2; j++){
                    int index = i * 2 + j;
                    if (index < childCount) {
                        //这里不考虑直接子view 为View.GONE的状态
                        View childView = getChildAt(index);
                        if (childView != null) {

                            //不考虑子view 的 margin 和自身的padding
                            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mChildWidth,MeasureSpec.EXACTLY);
                            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(mChildWidth,MeasureSpec.EXACTLY);
                            childView.measure(childWidthMeasureSpec,childHeightMeasureSpec);

                            int childMeasuredHeight = childView.getMeasuredHeight() ;
                            childHeight = Math.max(childMeasuredHeight,childHeight);
                        }
                    }else {
                        //已经测量完成  结束循环
                        break;
                    }
                }
                resultHeight = resultHeight +  childHeight ;
                if ( i != 1){
                    resultHeight += mMiddleSpace;
                }
            }
            setMeasuredDimension(widthSize,resolveSize(resultHeight,heightMeasureSpec));
        }else {
            //默认是 3列
            int line = childCount / 3;
            if (childCount %3 != 0) {
                line = line + 1;
            }
            int resultHeight =0;
            mChildWidth = (widthSize - 2*mMiddleSpace)/3;

            for (int i = 0; i < line; i++) { //一行一行测量
                int childHeight = 0;
                for (int j = 0; j < 3; j++) {
                    int index = i * 3 + j;
                    if (index < childCount) {
                        //这里不考虑直接子view 为View.GONE的状态
                        View childView = getChildAt(index);
                        if (childView != null) {
                            //默认父控件为 MATCH_PARENT
                            //不考虑子view 的 margin 和自身的padding
                            int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(mChildWidth,MeasureSpec.EXACTLY);
                            int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(mChildWidth,MeasureSpec.EXACTLY);
                            childView.measure(childWidthMeasureSpec,childHeightMeasureSpec);

                            int childMeasuredHeight = childView.getMeasuredHeight();
                            childHeight = Math.max(childMeasuredHeight, childHeight);
                        }
                    } else {
                        //已经测量完成  结束循环
                        break;
                    }
                }
                resultHeight = resultHeight + childHeight;
                if ( i != line -1){
                    resultHeight += mMiddleSpace;
                }
            }
            setMeasuredDimension(widthSize,resolveSize(resultHeight,heightMeasureSpec));
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        if (childCount == 1) {
            View childView = getChildAt(0);
            if (childView != null) {  //考虑padding 和 子控件的margin
                MarginLayoutParams layoutParams = (MarginLayoutParams) childView.getLayoutParams();
                childView.layout(getPaddingLeft() +layoutParams.leftMargin,getPaddingTop() + layoutParams.topMargin,childView.getMeasuredWidth(),childView.getMeasuredHeight());
            }
        }else if (childCount == 4) {
            layoutGrid(childCount,2);
        }else {
            layoutGrid(childCount,3);
        }
    }

    private void layoutGrid(int childCount,int mColumn) {
        int top = getPaddingTop();
        int left ;
        int line = childCount / mColumn;
        if (childCount %mColumn != 0) {
            line = line + 1;
        }

        for (int i = 0; i < line; i++) { //一行一行的排版
            int childHeight = 0;
            for (int j = 0; j < mColumn; j++) {
                int index = i * mColumn + j;
                if (index < childCount) {
                    View childView = getChildAt(index);
                    if (childView != null) {
                        left =  j*(mMiddleSpace + mChildWidth) ;
                        int childMeasuredHeight = childView.getMeasuredHeight();
                        childView.layout(left,top,left + childView.getMeasuredWidth(),top + childMeasuredHeight);
                        childHeight = Math.max(childHeight,childView.getMeasuredHeight());
                    }
                }
            }
            top = top + childHeight;
            if (i != line -1) {
                top += mMiddleSpace;
            }
        }
    }



    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) { //支持marginlayoutParams
        return new MarginLayoutParams(getContext(),attrs);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return super.generateLayoutParams(p);
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
    }


    /**
     * 重新设置adapter
     * @param adapter
     */
    public void setAdapter(NineAdapter adapter){
        if (mAdapter != null) {
            mAdapter.removeOnDataChangeListener(this);
        }
        mAdapter = adapter;
        if (mAdapter != null) {
            mAdapter.addOnDataChangeListener(this);
        }

        //removeAllViews();
//        mViewHodlers.clear();
//        recyleViewHodlers.clear();
        updateUi();


    }

    private void updateUi() {
        if (mAdapter != null) {
            int itemCount = mAdapter.getItemCount();
            if (mViewHodlers.size() > itemCount) {
                for (int i = mViewHodlers.size() -1 ; i >= itemCount; i--) {//把多余得view 放入待回收集合中
                    recyleViewHodlers.add(mViewHodlers.remove(i));
                }
            }else {
                for (int i = mViewHodlers.size(); i < itemCount; i++) {
                    InViewHodler viewHolder = getRecyleViewHolder();
                    if (viewHolder == null) {
                        viewHolder = mAdapter.onCreateViewHolder(this,i);
                    }
                    mViewHodlers.add(viewHolder);
                }
            }

            for (int i = 0; i < mViewHodlers.size(); i++) {
                mAdapter.onBindViewHolder(mViewHodlers.get(i),i);
            }
            checkView();
        }
    }

    private void checkView() {
        int childCount = getChildCount();
        int minCount = Math.min(mViewHodlers.size(),childCount);
        for (int i = 0; i < minCount; i++) {
            View childAt = getChildAt(i);
            if (childAt != mViewHodlers.get(i).getContentView()) {
                removeViewAt(i);
                addView(mViewHodlers.get(i).getContentView(),i);
            }
        }
        if (childCount < mViewHodlers.size()) {
            for (int i = childCount; i < mViewHodlers.size(); i++) {
                addView(mViewHodlers.get(i).getContentView());
            }
        }else {
            for (int i = childCount-1; i >= mViewHodlers.size(); i--) {
                removeViewAt(i);
            }
        }
    }

    public InAdapter getAdapter() {
        return mAdapter;
    }


    private InViewHodler getRecyleViewHolder(){
        if (recyleViewHodlers.size() == 0)return null;
        return recyleViewHodlers.remove(0);
    }

    @Override
    public void onDataChange() {
        updateUi();
    }



    public static abstract class NineAdapter extends InAdapter<String> {

        public NineAdapter(Context context) {
            super(0, context);
        }

        @Override
        protected void convert(InViewHodler hodler, String bean) {
            AppCompatImageView imageView = (AppCompatImageView) hodler.getContentView();
            displayIamge(imageView,bean,hodler.getPosition());
        }

        @Override
        protected View getView(ViewGroup viewGroup, int position) {
            AppCompatImageView imageView = new AppCompatImageView(mContext);
            imageView.setScaleType(getScaleType());
            //设置参数其实没有意义
//            MarginLayoutParams params = new MarginLayoutParams(LayoutParams.WRAP_CONTENT, FlowLayoutView.LayoutParams.WRAP_CONTENT);
//            imageView.setLayoutParams(params);
            return imageView;
        }

        /**
         * 再使用 Glide 加载图片的时，注意需要调用 dontTransform()方法，保证Glide不图原图做变换
         * 或者调用override 再在displayIamge中动态设置图片的ScaleType
         * @param imageView
         * @param url
         * @param position
         */
        protected abstract void displayIamge(ImageView imageView , String url,int position);

        protected ImageView.ScaleType getScaleType(){
            return ImageView.ScaleType.CENTER_CROP;
        }
    }

}
