package com.hzp.nzhbj.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hzp.nzhbj.activity.HomeActivity;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;


/**
 * created by hzp on 2019/6/17 19:40
 * 作者：codehan
 * 描述：
 */
public abstract class BaseFragment extends Fragment {

    public Activity activity;
    public SlidingMenu slidingMenu;

    // 初始化数据
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //getActivity : 获取管理fragment的activity
        activity = getActivity();
        slidingMenu = ((HomeActivity) activity).getSlidingMenu();
        super.onCreate( savedInstanceState );
    }

    // 加载fragment布局的方法
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //因为fragment加载布局的操作是调用onCreateView方法实现的，不是通过initView方法实现的
        View view = initView();
        return view;
    }

    // 加载显示数据的方法，类似activity的oncrate方法
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        initData();
        super.onActivityCreated( savedInstanceState );
    }

    /**
     *  加载布局的方法，由子类进行实现
     *  因为父类不知道子类要加载什么布局，显示什么数据，所以，创建抽象方法，子类实现抽象方法，根据自己的特性进行相应的实现
     */
    public abstract View initView();

    /**
     * 显示数据的方法，由子类进行实现
     */
    protected abstract void initData();
}
