package com.wsd.react.view;

import android.content.Context;
import android.os.Bundle;
import android.util.AttributeSet;

import com.facebook.react.ReactInstanceManager;
import com.facebook.react.ReactRootView;

import javax.annotation.Nullable;

/**
 * Created by Sen on 2017/11/14.
 */

public abstract class YjxRootView extends ReactRootView{

    private boolean mLayoutFinished = false;
    private boolean mReported = false;
    private ReactInstanceProgressListener reactInstanceProgressListener;

    public YjxRootView(Context context) {
        super(context);
    }

    public YjxRootView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public YjxRootView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void onAttachedToReactInstance() {
        super.onAttachedToReactInstance();
        mLayoutFinished = true;
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        if (mLayoutFinished && !mReported){
            getReactInstanceProgressListener().onReactLoadFinish();
            mReported = true;
        }
    }

    @Override
    public void startReactApplication(ReactInstanceManager reactInstanceManager, String moduleName, @Nullable Bundle initialProperties) {
        super.startReactApplication(reactInstanceManager, moduleName, initialProperties);
        mLayoutFinished = false;
        mReported = false;
    }

    public ReactInstanceProgressListener getReactInstanceProgressListener(){
        if(reactInstanceProgressListener == null)
            reactInstanceProgressListener = createReactInstanceProgressListener();
        return reactInstanceProgressListener;
    }

    protected abstract ReactInstanceProgressListener createReactInstanceProgressListener();
}
