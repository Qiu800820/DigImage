package com.wsd.react.update;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.pocoin.basemvp.data.cache.DataCacheImpl;
import com.pocoin.basemvp.data.cache.RealmManager;
import com.pocoin.basemvp.util.DownloadUtil;
import com.pocoin.basemvp.util.TextUtils;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmQuery;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Sen on 2017/9/26.
 */

public class BundleCacheImpl extends DataCacheImpl<BundleVersion> {

    private static volatile BundleCacheImpl INSTANCE;

    private BundleCacheImpl() {
        super();
    }

    public static BundleCacheImpl getInstance() {
        if (null == INSTANCE) {
            synchronized (BundleCacheImpl.class) {
                if (null == INSTANCE) {
                    INSTANCE = new BundleCacheImpl();
                }
            }
        }
        return INSTANCE;
    }

    @Override
    protected boolean putToDb(final BundleVersion bundleVersion) {
        RealmManager.safeWriteRealm(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(bundleVersion);
            }
        });
        return true;
    }

    @Override
    protected boolean clearFromDB() {
        RealmManager.safeWriteRealm(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.delete(BundleVersion.class);
            }
        });
        return false;
    }

    @Override
    protected BundleVersion getFromDb() {
        return getNewVersionFromDb(null);
    }

    /**
     * 获取下载成功并解压成功的最新版本
     * @param appVersionName app 版本名称
     * @return {@link BundleVersion}
     */
    public BundleVersion getNewVersionFromDb(@Nullable final String appVersionName){
        return RealmManager.safeReadRealm(new RealmManager.Reader<BundleVersion>() {
            @Override
            public BundleVersion read(Realm realm) {
                BundleVersion bundleVersion = null;
                RealmQuery<BundleVersion> bundleVersionRealmQuery = realm.where(BundleVersion.class)
                        .equalTo("downloadStatus", DownloadUtil.DownloadInfo.SUCCESS);
                if(!TextUtils.isEmpty(appVersionName)){
                    bundleVersionRealmQuery.equalTo("version", appVersionName);
                }
                RealmResults<BundleVersion> realmResults = bundleVersionRealmQuery
                        .findAllSorted("sourceVersion", Sort.DESCENDING);
                if(realmResults != null && realmResults.size() > 0){
                    bundleVersion = realm.copyFromRealm(realmResults.first());
                }

                return bundleVersion;
            }
        });
    }

    /**
     * 获取下载成功并解压成功的最新版本
     * @param appVersionName app 版本名称
     * @return {@link BundleVersion}
     */
    public BundleVersion getFromDbByUnzip(@Nullable final String appVersionName){
        return RealmManager.safeReadRealm(new RealmManager.Reader<BundleVersion>() {
            @Override
            public BundleVersion read(Realm realm) {
                BundleVersion bundleVersion = null;

                RealmQuery<BundleVersion> bundleVersionRealmQuery = realm.where(BundleVersion.class)
                        .equalTo("downloadStatus", DownloadUtil.DownloadInfo.SUCCESS)
                        .equalTo("isUnzip", true);
                if (!TextUtils.isEmpty(appVersionName)) {
                    bundleVersionRealmQuery.equalTo("version", appVersionName);
                }
                RealmResults<BundleVersion> realmResults = bundleVersionRealmQuery
                        .findAllSorted("sourceVersion", Sort.DESCENDING);

                if (realmResults != null && realmResults.size() > 0) {
                    bundleVersion = realm.copyFromRealm(realmResults.first());
                }

                return bundleVersion;
            }
        });
    }

    public List<BundleVersion> getAllFromDb(){
        return RealmManager.safeReadRealm(new RealmManager.Reader<List<BundleVersion>>() {
            @Override
            public List<BundleVersion> read(Realm realm) {
                RealmResults<BundleVersion> realmResults = realm.where(BundleVersion.class).findAll();
                return realm.copyFromRealm(realmResults);
            }
        });
    }

    public void delete(@NonNull final BundleVersion bundleVersion){
        RealmManager.safeWriteRealm(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(BundleVersion.class)
                        .equalTo("id", bundleVersion.getId())
                        .findAll()
                        .deleteAllFromRealm();
            }
        });
    }



    @Override
    protected boolean isUseMemory() {
        return false;
    }
}
