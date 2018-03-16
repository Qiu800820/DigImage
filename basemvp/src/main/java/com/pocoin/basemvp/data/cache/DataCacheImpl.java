package com.pocoin.basemvp.data.cache;

import com.jiongbull.jlog.JLog;

import java.lang.ref.WeakReference;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;

/**
 * 作者：wangdapeng on 2017/2/21 0021 14:56
 * 邮箱：417235067@qq.com
 */
public abstract class DataCacheImpl<T> implements DataCache<T>  {

    private WeakReference<T> userWeakReference;


    @Override
    public void put(final T t) {
            if (putToDb(t) && isUseMemory()){
                userWeakReference = new WeakReference<>(t);
            }
    }

    @Override
    public void destroy() {
            if (null != userWeakReference){
                userWeakReference.clear();
                userWeakReference = null;
            }
            clearFromDB();
    }
    @Override
    public Observable<T> get() {
        if(!isUseMemory()){
            return safeThreadFromDbGet();
        }

        return Observable.concat(safeThreadFromMemoryGet(), safeThreadFromDbGet()).firstOrDefault(null, new Func1<T, Boolean>() {
            @Override
            public Boolean call(T t) {
                return t != null;
            }
        });
    }

    private Observable<T> safeThreadFromMemoryGet(){

        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    subscriber.onNext(userWeakReference == null ? null : userWeakReference.get());
                    subscriber.onCompleted();
                }catch (Throwable e){
                    JLog.e(e.getMessage());
                    subscriber.onError(e);
                }
            }
        });
    }

    private Observable<T> safeThreadFromDbGet(){
        return Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                try {
                    T t = getFromDb();
                    subscriber.onNext(t);
                    subscriber.onCompleted();
                }catch (Exception e){
                    JLog.e(e.getMessage());
                    subscriber.onError(e);
                }
            }
        });
    }

    public boolean clearFromMemory(){
        if (null != userWeakReference){
            userWeakReference.clear();
            userWeakReference = null;
        }
        return true;
    }

    protected abstract boolean putToDb(T t);
    protected abstract boolean clearFromDB();
    protected abstract T getFromDb();
    protected boolean isUseMemory(){
        return true;
    }
}
