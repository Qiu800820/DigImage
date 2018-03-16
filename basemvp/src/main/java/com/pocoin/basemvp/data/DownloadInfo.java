package com.pocoin.basemvp.data;

/**
 * Created by Administrator on 2016/11/11.
 */

public class DownloadInfo {

    public static final int SUCCESS = 0x001;
    public static final int CANCEL = 0x002;
    public static final int WAIT = 0x003;

    private String name;
    private String path;
    private String uri;
    private String md5;
    private int status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMd5() {
        return md5;
    }

    public void setMd5(String md5) {
        this.md5 = md5;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
