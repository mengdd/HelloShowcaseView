package com.ddmeng.helloshowcaseview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.ColorInt;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;


public class ShowcaseView extends RelativeLayout implements View.OnLayoutChangeListener {

    public static final int DEFAULT_ANIMATION_DURATION = 300;
    public static final int DEFAULT_MASK_COLOR = 0x80000000;

    private int maskColor = DEFAULT_MASK_COLOR;
    private Target target;

    private Paint eraserPaint;
    private Bitmap bitmapBuffer;
    private Canvas bufferCanvas;
    private Shape shape;

    private boolean isFadeInEnabled;
    private boolean isFadeOutEnabled;
    private long fadeInDuration = DEFAULT_ANIMATION_DURATION;
    private long fadeOutDuration = DEFAULT_ANIMATION_DURATION;

    private ShowcaseViewEventListener eventListener;

    public interface ShowcaseViewEventListener {
        void onTargetViewClicked();

        void onContentDismissButtonClicked();

        void onDismissed();
    }

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

        eraserPaint = new Paint();
        eraserPaint.setColor(Color.WHITE);
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

                    if (eventListener != null) {
                        eventListener.onTargetViewClicked();
                    }
                }

                return true;
            default:
                break;
        }

        return super.onTouchEvent(event);
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom,
                               int oldLeft, int oldTop, int oldRight, int oldBottom) {
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (getMeasuredWidth() * getMeasuredHeight() == 0) {
            return;
        }
        updateBitmap();

        bufferCanvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        bufferCanvas.drawColor(maskColor);

        shape.onDraw(bufferCanvas, eraserPaint);

        canvas.drawBitmap(bitmapBuffer, 0, 0, null);
    }

    private void addViewToLayout(Activity activity) {
        removeViewFromLayout();
        ((ViewGroup) activity.getWindow().getDecorView()).addView(this);
    }

    public boolean isShowing() {
        return getParent() != null && getVisibility() == VISIBLE;
    }

    public void show() {
        if (isFadeInEnabled) {
            AnimationFactory.fadeIn(this, fadeInDuration, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    setVisibility(VISIBLE);
                }
            });

        } else {
            setVisibility(VISIBLE);
        }
    }

    public void dismiss() {
        if (isFadeOutEnabled) {
            AnimationFactory.fadeOut(this, fadeOutDuration, new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    doDismiss();
                }
            });
        } else {
            doDismiss();
        }
    }

    public void dismissWithoutAnimation() {
        doDismiss();
    }

    private void doDismiss() {
        target.getView().removeOnLayoutChangeListener(this);
        setVisibility(GONE);
        removeViewFromLayout();
        if (eventListener != null) {
            eventListener.onDismissed();
        }
    }

    private void removeViewFromLayout() {
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }
    }

    public void setTarget(Target target) {
        this.target = target;
        target.getView().addOnLayoutChangeListener(this);
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    public void setFadeInEnabled(boolean fadeInEnabled) {
        isFadeInEnabled = fadeInEnabled;
    }

    public void setFadeOutEnabled(boolean fadeOutEnabled) {
        isFadeOutEnabled = fadeOutEnabled;
    }

    public void setFadeInDuration(long fadeInDuration) {
        this.fadeInDuration = fadeInDuration;
    }

    public void setFadeOutDuration(long fadeOutDuration) {
        this.fadeOutDuration = fadeOutDuration;
    }

    public void setMaskColor(@ColorInt int maskColor) {
        this.maskColor = maskColor;
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
                if (eventListener != null) {
                    eventListener.onContentDismissButtonClicked();
                }
            }
        });
    }

    public void setEventListener(ShowcaseViewEventListener eventListener) {
        this.eventListener = eventListener;
    }

    public static class Builder {

        private ShowcaseView showcaseView;
        private Activity activity;

        public Builder(Activity activity) {
            this.activity = activity;
            showcaseView = new ShowcaseView(activity);
        }

        public Builder setTarget(View view) {
            if (view == null) {
                throw new IllegalArgumentException("the target view is null!");
            }
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

        public Builder setMaskColor(@ColorInt int maskColor) {
            showcaseView.setMaskColor(maskColor);
            return this;
        }

        public Builder setFadeInEnabled(boolean fadeInEnabled) {
            showcaseView.setFadeInEnabled(fadeInEnabled);
            return this;
        }

        public Builder setFadeOutEnabled(boolean fadeOutEnabled) {
            showcaseView.setFadeOutEnabled(fadeOutEnabled);
            return this;
        }

        public Builder setFadeInDuration(long fadeInDuration) {
            showcaseView.setFadeInDuration(fadeInDuration);
            return this;
        }

        public Builder setFadeOutDuration(long fadeOutDuration) {
            showcaseView.setFadeOutDuration(fadeOutDuration);
            return this;
        }

        public Builder setEventListener(ShowcaseViewEventListener listener) {
            showcaseView.setEventListener(listener);
            return this;
        }

        public Builder setShape(Shape shape) {
            showcaseView.setShape(shape);
            return this;
        }

        public ShowcaseView build() {
            showcaseView.addViewToLayout(activity);
            if (showcaseView.shape == null) {
                showcaseView.setShape(new RectShape(0, 0, 0));
            }
            showcaseView.shape.setTarget(showcaseView.target);
            return showcaseView;
        }

        public ShowcaseView show() {
            build().show();
            return showcaseView;
        }
    }
}
