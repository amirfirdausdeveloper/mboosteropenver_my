package org.app.mbooster.activity_folder;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

public class Mbooster extends Application {

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
