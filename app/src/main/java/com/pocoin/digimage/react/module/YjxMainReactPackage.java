package com.pocoin.digimage.react.module;

import com.facebook.react.bridge.ModuleSpec;
import com.facebook.react.bridge.NativeModule;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.modules.network.NetworkInterceptorCreator;
import com.facebook.react.modules.network.NetworkingModule;
import com.facebook.react.shell.MainReactPackage;
import com.pocoin.digimage.react.module.network.LoggerInterceptorCreator;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Provider;

/**
 * 替换MainReactNative NetworkModule
 * Created by Sen on 2017/12/26.
 */

public class YjxMainReactPackage extends MainReactPackage {

    @Override
    public List<ModuleSpec> getNativeModules(ReactApplicationContext context) {
        List<ModuleSpec> nativeModules = super.getNativeModules(context);
        return adjustModules(context, nativeModules);
    }

    private List<ModuleSpec> adjustModules(ReactApplicationContext context, List<ModuleSpec> moduleSpecs) {
        ArrayList<ModuleSpec> modules = new ArrayList<>(moduleSpecs);

        for (int i = 0; i < modules.size(); i++) {
            ModuleSpec spec = modules.get(i);
            if (spec.getType().equals(NetworkingModule.class)) {
                modules.set(i, getCustomNetworkingModule(context));
                break;
            }
        }

        return modules;
    }

    private ModuleSpec getCustomNetworkingModule(final ReactApplicationContext context) {

        return new ModuleSpec(NetworkingModule.class, new Provider<NativeModule>() {
            @Override
            public NativeModule get() {
                return new NetworkingModule(context, createNetworkInterceptor());
            }
        });
    }

    private List<NetworkInterceptorCreator> createNetworkInterceptor(){
        List<NetworkInterceptorCreator> networkInterceptorCreators = new ArrayList<>(4);
//        networkInterceptorCreators.add(new LoggerInterceptorCreator());
        return networkInterceptorCreators;
    }

}
