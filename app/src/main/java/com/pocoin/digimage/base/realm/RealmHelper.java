package com.pocoin.digimage.base.realm;

import android.content.Context;

import com.sum.base.cache.RealmManager;
import com.sum.xlog.core.XLog;
import com.wsd.react.update.ReactLibModule;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.exceptions.RealmMigrationNeededException;

/**
 * Created by Administrator on 2017/2/20.
 */

public class RealmHelper {

    public static void init(Context context) {
        Realm.init(context);
        Realm.setDefaultConfiguration(getConfig());
        RealmManager.setRealmProvider(new RealmManager.RealmProvider() {
            @Override
            public Realm provider() {
                return getDefaultInstance();
            }
        });
    }

    public static RealmConfiguration getConfig(){
        return new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .modules(Realm.getDefaultModule(), new ReactLibModule())
                .schemaVersion(5)
                .build();
    }

    private static RealmMigration getRealmMigration() {
        return new RealmMigration() {
            @Override
            public void migrate(DynamicRealm realm, long oldVersion, long newVersion) {
                try {
                    while (oldVersion < newVersion) {
                        MigrationApi migrationApi = getMigrationApi((int) oldVersion);
                        oldVersion = migrationApi.migrationVersion(realm.getSchema());
                    }
                }catch (Throwable e){
                    throw new RealmMigrationNeededException(realm.getPath(), e.getMessage());
                }
            }
        };
    }

    private static MigrationApi getMigrationApi(int version){
        return null;
    }

    /**
     * 全局获取Realm实例
     */
    public static Realm getDefaultInstance(){
        Realm realm;
        RealmConfiguration realmConfiguration = getConfig();
        try {
            realm = Realm.getInstance(realmConfiguration); // Will migrate if needed
        } catch (RealmMigrationNeededException e) {
            XLog.e("RealmHelper", e);
            Realm.deleteRealm(realmConfiguration);
            realm = Realm.getInstance(realmConfiguration);
        }
        return realm;
    }


}
