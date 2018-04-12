package com.wsd.react.update;


import io.reactivex.Observable;

/**
 * Created by Sen on 2017/12/22.
 */

public interface BundleRequest {
    Observable<BundleVersion> getBundle(String appVersion, String platform, String localBundleVersion);
}
