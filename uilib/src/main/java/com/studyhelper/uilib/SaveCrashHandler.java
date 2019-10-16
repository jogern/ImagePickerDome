package com.studyhelper.uilib;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Create on 2019/3/18.
 * @author jogern
 */
public class SaveCrashHandler implements Thread.UncaughtExceptionHandler {

    /**
     * 系统默认的UncaughtException处理类
     **/
    private Thread.UncaughtExceptionHandler mDefaultHandler;

    private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss", Locale.getDefault());
    /**
     * 存储设备信息和异常信息
     **/
    private Map<String, String> mInfos = new HashMap<>();
    /**
     * 程序context
     **/
    private Application mContext;
    /**
     * 设置crash文件位置
     **/
    private String mDRCrashFilePath;

    /**
     * 初始化
     *
     * @param context
     */
    public void init(Application context) {
        // 1、上下文
        mContext = context;
        File cacheDir = context.getExternalCacheDir();
        if (cacheDir != null) {
            mDRCrashFilePath = cacheDir.getPath();
        }
        System.out.println("CrashFilePath: " + mDRCrashFilePath);
        // 2、获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 4、设置当前CrashHandler为默认处理异常类
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        // Logcat.e(ex);
        if (!TextUtils.isEmpty(mDRCrashFilePath)) {
            handlerException(ex);
        }
        if (mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(thread, ex);
        }
    }

    /**
     * 5、处理异常<br>
     * <br>
     * 5.1 收集设备参数信息<br>
     * 5.2 弹出窗口提示信息<br>
     * 5.3 保存log和crash到文件<br>
     * 5.4 发送log和crash到服务器<br>
     *
     * @param ex
     * @return 是否处理了异常
     */
    private void handlerException(Throwable ex) {
        if (ex == null) {
            return;
        }
        // 5.1 收集设备参数信息
        collectDeviceInfo(mContext);
        // 5.3 保存log和crash到文件
        saveLogAndCrash(ex);
    }

    /**
     * 5.1 收集设备信息
     *
     * @param ctx
     */
    protected void collectDeviceInfo(Context ctx) {
        try {
            PackageManager pm = ctx.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(ctx.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                mInfos.put("应用版本" , versionName );
                mInfos.put("应用版本号" , versionCode);
                mInfos.put("品牌" , Build.MANUFACTURER );
                mInfos.put("机型" , Build.MODEL );
                mInfos.put("Android 版本" , Build.VERSION.RELEASE );
                mInfos.put("系统版本", Build.DISPLAY );
            }
        } catch (PackageManager.NameNotFoundException ignored) {
        }
    }

    /**
     * 5.3 保存log和crash到文件
     *
     * @param ex
     */
    protected void saveLogAndCrash(Throwable ex) {
        StringBuilder sb = new StringBuilder("DeviceInfo: \n ");
        // 遍历infos
        for (Map.Entry<String, String> entry : mInfos.entrySet()) {
            String key = entry.getKey().toLowerCase(Locale.getDefault());
            String value = entry.getValue();
            if (!TextUtils.isEmpty(key)) {
                sb.append(key).append(": ").append(value).append("\n");
            }
        }
        // 将错误手机到writer中
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        Throwable cause = ex.getCause();
        while (cause != null) {
            pw.write("\n");
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        pw.close();
        String result = writer.toString();
        sb.append("\nExcetpion: \n ");
        sb.append(result);
        // 5.3.1 记录异常到特定文件中
        saveToCrashFile(sb.toString());
    }

    /**
     * 5.3.1写入文本
     *
     * @param crashText
     */
    protected void saveToCrashFile(String crashText) {
        String fileName = "Exception-" + mDateFormat.format(new Date()) + ".log";
        //生成的crash文件
        File crashFile = new File(mDRCrashFilePath, fileName);
        System.out.println("save crash path: "+crashFile.getAbsolutePath());
        OutputStream out = null;
        try {
            out = new FileOutputStream(crashFile);
            out.write(crashText.getBytes());
            out.flush();
            System.out.println("Exception save ok .....");
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Exception save fail ......");
        } finally {
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
