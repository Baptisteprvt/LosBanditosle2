package com.example.mygame;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;

public class CircleView extends View {
    private Paint paint;
    private float x;
    private float y;
    private float radius;
    private float angle;
    private int pointerIndex;
    private int pointerId;


    public CircleView(Context context, float x, float y, float radius, int pointerId) {
        super(context);
        this.x = x;
        this.y = y;
        this.radius = radius;
        this.pointerId = pointerId;
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.BLUE);
    }

    public int getPointerId() {
        return pointerId;
    }

    public void updatePosition(float x, float y) {
        this.x = x;
        this.y = y;
        invalidate();
    }

    public int getPointerIndex() {
        return pointerIndex;
    }

    public void setPointerIndex(int pointerIndex) {
        this.pointerIndex = pointerIndex;
    }

    public void remove() {
        ViewGroup parent = (ViewGroup) getParent();
        if (parent != null) {
            parent.removeView(this);
        }
    }

    public static CircleView getCircleViewByPointerId(ViewGroup layout, int pointerId) {
        for (int i = 0; i < layout.getChildCount(); i++) {
            View child = layout.getChildAt(i);
            if (child instanceof CircleView) {
                CircleView circleView = (CircleView) child;
                if (circleView.getPointerIndex() == pointerId) {
                    return circleView;
                }
            }
        }
        return null;
    }

    public static void removeCircleByPointerId(ViewGroup layout, int pointerId) {
        CircleView circleView = getCircleViewByPointerId(layout, pointerId);
        if (circleView != null) {
            circleView.remove();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(x, y, radius, paint);
    }
}