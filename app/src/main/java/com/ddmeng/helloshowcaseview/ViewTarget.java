package com.ddmeng.helloshowcaseview;

import android.graphics.Rect;
import android.view.View;


public class ViewTarget implements Target {

    private final View view;
    private Rect rect;
    private int[] location;

    public ViewTarget(View view) {
        this.view = view;
    }

    @Override
    public View getView() {
        return view;
    }

    @Override
    public Rect getRect() {
        if (rect == null) {
            rect = new Rect();
            location = new int[2];
        }
        view.getLocationInWindow(location);
        rect.set(location[0], location[1],
                location[0] + view.getWidth(), location[1] + view.getHeight());
        return rect;
    }
}