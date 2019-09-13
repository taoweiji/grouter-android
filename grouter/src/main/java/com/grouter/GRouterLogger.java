package com.grouter;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

public abstract class GRouterLogger {
    public abstract void handleException(@NonNull Exception e);

    public abstract void logger(@NonNull String tag, @NonNull String message,@Nullable Throwable throwable);


    static GRouterLogger logger = new GRouterLogger() {
        @Override
        public void handleException(@NonNull Exception e) {
            e.printStackTrace();
        }

        @Override
        public void logger(@NonNull String tag, @NonNull String message, @Nullable Throwable throwable) {
            Log.e(tag, message);
        }
    };

    public static void setLogger(GRouterLogger logger) {
        GRouterLogger.logger = logger;
    }
}
