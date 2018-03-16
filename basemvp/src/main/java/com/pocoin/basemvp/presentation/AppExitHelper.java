package com.pocoin.basemvp.presentation;

import android.os.Bundle;

/**
 * Created by Robert yao on 2016/10/18.
 */

public interface AppExitHelper{
    void onCreate(Bundle savedInstanceState);
    void onDestroy();
    void exitApp();
}
