package com.hzp.nzhbj.pager.menu;

import android.app.Activity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.hzp.nzhbj.R;
import com.hzp.nzhbj.base.BaseMenupager;
import com.hzp.nzhbj.bean.NewsCenterInfo;
import com.hzp.nzhbj.bean.NewsCenterInfo.NewsChildInfo;
import com.hzp.nzhbj.pager.menu.item.MenuNewsCenterItemPager;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.viewpagerindicator.TabPageIndicator;

import java.util.ArrayList;

/**
 * created by hzp on 2019/6/18 10:29
 * 作者：codehan
 * 描述：新闻中心的菜单页新闻界面
 * 因为每个界面都要加载界面，显示数据，所以相同操作抽取到父类
 */
public class MenuNewsCenterPager extends BaseMenupager{

    //因为标签栏显示的数据，实在新闻中心中获取的数据的包含的，所以需要将数据从新闻中心界面中传递过来
    private NewsChildInfo mNewsChildInfo;
    private TabPageIndicator mIndicator;
    private ViewPager mViewPager;
    private ImageButton mNext;
    private ArrayList<MenuNewsCenterItemPager> itemPagers;
    private Myadapter myadapter;

    public MenuNewsCenterPager(Activity activity, NewsChildInfo newsChildInfo) {
        super( activity );
        this.mNewsChildInfo = newsChildInfo;
    }

    @Override
    public View initView() {
        	/*TextView textView = new TextView(activity);
		textView.setText("菜单详情页-新闻");
		textView.setTextSize(22);
		textView.setTextColor(Color.RED);
		textView.setGravity(Gravity.CENTER);*/
        rootView = View.inflate( activity, R.layout.menunewscenterpager, null );
        return rootView;
    }

    @Override
    public void initData() {
        mIndicator = (TabPageIndicator) rootView.findViewById(R.id.menunewscenter_tpi_indicator);
        mViewPager = (ViewPager) rootView.findViewById(R.id.menunewscenter_vp_pager);
        mNext = (ImageButton) rootView.findViewById(R.id.menunewscenter_ibtn_next);

        itemPagers = new ArrayList<>();
        itemPagers.clear();
        //显示数据
        //因为标签文字是在pageradapter的getpagerTitle中设置的，所以需要先给viewpager设置显示界面，才能设置标签文本
        //因为viewpager的界面中包含了一个viewPager和listview，并且viewpager还和listview关联实现的listview下刷新操作，
        // 所以viewPager和listview是不能直接在当前的viewpager中显示的，所以只能放到另外的一个界面中
        //有几个标签就创建几个界面
        for (int i = 0; i < mNewsChildInfo.children.size(); i++) {
            itemPagers.add( new MenuNewsCenterItemPager( activity,mNewsChildInfo.children.get( i ).url ) );
        }
        //通过viewpager展示界面
        if(myadapter==null) {
            myadapter = new Myadapter();
            mViewPager.setAdapter( myadapter );
        }else {
            myadapter.notifyDataSetChanged();
        }
        //将viewpager和标签关联起来
        mIndicator.setViewPager( mViewPager );

        mNext.setOnClickListener( new OnClickListener() {
            @Override
            public void onClick(View v) {
                //点击按钮实现切换界面操作
                //获取当前显示界面的位置
                int currentItem = mViewPager.getCurrentItem();
                currentItem++;
                mViewPager.setCurrentItem( currentItem );//设置当前显示的界面
            }
        } );

        //设置当前界面切换到北京的时候，可以进行侧拉菜单的侧拉操作，如果不是北京界面，侧拉菜单不能进行侧拉操作，只能进行切换界面操作
        //监听viewpager的界面切换
        //2.因为标签和viewpager关联，点标签viewpager也会跟着切换，说明viewpager是跟着标签进行切换的
        mIndicator.setOnPageChangeListener( new OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //判断是否切换到北京界面
                if(position==0){
                    //侧拉菜单可以侧拉
                    slidingMenu.setTouchModeAbove( SlidingMenu.TOUCHMODE_FULLSCREEN );
                }else {
                    //侧拉菜单不能侧拉
                    slidingMenu.setTouchModeAbove( SlidingMenu.TOUCHMODE_NONE );
                }
                //1.监听viewpager的界面切换的话，如果viewpager切换了，也要设置标签切换设置标签滑动
                //mIndicator.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        } );


    }

    /**
     * ViewPager的adapter
     */
    private class Myadapter extends PagerAdapter{

        //设置显示标签的文本
        @Override
        public CharSequence getPageTitle(int position) {
            return mNewsChildInfo.children.get( position ).title;
        }

        @Override
        public int getCount() {
            return itemPagers.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view==object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            MenuNewsCenterItemPager centerItemPager = itemPagers.get( position );
            View view = centerItemPager.rootView;
            container.addView( view );
            //显示界面之后，还要展示数据
            centerItemPager.initData();
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
//            super.destroyItem( container, position, object );
            container.removeView( (View) object );
        }
    }
}
