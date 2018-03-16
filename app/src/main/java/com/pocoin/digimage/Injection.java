package com.pocoin.digimage;

import com.pocoin.digimage.welcome.WelcomeUseCase;
import com.wsd.react.update.BundleRequest;
import com.wsd.react.update.BundleVersion;
import com.wsd.react.update.CodePush;
import com.wsd.react.update.UpdateEvent;

import rx.Observable;

import static com.pocoin.digimage.InjectionRepository.getOkHttpClient;
import static com.pocoin.digimage.InjectionRepository.provideConfigRepository;

/**
 * Created by Sen on 2018/3/16.
 */

public class Injection {


    public static CodePush provideCodePush(){
        return new CodePush.CodePushBuilder()
                .setBundleRequest(provideBundleRequest())
                .setOkHttpClient(getOkHttpClient())
                .setAppVersionName(BuildConfig.VERSION_NAME)
                .setUpdateEvent(new UpdateEvent() {
                    @Override
                    public void onUpdate(BundleVersion bundleVersion) {
                        if(MyApp.getApplicationInstance() != null)
                            MyApp.getApplicationInstance().updateReactNativeHost();
                    }
                }).build();
    }



    private static BundleRequest provideBundleRequest() {
        return new BundleRequest() {
            @Override
            public Observable<BundleVersion> getBundle(String appVersion, String platform, String localBundleVersion) {
                return provideConfigRepository().queryBundleVersion(appVersion, platform, localBundleVersion);
            }
        };
    }


    public static WelcomeUseCase provideWelcomeUseCase() {
        return new WelcomeUseCase();
    }
}
