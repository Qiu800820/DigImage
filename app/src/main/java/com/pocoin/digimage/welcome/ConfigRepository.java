package com.pocoin.digimage.welcome;


import com.wsd.react.update.BundleVersion;

import rx.Observable;

/**
 * Created by Administrator on 2017/1/9.
 */

public interface ConfigRepository {

    /**
     * 查询新版本
     */
    Observable<BundleVersion> queryBundleVersion(String version, String platform, String localBundleVersion);

}
