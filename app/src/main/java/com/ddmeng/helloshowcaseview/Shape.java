package com.ddmeng.helloshowcaseview;

import android.graphics.Canvas;
import android.graphics.Paint;


public abstract class Shape {
    protected Target target;

    public Shape() {
    }

    public void setTarget(Target target) {
        this.target = target;
    }

    public void onDraw(Canvas canvas, Paint paint) {
        calculate();
        draw(canvas, paint);
    }

    public abstract void calculate();

    public abstract void draw(Canvas canvas, Paint paint);
}
