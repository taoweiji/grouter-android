package com.grouter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.grouter.core.R;

import java.io.NotSerializableException;
import java.io.Serializable;
import java.util.List;


@SuppressWarnings({"unused", "WeakerAccess", "UnusedReturnValue"})
public class GActivityBuilder implements Parcelable {

    String activityClass;
    Bundle extras = new Bundle();
    Uri data;
    int enterAnim;
    int exitAnim;
    int flags;

    public static String NEXT_NAV_ACTIVITY_BUILDER = "NEXT_NAV_ACTIVITY_BUILDER";

    // 可以设置默认的转场动画
    public static int defaultEnterAnim = 0;
    // 可以设置默认的转场动画
    public static int defaultExitAnim = 0;

    protected GActivityBuilder(String activityClass) {
        this.activityClass = activityClass;
        this.enterAnim = defaultEnterAnim;
        this.exitAnim = defaultExitAnim;
    }

    protected GActivityBuilder(String activityClass, Uri data) {
        this(activityClass);
        this.data = data;
    }

    GActivityBuilder(String activityClass, Uri data, Bundle extras) {
        this(activityClass, data);
        this.extras = extras;
    }

    public GActivityBuilder put(String key, String value) {
        extras.putString(key, value);
        return this;
    }

    public GActivityBuilder put(String key, double value) {
        extras.putDouble(key, value);
        return this;
    }

    public GActivityBuilder putAll(Bundle bundle) {
        extras.putAll(bundle);
        return this;
    }


    public GActivityBuilder put(String key, double[] value) {
        extras.putDoubleArray(key, value);
        return this;
    }

    public GActivityBuilder put(String key, float value) {
        extras.putFloat(key, value);
        return this;
    }


    public GActivityBuilder put(String key, float[] value) {
        extras.putFloatArray(key, value);
        return this;
    }

    public GActivityBuilder put(String key, int value) {
        extras.putInt(key, value);
        return this;
    }

    public GActivityBuilder put(String key, int[] value) {
        extras.putIntArray(key, value);
        return this;
    }

    public GActivityBuilder put(String key, boolean value) {
        extras.putBoolean(key, value);
        return this;
    }

    public GActivityBuilder put(String key, boolean[] value) {
        extras.putBooleanArray(key, value);
        return this;
    }

    public GActivityBuilder put(String key, long value) {
        extras.putLong(key, value);
        return this;
    }

    public GActivityBuilder put(String key, long[] value) {
        extras.putLongArray(key, value);
        return this;
    }


    public GActivityBuilder put(String key, Object value) {
        if (GRouter.getSerialization() == null) {
            LoggerUtils.handleException(LoggerUtils.getSerializationException());
            return this;
        }
        if (value == null) {
            return this;
        }
        if (!Serializable.class.isAssignableFrom(value.getClass())) {
            LoggerUtils.handleException(new NotSerializableException(value.getClass().getName()));
        }
        extras.putString(key, GRouter.getSerialization().serialize(value));
        return this;
    }


    public GActivityBuilder put(String key, List value) {
        if (GRouter.getSerialization() == null) {
            LoggerUtils.handleException(LoggerUtils.getSerializationException());
            return this;
        }
        if (value.size() > 0 && !Serializable.class.isAssignableFrom(value.get(0).getClass())) {
            LoggerUtils.handleException(new NotSerializableException(value.get(0).getClass().getName()));
        }
        extras.putString(key, GRouter.getSerialization().serialize(value));
        return this;
    }

    public GActivityBuilder setData(Uri data) {
        this.data = data;
        return this;
    }

    public Intent getIntent(Context context) {
        Intent intent = new Intent();
        intent.putExtras(extras);
        intent.setClassName(context, activityClass);
        if (data != null) {
            intent.setData(data);
        }
        intent.addFlags(flags);
        return intent;
    }

    public GActivityBuilder addFlags(int flags) {
        this.flags |= flags;
        return this;
    }

    /**
     * 下一步跳转的页面
     */
    public GActivityBuilder nextNav(GActivityBuilder builder) {
        if (builder == null) {
            return this;
        }
        extras.putParcelable(NEXT_NAV_ACTIVITY_BUILDER, builder);
        return this;
    }

    private ActivityRequest getActivityRequest() {
        return new ActivityRequest(this);
    }

    public GActivityBuilder overridePendingTransition(int enterAnim, int exitAnim) {
        this.enterAnim = enterAnim;
        this.exitAnim = exitAnim;
        return this;
    }

    /**
     * 从右边往左边滑出界面，参考iOS的默认效果
     * 对应 {@linkplain GActivityUtils#finishTransitionRightOut(Activity)}
     */
    public GActivityBuilder transitionRightIn() {
        return overridePendingTransition(R.anim.activity_right_to_left_enter, R.anim.activity_right_to_left_exit);
    }

    /**
     * 从底部弹出界面
     * 对应 {@linkplain GActivityUtils#finishTransitionBottomOut(Activity)}
     */
    public GActivityBuilder transitionBottomIn() {
        return overridePendingTransition(R.anim.activity_bottom_to_top_enter, R.anim.no_anim);
    }

