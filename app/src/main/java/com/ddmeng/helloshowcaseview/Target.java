package com.ddmeng.helloshowcaseview;

import android.graphics.Point;
import android.graphics.Rect;

public interface Target {
    Point getPoint();

    Rect getRect();

    int getWidth();

    int getHeight();
}
