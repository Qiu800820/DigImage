package com.pocoin.basemvp.third_party.app_analysis;

/**
 * Created by Robert yao on 2016/11/7.
 */

public interface AppPageAnalysis {

    void onPause();
    void onResume();
    void onPause(String name);
    void onResume(String name);
    void onAppExit();
}
