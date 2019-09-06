package com.xiaozhanxiang.simplegridview.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.support.annotation.IntDef;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;


import com.xiaozhanxiang.simplegridview.R;
import com.xiaozhanxiang.simplegridview.utils.PaintUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;


/**
 * A~Z快速索引
 * dai
 */
public class SlideBar extends View {


    /**
     * 画笔
     */
    private Paint paint = new Paint();

    /**
     * 选中的字母索引
     */
    private int index = -1;

    /**
     * 字母默认颜色
     */
    private int defaultColor = Color.BLACK;

    /**
     * 字母选中颜色
     */
    private int chooseColor = Color.MAGENTA;

    /**
     * 选中背景颜色
     */
    private int chooseBackgroundColor = Color.LTGRAY;

    /**
     * 是否触摸
     */
    private boolean isTouch;

    /**
     * 字母字体大小
     */
    private int textSize;

    private int padding;

    private Rect rectBound;

    /**
     * 字母改变监听
     */
    private OnTouchLetterChangeListenner onTouchLetterChangeListenner;

    /**
     * 字母数组
     */
    private String[] letters = {"历史", "热门", "A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T",
            "U", "V", "W", "X", "Y", "Z"};

    private static final int DEFAULT = 0;
    private static final int NONE = 1;
    private static final int CIRCLE = 2;
    private static final int STRETCH = 3;
    private @style
    int chooseStyle = DEFAULT;
    private int[] mLocation;
    private int mTipsHeight;
    private int mTipsWidth;
    private int mWidth;
    private int mHeight;
    private ViewGroup mRootView;
    private TextView mTipsView;
    /**
     * 提示是否是显示状态
     */
    private boolean mTipsIsShow;
    private float mTipsTextSize;
    private int mTipsBg;
    private int mTipsTextColor;
    /**
     * 是否显示提示
     */
    private boolean mIsShowTips;
    private AnimatorSet mAnimatorSet;
    private AnimatorSet mAnimatorEndSet;


    @IntDef({DEFAULT, NONE, CIRCLE, STRETCH})
    @Retention(RetentionPolicy.SOURCE)
    //RetentionPolicy.SOURCE 的注解类型的生命周期只存在Java源文件这一阶段，是3种生命周期中最短的注解
    public @interface style {
    }

    public SlideBar(Context context) {
        super(context);
        init(context, null);
    }

