package com.sum.base.util;

import android.util.Log;

import java.io.File;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

/**
 * 拓展断点续传
 * Created by Administrator on 2016/11/11.
 */

public class DownloadUtil {

    public static DownloadInfo downloadInfoSync(OkHttpClient httpClient, DownloadInfo downloadInfo, boolean isResume) throws Exception{
        BufferedSink sink = null;
        BufferedSource source = null;
        float currentSize = 0;
        float totalSize = 0;
        long lastTime = System.currentTimeMillis();
        try {

            // 1.计算断点位置并创建请求
            File file = new File(downloadInfo.getPath());
            Request.Builder builder = new Request.Builder();
            builder.url(downloadInfo.getUri());
            if (isResume && file.exists()) {
                currentSize = file.length();
                builder.header("RANGE", "bytes=" + currentSize + "-");
            }
            Request request = builder.build();
            Call call = httpClient.newCall(request);
            Response response = call.execute();

            //文件传输
            byte[] buf = new byte[2048];
            int len = 0;
            source = response.body().source();
            try {
                totalSize = Float.valueOf(response.header("Content-Length"));
                totalSize = totalSize > 0?totalSize:1;
            }catch (Exception e){
                totalSize = 1;
            }
            if(isResume){
                sink = Okio.buffer(Okio.appendingSink(file));
            }else{
                currentSize = 0;
                sink = Okio.buffer(Okio.sink(file));
            }
            while ((len = source.read(buf)) != -1) {
                sink.write(buf, 0, len);
                currentSize += len;
                if(System.currentTimeMillis() - lastTime > 1000)
                    Log.d("DownloadUtil", String.format("=== download progress %f===", currentSize / totalSize));
            }
            downloadInfo.setStatus(DownloadInfo.SUCCESS);
        }finally {
            CloseUtils.closeIOQuietly(sink);
            CloseUtils.closeIOQuietly(source);
        }
        return downloadInfo;
    }

    public static Observable<DownloadInfo> downloadFile(final OkHttpClient httpClient, final DownloadInfo downloadInfo, final boolean isResume) {
        ObservableOnSubscribe<DownloadInfo> subscribe = new ObservableOnSubscribe<DownloadInfo>() {
            @Override
            public void subscribe(ObservableEmitter<DownloadInfo> emitter) throws Exception {
                DownloadInfo downloadinfo = downloadInfoSync(httpClient, downloadInfo, isResume);
                emitter.onNext(downloadinfo);
                emitter.onComplete();
            }
        };

        return Observable.create(subscribe);

    }

    public static interface DownloadInfo {

        public static final int SUCCESS = 0x001;
        public static final int CANCEL = 0x002;
        public static final int WAIT = 0x003;

        String getPath();

        String getUri();

        /**
         * download status
         *
         * @param status <li>{@link #SUCCESS}</li><li>{@link #CANCEL}</li><li>{@link #WAIT}</li>
         */
        void setStatus(int status);

        int getStatus();
    }


}
