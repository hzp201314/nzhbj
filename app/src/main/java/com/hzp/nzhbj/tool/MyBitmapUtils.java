package com.hzp.nzhbj.tool;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.hzp.nzhbj.R;

/**
 * created by hzp on 2019/6/24 14:35
 * 作者：codehan
 * 描述：加载图片的工具类
 */
public class MyBitmapUtils {
    private MyCacheBitmapUtils myCacheBitmapUtils;
    private LocalCacheBitmap localCacheBitmap;
    private NetCacheBitmap netCacheBitmap;

    public MyBitmapUtils(){
        myCacheBitmapUtils=new MyCacheBitmapUtils();
        localCacheBitmap=new LocalCacheBitmap();
        netCacheBitmap=new NetCacheBitmap( myCacheBitmapUtils,localCacheBitmap );
    }

    /**
     * 加载显示图片
     * @param imageView
     * @param url
     */
    public void display(ImageView imageView,String url){
        //1.设置一个默认的图片
        imageView.setImageResource( R.drawable.pic_item_list_default );
        //2.从内存获取图片
        Bitmap bitmap = myCacheBitmapUtils.getCacheBitmap( url );
        if(bitmap!=null){
            imageView.setImageBitmap( bitmap );
            return;
        }
        //3.内存中没有，从本地文件获取，重新缓存到内存中
        bitmap=localCacheBitmap.getLocalCacheBitmap( url );
        if(bitmap!=null){
            imageView.setImageBitmap( bitmap );
            //如果本地获取到图片，重新缓存到内存中，方便下一次显示图片，从内存中获取
            myCacheBitmapUtils.setCacheBitmap( url,bitmap );
            return;
        }
        //4.本地文件没有，重新从网络下载，重新缓存到本地和内存中
        netCacheBitmap.getNetBitmap( imageView,url );
    }
}
