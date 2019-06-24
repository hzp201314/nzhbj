package com.hzp.nzhbj.tool;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * created by hzp on 2019/6/24 14:36
 * 作者：codehan
 * 描述：网络获取图片的操作
 */
public class NetCacheBitmap {
    private MyCacheBitmapUtils mMyCacheBitmapUtils;
    private LocalCacheBitmap mLocalCacheBitmap;

    public NetCacheBitmap(MyCacheBitmapUtils myCacheBitmapUtils, LocalCacheBitmap localCacheBitmap) {
        this.mMyCacheBitmapUtils = myCacheBitmapUtils;
        this.mLocalCacheBitmap = localCacheBitmap;
    }

    /**
     *  根据图片的路径获取图片
     * @param imageView
     * @param url
     */
    public void getNetBitmap(ImageView imageView,String url){
        //params : 就是doInBackground子线程之中执行的操作所需的参数
    }

    private class MyAsyncTask extends AsyncTask<Object,Integer,Bitmap>{

        private ImageView imageView;
        private String url;

        //在子线程之前调用的方法
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        //在子线程之中调用的方法
        @Override
        protected Bitmap doInBackground(Object... params) {
            //获取条目对应的imageView
            imageView = (ImageView) params[0];
            //获取传递过来的图片的路径
            url = (String) params[1];
            //因为adapter的getview是执行非常快的，就会存在这样一个问题，当加载一个条目的时候，开启子线程获取图片，第一个图片还没有下载完成，紧接着就会加载第二个条目
            //又会开启子线程获取图片，这个时候会获取到两个图片，就不知道那个图片对应的是哪个条目的imageview
            //将imageView和url绑定在一起，这样就可以确定一个url对应一个具体的imageview
            imageView.setTag( url );
            //下载图片操作
            Bitmap bitmap = downLoad( url );
            return bitmap;
        }

        //在子线程之后调用的方法
        @Override
        protected void onPostExecute(Bitmap bitmap) {
            Log.d( "tag", "onPostExecute: 从网络获取操作" );
            //获取doInBackground返回的bitmap，展示到相应的imageView
            //获取到图片之后，因为是在子线程中获取的，所以需要判断图片的url和imageview是否是一对
            String mUrl = (String) imageView.getTag();
            if(mUrl.equals( url )){
                //展示图片
                if (bitmap!=null){
                    imageView.setImageBitmap( bitmap );
                    //展示完从网络获取的图片之后，还要将图片保存本地，缓存到内存中
                    mLocalCacheBitmap.saveLocalCacheBitmap( mUrl,bitmap );
                    mMyCacheBitmapUtils.setCacheBitmap( mUrl,bitmap );
                }
            }
            super.onPostExecute( bitmap );
        }

        //在子线程中更新加载进度方法
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate( values );
        }
    }

    /**
     * 根据图片的路径获取相应的图片
     * @param url
     * @return
     */
    private Bitmap downLoad(String url) {

        try {
            URL connectUrl = new URL( url );
            HttpURLConnection urlConnection = (HttpURLConnection) connectUrl.openConnection();
            urlConnection.setConnectTimeout( 3000 );//设置超时时间
            urlConnection.setReadTimeout( 3000 );//设置读取的超时时间
            urlConnection.connect();//连接操作
            int responseCode = urlConnection.getResponseCode();//服务器返回码
            if(responseCode==200){
                //链接成功
                InputStream inputStream = urlConnection.getInputStream();//获取返回的数据，流的形式
                Bitmap bitmap = BitmapFactory.decodeStream( inputStream );
                return bitmap;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}
