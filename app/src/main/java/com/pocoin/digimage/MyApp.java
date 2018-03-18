package com.pocoin.digimage;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.BV.LinearGradient.LinearGradientPackage;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.oblador.vectoricons.VectorIconsPackage;
import com.pocoin.digimage.react.module.YjxMainReactPackage;
import com.pocoin.digimage.util.ComUtils;
import com.reactnative.ivpusic.imagepicker.PickerPackage;
import com.wsd.react.update.CodePush;

import java.util.Arrays;
import java.util.List;

import static com.pocoin.digimage.InjectionRepository.REACT_NATIVE_DEVELOPER_SUPPORT;


/**
 * Created by Administrator on 2016/10/17.
 */
public class MyApp extends Application implements ReactApplication {


    private static MyApp mInstance;
    private AppLibLifecycleManager appLibLifecycleManager;
    private ReactNativeHost mReactNativeHost;


    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        mReactNativeHost = createReactNativeHost();
        if(ComUtils.inMainProcess(this)){
            initAppLibLifecycleManager();
        }
    }

    public static MyApp getApplicationInstance() {
        return mInstance;
    }

    private void initAppLibLifecycleManager() {
        appLibLifecycleManager = new AppLibLifecycleManagerImpl(this);
        appLibLifecycleManager.onCreate();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        appLibLifecycleManager.onLowMemory();
    }

    @NonNull
    private ReactNativeHost createReactNativeHost() {

        return new ReactNativeHost(this) {

            @Nullable
            @Override
            protected String getJSBundleFile() {
                return CodePush.getJsBundleFile(BuildConfig.VERSION_NAME);
            }

            @Override
            public boolean getUseDeveloperSupport() {
                return REACT_NATIVE_DEVELOPER_SUPPORT;
            }

            @Override
            protected List<ReactPackage> getPackages() {
                return Arrays.<ReactPackage>asList(
                        new YjxMainReactPackage(),
                        new LinearGradientPackage(),
                        new VectorIconsPackage(),
                        new PickerPackage()
                );
            }

            @Override
            protected String getBundleAssetName() {
                return "main.jsbundle";
            }
        };
    }

    @Override
    public ReactNativeHost getReactNativeHost() {
        return mReactNativeHost;
    }

    public void updateReactNativeHost(){
        this.mReactNativeHost = createReactNativeHost();
    }


}