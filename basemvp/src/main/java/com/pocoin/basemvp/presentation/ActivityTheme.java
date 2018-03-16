package com.pocoin.basemvp.presentation;

import android.app.Activity;

/**
 * Created by Sen on 2018/1/8.
 */

public interface ActivityTheme {
    void customTheme(Activity activity);
    boolean isLight();
    boolean isSupportCustomStatusBarColor();

}
