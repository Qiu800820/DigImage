package com.sum.base.cache;

import java.lang.ref.WeakReference;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Predicate;

/**
 * Created by Robert yao on 2016/11/15.
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

        return Observable.concat(safeThreadFromMemoryGet(), safeThreadFromDbGet())
                .filter(new Predicate<T>() {
                    @Override
                    public boolean test(T t) {
                        return t != null;
                    }
                })
                .firstOrError().toObservable();
    }

    private Observable<T> safeThreadFromMemoryGet(){

        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                emitter.onNext(userWeakReference == null ? null : userWeakReference.get());
                emitter.onComplete();
            }
        });
    }

    private Observable<T> safeThreadFromDbGet(){
        return Observable.create(new ObservableOnSubscribe<T>() {
            @Override
            public void subscribe(ObservableEmitter<T> emitter) throws Exception {
                T t = getFromDb();
                emitter.onNext(t);
                emitter.onComplete();
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
    protected abstract void clearFromDB();
    protected abstract T getFromDb();
    protected boolean isUseMemory(){
        return true;
    }
}
