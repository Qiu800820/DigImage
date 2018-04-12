package com.sum.base.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Scanner;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;


/**
 * Created by Administrator on 2016/11/10.
 */

public class FileUtils {

    private static final String TAG = "FileUtils";

    static final String UTF_8 = "UTF-8";
    static final int SHORT_LENGTH = 2;
    static final byte[] MAGIC = new byte[]{0x21, 0x5a, 0x58, 0x4b, 0x21}; //!ZXK!

    public static String readZipComment(Context context) {
        if (context != null) {
            long startTime = System.currentTimeMillis();
            Log.d(TAG, "读取安装包渠道--开始时间" + startTime);
            String sourceDir = context.getApplicationInfo().publicSourceDir;
            String channelId = readZipComment(new File(sourceDir));
            Log.d(TAG, String.format("读取安装包渠道--结束耗时%s, ChannelId%s", (System.currentTimeMillis() - startTime), channelId));
            return channelId;
        }
        return null;
    }

    /**
     * 获取安装包渠道信息
     *
     * @param file 安装包
     * @return String
     */
    private static String readZipComment(File file) {
        RandomAccessFile raf = null;
        try {
            raf = new RandomAccessFile(file, "r");
            long index = raf.length();
            byte[] buffer = new byte[MAGIC.length];
            index -= MAGIC.length;
            // 跳过内容区
            raf.seek(index);
            raf.readFully(buffer);
            if (isMagicMatched(buffer)) {
                index -= SHORT_LENGTH;
                raf.seek(index);
                // read content length field
                byte[] buf = new byte[SHORT_LENGTH];
                raf.readFully(buf);
                ByteBuffer bb = ByteBuffer.wrap(buf).order(ByteOrder.LITTLE_ENDIAN);
                int length = bb.getShort(0);
                if (length > 0) {
                    index -= length;
                    raf.seek(index);
                    // 读取Comment 内容
                    byte[] bytesComment = new byte[length];
                    raf.readFully(bytesComment);
                    return new String(bytesComment, UTF_8);
                } else {
                    return null;
                }
            } else {
                return null;
            }
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            CloseUtils.closeIOQuietly(raf);
        }

        return null;
    }

    /**
     * 判断是否是一个有效的Zip Comment
     *
     * @param buffer byte[]
     * @return boolean
     */
    private static boolean isMagicMatched(byte[] buffer) {
        if (buffer.length != MAGIC.length) {
            return false;
        }
        for (int i = 0; i < MAGIC.length; ++i) {
            if (buffer[i] != MAGIC[i]) {
                return false;
            }
        }
        return true;
    }

    public static Flowable<String> getTotalCacheSize(final File... files){
        return Flowable.create(new FlowableOnSubscribe<String>() {
            @Override
            public void subscribe(FlowableEmitter<String> emitter) throws Exception {
                long cacheSize = 0;
                for (File file : files) {
                    if (file != null) {
                        cacheSize += getFolderSize(file);
                    }
                }
                emitter.onNext(getFormatSize(cacheSize));
                emitter.onComplete();
            }
        }, BackpressureStrategy.LATEST);
    }


    public static Observable<Boolean> clearAllCache(final File... files) {
        return Observable.create(new ObservableOnSubscribe<Boolean>() {
            @Override
            public void subscribe(ObservableEmitter<Boolean> emitter) throws Exception {
                for (File file : files) {
                    if (file != null) {
                        deleteDirOrFile(file);
                    }
                }
                emitter.onNext(true);
                emitter.onComplete();
            }
        });
    }

    public static boolean deleteDirOrFile(@NonNull final File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();

                boolean success;
                for (String aChildren : children) {
                    success = deleteDirOrFile(new File(dir, aChildren));
                    if (!success) {
                        return false;
                    }
                }

        }
        return dir.delete();
    }

    private static long getFolderSize(File file) throws Exception {
        long size = 0;
        try {
            File[] fileList = file.listFiles();
            for (File aFileList : fileList) {
                // 如果下面还有文件
                if (aFileList.isDirectory()) {
                    size = size + getFolderSize(aFileList);
                } else {
                    size = size + aFileList.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return size;
    }

    /**
     * 格式化单位
     */
    public static String getFormatSize(double size) {

        String[] units = new String[]{"B", "KB", "MB", "GB", "TB"};
        int index = 0;
        while (size > 1024 && index < units.length) {
            size = size / 1024;
            index++;
        }

        BigDecimal result = new BigDecimal(Double.toString(size));
        return result.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString() + units[index];


    }

    public static String readStreamIntoString(InputStream inputStream) throws Exception{
        Scanner s = new Scanner(inputStream).useDelimiter("\\A");
        return s.hasNext() ? s.next() : "";
    }

}
