package com.grouter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
            if (throwable != null && throwable.getMessage() != null){
                message += ",case " + throwable.getMessage();
            }
            Log.e(tag, message);
        }
    };

    public static void setLogger(GRouterLogger logger) {
        GRouterLogger.logger = logger;
    }
}
