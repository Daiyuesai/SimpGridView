package com.xiaozhanxiang.simplegridview.view.test;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * author: dai
 * date:2019/8/16
 */
public class TestDecoration extends RecyclerView.ItemDecoration {


    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        //outRect.set(3,3,3,3);

    }

    @Override
    public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
        RectF rectF = new RectF();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.BLUE);
        for (int i = 0; i < parent.getChildCount(); i++) {
            View childAt = parent.getChildAt(i);

            rectF.set(childAt.getLeft() - 3,childAt.getTop() - 3,childAt.getRight() + 3,childAt.getTop());
            c.drawRect(rectF,paint);
            rectF.set(childAt.getLeft() - 3,childAt.getTop(),childAt.getLeft(),childAt.getBottom());
            c.drawRect(rectF,paint);
            rectF.set(childAt.getLeft() - 3,childAt.getBottom() ,childAt.getRight() + 3,childAt.getBottom() + 3);
            c.drawRect(rectF,paint);

            rectF.set(childAt.getRight() ,childAt.getTop(),childAt.getRight() + 3,childAt.getBottom());
            c.drawRect(rectF,paint);
        }
    }

    @Override
    public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
//        RectF rectF = new RectF();
//        Paint paint = new Paint();
//        paint.setAntiAlias(true);
//        paint.setStyle(Paint.Style.FILL);
//        paint.setColor(Color.BLUE);
//        for (int i = 0; i < parent.getChildCount(); i++) {
//            View childAt = parent.getChildAt(i);
//
//            rectF.set(childAt.getLeft() - 3,childAt.getTop() - 3,childAt.getRight() + 3,childAt.getTop());
//            c.drawRect(rectF,paint);
//            rectF.set(childAt.getLeft() - 3,childAt.getTop(),childAt.getLeft(),childAt.getBottom());
//            c.drawRect(rectF,paint);
//            rectF.set(childAt.getLeft() - 3,childAt.getBottom() ,childAt.getRight() + 3,childAt.getBottom() + 3);
//            c.drawRect(rectF,paint);
//
//            rectF.set(childAt.getRight() ,childAt.getTop(),childAt.getRight() + 3,childAt.getBottom());
//            c.drawRect(rectF,paint);
//        }
    }


}
