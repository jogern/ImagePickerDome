package com.studyhelper.uilib.mvp.expansion;

import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

import com.studyhelper.uilib.mvp.BoardView;
import com.studyhelper.uilib.mvp.Presenter;
import com.studyhelper.uilib.mvp.PresenterHolder;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Create on 2019/7/8.
 *
 * @author jogern
 */
public abstract class BaseHandlerThreadHolder<V extends BoardView, P extends Presenter>
        extends PresenterHolder<V, P> {

    private AtomicBoolean mIsQuit = new AtomicBoolean(true);
    private HandlerThread mHandlerThread;
    private Handler mWorkHandler;

    protected Handler getWorkHandler() {
        return mWorkHandler;
    }

    protected boolean isQuit() {
        return mIsQuit.get();
    }

    @Override
    public void onCreated() {
        super.onCreated();
        mHandlerThread = new HandlerThread(this.getClass().getSimpleName());
        mHandlerThread.start();
        mIsQuit.set(false);
        mWorkHandler = new Handler(mHandlerThread.getLooper()) {
            @Override
            public void handleMessage(Message msg) {
               // super.handleMessage(msg);
                onHandleMessage(msg.what, msg);
            }
        };
    }

    @Override
    public void onDestroyed() {
        super.onDestroyed();
        if (mHandlerThread != null) {
            mIsQuit.set(true);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                mHandlerThread.quitSafely();
            } else {
                mHandlerThread.quit();
            }
        }
    }

    /**
     * 线程是否可用
     *
     * @return
     */
    private boolean isAliveThread() {
        return !isQuit() && mHandlerThread.isAlive();
    }

    /**
     * 处理
     *
     * @param what 消息 code
     * @param msg 接收消息处理
     */
    protected abstract void onHandleMessage(int what, Message msg);

    /**
     * 发送消息执行
     *
     * @param what 消息 code,分配给返回的Message.what字段的值
     */
    protected void send(int what) {
        send(what, 0);
    }

    /**
     * 延迟执行发送消息
     *
     * @param what    消息 code,分配给返回的Message.what字段的值
     * @param delayed 延迟时间(毫秒),0 即时发送
     * @return 如果消息已成功放入消息队列，则返回true。失败时返回false，
     * 通常是因为正在处理消息队列的循环程序正在退出。
     * 注意结果为true并不意味着将处理该消息-如果在消息的传递时间出现之前，
     * 循环程序已退出，则该消息将被丢弃
     */
    protected boolean send(int what, long delayed) {
        return send(what, null, delayed);
    }

    /**
     * 延迟执行发送消息
     *
     * @param what    消息 code,分配给返回的Message.what字段的值
     * @param obj     分配给返回的Message.obj字段的值
     * @param delayed 延迟时间(毫秒),0 即时发送
     * @return 如果消息已成功放入消息队列，则返回true。失败时返回false，
     * 通常是因为正在处理消息队列的循环程序正在退出。
     * 注意结果为true并不意味着将处理该消息-如果在消息的传递时间出现之前，
     * 循环程序已退出，则该消息将被丢弃
     */
    protected boolean send(int what, Object obj, long delayed) {
        return send(what, 0, 0, obj, delayed);
    }

    /**
     * 延迟执行发送消息
     *
     * @param what    消息 code,分配给返回的Message.what字段的值
     * @param arg1    分配给返回的Message.arg1字段的值
     * @param arg2    分配给返回的Message.arg2字段的值
     * @param delayed 延迟时间(毫秒),0 即时发送
     * @return 如果消息已成功放入消息队列，则返回true。失败时返回false，
     * 通常是因为正在处理消息队列的循环程序正在退出。
     * 注意结果为true并不意味着将处理该消息-如果在消息的传递时间出现之前，
     * 循环程序已退出，则该消息将被丢弃
     */
    protected boolean send(int what, int arg1, int arg2, long delayed) {
        return send(what, arg1, arg2, null, delayed);
    }

    /**
     * 延迟执行发送消息
     * @param what 消息 code,分配给返回的Message.what字段的值
     * @param arg1 分配给返回的Message.arg1字段的值
     * @param arg2 分配给返回的Message.arg2字段的值
     * @param obj 分配给返回的Message.obj字段的值
     * @param delayed 延迟时间(毫秒),0 即时发送
     * @return 如果消息已成功放入消息队列，则返回true。失败时返回false，
     * 通常是因为正在处理消息队列的循环程序正在退出。
     * 注意结果为true并不意味着将处理该消息-如果在消息的传递时间出现之前，
     * 循环程序已退出，则该消息将被丢弃
     */
    protected boolean send(int what, int arg1, int arg2, Object obj, long delayed) {
        if (!isAliveThread()) {
            return false;
        }
        return mWorkHandler.sendMessageDelayed(mWorkHandler.obtainMessage(what, arg1, arg2, obj), delayed);
    }

    /**
     * 执行发送消息
     * @param what 消息 code,分配给返回的Message.what字段的值
     * @param obj 分配给返回的Message.obj字段的值
     */
    protected void send(int what, Object obj) {
        send(what, 0, 0, obj);
    }

    /**
     * 执行发送消息
     * @param what
     * @param arg1
     * @param arg2
     */
    protected void send(int what, int arg1, int arg2) {
        send(what, arg1, arg2, null);
    }

    /**
     * 执行发送消息
     * @param what
     * @param arg1
     * @param arg2
     * @param obj
     */
    protected void send(int what, int arg1, int arg2, Object obj) {
        if (!isAliveThread()) {
            return;
        }
        mWorkHandler.obtainMessage(what, arg1, arg2, obj).sendToTarget();
    }

    /**
     * 删除消息队列中所有带有代码“what”的待处理消息
     * @param what 消息 code,分配给返回的Message.what字段的值
     */
    protected void remove(int what) {
        if (!isAliveThread()) {
            return;
        }
        mWorkHandler.removeMessages(what);
    }

    /**
     * 使Runnable添加到消息队列中。可运行对象将在此处理程序所连接的线程上运行
     * @param runnable 将要执行的Runnable
     * @return  如果Runnable已成功放入消息队列，则返回true。失败时返回false
     */
    protected boolean send(Runnable runnable) {
       return sendDelayed(runnable,0);
    }

    /**
     * 延迟(毫秒)执行Runnable
     * @param runnable
     * @param delayed  延迟时间(毫秒),0 即时发送
     * @return
     */
    protected boolean sendDelayed(Runnable runnable, long delayed) {
        if (!isAliveThread() || runnable == null) {
            return false;
        }
        return mWorkHandler.postDelayed(runnable, delayed);
    }

    /**
     * 删除消息队列中加入的Runnable
     * @param runnable  加入的Runnable
     */
    protected void remove(Runnable runnable) {
        if (!isAliveThread() || runnable == null) {
            return;
        }
        mWorkHandler.removeCallbacks(runnable);
    }


}
