package com.ddmeng.helloshowcaseview;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

public interface Target {
    Point getPoint();

    Rect getRect();

    RectF getRectF();

    int getWidth();

    int getHeight();

    View getView();
}
