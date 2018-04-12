package com.sum.base.cache;

import android.support.annotation.NonNull;
import com.sum.base.util.CloseUtils;
import io.realm.Realm;

/**
 * Created by Sen on 2017/12/22.
 */

public class RealmManager {

    private static RealmProvider realmProvider;

    public static void setRealmProvider(RealmProvider realmProvider) {
        RealmManager.realmProvider = realmProvider;
    }

    public static <T> T safeReadRealm(@NonNull Reader<T> reader) {
        Realm realm = null;
        T result = null;
        try {
            realm = getDefaultInstance();
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
            realm = getDefaultInstance();
            realm.executeTransaction(transaction);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            CloseUtils.closeIOQuietly(realm);
        }
    }

    private static Realm getDefaultInstance(){
        Realm realm;
        if(realmProvider != null){
            realm = realmProvider.provider();
        }else{
            realm = Realm.getDefaultInstance();
        }
        return realm;
    }

    public interface Reader<R> {
        R read(Realm realm);
    }

    public interface RealmProvider{
        Realm provider();
    }

}
