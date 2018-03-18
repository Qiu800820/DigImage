package com.pocoin.digimage;

import android.content.Context;

import com.jiongbull.jlog.JLog;
import com.pocoin.digimage.welcome.ConfigApi;
import com.pocoin.digimage.welcome.ConfigRepository;
import com.pocoin.digimage.welcome.ConfigRepositoryImpl;

import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Sen on 2018/3/16.
 */

public class InjectionRepository {
    public static boolean REACT_NATIVE_DEVELOPER_SUPPORT = true;
    public static final String BASE_URL = "http://192.168.118.111:7000/";
    private static OkHttpClient okHttpClient;
    private static ConfigApi configApi;

    static Retrofit getRetrofitInstance(String baseUrl) {
        return new Retrofit.Builder().client(getOkHttpClient()).baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
    }

    public static OkHttpClient getOkHttpClient() {
        if (okHttpClient == null) {
            okHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(50, TimeUnit.SECONDS)
                    .readTimeout(50, TimeUnit.SECONDS)
                    .cache(getCache())
//                    .addInterceptor(getHttpLoggingInterceptor())
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
                JLog.d(message);
            }
        };

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(logger);
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        return logging;
    }

    public static ConfigApi provideConfigApi() {
        if (configApi == null) {
            configApi = getRetrofitInstance(BASE_URL).create(ConfigApi.class);
        }
        return configApi;
    }

    public static ConfigRepository provideConfigRepository() {
        return new ConfigRepositoryImpl(provideConfigApi());
    }

}
