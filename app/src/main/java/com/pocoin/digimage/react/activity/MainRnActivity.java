package com.pocoin.digimage.react.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pocoin.digimage.InjectionRepository;
import com.pocoin.digimage.util.ThrowableToErrorMessage;
import com.wsd.react.view.ErrorView;
import com.wsd.react.view.RNActivity;

/**
 * Created by Sen on 2017/12/22.
 */

public class MainRnActivity extends RNActivity {

    private static final String EXTRA_KEY_PATH = "path";
    private static final String EXTRA_KEY_PARAMS = "params";
    private static final String EXTRA_KEY_MODULE_NAME = "module_name";

    public static Intent getMainRnIntent(Context context){
        return getMainRnIntent(context, "Commit", "POCoin", null);
    }

    public static Intent getMainRnIntent(Context context, String path, String moduleName, Bundle params){
        return new Intent(context, MainRnActivity.class)
                .putExtra(EXTRA_KEY_PATH, path)
                .putExtra(EXTRA_KEY_MODULE_NAME, moduleName)
                .putExtra(EXTRA_KEY_PARAMS, params);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @NonNull
    @Override
    protected String getMainComponentName() {
        return getIntent().getStringExtra(EXTRA_KEY_MODULE_NAME);
    }

    @Nullable
    @Override
    protected String getToken() {
        return null;
    }

    @NonNull
    @Override
    protected String getPath() {
        return getIntent().getStringExtra(EXTRA_KEY_PATH);
    }

    @Nullable
    @Override
    protected Bundle getParams() {
        return getIntent().getBundleExtra(EXTRA_KEY_PARAMS);
    }

    public void showErrorMessage(Throwable e) {

        final ErrorView.ErrorMessage errorMessage = ThrowableToErrorMessage.toErrorMessage(e, this);
        errorView.with(errorMessage);

        errorView.setOnErrorProcessListener(new ErrorView.OnErrorProcessListener() {
            @Override
            public void onErrorProcessButtonClick() {
                if (errorMessage.getErrorProcessRunnable() != null) {
                    errorMessage.getErrorProcessRunnable().run();
                } else {
                    reloadApp();
                }
            }
        });

    }

    @Override
    protected boolean getReactDeveloperSupport() {
        return InjectionRepository.REACT_NATIVE_DEVELOPER_SUPPORT;
    }
}
