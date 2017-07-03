package com.ddmeng.helloshowcaseview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

public class ShowcaseView extends RelativeLayout {
    private Bitmap bitmapBuffer;
    private boolean hasNoTarget = false;
    private final int[] positionInWindow = new int[2];
    private ShowcaseDrawer showcaseDrawer;
    private int showcaseX = -1;
    private int showcaseY = -1;
    private int showcaseWidth = -1;
    private int showcaseHeight = -1;

    public ShowcaseView(Context context) {
        super(context);
        init();
    }

    public ShowcaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ShowcaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        showcaseDrawer = new ShowcaseDrawer();
        int backgroundColor = Color.argb(128, 80, 80, 80);
        showcaseDrawer.setBackgroundColour(backgroundColor);
        setBackgroundColor(backgroundColor);
    }

    public void show() {
        if (canUpdateBitmap()) {
            updateBitmap();
        }
        setVisibility(VISIBLE);
    }

    public void hide() {
        setVisibility(GONE);
    }

    public void setTarget(final Target target) {
        setShowcase(target);
    }

    public void setShowcase(final Target target) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                if (canUpdateBitmap()) {
                    updateBitmap();
                }

                Point targetPoint = target.getPoint();
                if (targetPoint != null) {
                    hasNoTarget = false;
                    setShowcasePosition(targetPoint);
                    showcaseWidth = target.getWidth();
                    showcaseHeight = target.getHeight();
                } else {
                    hasNoTarget = true;
                    invalidate();
                }

            }
        }, 100);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (showcaseX < 0 || showcaseY < 0 || bitmapBuffer == null) {
            super.dispatchDraw(canvas);
            return;
        }

        //Draw background color
        showcaseDrawer.erase(bitmapBuffer);

        // Draw the showcase drawable
        if (!hasNoTarget) {
            showcaseDrawer.drawShowcase(bitmapBuffer, showcaseX, showcaseY, showcaseWidth, showcaseHeight);
            showcaseDrawer.drawToCanvas(canvas, bitmapBuffer);
        }

        super.dispatchDraw(canvas);

    }

    void setShowcasePosition(Point point) {
        setShowcasePosition(point.x, point.y);
    }

    void setShowcasePosition(int x, int y) {

        getLocationInWindow(positionInWindow);
        showcaseX = x - positionInWindow[0];
        showcaseY = y - positionInWindow[1];
        invalidate();
    }

    private boolean canUpdateBitmap() {
        return getMeasuredHeight() > 0 && getMeasuredWidth() > 0;
    }

    private void updateBitmap() {
        if (bitmapBuffer == null || haveBoundsChanged()) {
            if (bitmapBuffer != null) {
                bitmapBuffer.recycle();
            }
            bitmapBuffer = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        }
    }

    private boolean haveBoundsChanged() {
        return getMeasuredWidth() != bitmapBuffer.getWidth() ||
                getMeasuredHeight() != bitmapBuffer.getHeight();
    }


    private static void insertShowcaseView(ShowcaseView showcaseView, ViewGroup parent, int parentIndex) {
        parent.addView(showcaseView, parentIndex);
        showcaseView.show();

    }

    public static class Builder {
        private final Activity activity;
        private final ShowcaseView showcaseView;
        private ViewGroup parent;
        private int parentIndex;

        public Builder(Activity activity) {
            this.activity = activity;
            this.showcaseView = new ShowcaseView(activity);
            this.parent = (ViewGroup) activity.findViewById(android.R.id.content);
            this.parentIndex = parent.getChildCount();
        }

        public Builder setTarget(Target target) {
            showcaseView.setTarget(target);
            return this;
        }

        public ShowcaseView build() {
            insertShowcaseView(showcaseView, parent, parentIndex);
            return showcaseView;
        }

    }

}
