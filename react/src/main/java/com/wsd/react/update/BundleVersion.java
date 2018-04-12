package com.wsd.react.update;


import com.sum.base.util.DownloadUtil;

import java.io.File;

import io.realm.RealmObject;
import io.realm.internal.Keep;

/**
 * Created by Sen on 2017/9/25.
 */
@Keep
public class BundleVersion extends RealmObject implements DownloadUtil.DownloadInfo{

    private String id;
    private String downloadUrl;
    /**
     * APP版本
     */
    private String version;
    /**
     * Bundle版本
     */
    private String sourceVersion;

    private String downloadPath;
    private int downloadStatus;
    private boolean isUnzip;

    @Override
    public String getPath() {
        return getDownloadPath();
    }

    @Override
    public String getUri() {
        return getDownloadUrl();
    }

    @Override
    public void setStatus(int status) {
        setDownloadStatus(status);
    }

    @Override
    public int getStatus() {
        return getDownloadStatus();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getSourceVersion() {
        return sourceVersion;
    }

    public void setSourceVersion(String sourceVersion) {
        this.sourceVersion = sourceVersion;
    }

    public String getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(String downloadPath) {
        this.downloadPath = downloadPath;
    }

    public int getDownloadStatus() {
        return downloadStatus;
    }

    public void setDownloadStatus(int downloadStatus) {
        this.downloadStatus = downloadStatus;
    }

    public void createDownloadPath(String downloadDirPath){
        if(downloadDirPath != null){
            File file = new File(downloadDirPath, createFileName());
            setDownloadPath(file.getPath());
        }
    }

    public String createFileName(){
        return id + "_jsbundle.zip";
    }

    public String getUnzipPath() {
        File file = new File(getDownloadPath());
        return file.getParent() + File.separator + id + "_jsbundle";
    }

    public String getJsBundlePath(){
        return getUnzipPath() + File.separator + "main.jsbundle";
    }

    public boolean isUnzip() {
        return isUnzip;
    }

    public void setUnzip(boolean unzip) {
        isUnzip = unzip;
    }
}
