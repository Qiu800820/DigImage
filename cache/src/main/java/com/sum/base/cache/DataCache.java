package com.sum.base.cache;


import io.reactivex.Observable;

/**
 * Created by Robert yao on 2016/11/15.
 */
public interface DataCache<T> {

    void put(T t);
    void destroy();

    Observable<T> get();
}
