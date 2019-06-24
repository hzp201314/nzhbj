package com.hzp.nzhbj.activity;

import android.app.Activity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.hzp.nzhbj.R;
import com.hzp.nzhbj.fragment.HomeFragment;
import com.hzp.nzhbj.fragment.MenuFragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;

/**
 *
 */
//1.将activity改为继承SlidingFragmentActivity
public class HomeActivity extends SlidingFragmentActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.activity_home );
        initSlidingMenu();
        initFragment();
    }



    /**
     * 2.添加侧拉菜单
     */
    private void initSlidingMenu() {
        //2.1.获取Slidingmenu
        SlidingMenu slidingMenu = getSlidingMenu();
        //2.2.设置侧拉菜单的显示模式
        slidingMenu.setMode( SlidingMenu.LEFT );
        //2.3.设置触摸模式
        //TOUCHMODE_FULLSCREEN : 全屏触摸
        //TOUCHMODE_MARGIN : 边框触摸
        //TOUCHMODE_NONE : 不可触摸
        slidingMenu.setTouchModeAbove( SlidingMenu.TOUCHMODE_NONE );
        //2.4.指定侧拉菜单的布局，id:布局文件的id
        setBehindContentView( R.layout.menu );
        //2.5.设置侧拉菜单宽度,单位px
        slidingMenu.setBehindWidth(300);
        //设置侧拉菜单内容页（主页面 ）的宽度
//        slidingMenu.setBehindOffset( 220 );
        //2.6.设置分割线的样式
        slidingMenu.setShadowDrawable( R.drawable.shape_slidingmenu_divirer );
        slidingMenu.setShadowWidth( 5 );
        //2.7.设置第二个菜单的布局，如果显示模式是左边的单个显示，布局会重复设置到左边的侧拉菜单中
        //slidingMenu.setSecondaryMenu(R.layout.menu_right);
        //2.8.设置第二个侧拉菜单的分割线,可以共享分割线的宽度
        //slidingMenu.setSecondaryShadowDrawable(R.drawable.shape_slidingmenu_divirer);


    }

    /**
     * 添加fragment到activity
     */
    private void initFragment() {
        //1.获取fragment的管理者
        FragmentManager fragmentManager = getSupportFragmentManager();
        //2.开启添加的事务
        FragmentTransaction beginTransaction = fragmentManager.beginTransaction();
        //3.添加fragment
        //containerViewId : 添加fragment容器的id，布局文件中framlayout的id
        //fragment : 添加fragment
        //tag : 添加的fragment的标示,方便获取根据标示获取添加的fragment
        beginTransaction.replace( R.id.home_root,new HomeFragment(),"HOME" );
        beginTransaction.replace( R.id.menu_root,new MenuFragment(),"MENU" );
        //4.提交事务，操作生效
        beginTransaction.commit();

        //fragmentManager.findFragmentByTag(tag);//根据标示获取到已经添加过的fragment
    }

    /**
     * 获取添加的MenuFragment的对象
     * @return
     */
    public MenuFragment getMenuFragment(){
        MenuFragment fragment= (MenuFragment) getSupportFragmentManager().findFragmentByTag( "MENU" );
        return fragment;
    }

    /**
     * 获取添加的HomeFragment的对象
     * @return
     */
    public HomeFragment getHomeFragment(){
        HomeFragment fragment= (HomeFragment) getSupportFragmentManager().findFragmentByTag( "HOME" );
        return fragment;
    }


}
