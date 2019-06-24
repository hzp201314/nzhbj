package com.hzp.nzhbj.fragment;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.hzp.nzhbj.R;
import com.hzp.nzhbj.base.BaseFragment;
import com.hzp.nzhbj.base.BasePager;
import com.hzp.nzhbj.pager.GovPager;
import com.hzp.nzhbj.pager.HomePager;
import com.hzp.nzhbj.pager.NewsCenterPager;
import com.hzp.nzhbj.pager.SettingPager;
import com.hzp.nzhbj.pager.SmartServicePager;
import com.hzp.nzhbj.ui.NoScrollViewPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;

/**
 * created by hzp on 2019/6/17 19:39
 * 作者：codehan
 * 描述：首页的fragment
 *      因为HomeFragment和MenuFragment都需要加载布局，显示数据，相同操作，抽取到父类
 *      包含：首页、新闻中心、智慧服务、政务、设置五个界面
 */
public class HomeFragment extends BaseFragment {

    private View view;
    private NoScrollViewPager mViewPager;
    private RadioGroup mButtons;
    private ArrayList<BasePager> list;
    private Myadapter myadapter;

    @Override
    public View initView() {
        /*TextView textView = new TextView( activity );
        textView.setText( "我是首页的fragment" );
        return textView;*/
        view = View.inflate( activity, R.layout.homefragment, null );
        return view;
    }

    @Override
    protected void initData() {
        // 将首页，新闻中心等界面，添加到viewpager中的进行展示操作
        mViewPager = (NoScrollViewPager)view.findViewById( R.id.homefragment_vp_viewpager );
        mButtons = (RadioGroup)view.findViewById( R.id.homefragment_rg_buttons );

        // 1.创建首页，新闻中心界面，保存到list集合中，方便viewpager的adapter使用
        list = new ArrayList<>();
        list.clear();// 清除：保存每次保存的都是最新的界面
        list.add( new HomePager(activity) );
        list.add( new NewsCenterPager(activity) );
        list.add( new SmartServicePager(activity) );
        list.add( new GovPager(activity) );
        list.add( new SettingPager(activity) );
        // 2.设置viewpager的adapter显示数据
        if(myadapter==null) {
            myadapter = new Myadapter();
            mViewPager.setAdapter( myadapter );
        }else {
            myadapter.notifyDataSetChanged();
        }
        // 3.监听viewpager界面切换监听，当切换到相应的界面的时候，加载相应界面的数据,刚进入界面的时候，
        //  没有进行界面的切换，是不会调用界面切换监听的，需要手动设置第一个界面的加载数据
        mViewPager.setOnPageChangeListener( new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                // 切换到哪个界面，加载哪个界面的数据
                // 获取切换到的界面
                BasePager pager = list.get( position );
                pager.initData();

                //6.判断当切换到首页和设置页面的时候，不能进行侧拉菜单侧拉，当切换到其他三个界面的时候可以进行侧拉菜单侧拉
                if(position==0||position==list.size()-1){
                    //不能进行侧拉菜单侧拉\
                    slidingMenu.setTouchModeAbove( SlidingMenu.TOUCHMODE_NONE );
                }else {
                    //可以进行侧拉菜单侧拉
                    slidingMenu.setTouchModeAbove( SlidingMenu.TOUCHMODE_FULLSCREEN );
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        } );

        // 4.加载第一个界面的数据
        list.get( 0 ).initData();

        // 5.点击相应的RadioButton实现界面切换
        //设置默认选中首页的Radiobutton
        mButtons.check( R.id.homefragment_rbtn_shou );//设置选中哪个Radiobutton
        // 监听Radiogroup中的Radiobutton选中操作
        mButtons.setOnCheckedChangeListener( new OnCheckedChangeListener() {
            // 当Radiobutton选中的时候调用的方法
            // checkedId : 选中的Radiobutton的id
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.homefragment_rbtn_shou://首页
                        //设置当前显示的界面是首页的界面
                        //mViewPager.setCurrentItem(0);//设置当前显示的界面，item：界面的索引
                        mViewPager.setCurrentItem( 0,false );//设置当前显示的界面，同时设置切换界面的时候是否执行界面切换的动画效果，true:执行，false:不执行
                        break;
                    case R.id.homefragment_rbtn_newscenter://新闻中心
                        mViewPager.setCurrentItem( 1,false );//设置当前显示的界面，同时设置切换界面的时候是否执行界面切换的动画效果，true:执行，false:不执行
                        break;
                    case R.id.homefragment_rbtn_smartservice://智慧服务
                        mViewPager.setCurrentItem( 2,false );//设置当前显示的界面，同时设置切换界面的时候是否执行界面切换的动画效果，true:执行，false:不执行
                        break;
                    case R.id.homefragment_rbtn_gov://政务
                        mViewPager.setCurrentItem( 3,false );//设置当前显示的界面，同时设置切换界面的时候是否执行界面切换的动画效果，true:执行，false:不执行
                        break;
                    case R.id.homefragment_rbtn_setting://设置
                        mViewPager.setCurrentItem( 4,false );//设置当前显示的界面，同时设置切换界面的时候是否执行界面切换的动画效果，true:执行，false:不执行
                        break;
                
                    default:
                        break;
                }
            }
        } );

    }

    /**
     * ViewPager的adapter
     */
    public class Myadapter extends PagerAdapter{

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            BasePager pager = list.get( position );
            // viewpager只能存放view对象
            View rootView = pager.view;
            container.addView( rootView );
            return rootView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem( container, position, object );
            container.removeView( (View) object );
        }

    }

    /**
     * 获取NewsCenterPager界面对象的
     * @return
     */
    public NewsCenterPager getNewsCenterPager(){
        return (NewsCenterPager)list.get( 1 );
    }
}
