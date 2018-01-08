package com.jci.teacherclasslock.core;

import android.app.Application;

import com.orhanobut.logger.Logger;

import cn.jpush.im.android.api.JMessageClient;

/**
 * 创建人: 谭火朋
 * 创建时间: 2017/11/9 0009 15:56
 */

public class GlobalApplication extends Application {
    public static final String TAG = "fire";
    private static GlobalApplication mClassLockApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        initApp();
    }

    private void initApp() {
        if (mClassLockApplication == null){
            mClassLockApplication = this;
        }
        Logger.init(TAG);
        JMessageClient.init(this);
    }

    public static synchronized GlobalApplication getInstance() {
        return mClassLockApplication;
    }
}
