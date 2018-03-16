package com.pocoin.basemvp.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by Robert yao on 2016/11/1.
 */

public class CloseUtils {

    private CloseUtils() {
    }

    public static void closeIOQuietly(Closeable... closeables) {
        if (closeables == null) return;
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                } catch (IOException ignored) {
                }
            }
        }
    }
}
