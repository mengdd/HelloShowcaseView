package com.ddmeng.helloshowcaseview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;

public class AnimationFactory {
    public static void fadeIn(View targetView, long duration, final Animator.AnimatorListener listener) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(targetView, "alpha", 0f, 1f);
        objectAnimator.setDuration(duration);
        objectAnimator.addListener(listener);
        objectAnimator.start();
    }

    public static void fadeOut(View targetView, long duration, final Animator.AnimatorListener listener) {
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(targetView, "alpha", 1, 0);
        objectAnimator.setDuration(duration);
        objectAnimator.addListener(listener);
        objectAnimator.start();
    }
}
