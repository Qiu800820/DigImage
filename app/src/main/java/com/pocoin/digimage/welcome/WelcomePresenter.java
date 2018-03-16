package com.pocoin.digimage.welcome;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.pocoin.digimage.R;

import rx.Subscriber;

public class WelcomePresenter extends MvpBasePresenter<WelcomeContract.View> implements WelcomeContract.Presenter {


    private WelcomeUseCase welcomeCase;

    public WelcomePresenter(WelcomeUseCase welcomeCase) {
        this.welcomeCase = welcomeCase;
    }

    @Override
    public void detachView(boolean retainInstance) {
        super.detachView(retainInstance);
        welcomeCase.unSubscribe();
    }

    @Override
    public void init() {
        initCountDown();
    }


    private void initCountDown() {
        welcomeCase.unSubscribe();
        welcomeCase.execute(new Subscriber<Integer>() {
            @Override
            public void onCompleted() {
                getView().skip(true);
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(Integer s) {
                if (!isViewAttached())
                    return;
                getView().setCountDown(getView().getActivityContext().getString(R.string.format_skip, s));
            }
        }, null);
    }





}
