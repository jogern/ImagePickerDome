package com.studyhelper.uilib;

import android.app.Application;

/**
 * Create on 2019-08-13.
 *
 * @author zujianliang
 */
public abstract class BaseApplication extends Application {


    protected void setSaveCrash() {
        SaveCrashHandler crashHandler = new SaveCrashHandler();
        crashHandler.init(this);
    }

}
