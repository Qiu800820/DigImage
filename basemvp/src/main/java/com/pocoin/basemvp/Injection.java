package com.pocoin.basemvp;

import android.content.Context;

import com.pocoin.basemvp.presentation.ActivityHintView;
import com.pocoin.basemvp.presentation.ActivityHintViewImpl;
import com.pocoin.basemvp.presentation.AppExitHelper;
import com.pocoin.basemvp.presentation.AppExitHelperImpl;
import com.pocoin.basemvp.third_party.app_analysis.AppAnalysisClient;
import com.pocoin.basemvp.third_party.app_analysis.AppAnalysisClientImpl;
import com.pocoin.basemvp.third_party.app_analysis.AppPageAnalysis;
import com.pocoin.basemvp.third_party.app_analysis.AppPageAnalysisImpl;
import com.pocoin.basemvp.third_party.app_analysis.AppUserLoginAnalysis;
import com.pocoin.basemvp.third_party.app_analysis.AppUserLoginAnalysisImpl;


/**
 * Created by Robert yao on 2016/10/18.
 */
public class Injection {


    public static ActivityHintView provideActivityHintView(Context context) {
        return new ActivityHintViewImpl(context);
    }
    public static AppExitHelper provideAppExitHelper(Context context) {
        return new AppExitHelperImpl(context);
    }
    public static AppAnalysisClient provideAppAnalysisClient(Context context) {
        return new AppAnalysisClientImpl(context);
    }

    public static AppUserLoginAnalysis provideAppUserLoginAnalysis(){
        return new AppUserLoginAnalysisImpl();
    }

    public static AppPageAnalysis provideAppAnalysis(Context context) {
        return new AppPageAnalysisImpl(context);
    }
}
