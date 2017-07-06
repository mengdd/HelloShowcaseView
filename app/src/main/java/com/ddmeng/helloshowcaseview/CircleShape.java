package com.ddmeng.helloshowcaseview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;


public class CircleShape extends Shape {
    private Point center;
    private int radius;
    private int padding;

    public CircleShape(int padding) {
        this.padding = padding;
    }

    @Override
    public void calculate() {
        if (center == null) {
            center = new Point();
        }
        Rect rect = target.getRect();
        int halfWidth = rect.width() / 2;
        int halfHeight = rect.height() / 2;
        center.set(rect.left + halfWidth, rect.top + halfHeight);
        radius = Math.max(halfWidth, halfHeight) + padding;
    }

    @Override
    public void draw(Canvas canvas, Paint paint) {
        canvas.drawCircle(center.x, center.y, radius, paint);
    }
}
