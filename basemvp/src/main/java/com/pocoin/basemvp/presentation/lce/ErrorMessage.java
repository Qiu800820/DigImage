package com.pocoin.basemvp.presentation.lce;

import android.support.annotation.DrawableRes;

/**
 * Created by Robert yao on 2016/11/9.
 */
public interface ErrorMessage {
    /**
     * 是否强制显示ErrorView (针对下拉刷新情况下展示ErrorView)
     * 比如下拉刷新情况下 token失效 强制显示ErrorView
     * @return boolean
     */
    boolean isForceShowErrorView();
    boolean isVisibleButton();
    String getHintText();
    String getErrorProcessButtonText();
    @DrawableRes int getHintImage();
    Runnable getErrorProcessRunnable();
    Runnable getLceErrorProcessRunnable();

}
