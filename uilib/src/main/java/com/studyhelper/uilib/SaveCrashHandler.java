package com.studyhelper.uilib;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;
import android.os.Process;
import android.text.TextUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Create on 2019/3/18.
 *
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
    private Map<String, String> mDeviceInfo = new HashMap<>();
    /**
     * 程序context
     **/
    private BaseApplication mContext;
    /**
     * 异常是否交回给系统处理,默认是给系统处理
     */
    private boolean mIsFilterSysDeal = true;
    /**
     * 设置crash文件位置
     **/
    private String mCrashFilePath;

    /**
     * 是否交回给系统处理
     *
     * @param filterSysDeal 默认 true 交回给系统处理,false 则自己处理完了就结束apk
     * @return this
     */
    public SaveCrashHandler setFilterSysDeal(boolean filterSysDeal) {
        mIsFilterSysDeal = filterSysDeal;
        return this;
    }

    /**
     * 初始化
     *
     * @param context apk Application
     */
    public void init(BaseApplication context) {
        // 1、上下文
        mContext = context;
        //得到保存 Crash 的路径
        mCrashFilePath = SaveHandlerUtil.getSaveDir(context);
        System.out.println("CrashFilePath: " + mCrashFilePath);
        // 2、获取系统默认的UncaughtException处理器
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        // 4、设置当前CrashHandler为默认处理异常类
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        boolean isDeal = false;
        if (!TextUtils.isEmpty(mCrashFilePath) || mContext != null) {
            isDeal = handlerException(ex);
        }

        //如果有处理了，就直接结束 apk
        if (isDeal && !mIsFilterSysDeal) {
            ActivityCollector collector = mContext.getActivityCollector();
            if (collector != null) {
                collector.finishAllActivity();
            }

            Process.killProcess(Process.myPid());
            System.exit(0);
        } else {
            if (mDefaultHandler != null) {
                //把异常给回系统处理
                mDefaultHandler.uncaughtException(thread, ex);
            }
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
     * @param ex 异常对象
     * @return 是否处理了异常
     */
    private boolean handlerException(Throwable ex) {
        final Context context = mContext;
        if (ex == null || context == null) {
            return false;
        }
        // 5.1 收集设备参数信息
        collectDeviceInfo(context);
        // 5.3 保存log和crash到文件
        saveLogAndCrash(ex);
        return true;
    }

    /**
     * 5.1 收集设备信息
     *
     * @param ctx 上下文
     */
    private void collectDeviceInfo(Context ctx) {
        if (mDeviceInfo.size() > 0) {
            return;
        }
        Map<String, String> pkgInfo = SaveHandlerUtil.getPkgInfo(ctx);
        if (pkgInfo != null && pkgInfo.size() > 0) {
            mDeviceInfo.putAll(pkgInfo);
        }
    }

    /**
     * 5.3 保存log和crash到文件
     *
     * @param ex 异常对象
     */
    private void saveLogAndCrash(Throwable ex) {
        StringBuilder sb = new StringBuilder("DeviceInfo: \n ");
        // 遍历infos
        for (Map.Entry<String, String> entry : mDeviceInfo.entrySet()) {
            String key = entry.getKey().toLowerCase(Locale.getDefault());
            String value = entry.getValue();
            if (!TextUtils.isEmpty(key)) {
                sb.append(key).append(": ").append(value).append("\n");
            }
        }
        sb.append("createTime: ").append(mDateFormat.format(new Date())).append("\n");
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
        saveToCrashFile(md5(result), sb.toString());
    }

    /**
     * 5.3.1写入文本
     *
     * @param crashText 保存的异常文本
     */
    private void saveToCrashFile(String md5, String crashText) {
        String fileName = "Crash" + getApkName() + "-" + md5 + ".log";
        //生成的crash文件
        File crashFile = new File(mCrashFilePath, fileName);
        System.out.println("save crash path: " + crashFile.getAbsolutePath());
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


    private String getApkName() {
        String apkName = mDeviceInfo.get(SaveHandlerUtil.KEY_APK_NAME);
        if (apkName == null || apkName.length() <= 0) {
            Application context = mContext;
            if (context != null) {
                apkName = context.getPackageName().replace(".", "-");
            }
        }
        return apkName == null ? "crash" : apkName;
    }

    /**
     * 产生 md5
     *
     * @param code 文本
     * @return md5值
     */
    private String md5(String code) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.reset();
            digest.update(code.getBytes());
            byte[] bt = digest.digest();
            StringBuilder sb = new StringBuilder();
            String temp;
            for (byte b : bt) {
                temp = Integer.toHexString(b & 0xff);
                if (temp.length() == 1) {
                    temp = String.format("0%s", temp);
                }
                sb.append(temp);
            }
            return sb.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
