package com.pocoin.digimage;

import android.app.Application;
import android.util.Log;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.jiongbull.jlog.JLog;
import com.jiongbull.jlog.constant.LogLevel;
import com.pocoin.digimage.base.realm.RealmHelper;
import com.squareup.leakcanary.LeakCanary;

import java.util.ArrayList;
import java.util.List;


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
            JLog.e("=== 初始化失败 ===", e);
        }
        Log.d(TAG, "=== endTime :" + System.currentTimeMillis());
    }


    private void initDb() {
        RealmHelper.init(context);
        Log.d(TAG, "=== RealmHelper endTime :" + System.currentTimeMillis());
    }



//    private void initPush() {
//        try {
//            PushClient pushClient = Injection.providePushClient(context);
//            pushClient.init();
//            PushMessageStrategyFactory.getInstance().registerReceiverStrategy(PushMessageStrategyFactory.DEFAULT_TYPE,
//                    TextPushMessageStrategy.class.getConstructor(PushMessage.class));
//            PushMessageStrategyFactory.getInstance().registerReceiverStrategy(ActivityPushMessageStrategy.TYPE_ACTIVITY,
//                    ActivityPushMessageStrategy.class.getConstructor(PushMessage.class));
//            PushMessageStrategyFactory.getInstance().registerReceiverStrategy(IllegalPushMessageStrategy.TYPE_ILLEGAL,
//                    IllegalPushMessageStrategy.class.getConstructor(PushMessage.class));
//            PushMessageStrategyFactory.getInstance().registerReceiverStrategy(VersionPushMessageStrategy.TYPE_NEW_APP_VERSION,
//                    VersionPushMessageStrategy.class.getConstructor(PushMessage.class));
//        }catch (Exception e){
//            JLog.e(e);
//        }
//    }
    private void initAppAnalysis() {
        JLog.init(context);
        if(!BuildConfig.DEBUG) {
            JLog.getSettings()
                    .writeToFile(true)
                    .setLogLevelsForFile(createDebugLevel());

        }
//        AppAnalysisClient appAnalysisClient = Injection.provideAppAnalysisClient(context);
//        appAnalysisClient.init();
    }
    @Override
    public void onLowMemory() {
        destroyPush();
        destroyAppAnalysis();
    }
    private void destroyPush() {
//        PushClient pushClient = Injection.providePushClient(context);
//        pushClient.destroy();
    }
    private void destroyAppAnalysis() {
//        AppAnalysisClient appAnalysisClient = Injection.provideAppAnalysisClient(context);
//        appAnalysisClient.destroy();
    }

    private List<LogLevel> createDebugLevel(){
        List<LogLevel> logLevels = new ArrayList<>(5);
        logLevels.add(LogLevel.DEBUG);
        logLevels.add(LogLevel.VERBOSE);
        logLevels.add(LogLevel.INFO);
        logLevels.add(LogLevel.WARN);
        logLevels.add(LogLevel.ERROR);
        return logLevels;
    }

}
