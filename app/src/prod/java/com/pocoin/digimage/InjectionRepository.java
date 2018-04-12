package com.pocoin.digimage;

import android.content.Context;

import com.sum.xlog.core.XLog;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Sen on 2018/3/16.
 */

public class InjectionRepository {
    public static boolean REACT_NATIVE_DEVELOPER_SUPPORT = false;
    public static final String BASE_URL = "http://api.markartisan.com/";
    private static OkHttpClient okHttpClient;

    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(50, TimeUnit.SECONDS)
                    .readTimeout(50, TimeUnit.SECONDS)
                    .cache(getCache())
                    .addInterceptor(getHttpLoggingInterceptor())
                    .build();
        }
        return okHttpClient;
    }

    private static Cache getCache() {
        Context context = MyApp.getApplicationInstance();
        return context == null ? null : new Cache(context.getCacheDir(), 2L << 22); // 8M
    }

    public static HttpLoggingInterceptor getHttpLoggingInterceptor() {

        HttpLoggingInterceptor.Logger logger;

        logger = new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                XLog.d(message);
            }
        };

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(logger);
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;
    }

}
