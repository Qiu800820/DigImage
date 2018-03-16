package com.pocoin.basemvp.presentation.widgets.badge;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.pocoin.basemvp.presentation.ActivityHelperView;

/**
 * Created by Administrator on 2017/5/12.
 */
public interface BadgeContract {
    interface View extends MvpView, ActivityHelperView {
        void setVisible(boolean isVisible);
        String getMessageId();
    }

    interface Presenter extends MvpPresenter<View> {
        void queryNewMessage(String messageId);
    }
}