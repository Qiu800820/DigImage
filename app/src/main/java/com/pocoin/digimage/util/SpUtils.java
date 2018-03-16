package com.pocoin.digimage.util;

/**
 * Created by Administrator on 2016/11/22.
 */

import android.content.SharedPreferences;
import android.text.TextUtils;

import com.jiongbull.jlog.JLog;
import com.pocoin.digimage.MyApp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 管理程序里Sp存储
 */
public class SpUtils {

    public static final String TAG = SpUtils.class.getSimpleName();

    private SharedPreferences mSp;
    private static ReentrantLock mLock = new ReentrantLock();
    private static SpUtils mInstance;

    public static SpUtils getInstance(){
        try{
            mLock.lock();
            if(null == mInstance){
                mInstance = new SpUtils(null);
            }
            return mInstance;
        }finally{
            mLock.unlock();
        }
    }

    private static final String SP_NAME = "config";
    private static final int SP_OPEN_MODE = 0;

    private SpUtils(SharedPreferences sharedPreferences) {
        if(sharedPreferences == null) {
            mSp = MyApp.getApplicationInstance().getSharedPreferences(SP_NAME, SP_OPEN_MODE);
        }else{
            mSp = sharedPreferences;
        }

    }

    /**
     * 保存字符串到sp文件
     * @param key 字符串对应的key
     * @param value 字符串的vaule
     * @return true 保存成功  false 保存失败
     */
    public boolean saveStringToSp(String key , String value){

        if(TextUtils.isEmpty(key) || TextUtils.isEmpty(value)){
            JLog.e(TAG, "=== saveStringToSp(),保存失败,key不能为null, 自动删除 === ");
            remove(key);
            return false;
        }
        mSp.edit().putString(key, value).apply();
        return true;
    }

    /**
     * 删除字段
     */
    public boolean remove(String key){

        if(TextUtils.isEmpty(key)){
            JLog.e(TAG, "=== remove(),删除失败,key不能为null === ");
            return false;
        }
        mSp.edit().remove(key).apply();
        return true;
    }

    /**
     *
     * 保存整形值到sp
     * @param key 整形值对应的key
     * @param value 整形值对应的value
     * @return true 保存成功  false 保存失败
     */
    public boolean saveIntToSp(String key , int value){

        if(TextUtils.isEmpty(key)){
            JLog.e(TAG, "=== saveIntToSp(),保存失败,key不能为null === ");
            return false;
        }
        mSp.edit().putInt(key, value).apply();
        return true;
    }

    /**
     *
     * 保存整形值到sp
     * @param key 整形值对应的key
     * @param value 整形值对应的value
     * @return true 保存成功  false 保存失败
     */
    public boolean saveLongToSp(String key , long value){

        if(TextUtils.isEmpty(key)){
            JLog.e(TAG , "=== saveLongToSp(),保存失败,key不能为null === ");
            return false;
        }
        mSp.edit().putLong(key, value).apply();
        return true;
    }

    public boolean saveStringListToSp(String key , List<String> value){

        if(TextUtils.isEmpty(key)){
            JLog.e(TAG , "=== saveLongToSp(),保存失败,key不能为null === ");
            return false;
        }
        JSONArray jsonArray = new JSONArray();
        for (int i = 0; i < value.size(); i++) {
            try {
                jsonArray.put(i,value.get(i));
            } catch (JSONException e) {
                JLog.e(TAG , "=== saveStringListToSp " + e.getMessage()+" ===");
                return false;
            }
        }
        if (jsonArray.length() > 0){
            mSp.edit().putString(key,jsonArray.toString());
            return true;
        }else {
            return false;
        }
    }

    public List<String> getStringListValue(String key,List<String> defaultValue){

        if(TextUtils.isEmpty(key)){
            JLog.e(TAG , "=== getStringListValue(),获取失败,key不能为null === ");
            return defaultValue;
        }
        String fromSp = mSp.getString(key,"");
        if (TextUtils.isEmpty(fromSp)){
            return defaultValue;
        }
        try {
            JSONArray jsonArray = new JSONArray(fromSp);
            List<String> strings = new ArrayList<>();
            JLog.e(TAG , "=== getStringListValue(),获取失败,json数据异常 === ");
            for (int i = 0; i < jsonArray.length(); i++) {
                strings.add(jsonArray.get(i).toString());
            }
            return strings;
        } catch (JSONException e) {
            return defaultValue;
        }

    }


    /**
     *
     * 保存布尔值到sp
     * @param key 布尔值对应的key
     * @param value 布尔值对应的value
     * @return true 保存成功 fasle 保存失败
     */
    public boolean saveBoolenTosp(String key , boolean value){

        if(TextUtils.isEmpty(key)){
            JLog.e(TAG , "=== saveBoolenTosp(),保存失败,key不能为null === ");
            return false;
        }
        mSp.edit().putBoolean(key, value).apply();
        return true;
    }

    /**
     * 根据保存到sp的key 取出String value
     * @param key  String对应的key
     * @param defaultValue String对应的Value
     */
    public String getStringValue(String key , String defaultValue){

        if(TextUtils.isEmpty(key)){
            return defaultValue;
        }
        return mSp.getString(key, defaultValue);
    }

    public JSONArray getJSONArray(String key){
        if (TextUtils.isEmpty(key)){
            return new JSONArray();
        }

        String jsonStr = mSp.getString(key, "[]");
        JSONArray json;
        try {
            json = new JSONArray(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONArray();
        }
        return json;
    }

    public JSONObject getJSONObject(String key){
        if (TextUtils.isEmpty(key)){
            return new JSONObject();
        }

        String jsonStr = mSp.getString(key, "{}");
        JSONObject json;
        try {
            json = new JSONObject(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
            return new JSONObject();
        }
        return json;
    }

    /**
     * 根据保存到sp的key 取出Int value
     * @param key 保存的key
     * @param defaultValue 默认值
     * @return key 对应的 int value
     */
    public int getIntValue(String key , int defaultValue){

        if(TextUtils.isEmpty(key)){
            return defaultValue;
        }
        return mSp.getInt(key, defaultValue);

    }

    /**
     * 根据保存到sp的key 取出Int value
     * @param key 保存的key
     * @param defaultValue 默认值
     * @return key 对应的 int value
     */
    public long getLongValue(String key , long defaultValue){

        if(TextUtils.isEmpty(key)){
            return defaultValue;
        }
        return mSp.getLong(key, defaultValue);

    }

    /**
     * 根据保存到sp的key Boolean value
     * @param key 保存的key
     * @param defaultValue 默认值
     * @return key 对应的 Boolean value
     */
    public boolean getBooleanValue(String key , boolean defaultValue){
        if(TextUtils.isEmpty(key)){
            return defaultValue;
        }
        return mSp.getBoolean(key, defaultValue);
    }

}