    public SlideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public SlideBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        padding = (int) (10 * getResources().getDisplayMetrics().density);
        rectBound = new Rect();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.SlideBar);
        textSize = a.getDimensionPixelSize(R.styleable.SlideBar_slTextSize, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
        defaultColor = a.getColor(R.styleable.SlideBar_slTextColor, Color.BLACK);
        chooseColor = a.getColor(R.styleable.SlideBar_chooseTextColor, Color.MAGENTA);
        chooseBackgroundColor = a.getColor(R.styleable.SlideBar_slideBar_chooseBackgroundColor, Color.LTGRAY);
        chooseStyle = a.getInt(R.styleable.SlideBar_slideBar_style, 0);
        mIsShowTips = a.getBoolean(R.styleable.SlideBar_isShowTips, true);
        mTipsHeight = a.getDimensionPixelOffset(R.styleable.SlideBar_tipsSize, dp2px(65));
        mTipsWidth = a.getDimensionPixelOffset(R.styleable.SlideBar_tipsSize, dp2px(65));
        mTipsTextSize = a.getDimension(R.styleable.SlideBar_tipsTextSize, sp2px(18));
        mTipsBg = a.getColor(R.styleable.SlideBar_tipsBg, 0x44ff4b00);
        mTipsTextColor = a.getColor(R.styleable.SlideBar_tipsTextColor, 0xffff4b00);
        a.recycle();


    }


    /**
     * 设置字母默认色
     *
     * @param color
     */
    public void setDefaultColor(int color) {
        this.defaultColor = color;
    }

    /**
     * 设置字母选中色
     *
     * @param color
     */
    public void setChooseColor(int color) {
        this.chooseColor = color;

    }

    /**
     * 设置选中时控件的背景色
     *
     * @param color
     */
    public void setChooseBacegroundColor(int color) {
        this.chooseBackgroundColor = color;

    }

    /**
     * 设置选中时控件的风格
     *
     * @param style
     */
    public void setChooseStyle(@style int style) {
        this.chooseStyle = style;
    }

    /**
     * 文本字体大小  单位：dp
     *
     * @param size
     */
    public void setTextSize(int size) {
        this.textSize = size;
    }


    public String[] getLetters() {
        return letters;
    }

    /**
     * 设置字母数据
     *
     * @param letters
     */
    public void setLetters(String[] letters) {
        this.letters = letters;
        requestLayout();
    }

    /**
     * 设置字母改变回调监听
     *
     * @param onTouchLetterChangeListenner
     */
    public void setOnTouchLetterChangeListenner(OnTouchLetterChangeListenner onTouchLetterChangeListenner) {
        this.onTouchLetterChangeListenner = onTouchLetterChangeListenner;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        paint.setTextSize(textSize);
        int w = 0;
        int h = 0;
        if (letters != null) {
            for (String s : letters) {
                paint.getTextBounds(s, 0, s.length(), rectBound);
                if (rectBound.width() > w) {
                    w = rectBound.width();
                }
                if (rectBound.height() > h) {
                    h = rectBound.height();
                }
            }
        }
        w += padding;
        h += padding;
        int defaultWidth = getPaddingLeft() + w + getPaddingRight();
        int defaultHeight = getPaddingTop() + h * letters.length + getPaddingBottom();
        int width = measureHandler(widthMeasureSpec, defaultWidth);
        int height = measureHandler(heightMeasureSpec, defaultHeight);
        setMeasuredDimension(width, height);

    }

    private int measureHandler(int measureSpec, int defaultSize) {

        int result = defaultSize;
        int measureMode = MeasureSpec.getMode(measureSpec);
        int measureSize = MeasureSpec.getSize(measureSpec);
        if (measureMode == MeasureSpec.EXACTLY) {
            result = measureSize;
        } else if (measureMode == MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, measureSize);
        }
        return result;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        //字母的个数
        int len = letters.length;
        //单个字母的高度
        int singleHeight = mHeight / len;

        if (isTouch && chooseBackgroundColor != Color.TRANSPARENT && chooseStyle != NONE) { //触摸时画出背景色
            paint.setAntiAlias(true);
            paint.setColor(chooseBackgroundColor);
            if (chooseStyle == CIRCLE) {//选中 圆形背景效果
                float maxVaule = Math.max(mWidth, singleHeight);
                float minVaule = Math.min(mWidth, singleHeight);
                canvas.drawArc(new RectF((maxVaule - minVaule) / 2, singleHeight * (index), singleHeight + (maxVaule - minVaule) / 2, singleHeight * (index) + singleHeight), 0, 360, true, paint);
            } else if (chooseStyle == STRETCH) {//选中背景拉伸效果
                canvas.drawArc(new RectF(0, 0, mWidth, singleHeight * index), 0, 360, true, paint);
            } else {//默认：全椭圆背景效果
                canvas.drawArc(new RectF(0, 0, mWidth, singleHeight), 180, 180, true, paint);
                canvas.drawRect(new RectF(0, singleHeight / 2, mWidth, mHeight - singleHeight / 2), paint);
                canvas.drawArc(new RectF(0, mHeight - singleHeight, mWidth, mHeight), 0, 180, true, paint);

            }
        }

        //画字母
        for (int i = 0; i < len; i++) {

            // 设置字体格式
            paint.setTypeface(Typeface.DEFAULT);
            paint.setTextAlign(Align.CENTER);
            // 抗锯齿
            paint.setAntiAlias(true);
            // 设置字体大小
            paint.setTextSize(textSize);

            if (i == index) {//选中时的画笔颜色
                paint.setColor(chooseColor);
            } else {//未选中时的画笔颜色
                paint.setColor(defaultColor);
            }
            if (isTouch) {//触摸时设为粗体字
                paint.setFakeBoldText(true);
            } else {
                paint.setFakeBoldText(false);
            }

            //要画的字母的x,y坐标
            float x = mWidth / 2;
            //float y = singleHeight*(i+1)- paint.measureText(letters[i])/2;
            float y = singleHeight * (i) + PaintUtils.getTextVerticalCenter(singleHeight, paint.getFontMetrics());
            //画字母
            canvas.drawText(letters[i], x, y, paint);
            //重置画笔
            paint.reset();

        }

    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        //当前选中字母的索引
        final int index = (int) (event.getY() / getHeight() * letters.length);
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            mLocation = new int[2];
            getLocationInWindow(mLocation);
        }


        //老的索引
        int oldIndex = this.index;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                isTouch = true;
                if (index != oldIndex && index >= 0 && index < letters.length) {
                    this.index = index;
                    if (onTouchLetterChangeListenner != null) {//监听回调
                        onTouchLetterChangeListenner.onTouchLetterChange(isTouch, letters[index]);
                    }
                    showTips(index, letters[index]);
                    invalidate();
                }
                //mTipsPop.showPopupWindow(x,y);


                break;
            case MotionEvent.ACTION_MOVE:

                isTouch = true;
                if (index != oldIndex && index >= 0 && index < letters.length) {
                    this.index = index;
                    if (onTouchLetterChangeListenner != null) {//监听回调
                        onTouchLetterChangeListenner.onTouchLetterChange(isTouch, letters[index]);
                    }
                    showTips(index, letters[index]);
                    invalidate();
                }

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                dissMissTips();
                isTouch = false;
                //if(index>=0 && index<letters.length){
                if (onTouchLetterChangeListenner != null) {//监听回调
                    onTouchLetterChangeListenner.onTouchLetterChange(isTouch, "");
                }
                // }
                this.index = -1;
                invalidate();

                break;

            default:
                break;
        }
