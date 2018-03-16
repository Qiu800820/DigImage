package com.pocoin.basemvp.data.cache;

import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by Robert on 2018/1/31.
 *
 *  * 考虑加入读写锁
 *
 * class CachedData {
 Object data;
 volatile boolean cacheValid;
 ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();

 void processCachedData() {
 rwl.readLock().lock();
 if (!cacheValid) {
 // Must release read lock before acquiring write lock
 rwl.readLock().unlock();
 rwl.writeLock().lock();
 // Recheck state because another thread might have acquired
 //   write lock and changed state before we did.
 if (!cacheValid) {
 data = ...
 cacheValid = true;
 }
 // Downgrade by acquiring read lock before releasing write lock
 rwl.readLock().lock();
 rwl.writeLock().unlock(); // Unlock write, still hold read
 }
 use(data);
 rwl.readLock().unlock();
 }
 }
 *
 *
 */

public abstract class SafeThreadDataCacheImpl<T> implements FixedDataCache<T> {

    private T t;
    private volatile boolean cacheValid;
    private ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();

    @Override
    public void put(T t) {
        reentrantReadWriteLock.readLock().lock();
        if (!cacheValid){
            reentrantReadWriteLock.readLock().unlock();
            reentrantReadWriteLock.writeLock().lock();
            if (!cacheValid && putToDb(t)){
                this.t = t;
                cacheValid = true;
            }
            reentrantReadWriteLock.readLock().lock();
            reentrantReadWriteLock.writeLock().unlock();


            //TODO 未完成
        }
    }

    @Override
    public void destroy() {

    }

    @Override
    public T get() {
        return null;
    }

    abstract boolean putToDb(T t);

}
