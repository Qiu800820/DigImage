package com.wsd.react.update;

import android.support.annotation.NonNull;
import android.util.Log;

import com.jiongbull.jlog.JLog;
import com.pocoin.basemvp.util.DownloadUtil;
import com.pocoin.basemvp.util.FileUtils;
import com.wsd.react.BuildConfig;

import java.io.File;
import java.util.List;

import okhttp3.OkHttpClient;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Sen on 2017/9/25.
 */

public class CodePush {

    private static final String TAG = "CodePush";
    private static final String PLATFORM_ANDROID = "android";

    private BundleRequest bundleRequest;
    private UpdateEvent updateEvent;
    private OkHttpClient okHttpClient;
    private String appVersionName;

    private CodePush(@NonNull BundleRequest bundleRequest, UpdateEvent updateEvent, @NonNull OkHttpClient okHttpClient, @NonNull String appVersionName){
        this.bundleRequest = bundleRequest;
        this.updateEvent= updateEvent;
        this.okHttpClient = okHttpClient;
        this.appVersionName = appVersionName;

    }

    /**
     * 查询新版本并下载
     */
    public void updateVersion(final String downloadDirPath) {
        JLog.d(TAG, "=== update version ===");
        BundleCacheImpl.getInstance().get() // 1.获取本地最新Bundle
                .map(new Func1<BundleVersion, BundleVersion>() {
                    @Override
                    public BundleVersion call(BundleVersion bundleVersion) {
                        if(bundleVersion != null) {
                            File file = new File(bundleVersion.getJsBundlePath());
                            if (!file.exists()){
                                JLog.w(TAG, "=== 当前Bundle版本文件已删除，清除记录 ===");
                                BundleCacheImpl.getInstance().destroy();
                                bundleVersion = null;
                            }
                        }
                        JLog.w(TAG, String.format("=== 当前Bundle版本%s ===", bundleVersion == null?"0":bundleVersion.getSourceVersion()));
                        return bundleVersion;
                    }
                })
                .flatMap(new Func1<BundleVersion, Observable<BundleVersion>>() {
                    @Override
                    public Observable<BundleVersion> call(BundleVersion bundleVersion) {
                        //2.请求服务器最新Bundle
                        return bundleRequest.getBundle(
                                BuildConfig.VERSION_NAME, PLATFORM_ANDROID,
                                bundleVersion == null?"0":bundleVersion.getSourceVersion());
                    }
                })
                .subscribeOn(Schedulers.io())
                .map(new Func1<BundleVersion, BundleVersion>() {
                    //2.1有新版本下载并插入数据库, 返回下载后Bundle
                    @Override
                    public BundleVersion call(BundleVersion bundleVersion){
                        bundleVersion.createDownloadPath(downloadDirPath);
                        download(bundleVersion);
                        BundleCacheImpl.getInstance().put(bundleVersion);
                        return bundleVersion;
                    }
                })
                .onErrorResumeNext(new Func1<Throwable, Observable<? extends BundleVersion>>() {
                    //2.2没有新版本情况 返回本地数据库版本
                    @Override
                    public Observable<? extends BundleVersion> call(Throwable throwable) {
                        JLog.d(TAG, "=== 服务器无新版本， 检查本地未解压记录===");
                        return BundleCacheImpl.getInstance().get();
                    }
                })
                .map(new Func1<BundleVersion, BundleVersion>() {
                    //3解压并更新状态
                    @Override
                    public BundleVersion call(BundleVersion bundleVersion) {
                        unZip(bundleVersion);
                        JLog.d(TAG, "=== 解压成功  更新数据===");
                        deleteHistoryBundleFileAndData(bundleVersion);
                        BundleCacheImpl.getInstance().put(bundleVersion);
                        return bundleVersion;
                    }
                })
                .subscribe(new Subscriber<BundleVersion>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        JLog.e(TAG, e, "=== update new version error ===");
                    }

                    @Override
                    public void onNext(BundleVersion bundleVersion) {
                        JLog.d(TAG, "=== 更新处理完成  通知React刷新Host===");
                        if(updateEvent != null){
                            updateEvent.onUpdate(bundleVersion);
                        }
                    }
                });
    }

    private DownloadUtil.DownloadInfo download(final BundleVersion bundleVersion){
        if (!isNeedDownload(bundleVersion)) {
            throw new IllegalArgumentException("服务器返回Bundle信息错误，与当前React版本不兼容，抛弃更新请求");
        }
        DownloadUtil.DownloadInfo downloadInfo;
        JLog.d(TAG, String.format("=== download new version！ path:%s===", bundleVersion.getDownloadPath()));
        try {
            downloadInfo = DownloadUtil.downloadInfoSync(okHttpClient, bundleVersion, false);
        } catch (Throwable throwable) {
            throw new RuntimeException("下载失败", throwable);
        }
        return downloadInfo;
    }

    private boolean isNeedDownload(@NonNull BundleVersion serviceVersion) {
        BundleVersion localVersion = BundleCacheImpl.getInstance().getNewVersionFromDb(appVersionName);
        Log.d(TAG, String.format("=== localBundleVersion:%s, serviceBundleVersion:%s   localAppVersion:%s, serviceAppVersion:%s===",
                localVersion == null?"0":localVersion.getSourceVersion(), serviceVersion.getSourceVersion(),
                BuildConfig.VERSION_NAME, serviceVersion.getVersion()));

        return localVersion == null ||
                (safeIntValue(localVersion.getSourceVersion()) < safeIntValue(serviceVersion.getSourceVersion())
                && BuildConfig.VERSION_NAME.equals(serviceVersion.getVersion()));
    }

    private void unZip(BundleVersion bundleVersion){
        if(bundleVersion == null){
            throw new IllegalArgumentException("本地Bundle不存在，抛弃更新请求");
        }

        if(bundleVersion.isUnzip()){
            throw new IllegalArgumentException("Bundle已解压完成，抛弃更新请求");
        }
        if(ZipUtils.unZipFolder(bundleVersion.getDownloadPath(), new File(bundleVersion.getUnzipPath()))){
            bundleVersion.setUnzip(true);
        }else{
            throw new IllegalArgumentException("Bundle解压异常，抛弃更新请求");
        }


    }

    public static String getJsBundleFile(String appVersionName) {
        String bundleFile = null;
        BundleVersion localVersion = BundleCacheImpl.getInstance().getFromDbByUnzip(appVersionName);
        if (localVersion != null && BuildConfig.VERSION_NAME.equals(localVersion.getVersion())) {
            File file = new File(localVersion.getJsBundlePath());
            if(!file.exists()){
                new Thread(new Runnable(){
                    @Override
                    public void run() {
                        BundleCacheImpl.getInstance().destroy();
                    }
                }).start();
            }else{
                bundleFile = file.getAbsolutePath();
            }
        }
        JLog.d(TAG, "bundle file:" + bundleFile);

        return bundleFile;
    }

    private void deleteHistoryBundleFileAndData(@NonNull BundleVersion exceptBundleVersion){
        try{
            List<BundleVersion> bundleVersionList = BundleCacheImpl.getInstance().getAllFromDb();
            boolean deleteResult;
            File temFile;
            for (BundleVersion bundleVersion : bundleVersionList) {

                if(bundleVersion.getId().equals(exceptBundleVersion.getId()))
                    continue;

                // 删除zip包
                temFile = new File(bundleVersion.getDownloadPath());
                deleteResult = !temFile.exists() || FileUtils.deleteDirOrFile(temFile);
                // 删除zip解压文件
                temFile = new File(bundleVersion.getUnzipPath());
                deleteResult = deleteResult && (!temFile.exists() || FileUtils.deleteDirOrFile(temFile));

                if(deleteResult){
                    BundleCacheImpl.getInstance().delete(bundleVersion);
                }
            }
        }catch (Exception e){
            JLog.w(TAG, String.format("=== 删除文件失败 %s===", e.getMessage()));
        }

    }

    public static int safeIntValue(String string){
        int value = 0;
        try {
            value = Integer.valueOf(string);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return value;
    }

    public static class CodePushBuilder {
        private BundleRequest bundleRequest;
        private UpdateEvent updateEvent;
        private OkHttpClient okHttpClient;
        private String appVersionName;

        public CodePushBuilder setBundleRequest(BundleRequest bundleRequest) {
            this.bundleRequest = bundleRequest;
            return this;
        }

        public CodePushBuilder setOkHttpClient(OkHttpClient okHttpClient) {
            this.okHttpClient = okHttpClient;
            return this;
        }

        public CodePushBuilder setUpdateEvent(UpdateEvent updateEvent) {
            this.updateEvent = updateEvent;
            return this;
        }


        public CodePushBuilder setAppVersionName(String appVersionName) {
            this.appVersionName = appVersionName;
            return this;
        }

        public CodePush build(){
            return new CodePush(bundleRequest, updateEvent, okHttpClient, appVersionName);
        }
    }

}
