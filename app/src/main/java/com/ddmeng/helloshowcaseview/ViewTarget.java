package com.ddmeng.helloshowcaseview;

import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.view.View;

public class ViewTarget implements Target {

    private final View view;
    private Rect rect;
    private RectF rectF;

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
    public Rect getRect() {
        if (rect == null) {
            int[] location = new int[2];
            view.getLocationInWindow(location);
            rect = new Rect(
                    location[0],
                    location[1],
                    location[0] + view.getWidth(),
                    location[1] + view.getHeight()
            );
        }
        return rect;
    }

    @Override
    public RectF getRectF() {
        if (rectF == null) {
            rectF = new RectF();
            rectF.set(getRect());
        }
        return rectF;
    }

    @Override
    public View getView() {
        return view;
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