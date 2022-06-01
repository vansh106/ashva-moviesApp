package com.example.movieapp;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class AppExecuters {
    //singleton pattern
    private static AppExecuters instance;
    public static AppExecuters getInstance() {
        if (instance == null) {
            instance = new AppExecuters();
        }
        return instance;
    }
    private final ScheduledExecutorService mNetworkIO = Executors.newScheduledThreadPool(3);
    public ScheduledExecutorService networkIO() {
        return mNetworkIO;
    }
}
