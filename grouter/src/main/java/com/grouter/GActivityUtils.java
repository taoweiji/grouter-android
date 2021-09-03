package com.grouter;

import android.app.Activity;

import com.grouter.core.R;


@SuppressWarnings({"unused", "WeakerAccess", "UnusedReturnValue"})
public class GActivityUtils {


    /**
     * 从右边离开
     * 对应 {@linkplain GActivityBuilder#transitionRightIn}
     */
    public static void finishTransitionRightOut(Activity activity) {
        if (!activity.isFinishing()) {
            activity.finish();
        }
        activity.overridePendingTransition(R.anim.activity_left_to_right_enter, R.anim.activity_left_to_right_exit);
    }

    /**
     * 从左边离开
     * 对应 {@linkplain GActivityBuilder#transitionLeftIn}
     */
    public static void finishTransitionLeftOut(Activity activity) {
        if (!activity.isFinishing()) {
            activity.finish();
        }
        activity.overridePendingTransition(R.anim.activity_right_to_left_enter, R.anim.activity_right_to_left_exit);
    }

    /**
     * 从底部离开
     * 对应 {@linkplain GActivityBuilder#transitionBottomIn}
     */
    public static void finishTransitionBottomOut(Activity activity) {
        if (!activity.isFinishing()) {
            activity.finish();
        }
        activity.overridePendingTransition(0, R.anim.activity_top_to_bottom_exit);
    }

    /**
     * 从顶部离开
     * 对应 {@linkplain GActivityBuilder#transitionTopIn}
     */
    public static void finishTransitionTopOut(Activity activity) {
        if (!activity.isFinishing()) {
            activity.finish();
        }
        activity.overridePendingTransition(0, R.anim.activity_bottom_to_top_exit);
    }


    /**
     * 淡出
     * 对应 {@linkplain GActivityBuilder#transitionFadeIn}
     */
    public static void finishTransitionFadeOut(Activity activity) {
        if (!activity.isFinishing()) {
            activity.finish();
        }
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


    /**
     * 从右边往左边滑出界面，属于常规的进入方式，参考iOS的默认效果
     * 对应 {@linkplain GActivityUtils#finishTransitionRightOut(Activity)}
     */
    public static void transitionRightIn(Activity activity) {
        if (!activity.isFinishing()) {
            activity.finish();
        }
        activity.overridePendingTransition(R.anim.activity_right_to_left_enter, R.anim.activity_right_to_left_exit);
    }

    /**
     * 从底部弹出界面
     * 对应 {@linkplain GActivityUtils#finishTransitionBottomOut(Activity)}
     */
    public static void transitionBottomIn(Activity activity) {
        activity.overridePendingTransition(R.anim.activity_bottom_to_top_enter, R.anim.no_anim);
    }

    /**
     * 从顶部下滑界面
     * 对应 {@linkplain GActivityUtils#finishTransitionTopOut(Activity)}
     */
    public static void transitionTopIn(Activity activity) {
        activity.overridePendingTransition(R.anim.activity_top_to_bottom_enter, R.anim.no_anim);
    }

    /**
     * 从左边往右滑出界面
     * 对应 {@linkplain GActivityUtils#finishTransitionLeftOut(Activity)}
     */
    public static void transitionLeftIn(Activity activity) {
        activity.overridePendingTransition(R.anim.activity_left_to_right_enter, R.anim.activity_left_to_right_exit);
    }

    /**
     * 淡入
     * 对应 {@linkplain GActivityUtils#finishTransitionFadeOut(Activity)}
     */
    public static void transitionFadeIn(Activity activity) {
        activity.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}