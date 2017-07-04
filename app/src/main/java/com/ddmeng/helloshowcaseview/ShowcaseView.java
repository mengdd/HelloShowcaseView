package com.ddmeng.helloshowcaseview;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


public class ShowcaseView extends RelativeLayout {

    private int maskColor;

    private Target target;

    private Paint eraserPaint;
    private Bitmap bitmapBuffer;
    private Canvas bufferCanvas;

    public ShowcaseView(Context context) {
        super(context);
        init(context);
    }

    public ShowcaseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ShowcaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setWillNotDraw(false);
        setVisibility(INVISIBLE);
        maskColor = 0xff123456;

        eraserPaint = new Paint();
        eraserPaint.setColor(0xFFFFFFFF);
        eraserPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        eraserPaint.setFlags(Paint.ANTI_ALIAS_FLAG);

    }

    private void updateBitmap() {
        if (bitmapBuffer == null || haveBoundsChanged()) {
            if (bitmapBuffer != null) {
                bitmapBuffer.recycle();
            }
            bitmapBuffer = Bitmap.createBitmap(getMeasuredWidth(), getMeasuredHeight(), Bitmap.Config.ARGB_8888);
            bufferCanvas = new Canvas(bitmapBuffer);
        }
    }

    private boolean haveBoundsChanged() {
        return getMeasuredWidth() != bitmapBuffer.getWidth() ||
                getMeasuredHeight() != bitmapBuffer.getHeight();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();

        boolean isTouchOnFocus = target.getRect().contains((int) x, (int) y);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                if (isTouchOnFocus) {
                    target.getView().setPressed(true);
                    target.getView().invalidate();
                }

                return true;
            case MotionEvent.ACTION_UP:

                if (target.getView().isPressed()) {
                    dismiss();
                    target.getView().performClick();
                    target.getView().setPressed(false);
                    target.getView().invalidate();
                }

                return true;
            default:
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        updateBitmap();

        bufferCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        bufferCanvas.drawColor(maskColor);

        bufferCanvas.drawRect(target.getRect(), eraserPaint);

        canvas.drawBitmap(bitmapBuffer, 0, 0, null);
    }

    private void addViewToLayout(Activity activity) {
        removeViewFromLayout();
        ((ViewGroup) activity.getWindow().getDecorView()).addView(this);
    }

    private void show() {
        setVisibility(VISIBLE);
    }

    public void dismiss() {
        setVisibility(GONE);
        removeViewFromLayout();
    }

    private void removeViewFromLayout() {
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
    }

    private void setTarget(Target target) {
        this.target = target;
    }

    public void setContentView(@LayoutRes final int contentViewLayout) {
        final View contentView = LayoutInflater.from(this.getContext()).inflate(contentViewLayout, this, false);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(CENTER_IN_PARENT, TRUE);
        contentView.setLayoutParams(layoutParams);
        addView(contentView);
    }

    public void setContentDismissButton(@IdRes final int dismissButtonId) {
        View dismissButton = findViewById(dismissButtonId);
        dismissButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public static class Builder {

        private ShowcaseView showcaseView;
        private Activity activity;

        public Builder(Activity activity) {
            this.activity = activity;
            showcaseView = new ShowcaseView(activity);
        }

        public Builder setTarget(View view) {
            showcaseView.setTarget(new ViewTarget(view));
            return this;
        }

        public Builder setContentView(@LayoutRes int contentView) {
            showcaseView.setContentView(contentView);
            return this;
        }

        public Builder setContentDismissButton(@IdRes int dismissButtonId) {
            showcaseView.setContentDismissButton(dismissButtonId);
            return this;
        }

        public ShowcaseView build() {
            showcaseView.addViewToLayout(activity);
            return showcaseView;
        }

        public ShowcaseView show() {
            build().show();
            return showcaseView;
        }

    }

}
