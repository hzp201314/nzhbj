package com.hzp.nzhbj.tool;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;

/**
 * created by hzp on 2019/6/24 14:36
 * 作者：codehan
 * 描述：内存缓存图片的操作
 */
public class MyCacheBitmapUtils {
    private LruCache<String,Bitmap> lruCache;

    //SoftReference<Bitmap> : 将bitmap使用SoftReference修饰
    //private HashMap<String, SoftReference<Bitmap>> hashMap;

    //1.软件引用（java）
    //强引用：new Name();系统不会及时回收引用对象
    //软引用：SoftReference<Bitmap>;当系统内存不足的时候，考虑回收的引用对象，通常和Hasmap结合起来去用
    //弱引用：WeakReference<Bitmap>;当系统内存不足的时候，考虑回收的引用对象,如果弱引用和软引用同时存在，优先回收弱引用
    //虚引用：PhantomReference<Bitmap>;当系统内存不足的时候，考虑回收的引用对象,优先级低于若引用
    //在android 2.3+，不管是软引用，弱引用，虚引用，系统都会及时回收，造成缓存图片失败
    //2.LruCache(android)
    //google建议缓存操作，根据图片使用的时间/使用的次数，判断图片是否应用回收


    public MyCacheBitmapUtils() {
        //hashMap = new HashMap<String, SoftReference<Bitmap>>();
        //maxSize : 缓存的大小,一般是内存的8分之一
        int maxSize = (int) (Runtime.getRuntime().maxMemory() / 8);
        lruCache=new LruCache<String,Bitmap>( maxSize ){
            //获取缓存的图片的大小
            //key:缓存图片的名称
            //value : 缓存的图片
            @Override
            protected int sizeOf(String key, Bitmap value) {
                //缓存大小是需要我们自己计算
                //getRowBytes() :获取图片一行的字节数
                int size = value.getRowBytes() * value.getHeight();
                return size;
            }
        };
    }

    /**
     * 缓存图片
     * @param url
     * @param bitmap
     */
    public void setCacheBitmap(String url,Bitmap bitmap){
        Log.d( "tag", "setCacheBitmap: 缓存到内存中" );
        //将bitmap使用软引用修饰
		/*SoftReference<Bitmap> softReference = new SoftReference<Bitmap>(bitmap);
		hashMap.put(url, softReference);*/
		lruCache.put( url,bitmap );
    }

    /**
     * 获取图片的操作
     * @param url
     * @return
     */
    public Bitmap getCacheBitmap(String url){
        Log.d( "tag", "getCacheBitmap: 从内存中获取" );
        //先获取软引用，在从软引用中获取的bitmap
		/*SoftReference<Bitmap> softReference = hashMap.get(url);
		if (softReference != null) {
			Bitmap bitmap = softReference.get();
			return bitmap;
		}*/
        Bitmap bitmap = lruCache.get( url );
        return bitmap;
    }
}
