package com.pocoin.basemvp.presentation;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.pocoin.basemvp.Injection;
import com.pocoin.basemvp.third_party.app_analysis.AppPageAnalysis;


/**
 * Created by Robert yao on 2016/10/18.
 */

public class AppExitHelperImpl implements AppExitHelper {

    public static final String APP_EXIT_ACTION = "app_exit_action";

    private Context context;
    private BroadcastReceiver appPublicBroadcastReceiver = new AppPublicBroadcastReceiver();
    private AppPageAnalysis appPageAnalysis = null;

    public AppExitHelperImpl(Context context) {
        if (context instanceof Activity){
            this.context = context;
            appPageAnalysis = Injection.provideAppAnalysis(context);
        }else {
            throw new IllegalArgumentException("AppExitHelperImpl must init by activity context");
        }
    }

    @Override
    public void exitApp() {
        Intent intent = new Intent(APP_EXIT_ACTION);
        context.sendBroadcast(intent);
        appPageAnalysis.onAppExit();
        System.exit(0);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(APP_EXIT_ACTION);
        context.registerReceiver(appPublicBroadcastReceiver,intentFilter);
    }
    @Override
    public void onDestroy() {
        context.unregisterReceiver(appPublicBroadcastReceiver);
    }
    public class AppPublicBroadcastReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            if (isAppExitAction(intent)){
                closeCurrentActivity();
            }
        }
    }
    private boolean isAppExitAction(Intent intent) {
        return null != intent && APP_EXIT_ACTION.equals(intent.getAction());
    }
    private void closeCurrentActivity() {
        if (null != context && context instanceof Activity){
            ((Activity)context).finish();
        }
    }
}
