package com.pocoin.basemvp.data.cache;

import android.support.annotation.NonNull;

import com.pocoin.basemvp.util.CloseUtils;

import io.realm.Realm;

/**
 * Created by Sen on 2017/12/22.
 */

public class RealmManager {

    public static <T> T safeReadRealm(@NonNull Reader<T> reader) {
        Realm realm = null;
        T result = null;
        try {
            realm = Realm.getDefaultInstance();
            result = reader.read(realm);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtils.closeIOQuietly(realm);
        }
        return result;
    }

    public static void safeWriteRealm(@NonNull Realm.Transaction transaction) {
        Realm realm = null;
        try {
            realm = Realm.getDefaultInstance();
            realm.executeTransaction(transaction);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtils.closeIOQuietly(realm);
        }
    }

    public static interface Reader<R> {
        R read(Realm realm);
    }

}
