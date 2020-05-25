package com.jogern.jui;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.studyhelper.uilib.BaseApplication;

/**
 * Create on 2018/12/28.
 *
 * @author jogern
 */
public class JUiApplication extends BaseApplication {

    private static final String TAG = JUiApplication.class.getSimpleName();

    @Override
    protected void receiverDebugLog(boolean isDebugLog) {
        Log.e(TAG,"isDebugLog: "+isDebugLog);
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);

        //初始化保存 logcat
        getLogcatHandler().init(this);
        //注册广播开启 logcat 打印
        debugLogRegister();
        setSaveCrash(false);
    }


    @Override
    public void onCreate() {
        super.onCreate();

        receiverDebugLog(true);

    }


}
