package com.studyhelper.uilib;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.SystemClock;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * Create on 2019/4/1.
 *
 * @author JogernHome
 */
public class SaveLogcatHandler {

    //日志等级：*:v , *:d , *:i , *:w , *:e , *:f , *:s
    private static final String CMD = "logcat *:f *:s *:e *:w *:i *:d *:v | grep ";
    private static final int M_START = 0x10;
    private static final int M_END = 0x11;

//    private static SaveLogcatHandler sSaveLogcat;
//
//    public static SaveLogcatHandler getInstance() {
//        if (sSaveLogcat == null) {
//            sSaveLogcat = new SaveLogcatHandler();
//        }
//        return sSaveLogcat;
//    }

    private String mPath;
    private String mAppName;
    private long mFileLimit = 5L;
    private SaveRun mSaveRun;
    private ExecutorService mCache = Executors.newSingleThreadExecutor();
    private Handler mHandler = new Handler(Looper.getMainLooper()) {

        @Override
        public void handleMessage(Message msg) {
            int what = msg.what;
            if (what == M_START) {
                if (mSaveRun != null && mSaveRun.isRunning) {
                    return;
                }
                mSaveRun = new SaveRun();
                mCache.submit(mSaveRun);
//                ThreadPoolUtil.executeThread(mSaveRun);
            } else if (what == M_END) {
                if (mSaveRun != null) {
                    mSaveRun.isRunning = false;
                    mSaveRun = null;
                }
            }
        }
    };

    SaveLogcatHandler() {
    }

    public SaveLogcatHandler init(Context context) {
        Map<String, String> pkgInfo = SaveHandlerUtil.getPkgInfo(context);
        if (pkgInfo != null && pkgInfo.size() > 0) {
            mAppName = pkgInfo.get(SaveHandlerUtil.KEY_APK_NAME);
        }
        if (mAppName == null || mAppName.length() <= 0) {
            mPath = SaveHandlerUtil.getCacheDir(context);
        } else {
            mPath = SaveHandlerUtil.getSaveDir(context);
        }
        return this;
    }

    /**
     * 设置保存文件的大小
     *
     * @param limitLen 10至100的范围,默认5
     * @return
     */
    public SaveLogcatHandler setFileLimit(int limitLen) {
        if (limitLen <= 2) {
            mFileLimit = 2;
        } else if (limitLen > 100) {
            mFileLimit = 100;
        } else {
            mFileLimit = limitLen;
        }
        return this;
    }

    public void startSave() {
        mHandler.obtainMessage(M_START).sendToTarget();
    }

    public void endSave() {
        mHandler.obtainMessage(M_END).sendToTarget();
    }

    public boolean isRunning() {
        return mSaveRun != null && mSaveRun.isRunning;
    }

    private String startName() {
        if (mAppName == null || mAppName.length() <= 0) {
            return "Logcat";
        }
        return "Logcat" + mAppName;
    }


    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        if (!mCache.isShutdown()) {
            mCache.shutdown();
        }
    }

    private class SaveRun implements Runnable {

        volatile boolean isRunning;

        @Override
        public void run() {
            isRunning = true;

            Process process = null;
            while (isRunning) {
                try {
                    String cmds = "\"(" + android.os.Process.myPid() + ")\"";
                    process = Runtime.getRuntime().exec(CMD + cmds);
                } catch (IOException e) {
                    e.printStackTrace();
                    process = null;
                }
                if (process == null) {
                    SystemClock.sleep(500);
                    continue;
                }
                BufferedReader reader = null;
                try {
                    reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
                    ExecSave(reader);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    process.destroy();
                }
            }

            isRunning = false;
        }

        private void ExecSave(BufferedReader reader) {
            File file = null;
            FileOutputStream out = null;
            while (true) {
                try {
                    if (TextUtils.isEmpty(mPath)) {
                        SystemClock.sleep(5000);
                        continue;
                    }
                    if (out == null) {
                        String name = startName() + "-" + System.currentTimeMillis() + ".log";
                        file = new File(mPath, name);
                        out = new FileOutputStream(file);
                        deleteFile(file.getParentFile());
                    }
                    String line = reader.readLine();
                    if (!TextUtils.isEmpty(line)) {
                        out.write((line.concat("\n")).getBytes());
                        out.flush();
                    }

                    long length = file == null ? 0 : file.length();
                    if (length > mFileLimit * 1024 * 1024) {
                        out.close();
                        out = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    if (out != null) {
                        try {
                            out.close();
                        } catch (IOException eOut) {
                            eOut.printStackTrace();
                        }
                    }
                    break;
                }
                if (isRunning) {
                    continue;
                }
                if (out != null) {
                    try {
                        out.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            }

        }

        private void deleteFile(File file) {
            File[] files = file.listFiles();
            if (files != null) {
                String startName = startName();

                List<File> fileList = new ArrayList<>();
                for (File f : files) {
                    if (f.isDirectory()) {
                        continue;
                    }
                    if (f.getName().startsWith(startName)) {
                        fileList.add(f);
                    }
                }
                if (fileList.size() > 2) {
                    Collections.sort(fileList, new Comparator<File>() {
                        @Override
                        public int compare(File o1, File o2) {
                            long x = o1.lastModified();
                            long y = o2.lastModified();
                            return (x < y) ? -1 : ((x == y) ? 0 : 1);
                        }
                    });
                    while (fileList.size() > 2) {
                        fileList.get(0).delete();
                        fileList.remove(0);
                    }
                }
            }
        }
    }

}