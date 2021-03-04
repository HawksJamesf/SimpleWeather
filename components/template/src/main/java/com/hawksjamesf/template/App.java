package com.hawksjamesf.template;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.util.Log;


import java.io.File;

import dalvik.system.DexClassLoader;

/**
 * Copyright ® $ 2017
 * All right reserved.
 *
 * @author: hawks.jamesf
 * @since: Nov/20/2020  Fri
 */
public class App extends Application{
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
    @Override public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
    }

}
