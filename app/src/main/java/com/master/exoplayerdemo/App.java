package com.master.exoplayerdemo;

import android.app.Application;
import android.content.Context;

/**
 * Created by cenzen on 2017/12/13.
 */

public class App extends Application {

    private static App INSTANCE;

    public static App instance() {
        return INSTANCE;
    }

    public static Context context() {
        return instance().getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE = this;
    }

}
