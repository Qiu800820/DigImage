package com.pocoin.basemvp.third_party.app_analysis;

import android.content.Context;

/**
 * Created by Robert yao on 2016/11/8.
 */

public class AppAnalysisClientImpl implements AppAnalysisClient {

    Context context;

    public AppAnalysisClientImpl(Context context) {
        this.context = context;
    }

    @Override
    public void init() {

    }

    private void initUmeng(String channel) {

    }

    private void initTalking(String channel) {
    }

    @Override
    public void destroy() {

    }

}