//        if (index!=oldIndex && index>=0 && index<letters.length) {
//            mTipsPop.setTipsContent(letters[index]);
//        }
        return true;
    }


    /**
     *
     */
    private void addLayoutAnimotor() {
        if (mRootView == null)return;
        LayoutTransition transition = new LayoutTransition();
        AnimatorSet testAnimator = new AnimatorSet();
        ObjectAnimator translateAnim =  ObjectAnimator.ofFloat(null,"translationX",mTipsWidth,0);
        ObjectAnimator scaleAnimX = ObjectAnimator.ofFloat(null,"scaleX",0.1f,1);
        ObjectAnimator scaleAnimY = ObjectAnimator.ofFloat(null,"scaleY",0.1f,1);
        testAnimator.playTogether(translateAnim,scaleAnimX,scaleAnimY);
        testAnimator.setInterpolator(new DecelerateInterpolator());

        //  testAnimator.setDuration(500);
        transition.setAnimator(LayoutTransition.APPEARING,testAnimator);
        transition.setDuration(500);
        transition.setStartDelay(LayoutTransition.APPEARING,0);
        //  transition.setStagger(LayoutTransition.APPEARING,20);

        AnimatorSet  testAnimator2 = new AnimatorSet();
        ObjectAnimator translateAnim2 =  ObjectAnimator.ofFloat(null,"translationX",0,mTipsWidth);
        ObjectAnimator scaleAnimX2 = ObjectAnimator.ofFloat(null,"scaleX",1,0.1f);
        ObjectAnimator scaleAnimY2 = ObjectAnimator.ofFloat(null,"scaleY",1,0.1f);
        testAnimator2.playTogether(translateAnim2,scaleAnimX2,scaleAnimY2);
        testAnimator2.setInterpolator(new AccelerateDecelerateInterpolator());

        transition.setAnimator(LayoutTransition.DISAPPEARING,testAnimator2);
        transition.setAnimator(LayoutTransition.CHANGE_APPEARING,transition.getAnimator(LayoutTransition.CHANGE_APPEARING));
        transition.setAnimator(LayoutTransition.CHANGE_DISAPPEARING,transition.getAnimator(LayoutTransition.CHANGE_DISAPPEARING));
        // transition.setAnimateParentHierarchy(false);
        mRootView.setLayoutTransition(transition);
    }



    private void showTips(int index, String tips) {
        if (!mIsShowTips) return;
        int x = mLocation[0] - mTipsWidth;
        int y = mLocation[1] + index * (mHeight / letters.length) + (mHeight / letters.length) / 2 - mTipsHeight / 2;

        if (mRootView == null) {
            mRootView = (ViewGroup) getRootView();
        }
        if (mRootView == null) return;
//        if (mTipsView == null) {
        if (!mTipsIsShow) {
            mTipsView = new TextView(getContext());
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setShape(GradientDrawable.OVAL);
            gradientDrawable.setColor(mTipsBg);
            mTipsView.setBackground(gradientDrawable);
            mTipsView.setGravity(Gravity.CENTER);
            mTipsView.setTextColor(mTipsTextColor);
            mTipsView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTipsTextSize);
        }
        if (mTipsView == null) {
            mTipsView = new TextView(getContext());
            GradientDrawable gradientDrawable = new GradientDrawable();
            gradientDrawable.setShape(GradientDrawable.OVAL);
            gradientDrawable.setColor(mTipsBg);
            mTipsView.setBackground(gradientDrawable);
            mTipsView.setGravity(Gravity.CENTER);
            mTipsView.setTextColor(mTipsTextColor);
            mTipsView.setTextSize(TypedValue.COMPLEX_UNIT_PX, mTipsTextSize);
        }

