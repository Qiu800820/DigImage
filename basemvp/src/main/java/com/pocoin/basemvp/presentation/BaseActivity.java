package com.pocoin.basemvp.presentation;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.hannesdorfmann.mosby.mvp.MvpActivity;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.jiongbull.jlog.JLog;
import com.pocoin.basemvp.Injection;
import com.pocoin.basemvp.R;
import com.pocoin.basemvp.third_party.app_analysis.AppPageAnalysis;
import com.pocoin.basemvp.third_party.push.PushMessage;
import com.pocoin.basemvp.third_party.push.PushMessageStrategy;
import com.pocoin.basemvp.third_party.push.PushMessageStrategyFactory;
import com.pocoin.basemvp.util.StatusBarUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Robert yao on 2016/10/17.
 */
public abstract class BaseActivity<V extends MvpView, P extends MvpPresenter<V>> extends MvpActivity<V, P> implements ActivityHintView, AppExit, EasyPermissions.PermissionCallbacks
    ,ActivityTheme{

    private ActivityHintView activityHintView = Injection.provideActivityHintView(this);
    private AppExitHelper appExitHelper = Injection.provideAppExitHelper(this);
    private AppPageAnalysis appPageAnalysis = Injection.provideAppAnalysis(this);
    private BroadcastReceiver broadcastReceiver;
    public static Set<String> isRunningSet = new HashSet<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        JLog.d(getClass().getSimpleName() + "onCreate");
        customTheme(this);
        appExitHelper.onCreate(savedInstanceState);
        broadcastReceiver = new PushMessageDialogBroadcastReceiver();
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        JLog.d(getClass().getSimpleName() + "onDestroy");
        appExitHelper.onDestroy();
    }
    @Override
    protected void onResume() {
        super.onResume();
        appPageAnalysis.onResume();
    }
    @Override
    protected void onPause() {
        super.onPause();
        appPageAnalysis.onPause();
    }

    @Override
    protected void onStart() {
        super.onStart();
        isRunningSet.add(getClass().getName());
        registerReceiver(broadcastReceiver, new IntentFilter(PushMessageStrategy.ACTION_PUSH_DIALOG));
    }

    @Override
    protected void onStop() {
        super.onStop();
        isRunningSet.remove(getClass().getName());
        unregisterReceiver(broadcastReceiver);
    }

    @Override
    public void showToast(String msg) {
        activityHintView.showToast(msg);
    }
    @Override
    public void showToast(int resId) {
        activityHintView.showToast(resId);
    }
    @Override
    public void showProgressDialog(int msgRes) {
        activityHintView.showProgressDialog(msgRes);
    }
    @Override
    public void showProgressDialog(String msg) {
        activityHintView.showProgressDialog(msg);
    }
    @Override
    public void showProgressDialog( String msg,String title) {
        activityHintView.showProgressDialog(msg,title);
    }
    @Override
    public void showProgressDialog(String msg, String title, View view) {
        activityHintView.showProgressDialog(msg,title,view);
    }
    @Override
    public void hideProgressDialogIfShowing() {
        activityHintView.hideProgressDialogIfShowing();
    }
    @Override
    public void appExit() {
        appExitHelper.exitApp();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {

    }

    @Override
    public void customTheme(Activity activity) {
        if(isSupportCustomStatusBarColor()){
            StatusBarUtil.setStatusBarLightMode(activity, isLight());
        }
    }

    @Override
    public boolean isSupportCustomStatusBarColor() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    @Override
    public boolean isLight() {
        return true;
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, list)) {
            new AppSettingsDialog.Builder(this).setRationale(R.string.rationale).build().show();
        }
    }

    public class PushMessageDialogBroadcastReceiver extends BroadcastReceiver{

        public static final String EXTRA_PUSH_MESSAGE = "push_message";

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals(PushMessageStrategy.ACTION_PUSH_DIALOG)){
                PushMessage pushMessage = intent.getParcelableExtra(EXTRA_PUSH_MESSAGE);
                JLog.d(String.format("===  收到PushDialog广播 PushMessageId:%s ===", pushMessage.getMsgId()));
                try {
                    PushMessageStrategy pushMessageStrategy = PushMessageStrategyFactory.getInstance().createStrategy(pushMessage);
                    pushMessageStrategy.showDialog(context);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
