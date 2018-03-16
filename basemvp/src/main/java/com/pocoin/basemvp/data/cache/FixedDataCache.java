package com.pocoin.basemvp.data.cache;

/**
 * Created by Robert on 2018/1/31.
 *
 * 修正了{@link DataCache}接口，因为该接口强依赖RxJava
 **
 */

public interface FixedDataCache<T> {

    void put(T t);
    void destroy();
    T get();
}