//        }
        if (y < 0) {
            y = 0;
        }
        if (y + mTipsHeight > mRootView.getBottom()) {
            y = mRootView.getBottom() - mTipsHeight;
        }
        if (mTipsIsShow) {
            updateTips(x, y, tips);
        } else {
            if (mAnimatorEndSet != null) {
                mAnimatorEndSet.cancel();
            }

            if (mAnimatorSet != null) {
                mAnimatorSet.cancel();
            }

            mTipsView.setText(tips);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(mTipsWidth, mTipsHeight);
            params.topMargin = y;
            params.leftMargin = x;

            mRootView.addView(mTipsView, params);
            mTipsIsShow = true;

            mAnimatorSet = new AnimatorSet();
            ObjectAnimator translateAnim =  ObjectAnimator.ofFloat(mTipsView,"translationX",mTipsWidth,0);
            ObjectAnimator scaleAnimX = ObjectAnimator.ofFloat(mTipsView,"scaleX",0.1f,1);
            ObjectAnimator scaleAnimY = ObjectAnimator.ofFloat(mTipsView,"scaleY",0.1f,1);
            mAnimatorSet.playTogether(translateAnim,scaleAnimX,scaleAnimY);
            mAnimatorSet.setInterpolator(new DecelerateInterpolator());
            mAnimatorSet.setDuration(500);
            mAnimatorSet.start();

        }


    }

    private void updateTips(int x, int y, String tips) {
        if (mTipsIsShow && mTipsView != null) {
            mTipsView.setText(tips);
            int top = mTipsView.getTop();
            int transY = y - top;
            mTipsView.setTranslationY(transY);
            // mTipsView.scrollTo(x,y);
        }

    }

    private void dissMissTips() {
        if (!mIsShowTips) return;
        if (mTipsIsShow) {

            mTipsIsShow = false;


            final TextView tempView = mTipsView;
            if (mAnimatorEndSet != null) {
                mAnimatorEndSet.cancel();
            }

            if (mAnimatorSet != null) {
                mAnimatorSet.cancel();
            }

            mAnimatorEndSet = new AnimatorSet();
            ObjectAnimator translateAnim =  ObjectAnimator.ofFloat(tempView,"translationX",0,mTipsWidth);
            ObjectAnimator scaleAnimX = ObjectAnimator.ofFloat(tempView,"scaleX",1,0.1f);
            ObjectAnimator scaleAnimY = ObjectAnimator.ofFloat(tempView,"scaleY",1,0.1f);
            mAnimatorEndSet.playTogether(translateAnim,scaleAnimX,scaleAnimY);
            mAnimatorEndSet.setInterpolator(new AccelerateDecelerateInterpolator());
            mAnimatorEndSet.setDuration(500);
            mAnimatorEndSet.start();

            mAnimatorEndSet.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    tempView.setTranslationY(0);
                    mRootView.removeView(tempView);

                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    tempView.setTranslationY(0);
                    mRootView.removeView(tempView);

                }
            });
        }

    }


    public int dp2px(final float dpValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }


    public int sp2px(float spValue) {
        final float scale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * scale + 0.5f);
    }

    @Override
    protected void onDetachedFromWindow() {
//        mTipsView.clearAnimation();
        if (mAnimatorSet != null && mAnimatorSet.isRunning()) {
            mAnimatorSet.cancel();
        }
       if (mAnimatorEndSet != null && mAnimatorEndSet.isRunning()) {
           mAnimatorEndSet.cancel();
       }
        super.onDetachedFromWindow();

    }

    //    private ViewGroup findRootView() {
//
//    }

    /**
     * 字母改变监听接口
     */
    public interface OnTouchLetterChangeListenner {

        void onTouchLetterChange(boolean isTouch, String letter);
    }

}

