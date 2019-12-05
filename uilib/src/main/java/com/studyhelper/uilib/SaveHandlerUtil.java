package com.studyhelper.uilib;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * -------------------------------------------------------------------------
 * <br/>本类主要用途描述：<br/>
 *
 * @author 作者：zujianliang
 * @version 版本号：v1.0
 * @date 创建时间： 2019-12-05 11:12
 * -------------------------------------------------------------------------
 */
class SaveHandlerUtil {
    public static final String KEY_APK_NAME = "apkName";
    public static final String KEY_DEBUG = "externalDebug";
    public static final String KEY_ACTION = "action_";
    public static final String KEY_DEBUG_LOG = "debugLog";

    private SaveHandlerUtil() {
    }

    /**
     * Crash or Logcat 得到保存文件的路径
     *
     * @return 路径, 可能为 null
     */
    public static String getSaveDir(Context context) {
        String path = "";
        File externalFile = Environment.getExternalStorageDirectory();
        if (externalFile != null) {
            File file = new File(externalFile, "CrashLogcat");
            if (!file.exists() && !file.mkdirs()) {
                System.out.println("sout: mkdirs Crash or Logcat dir fail");
            } else {
                path = file.getAbsolutePath();
            }
        }
        if (path == null || path.length() <= 0) {
            path = getCacheDir(context);
        }
        return path;
    }

    /**
     * 获取外部包名下的缓存路径
     *
     * @param context
     * @return
     */
    public static String getCacheDir(Context context) {
        if (context != null) {
            File cacheDir = context.getExternalCacheDir();
            if (cacheDir != null) {
                return cacheDir.getPath();
            }
        }
        return null;
    }

    /**
     * 获取当前包的信息
     *
     * @param context 上下文
     * @return 包的信息集合
     */
    public static Map<String, String> getPkgInfo(Context context) {
        Map<String, String> info = new HashMap<>();
        try {
            PackageManager pm = context == null ? null : context.getPackageManager();
            if (pm == null) {
                return info;
            }
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName;
                info.put("应用版本", versionName == null ? "null" : versionName);
                long versionCode;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.P) {
                    versionCode = pi.getLongVersionCode();
                } else {
                    versionCode = pi.versionCode;
                }
                info.put("应用版本号", String.valueOf(versionCode));
                info.put("品牌", Build.MANUFACTURER);
                info.put("机型", Build.MODEL);
                info.put("Android 版本", Build.VERSION.RELEASE);
                info.put("系统版本", Build.DISPLAY);
                info.put(KEY_APK_NAME, pi.applicationInfo.loadLabel(pm).toString());
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return info;
    }


}
