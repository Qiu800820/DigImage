package com.pocoin.digimage.welcome;


import com.wsd.react.update.BundleVersion;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by Administrator on 2016/11/24.
 */

public interface ConfigApi {

    @GET("api/user/version/invoice")
    Observable<BundleVersion> getBundle(@Query("version") String version, @Query("platform") String platform, @Query("sourceVersion") String localBundleVersion);



}
