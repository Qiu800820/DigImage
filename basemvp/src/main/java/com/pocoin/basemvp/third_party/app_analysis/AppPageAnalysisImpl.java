package com.pocoin.basemvp.third_party.app_analysis;

import android.app.Activity;
import android.content.Context;

/**
 * Created by Robert yao on 2016/11/7.
 */

public class AppPageAnalysisImpl implements AppPageAnalysis {

    Context context;

    public AppPageAnalysisImpl(Context context) {
        this.context = context;
    }
    @Override
    public void onPause() {
    }
    @Override
    public void onResume() {
    }
    @Override
    public void onAppExit() {
    }
    private boolean isIllegalContext(){
        return null == context  || !(context instanceof Activity);
    }

    @Override
    public void onPause(String name) {
    }

    @Override
    public void onResume(String name) {
    }
}
