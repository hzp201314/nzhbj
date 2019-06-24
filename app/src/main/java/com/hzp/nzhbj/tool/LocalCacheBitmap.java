package com.hzp.nzhbj.tool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * created by hzp on 2019/6/24 14:36
 * 作者：codehan
 * 描述：本地缓存图片
 */
public class LocalCacheBitmap {
    private static  final String PATH=Environment.getExternalStorageDirectory().getAbsolutePath()+"/zhbj_cache";

    public void saveLocalCacheBitmap(String url, Bitmap bitmap){
        Log.d( "tag", "saveLocalCacheBitmap: 缓存到本地中" );
        File dir = new File( PATH );
        //判断文件夹是否存在
        //isDirectory : 判断是否是文件夹
        if(!dir.exists()||!dir.isDirectory()){
            dir.mkdirs();
        }

        try {
            //创建图片对应的文件
            File file = new File( dir, MD5Util.Md5( url ).substring( 0, 10 ) );
            FileOutputStream stream = new FileOutputStream( file );
            //compress : 将bitmap以什么样的类型，什么样的质量，保存到哪个文件中
            //format : 图片的类型
            //quality : 图片的质量
            //stream : 输出流，表示写入到哪个文件中
            bitmap.compress( Bitmap.CompressFormat.JPEG,100,stream );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取本地图片的操作
     * @param url
     * @return
     */
    public Bitmap getLocalCacheBitmap(String url){
        Log.d( "tag", "getLocalCacheBitmap: 从本地获取" );
        try {
            //从本地文件中获取图片
            //因为如果保存图片失败，文件是没有的，所以也会报出错误
            File file = new File( PATH, MD5Util.Md5( url ).substring( 0, 10 ) );
            //decodeFile : 将文件转化成bitmap
            Bitmap bitmap = BitmapFactory.decodeFile( file.getAbsolutePath() );
            return bitmap;
        }catch (Exception e){

        }
        return null;

    }
}
