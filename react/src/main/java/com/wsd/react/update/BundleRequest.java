package com.wsd.react.update;

import rx.Observable;

/**
 * Created by Sen on 2017/12/22.
 */

public interface BundleRequest {
    Observable<BundleVersion> getBundle(String appVersion, String platform, String localBundleVersion);
}
