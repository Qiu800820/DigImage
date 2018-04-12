package com.pocoin.digimage;

import android.app.Application;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.pocoin.digimage.base.realm.RealmHelper;
import com.squareup.leakcanary.LeakCanary;
import com.sum.xlog.core.LogLevel;
import com.sum.xlog.core.XLog;
import com.sum.xlog.core.XLogConfiguration;


/**
 * Created by Robert yao on 2016/11/9.
 */

public class AppLibLifecycleManagerImpl implements AppLibLifecycleManager {

    Application context;
    private static final String TAG = "AppLibLifecycleManager";

    public AppLibLifecycleManagerImpl(Application context) {
        this.context = context;
    }
    @Override
    public void onCreate() {
        Log.d(TAG, "=== startTime :" + System.currentTimeMillis());
        try {
            LeakCanary.install(context);
            initAppAnalysis();
            initDb();
            Fresco.initialize(context);
        }catch (Exception e){
            XLog.e("=== 初始化失败 ===", e);
        }
        Log.d(TAG, "=== endTime :" + System.currentTimeMillis());
    }


    private void initDb() {
        RealmHelper.init(context);
        Log.d(TAG, "=== RealmHelper endTime :" + System.currentTimeMillis());
    }

    private void initAppAnalysis() {
        XLogConfiguration.Builder builder = new XLogConfiguration.Builder(context)
                .setConsoleLogLevel(LogLevel.D) //Logger输出最低级别
                .setFileLogLevel(LogLevel.D) //保存至文件最低级别
                .setCrashHandlerOpen(true) //开启异常捕获
                .setDefaultTag("XLog") //默认TAG
                .setOriginalHandler(Thread.getDefaultUncaughtExceptionHandler()) //第三方统计
                .setOnUpdateCrashInfoListener(null) //Crash自动上传处理
                .setFileLogRetentionPeriod(7); //过期删除
        XLog.init(builder.build());
//        AppAnalysisClient appAnalysisClient = Injection.provideAppAnalysisClient(context);
//        appAnalysisClient.init();
    }
    @Override
    public void onLowMemory() {
        destroyPush();
        destroyAppAnalysis();
    }
    private void destroyPush() {
    }
    private void destroyAppAnalysis() {
    }


}
