package com.wsd.react.update;

import android.util.Log;

import com.jiongbull.jlog.JLog;
import com.pocoin.basemvp.util.CloseUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by Sen on 2017/11/17.
 */

public class ZipUtils {

    private static final String TAG = "ZipUtils";


    public static boolean unZipFolder(String zipFilePath, File outFolder){

        ZipInputStream zipInputStream = null;
        FileOutputStream out = null;
        ZipEntry zipEntry;
        boolean result = false;

        try{
            createFolder(outFolder);
            zipInputStream = new ZipInputStream(new FileInputStream(zipFilePath));
            while ((zipEntry = zipInputStream.getNextEntry()) != null) {
                String fileName = zipEntry.getName();
                if (zipEntry.isDirectory()) {
                    createFolder(new File(outFolder, fileName));
                } else {
                    File file = new File(outFolder, fileName);
                    if (!file.exists()) {
                        Log.d(TAG, String.format("Create the file:%s", file.getAbsoluteFile()));
                        file.createNewFile();
                    }

                    out = new FileOutputStream(file);
                    int len;
                    byte[] buffer = new byte[1024];
                    while ((len = zipInputStream.read(buffer)) != -1) {
                        out.write(buffer, 0, len);
                        out.flush();
                    }
                }
            }

            result = true;
        }catch (Exception e){
            JLog.e(e);
            outFolder.delete();
        }finally {
            CloseUtils.closeIOQuietly(out);
            CloseUtils.closeIOQuietly(zipInputStream);
        }
        return result;
    }

    private static void createFolder(File folder){
        if(!folder.exists())
            folder.mkdirs();
    }
}
