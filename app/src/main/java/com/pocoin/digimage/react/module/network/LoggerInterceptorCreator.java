package com.pocoin.digimage.react.module.network;

import com.facebook.react.modules.network.NetworkInterceptorCreator;
import com.pocoin.digimage.InjectionRepository;

import okhttp3.Interceptor;

/**
 * Created by Sen on 2017/12/26.
 */

public class LoggerInterceptorCreator implements NetworkInterceptorCreator {
    @Override
    public Interceptor create() {
        return InjectionRepository.getHttpLoggingInterceptor();
    }
}
