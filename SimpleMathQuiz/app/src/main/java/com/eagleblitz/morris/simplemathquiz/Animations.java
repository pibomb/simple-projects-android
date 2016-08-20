package com.eagleblitz.morris.simplemathquiz;

import android.app.Activity;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by mcmor on 2016-08-20.
 */
public class Animations {
    public static void fadeSwitch(Activity activity, View from, View to) {
        from.setVisibility(View.VISIBLE);
        fadeOut(activity, from);
        from.setVisibility(View.INVISIBLE);

        to.setVisibility(View.INVISIBLE);
        fadeIn(activity, to);
        to.setVisibility(View.VISIBLE);
    }

    public static Animation fadeIn(Activity ctx, View target) {
        android.view.animation.Animation animation = AnimationUtils.loadAnimation(ctx, R.anim.fade_in);
        target.startAnimation(animation);

        return animation;
    }

    public static Animation fadeOut(Activity ctx, View target) {
        android.view.animation.Animation animation = AnimationUtils.loadAnimation(ctx, R.anim.fade_out);
        target.startAnimation(animation);

        return animation;
    }
}
