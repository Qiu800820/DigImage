package com.pocoin.digimage.react.module.network;

import android.support.annotation.NonNull;

import com.facebook.react.modules.network.ProgressRequestBody;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

/**
 * Created by Sen on 2018/3/23.
 */

public class CustomLoggerInterceptor implements Interceptor{

    private HttpLoggingInterceptor httpLoggingInterceptor;

    public CustomLoggerInterceptor(@NonNull HttpLoggingInterceptor httpLoggingInterceptor){
        this.httpLoggingInterceptor = httpLoggingInterceptor;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response response = null;
        Request request = chain.request();
        if(request.body() instanceof ProgressRequestBody){ //ProgressRequestBody直接跳过
            response = chain.proceed(request);
        }else{
            response = httpLoggingInterceptor.intercept(chain);
        }
        return response;
    }
}
