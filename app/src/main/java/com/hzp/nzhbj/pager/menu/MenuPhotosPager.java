package com.hzp.nzhbj.pager.menu;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.hzp.nzhbj.R;
import com.hzp.nzhbj.base.BaseMenupager;
import com.hzp.nzhbj.bean.PhotosBean;
import com.hzp.nzhbj.bean.PhotosBean.PhotosItem;
import com.hzp.nzhbj.fragment.HomeFragment;
import com.hzp.nzhbj.net.NetUrl;
import com.hzp.nzhbj.tool.Constants;
import com.hzp.nzhbj.tool.MyBitmapUtils;
import com.hzp.nzhbj.tool.SharedPreferencesTool;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

/**
 * created by hzp on 2019/6/18 16:06
 * 作者：codehan
 * 描述：新闻中心的菜单页组图界面
 * 因为每个界面都要加载界面，显示数据，所以相同操作抽取到父类
 */
public class MenuPhotosPager extends BaseMenupager implements View.OnClickListener {

    private View rootView;

    @ViewInject( R.id.photos_lv_listview )
    private ListView mListView;

    @ViewInject( R.id.photos_gv_gridview )
    private GridView mGridView;

    private List<PhotosItem> news;

    private ImageButton mMenu;

    public MenuPhotosPager(Activity activity,ImageButton menu) {
        super( activity );
        this.mMenu=menu;
        mMenu.setOnClickListener( this);

    }

    @Override
    public View initView() {
        /*
        TextView textView = new TextView(activity);
        textView.setText("菜单详情页-组图");
        textView.setTextSize(22);
        textView.setTextColor( Color.RED);
        textView.setGravity( Gravity.CENTER);
        return textView;*/
        rootView = View.inflate( activity, R.layout.menuphotospager, null );

        ViewUtils.inject( this,rootView );
        return rootView;

    }

    @Override
    public void initData() {
        String sp_msg = SharedPreferencesTool.getString( activity, Constants.PHOTOMSG, "" );
        if(!TextUtils.isEmpty(sp_msg)){
            processJson(sp_msg);
        }
        getData();

    }

    /**
     * 请求服务器数据
     */
    private void getData() {
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send( HttpMethod.GET, NetUrl.PHOTOURL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                Log.d( "tag", "onSuccess: "+result );
                SharedPreferencesTool.saveString( activity,Constants.PHOTOMSG,result );
                processJson( result );
            }

            @Override
            public void onFailure(HttpException e, String s) {

            }
        } );
    }

    /**
     * 解析json串
     * @param json
     */
    private void processJson(String json) {
        Gson gson = new Gson();
        PhotosBean photosBean = gson.fromJson( json, PhotosBean.class );
        news = photosBean.data.news;

        mListView.setAdapter( new Myadapter() );
    }

    private class Myadapter extends BaseAdapter{
        private  MyBitmapUtils myBitmapUtils;
        public Myadapter() {
            myBitmapUtils=new MyBitmapUtils();
        }

        @Override
        public int getCount() {
            return news.size();
        }

        @Override
        public Object getItem(int position) {
            return news.get( position );
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ViewHolder viewHolder;
            if(convertView==null){
                view=View.inflate( activity,R.layout.photos_item,null );
                viewHolder=new ViewHolder();
                viewHolder.mIcon = (ImageView) view.findViewById(R.id.item_iv_icon);
                viewHolder.mTitle = (TextView) view.findViewById(R.id.item_tv_text);
                view.setTag( viewHolder );
            }else {
                view=convertView;
                viewHolder= (ViewHolder) view.getTag();
            }
            //显示数据
            PhotosItem photosItem = news.get( position );
            viewHolder.mTitle.setText( photosItem.title );

            /*BitmapUtils bitmapUtils = new BitmapUtils(activity);
			bitmapUtils.display(viewHolder.mIcon, photosItem.listimage);*/
            myBitmapUtils.display( viewHolder.mIcon,photosItem.listimage );


            return view;
        }
    }
    static class ViewHolder{
        ImageView mIcon;
        TextView mTitle;
    }

    /*标示listview隐藏显示*/
    private boolean isShowListView=true;

    @Override
    public void onClick(View v) {
        //隐藏显示listview和gridview
        //如果listview显示，点击隐藏listview显示gridview，并且更换按钮的图片
        //如果gridview显示，点击隐藏gridview显示listview，并且更换按钮的图片
        //问题：需要知道你身体view是隐藏还是显示
        if(isShowListView){
            mListView.setVisibility( View.GONE );
            mGridView.setVisibility( View.VISIBLE );
            mMenu.setBackgroundResource( R.drawable.icon_pic_grid_type );
            isShowListView=false;
        }else {
            mGridView.setVisibility( View.GONE );
            mListView.setVisibility( View.VISIBLE );
            mMenu.setBackgroundResource( R.drawable.icon_pic_list_type );
            isShowListView=true;
        }
    }
}
