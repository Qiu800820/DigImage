package com.pocoin.basemvp.data.cache;

import rx.Observable;

/**
 * Created by Robert yao on 2016/11/15.
 */

public interface DataCache<T> {

    void put(T t);
    void destroy();

    Observable<T> get();
}
