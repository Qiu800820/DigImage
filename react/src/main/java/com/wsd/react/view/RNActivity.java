package com.wsd.react.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.facebook.react.ReactActivity;
import com.facebook.react.ReactActivityDelegate;
import com.facebook.react.ReactRootView;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.sum.xlog.core.XLog;
import com.wsd.react.BuildConfig;
import com.wsd.react.R;



/**
 * Created by Administrator on 2017/8/22.
 */

public abstract class RNActivity extends ReactActivity {

    static int LOAD_REACT_ERROR_MSG = 1001;

    private static final String TAG = "RNActivity";
    private ReactLoadHandler reactLoadHandler;

    protected LinearLayout loadingView;
    protected ErrorView errorView;
    protected FrameLayout centerView;

    @NonNull
    @Override
    abstract protected String getMainComponentName();

    @Nullable
    abstract protected String getToken();

    @NonNull
    abstract protected String getPath();

    @Nullable
    abstract protected Bundle getParams();

    protected ReactActivityDelegate createReactActivityDelegate() {
        return new ReactActivityDelegate(this, null) {

            protected
            Bundle getLaunchOptions() {
                Bundle bundle = new Bundle();
                bundle.putString("path", getPath());
                bundle.putString("token", getToken());
                bundle.putBundle("params", getParams());
                return bundle;
            }

            @Override
            protected ReactRootView createRootView() {
                return new YjxRootView(RNActivity.this) {
                    @Override
                    protected ReactInstanceProgressListener createReactInstanceProgressListener() {
                        return new ReactProgress();
                    }
                };
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(provideLayout());
        initView();
        loadAppAndShowProgress();

    }

    protected @LayoutRes int provideLayout(){
        return R.layout.activity_react_view;
    }

    protected void initView() {
        loadingView = (LinearLayout) findViewById(R.id.loading_view);
        errorView = (ErrorView) findViewById(R.id.error_view);
        centerView = (FrameLayout) findViewById(R.id.center_view);
    }

    @Override
    public void setContentView(View view) {
        if(view instanceof ReactRootView){
            centerView.addView(view);
        }
    }

    public void loadAppAndShowProgress() {
        if(!getReactDeveloperSupport()) {
            getReactLoadHandler().sendEmptyMessageDelayed(LOAD_REACT_ERROR_MSG, 10000);
        }
        loadApp(getMainComponentName());
        showLoading();

    }

    protected boolean getReactDeveloperSupport() {
        return BuildConfig.DEBUG;
    }

    @NonNull
    public ReactLoadHandler getReactLoadHandler() {
        if (reactLoadHandler == null)
            reactLoadHandler = new ReactLoadHandler();
        return reactLoadHandler;
    }

    protected void sendEvent(String eventName, @Nullable WritableMap params) {
        try {
            ReactContext reactContext = getReactInstanceManager().getCurrentReactContext();
            if (reactContext != null)
                reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                        .emit(eventName, params);
        } catch (Exception e) {
            Log.e(TAG, "=== sendEvent ===", e);
        }
    }

    public void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        centerView.setVisibility(View.VISIBLE);
    }


    public void showContent() {
        loadingView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        centerView.setVisibility(View.VISIBLE);
    }


    public void showError(Throwable e) {

        loadingView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
        centerView.setVisibility(View.GONE);
    }

    protected abstract void showErrorMessage(Throwable e);

    /**
     * reload react native app
     */
    public void reloadApp() {
        try {
            getReactInstanceManager().destroy();
            loadAppAndShowProgress();
        } catch (Exception e) {
            XLog.e("reloadApp error", e);
        }

    }

    private class ReactProgress implements ReactInstanceProgressListener {
        @Override
        public void onReactLoadFinish() {
            showContent();
            getReactLoadHandler().removeMessages(LOAD_REACT_ERROR_MSG);
        }
    }


    private class ReactLoadHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == LOAD_REACT_ERROR_MSG) {
                showError(new ReactLoadException());
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1111 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(getApplicationContext())) {
                loadAppAndShowProgress();
            }
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (reactLoadHandler != null) {
            reactLoadHandler.removeMessages(LOAD_REACT_ERROR_MSG);
            reactLoadHandler = null;
        }
    }



}
