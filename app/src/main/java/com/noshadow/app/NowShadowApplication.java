package com.noshadow.app;

import android.app.Application;
import android.support.multidex.MultiDexApplication;

import com.noshadow.app.managers.ManagerHelper;

public class NowShadowApplication extends MultiDexApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        ManagerHelper.getInstance().initialize(this, "com.noshadow.tcc");
    }
}
