package com.aashreys.walls.utils;

import javax.inject.Inject;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by aashreys on 18/06/17.
 */

public class SchedulerProvider {

    @Inject
    public SchedulerProvider() {}

    public Scheduler mainThread() {
        return AndroidSchedulers.mainThread();
    }

    public Scheduler io() {
        return Schedulers.io();
    }

}
