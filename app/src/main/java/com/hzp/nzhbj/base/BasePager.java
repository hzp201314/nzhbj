package com.hzp.nzhbj.base;

import android.app.Activity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hzp.nzhbj.R;
import com.hzp.nzhbj.activity.HomeActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * created by hzp on 2019/6/17 22:09
 * 作者：codehan
 * 描述：首页，新闻中心，智慧服务等界面的父类
 */
public class BasePager {
    public SlidingMenu slidingMenu;
    public Activity activity;
    public View view;
    public FrameLayout mContent;
    public TextView mTitle;
    public ImageButton mMenu;
    public ImageButton mImage;

    public BasePager(Activity activity) {
        this.activity=activity;
        //获取侧拉菜单
        slidingMenu = ((HomeActivity) activity).getSlidingMenu();
        //在new出来界面的时候就加载界面
        view=initView();
    }

    /**
     *  加载布局
     * @return
     */
    public View initView() {
        view = View.inflate( activity, R.layout.basepager, null );
        //初始化控件，方便填充显示子类的数据
        mTitle = (TextView) view.findViewById(R.id.titlebar_tv_title);
        mContent = (FrameLayout)view.findViewById( R.id.basepager_fl_content );
        mMenu = (ImageButton) view.findViewById(R.id.titlebar_btn_menu);
        mImage = (ImageButton)view.findViewById( R.id.titlebar_btn_image );

        mMenu.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
                slidingMenu.toggle();
            }
        } );
        return view;
    }

    /**
     * 显示数据
     */
    public void initData(){

    }
}
