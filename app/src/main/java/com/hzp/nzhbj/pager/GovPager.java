package com.hzp.nzhbj.pager;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.hzp.nzhbj.base.BasePager;

/**
 * created by hzp on 2019/6/17 22:23
 * 作者：codehan
 * 描述： 首页的界面
 * 1.因为，新闻中心，智慧服务等界面也要使用普通的java类进行操作，所以每个java中都会有加载界面显示数据的操作，相同操作，抽取到父类
 */
public class GovPager extends BasePager{

    public GovPager(Activity activity) {
        super( activity );
    }

    //但是普通的java类不能返回界面的view对象，所以在java类中创建一个initView方法，在initView方法中可以通过View.inflate形式将布局文件转化成view对象，返回


    @Override
    public void initData() {
        TextView textView = new TextView( activity );
        textView.setText( "政务" );
        textView.setTextColor( Color.RED );
        textView.setTextSize( 20 );
        textView.setGravity( Gravity.CENTER );

        //将子类的显示内容，填充到父类显示区域显示
        mContent.addView( textView );

        //设置子类的标题
        mTitle.setText( "鼓励二胎" );

        //隐藏menu菜单
        mMenu.setVisibility( View.VISIBLE );

        super.initData();
    }
}
