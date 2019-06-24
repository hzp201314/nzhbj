package com.hzp.nzhbj.base;

import android.app.Activity;
import android.view.View;

import com.hzp.nzhbj.activity.HomeActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

/**
 * created by hzp on 2019/6/18 10:23
 * 作者：codehan
 * 描述：新闻中心的菜单页页面的父类
 * 因为父类不知道子类加载什么样的界面，显示什么样的数据，所以创建抽象方法，
 * 子类根据自己的特性进行实现
 */
public abstract class BaseMenupager {
    public View rootView;
    public SlidingMenu slidingMenu;
    public Activity activity;

    public BaseMenupager(Activity activity) {
        this.activity=activity;
        slidingMenu = ((HomeActivity) activity).getSlidingMenu();
        rootView = initView();
    }

    /**
     * 加载布局
     * @return
     */
    public abstract View initView();

    /**
     * 显示数据
     */
    public abstract void initData();


}
