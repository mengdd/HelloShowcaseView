package com.ddmeng.helloshowcaseview;

import android.graphics.Point;
import android.view.View;

public class ViewTarget implements Target {

    private final View view;

    public ViewTarget(View view) {
        this.view = view;
    }


    @Override
    public Point getPoint() {
        int[] location = new int[2];
        view.getLocationInWindow(location);
        int x = location[0] + view.getWidth() / 2;
        int y = location[1] + view.getHeight() / 2;
        return new Point(x, y);
    }

    @Override
    public int getWidth() {
        return view.getWidth();
    }

    @Override
    public int getHeight() {
        return view.getHeight();
    }
}