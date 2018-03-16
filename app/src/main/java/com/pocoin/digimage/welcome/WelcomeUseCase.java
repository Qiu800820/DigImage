package com.pocoin.digimage.welcome;


import com.pocoin.basemvp.domain.UseCase;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.functions.Func1;

public class WelcomeUseCase extends UseCase {

    private int countDownTime = 4;

    @Override
    protected Observable buildUseCaseObservable(UseCase.RequestValues rv) {

        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .map(new Func1<Long, Integer>() {
                    @Override
                    public Integer call(Long o) {
                        return countDownTime - o.intValue();
                    }
                }).take(countDownTime + 1);
    }
}
