package com.hzp.nzhbj.tool;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * created by hzp on 2019/6/17 16:01
 * 作者：codehan
 * 描述：SharedPreferences的工具类，用于存取数据到sp中
 */
public class SharedPreferencesTool {

    private static SharedPreferences sp;

    /**
     * 保存boolean信息的操作
     * @param context
     * @param key
     * @param value
     */
    public static void saveBoolean(Context context, String key, boolean value){
        if(sp==null) {
            sp = context.getSharedPreferences( "config", Context.MODE_PRIVATE );
        }
        sp.edit().putBoolean( key,value ).commit();
    }

    /**
     * 获取boolean信息的值
     * @param context
     * @param key
     * @param defvalue
     * @return
     */
    public static boolean getBoolean(Context context,String key,boolean defvalue){
        if(sp==null){
            sp=context.getSharedPreferences( "config",Context.MODE_PRIVATE );
        }
        return sp.getBoolean( key,defvalue );
    }

    /**
     * 保存String的值
     * @param context
     * @param key
     * @param value
     */
    public static void saveString(Context context,String key,String value){
        if(sp==null){
            sp=context.getSharedPreferences( "config",Context.MODE_PRIVATE );
        }
        sp.edit().putString( key,value ).commit();
    }

    /**
     * 获取保存的String的值
     * @param context
     * @param key
     * @param defvalue
     * @return
     */
    public static String getString(Context context,String key,String defvalue){
        if(sp==null){
            sp=context.getSharedPreferences( "config",Context.MODE_PRIVATE );
        }
        return sp.getString( key,defvalue );
    }
}