    /**
     * 从顶部下滑界面
     * 对应 {@linkplain GActivityUtils#finishTransitionTopOut(Activity)}
     */
    public GActivityBuilder transitionTopIn() {
        return overridePendingTransition(R.anim.activity_top_to_bottom_enter, R.anim.no_anim);
    }

    /**
     * 从左边往右滑出界面
     * 对应 {@linkplain GActivityUtils#finishTransitionLeftOut(Activity)}
     */
    public GActivityBuilder transitionLeftIn() {
        return overridePendingTransition(R.anim.activity_left_to_right_enter, R.anim.activity_left_to_right_exit);
    }

    /**
     * 淡入
     * 对应 {@linkplain GActivityUtils#finishTransitionFadeOut(Activity)}
     */
    public GActivityBuilder transitionFadeIn() {
        return overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }


    public Result startForResult(Activity activity, int requestCode) {
        return startForResult(activity, requestCode, null);
    }

    public Result startForResult(Activity activity, int requestCode, @Nullable Bundle options) {
        ActivityRequest request = getActivityRequest();
        request.setContext(activity);
        request.setRequestCode(requestCode);
        try {
            if (InterceptorUtils.processActivity(request).isInterrupt()) {
                return new Result(request, false);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                activity.startActivityForResult(request.getIntent().addFlags(flags), requestCode, options);
            }else {
                activity.startActivityForResult(request.getIntent().addFlags(flags), requestCode);
            }
            if (enterAnim != 0 || exitAnim != 0) {
                activity.overridePendingTransition(enterAnim, exitAnim);
            }
            return new Result(request, true);
        } catch (Exception e) {
            InterceptorUtils.onActivityError(request, e);
        }
        return new Result(request, false);
    }

    public Result startForResult(Fragment fragment, int requestCode) {
        return startForResult(fragment, requestCode, null);
    }

    public Result startForResult(Fragment fragment, int requestCode, @Nullable Bundle options) {
        if (fragment.getActivity() == null) {
            LoggerUtils.handleException(new NullPointerException("fragment.getActivity() == null"));
            return new Result(false);
        }
        ActivityRequest request = getActivityRequest();
        request.setContext(fragment.getActivity());
        request.setRequestCode(requestCode);

        try {
            if (InterceptorUtils.processActivity(request).isInterrupt()) {
                return new Result(request, false);
            }
            fragment.startActivityForResult(request.getIntent().addFlags(flags), requestCode, options);
            if (enterAnim != 0) {
                fragment.getActivity().overridePendingTransition(enterAnim, exitAnim);
            }
            return new Result(request, true);
        } catch (Exception e) {
            InterceptorUtils.onActivityError(request, e);
        }
        return new Result(request, false);
    }

    public Result start(Fragment fragment) {
        return start(fragment, null);
    }

    public Result start(Fragment fragment, @Nullable Bundle options) {
        return startForResult(fragment, -1, options);
    }

    public Result start(Activity activity) {
        return start(activity, null);
    }

    public Result start(Activity activity, @Nullable Bundle options) {
        return startForResult(activity, -1, options);
    }

    public Result start(Context context) {
        if (context instanceof Activity) {
            return start((Activity) context);
        }
        ActivityRequest request = getActivityRequest();
        request.setContext(context);
        try {
            if (InterceptorUtils.processActivity(request).isInterrupt()) {
                return new Result(request, false);
            }
            Intent intent = request.getIntent().addFlags(flags);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
            return new Result(request, true);
        } catch (Exception e) {
            InterceptorUtils.onActivityError(request, e);
        }
        return new Result(request, false);
    }

    public static class Result {
        ActivityRequest request;
        private boolean succeed;

        public Result(ActivityRequest request, boolean succeed) {
            this.request = request;
            this.succeed = succeed;
        }

        public Result(boolean succeed) {
            this.succeed = succeed;
        }

        public boolean isSucceed() {
            return succeed;
        }
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.activityClass);
        dest.writeBundle(this.extras);
        dest.writeParcelable(this.data, flags);
        dest.writeInt(this.enterAnim);
        dest.writeInt(this.exitAnim);
        dest.writeInt(this.flags);
    }

    protected GActivityBuilder(Parcel in) {
        this.activityClass = in.readString();
        this.extras = in.readBundle(getClass().getClassLoader());
        this.data = in.readParcelable(Uri.class.getClassLoader());
        this.enterAnim = in.readInt();
        this.exitAnim = in.readInt();
        this.flags = in.readInt();
    }

    public static final Parcelable.Creator<GActivityBuilder> CREATOR = new Parcelable.Creator<GActivityBuilder>() {
        @Override
        public GActivityBuilder createFromParcel(Parcel source) {
            return new GActivityBuilder(source);
        }

        @Override
        public GActivityBuilder[] newArray(int size) {
            return new GActivityBuilder[size];
        }
    };
}