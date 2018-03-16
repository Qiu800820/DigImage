package com.pocoin.digimage.base.realm;

import android.content.Context;

import com.jiongbull.jlog.JLog;
import com.pocoin.basemvp.data.YjxLibraryModule;

import io.realm.DynamicRealm;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmMigration;
import io.realm.exceptions.RealmMigrationNeededException;

/**
 * Created by Administrator on 2017/2/20.
 */

public class RealmHelper {

    public static volatile RealmConfiguration config;

    public static void init(Context context) {
        Realm.init(context);
        Realm.setDefaultConfiguration(getConfig());
    }

    public static RealmConfiguration getConfig(){

        if(config == null){
            config = new RealmConfiguration.Builder()
//                .name("WinStar.realm")
//                .encryptionKey(new byte[]{0x11, 0x22, 0x23})
                    .migration(getRealmMigration())
                    .modules(Realm.getDefaultModule(), new YjxLibraryModule())
                    .schemaVersion(5)
                    .build();
        }

        return config;
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
            JLog.e(e);
            Realm.deleteRealm(realmConfiguration);
            realm = Realm.getInstance(realmConfiguration);
        }
        return realm;
    }


}
