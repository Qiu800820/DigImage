package com.pocoin.basemvp.util;


import com.pocoin.basemvp.domain.JobSchedule;

import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;

/**
 * Created by Robert yao on 2016/10/21.
 */

public class Transformers {


    public static <T> Observable.Transformer<T, T> checkResponseTransformer(final Throwable caseThrowable) {

        return new Observable.Transformer<T, T>() {
            @Override
            public Observable<T> call(Observable<T> responseObservable) {
                return responseObservable.onErrorResumeNext(new Func1<Throwable, Observable<? extends T>>() {
                    @Override
                    public Observable<? extends T> call(Throwable throwable) {
                        //兼容服务器404错误
                        if(caseThrowable != null && throwable instanceof HttpException && ((HttpException)throwable).code() == 404){
                            return Observable.error(caseThrowable);
                        }
                        return Observable.error(throwable);
                    }
                });
            }
        };
    }


    public static <T> Observable.Transformer<T, T> switchSchedulers() {
       return new Observable.Transformer<T, T>() {
           @Override
           public Observable<T> call(Observable<T> tObservable) {
               return tObservable.observeOn(AndroidSchedulers.mainThread()).subscribeOn(JobSchedule.getInstance().getScheduler());
           }
       };
    }

}
