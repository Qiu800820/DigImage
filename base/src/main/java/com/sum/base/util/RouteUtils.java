package com.sum.base.util;

import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;

/**
 * Created by Sen on 2017/12/22.
 */

public class RouteUtils {

    private static final String TAG = "RouteUtils";

    public static Intent[] parseRoutes(String uris){
        if(TextUtils.isEmpty(uris)) {
            Log.e(TAG, "parseIntents uris is null");
            return null;
        }
        String[] intentStringArray = uris.split(";end", 0);
        if(intentStringArray.length == 0){
            return null;
        }

        Intent[] intents = new Intent[intentStringArray.length];

        for (int i = 0; i < intentStringArray.length; i++) {
            String intentStr = intentStringArray[i];
            if(intentStr.startsWith("android-app://"))
                intentStr = intentStr + ";end";
            intents[i] = parseRoute(intentStr);
        }
        return intents;
    }

    /**
     * 根据Uri String 解析成Intent
     * @param uri android-app://component=com.my.package%2f.package.MyActivity;i.int_params=1;launchFlags=0x07000000;end
     * @return Intent
     */
    public static Intent parseRoute(String uri){
        if(TextUtils.isEmpty(uri)) {
            Log.e(TAG, "parseIntent uri is null");
            return null;
        }
        Intent intent = new Intent();
        Bundle bundle = null;
        int i = 0;
        if(uri.startsWith("android-app://")){
            i = 14;
            while (i >= 0 && !uri.startsWith("end", i)) {
                int eq = uri.indexOf('=', i);
                if (eq < 0) eq = i-1;
                int semi = uri.indexOf(';', i);
                String value = eq < semi ? Uri.decode(uri.substring(eq + 1, semi)) : "";

                // action
                if (uri.startsWith("action=", i)) {
                    intent.setAction(value);
                }

                // categories
                else if (uri.startsWith("category=", i)) {
                    intent.addCategory(value);
                }

                //data
                else if(uri.startsWith("data=", i)){
                    intent.setData(Uri.parse(value));
                }

                // launch flags
                else if (uri.startsWith("launchFlags=", i)) {
                    int flags = Integer.decode(value);
                    intent.setFlags(flags);
                }

                // package
                else if (uri.startsWith("package=", i)) {
                    intent.setPackage(value);
                }

                // component
                else if (uri.startsWith("component=", i)) {
                    intent.setComponent(ComponentName.unflattenFromString(value));
                }
                // extra
                else {
                    String key = Uri.decode(uri.substring(i + 2, eq));
                    // create Bundle if it doesn't already exist
                    if (bundle == null) bundle = new Bundle();
                    // add EXTRA
                    if      (uri.startsWith("S.", i)) bundle.putString(key, value);
                    else if (uri.startsWith("B.", i)) bundle.putBoolean(key, Boolean.parseBoolean(value));
                    else if (uri.startsWith("b.", i)) bundle.putByte(key, Byte.parseByte(value));
                    else if (uri.startsWith("c.", i)) bundle.putChar(key, value.charAt(0));
                    else if (uri.startsWith("d.", i)) bundle.putDouble(key, Double.parseDouble(value));
                    else if (uri.startsWith("f.", i)) bundle.putFloat(key, Float.parseFloat(value));
                    else if (uri.startsWith("i.", i)) bundle.putInt(key, Integer.parseInt(value));
                    else if (uri.startsWith("l.", i)) bundle.putLong(key, Long.parseLong(value));
                    else if (uri.startsWith("s.", i)) bundle.putShort(key, Short.parseShort(value));
                    else return null;


                }

                // move to the next item
                i = semi + 1;
            }

            if(bundle != null)
                intent.putExtras(bundle);
        }else  {
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(uri));
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        return intent;
    }

}
