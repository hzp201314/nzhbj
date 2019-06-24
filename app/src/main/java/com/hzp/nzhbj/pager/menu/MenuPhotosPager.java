package com.hzp.nzhbj.pager.menu;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.hzp.nzhbj.base.BaseMenupager;

/**
 * created by hzp on 2019/6/18 16:06
 * 作者：codehan
 * 描述：新闻中心的菜单页组图界面
 * 因为每个界面都要加载界面，显示数据，所以相同操作抽取到父类
 */
public class MenuPhotosPager extends BaseMenupager {

    public MenuPhotosPager(Activity activity) {
        super( activity );
    }

    @Override
    public View initView() {
        TextView textView = new TextView(activity);
        textView.setText("菜单详情页-组图");
        textView.setTextSize(22);
        textView.setTextColor( Color.RED);
        textView.setGravity( Gravity.CENTER);
        return textView;
    }

    @Override
    public void initData() {

    }
}
