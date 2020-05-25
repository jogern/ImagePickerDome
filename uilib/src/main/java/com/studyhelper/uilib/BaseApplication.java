package com.studyhelper.uilib;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import java.io.File;

/**
 * Create on 2019-08-13.
 *
 * @author zujianliang
 */
public abstract class BaseApplication extends Application {

    private SaveCrashHandler crashHandler = new SaveCrashHandler();
    private SaveLogcatHandler mLogcatHandler = new SaveLogcatHandler();
    private ActivityCollector mActivityCollector=new ActivityCollector();

    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (getLogAction().equals(action)) {
                boolean isDebugLog = false;
                String extra = intent.getStringExtra(SaveHandlerUtil.KEY_DEBUG_LOG);
                if (extra != null && extra.length() > 0) {
                    isDebugLog = Boolean.parseBoolean(extra);
                }
                receiverDebugLog(isDebugLog);
            }
        }
    };

    /**
     * 是否开启 debug log
     *
     * @param isDebugLog
     */
    protected void receiverDebugLog(boolean isDebugLog) {
    }

    /**
     * 注册 debug log 是否开启打印的广播<br/>
     * 可以用 adb 命令发送广播开启:<br/>
     * am broadcast -a action_+包名 --es debugLog true
     * 关闭:<br/>
     * am broadcast -a action_+包名
     */
    protected void debugLogRegister() {
        IntentFilter filter = new IntentFilter();
        filter.addAction(getLogAction());
        registerReceiver(mReceiver, filter);
    }

    /**
     * 开启 debug log 通知 action
     *
     * @return action_包名
     */
    private String getLogAction() {
        return SaveHandlerUtil.KEY_ACTION + getPackageName();
    }

    /**
     * 设置保存异常
     *
     * @param isFilterSysDeal 是否交回系统处理
     */
    protected void setSaveCrash(boolean isFilterSysDeal) {
        crashHandler.setFilterSysDeal(isFilterSysDeal).init(this);
    }

    /**
     * 保存 logcat 对象
     *
     * @return
     */
    protected SaveLogcatHandler getLogcatHandler() {
        return mLogcatHandler;
    }

    /**
     * 设置开始保存 logcat
     *
     * @param limitLen
     */
    protected void setSaveLogcat(int limitLen) {
        mLogcatHandler.setFileLimit(limitLen).init(this).startSave();
    }

    public ActivityCollector getActivityCollector() {
        return mActivityCollector;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(mActivityCollector);
    }

    protected boolean isPrintfLog(boolean isDebug) {
        if (!isDebug) {
            File file = getExternalCacheDir();
            if (file != null) {
                //externalDebug
                File debugFile = new File(file, SaveHandlerUtil.KEY_DEBUG);
                isDebug = debugFile.exists();
            }
        }
        return isDebug;
    }
}
