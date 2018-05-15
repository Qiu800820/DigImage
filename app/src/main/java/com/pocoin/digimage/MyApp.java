package com.pocoin.digimage;

import android.app.Application;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;

import com.BV.LinearGradient.LinearGradientPackage;
import com.facebook.react.ReactApplication;
import com.facebook.react.ReactNativeHost;
import com.facebook.react.ReactPackage;
import com.microsoft.codepush.react.CodePush;
import com.microsoft.codepush.react.CodePushBuilder;
import com.oblador.vectoricons.VectorIconsPackage;
import com.pocoin.digimage.react.module.YjxMainReactPackage;
import com.pocoin.digimage.util.ComUtils;
import com.theweflex.react.WeChatPackage;

import org.reactnative.camera.RNCameraPackage;

import java.util.Arrays;
import java.util.List;

import cn.qiuxiang.react.amap3d.AMap3DPackage;
import fr.bamlab.rnimageresizer.ImageResizerPackage;

import static com.pocoin.digimage.InjectionRepository.REACT_NATIVE_DEVELOPER_SUPPORT;


/**
 * Created by Administrator on 2016/10/17.
 */
public class MyApp extends Application implements ReactApplication {


    private static MyApp mInstance;
    private AppLibLifecycleManager appLibLifecycleManager;
    private ReactNativeHost mReactNativeHost;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(base);
    }

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
                return CodePush.getJSBundleFile();
            }

            @Override
            public boolean getUseDeveloperSupport() {
                return REACT_NATIVE_DEVELOPER_SUPPORT;
            }

            @Override
            protected List<ReactPackage> getPackages() {
                return Arrays.<ReactPackage>asList(
                        new YjxMainReactPackage(),
                        new RNCameraPackage(),
                        new LinearGradientPackage(),
                        new VectorIconsPackage(),
                        new ImageResizerPackage(),
                        new AMap3DPackage(),
                        new WeChatPackage(),
                        new CodePushBuilder("1GScdrWkJNajepr9kr2bmnQ2MDnc4ksvOXqog", MyApp.this)
                                .setIsDebugMode(BuildConfig.DEBUG)
                                .setServerUrl("http://push.markartisan.com/")
                                .build()

                );
            }

            @Override
            protected String getBundleAssetName() {
                return "index.android.bundle";
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