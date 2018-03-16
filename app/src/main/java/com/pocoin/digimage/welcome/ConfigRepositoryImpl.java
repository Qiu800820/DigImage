package com.pocoin.digimage.welcome;


import com.wsd.react.update.BundleVersion;

import rx.Observable;

/**
 * Created by Administrator on 2017/1/9.
 */

public class ConfigRepositoryImpl implements ConfigRepository{

    private ConfigApi configApi;

    public ConfigRepositoryImpl(ConfigApi configApi) {
        this.configApi = configApi;
    }

    @Override
    public Observable<BundleVersion> queryBundleVersion(String version, String platform, String localBundleVersion) {
        return configApi.getBundle(version, platform, localBundleVersion);
    }
}
