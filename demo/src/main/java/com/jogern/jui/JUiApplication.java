package com.jogern.jui;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.studyhelper.uilib.BaseApplication;

/**
 * Create on 2018/12/28.
 * @author jogern
 */
public class JUiApplication extends BaseApplication {

      @Override
      protected void attachBaseContext(Context base) {
            super.attachBaseContext(base);
            MultiDex.install(this);
      }


      @Override
      public void onCreate() {
            super.onCreate();

      }



}
