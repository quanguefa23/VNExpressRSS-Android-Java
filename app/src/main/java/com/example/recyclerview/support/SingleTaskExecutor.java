package com.example.recyclerview.support;


import androidx.annotation.NonNull;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * Single Executor handle all database task (singleton implement)
 */
public final class SingleTaskExecutor {
    private static ScheduledExecutorService mExecutor;

    private SingleTaskExecutor() {
    }

    static {
        mExecutor = Executors.newSingleThreadScheduledExecutor();
    }

    public static void queueRunnable(Runnable runnable) {
        mExecutor.execute(runnable);
    }
}
