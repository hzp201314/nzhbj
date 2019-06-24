package com.hzp.nzhbj.pager;

import android.app.Activity;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hzp.nzhbj.activity.HomeActivity;
import com.hzp.nzhbj.base.BaseMenupager;
import com.hzp.nzhbj.base.BasePager;
import com.hzp.nzhbj.bean.NewsCenterInfo;
import com.hzp.nzhbj.net.NetUrl;
import com.hzp.nzhbj.pager.menu.MenuActionPager;
import com.hzp.nzhbj.pager.menu.MenuNewsCenterPager;
import com.hzp.nzhbj.pager.menu.MenuPhotosPager;
import com.hzp.nzhbj.pager.menu.MenuSpecialPager;
import com.hzp.nzhbj.tool.Constants;
import com.hzp.nzhbj.tool.SharedPreferencesTool;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;

import java.util.ArrayList;

/**
 * created by hzp on 2019/6/17 22:23
 * 作者：codehan
 * 描述： 新闻界面
 * 包含：四个menu菜单：新闻、专题、组图、互动
 *          新闻主页面：指示器+viewpager+listview
 *          专题主页面：
 *          组图主页面：
 *          互动主页面：
 * 1.因为，新闻中心，智慧服务等界面也要使用普通的java类进行操作，所以每个java中都会有加载界面显示数据的操作，相同操作，抽取到父类
 */
public class NewsCenterPager extends BasePager{

    private static final String TAG = "NewsCenterPager";
    private ArrayList<String> titles;
    private ArrayList<BaseMenupager> list;

    public NewsCenterPager(Activity activity) {
        super( activity );
    }

    //但是普通的java类不能返回界面的view对象，所以在java类中创建一个initView方法，在initView方法中可以通过View.inflate形式将布局文件转化成view对象，返回


    @Override
    public void initData() {
/*        TextView textView = new TextView( activity );
        textView.setText( "新闻中心" );
        textView.setTextColor( Color.RED );
        textView.setTextSize( 20 );
        textView.setGravity( Gravity.CENTER );

        //将子类的显示内容，填充到父类显示区域显示
        mContent.addView( textView );*/

        //设置子类的标题
        mTitle.setText( "新闻" );

        //隐藏menu菜单
        mMenu.setVisibility( View.VISIBLE );

        //请求网络，获取服务器回传的数据
        //2.再次请求服务的时候，查看是否缓存数据，有缓存数据，加载显示缓存数据，没有缓存数据，直接请求网络获取数据
        String sp_msg = SharedPreferencesTool.getString( activity, Constants.NEWSCENTERMSG, "" );
        if(!TextUtils.isEmpty( sp_msg )){
            //加载显示缓存数据
            processJSON(sp_msg);
        }
        //请求服务器获取数据
        getData();

        super.initData();
    }

    /**
     * 请求服务器获取数据
     */
    private void getData() {
        //1.联网权限      2.使用xutils
        HttpUtils httpUtils = new HttpUtils();
        httpUtils.send( HttpMethod.GET, NetUrl.NEWSCENTERURL, new RequestCallBack<String>() {
            @Override
            public void onSuccess(ResponseInfo<String> responseInfo) {
                String result = responseInfo.result;
                Log.d( TAG, "onSuccess: "+result );

                //1.请求服务器成功，将服务器返回的数据，保存到本地
                SharedPreferencesTool.saveString( activity,Constants.NEWSCENTERMSG,result );
                //3.加载解析获取最新数据，将原来的缓存数据覆盖
                processJSON(result);

            }

            @Override
            public void onFailure(HttpException e, String s) {
                Toast.makeText( activity,"获取数据失败",Toast.LENGTH_SHORT ).show();
            }
        } );
    }

    /**
     * 解析json数据
     * @param json
     */
    private void processJSON(String json) {
        Gson gson = new Gson();
        NewsCenterInfo newsCenterInfo = gson.fromJson( json, NewsCenterInfo.class );
        Log.d( TAG, "processJSON: "+newsCenterInfo.data.get( 0 ).title );

        //展示数据
        //获取menufragment所需的数据，传递给menufragment进行显示操作
        setMenuFragmentMsg(newsCenterInfo);
    }

    /**
     * 获取menufragment所需的数据，传递给menufragment进行显示
     * @param newsCenterInfo
     */
    private void setMenuFragmentMsg(NewsCenterInfo newsCenterInfo) {
        //获取menuFragment所需的数据
        titles = new ArrayList<String>();
        titles.clear();
        for (int i = 0; i < newsCenterInfo.data.size(); i++) {
            titles.add( newsCenterInfo.data.get( i ).title );
        }
        //将数据传递给MenuFragment
        ((HomeActivity)activity).getMenuFragment().initList(titles);

        //创建菜单页对应的界面，将菜单页条目对应的界面添加到集合中，方便切换操作
        list = new ArrayList<BaseMenupager>();
        list.clear();
        list.add( new MenuNewsCenterPager( activity,newsCenterInfo.data.get( 0 ) ) );
        list.add(new MenuSpecialPager(activity));
        list.add(new MenuPhotosPager(activity));
        list.add(new MenuActionPager(activity));

        //设置默认显示的界面为新闻界面
        switchPage( 0 );
    }

    /**
     * 新闻中心切换新闻，专题，组图，互动界面的操作
     * @param position 切换界面的索引(点击MenuFragment的条目的索引)
     */
    public void switchPage(int position){
        //1.切换标题
        mTitle.setText( titles.get( position ) );
        //2.切换内容显示界面
        //根据条目的索引获取对应的界面
        BaseMenupager baseMenupager = list.get( position );
        View rootView = baseMenupager.rootView;//获取界面的view对象
        //先将之前保存的界面清除，在添加新的界面
        mContent.removeAllViews();//清除控件中所有的内容
        //添加新的界面在显示内容中显示
        mContent.addView( rootView );
        //界面显示出来之后，还有给界面填充显示的数据
        baseMenupager.initData();

        //如果切换到组图界面，显示组图的按钮,其他界面隐藏
        //instanceof : 判断获取的界面是否是MenuPhotosPager组图界面
        if(baseMenupager instanceof  MenuPhotosPager){
            mImage.setVisibility( View.VISIBLE );
        }else {
            mImage.setVisibility( View.GONE );
        }

    }
}
