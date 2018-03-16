package com.pocoin.basemvp.presentation;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;

import com.hannesdorfmann.mosby.mvp.MvpFragment;
import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.pocoin.basemvp.Injection;
import com.pocoin.basemvp.third_party.app_analysis.AppPageAnalysis;

import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Robert yao on 2016/10/17.
 */
public abstract class BaseFragment<V extends MvpView, P extends MvpPresenter<V>> extends MvpFragment<V, P> implements ActivityHintView, AppExit, EasyPermissions.PermissionCallbacks{

    private ActivityHintView activityHintView;
    private AppExitHelper appExitHelper;
    private AppPageAnalysis appPageAnalysis;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHintView = Injection.provideActivityHintView(getActivity());
        appExitHelper = Injection.provideAppExitHelper(getActivity());
        appPageAnalysis = Injection.provideAppAnalysis(getActivity());
    }
    @Override
    public void onResume() {
        super.onResume();
        appPageAnalysis.onResume(getClass().getSimpleName());
    }
    @Override
    public void onPause() {
        super.onPause();
        appPageAnalysis.onPause(getClass().getSimpleName());
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
    public void onPermissionsDenied(int requestCode, List<String> list) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, list)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }
}
