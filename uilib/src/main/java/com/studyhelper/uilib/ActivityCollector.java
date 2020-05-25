package com.studyhelper.uilib;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;

/**
 * -------------------------------------------------------------------------
 * <br/>本类主要用途描述：<br/>
 *
 * @author 作者：zujianliang
 * @version 版本号：v1.0
 * @date 创建时间： 2020/5/25 3:38 PM
 * -------------------------------------------------------------------------
 */
public class ActivityCollector implements Application.ActivityLifecycleCallbacks {

    private List<Activity> allList = new ArrayList<>();
    private List<Activity> displayList = new ArrayList<>();

    public List<Activity> getAllList() {
        return allList;
    }

    public List<Activity> getDisplayList() {
        return displayList;
    }

    public void finishAllActivity() {
        for (Activity activity : allList) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        allList.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        displayList.add(activity);
    }

    @Override
    public void onActivityResumed(Activity activity) { }

    @Override
    public void onActivityPaused(Activity activity) { }

    @Override
    public void onActivityStopped(Activity activity) {
        displayList.remove(activity);
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) { }

    @Override
    public void onActivityDestroyed(Activity activity) {
        allList.remove(activity);
    }
}
