package com.ddmeng.helloshowcaseview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;


public class RectShape extends Shape {

    private RectF drawRect;
    private int paddingHorizontal;
    private int paddingVertical;
    private int cornerRadius;

    public RectShape(int paddingHorizontal, int paddingVertical, int cornerRadius) {
        this.paddingHorizontal = paddingHorizontal;
        this.paddingVertical = paddingVertical;
        this.cornerRadius = cornerRadius;
    }

    @Override
    public void calculate() {
        if (drawRect == null) {
            drawRect = new RectF();
        }
        drawRect.set(target.getRect());

        drawRect.left -= paddingHorizontal;
        drawRect.top -= paddingVertical;
        drawRect.right += paddingHorizontal;
        drawRect.bottom += paddingVertical;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawRoundRect(drawRect, cornerRadius, cornerRadius, paint);
    }
}
