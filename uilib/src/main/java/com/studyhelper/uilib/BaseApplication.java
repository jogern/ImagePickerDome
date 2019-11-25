package com.studyhelper.uilib;

import android.app.Application;

/**
 * Create on 2019-08-13.
 *
 * @author zujianliang
 */
public abstract class BaseApplication extends Application {

    protected SaveCrashHandler crashHandler = new SaveCrashHandler();

    /**
     * 设置保存异常
     * @param isFilterSysDeal 是否交回系统处理
     */
    protected void setSaveCrash(boolean isFilterSysDeal) {
        crashHandler.setFilterSysDeal(isFilterSysDeal).init(this);
    }

}
