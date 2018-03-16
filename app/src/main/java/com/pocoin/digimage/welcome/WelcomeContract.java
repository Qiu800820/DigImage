package com.pocoin.digimage.welcome;

import com.hannesdorfmann.mosby.mvp.MvpPresenter;
import com.hannesdorfmann.mosby.mvp.MvpView;
import com.pocoin.basemvp.presentation.ActivityHelperView;
import com.pocoin.basemvp.presentation.ActivityHintView;

public interface WelcomeContract {

    interface View extends ActivityHintView, MvpView, ActivityHelperView {
        void gotoHomePage(boolean finish);
        void gotoGuidePage(boolean finish);
        void skip(boolean finish);
        void setCountDown(String countDown);
    }

    interface Presenter extends MvpPresenter<View> {
        void init();
    }
}
