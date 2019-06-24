package com.hzp.nzhbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hzp.nzhbj.R;
import com.hzp.nzhbj.tool.Constants;
import com.hzp.nzhbj.tool.SharedPreferencesTool;

import java.util.ArrayList;

public class GuideActivity extends Activity implements View.OnClickListener {
    private static final String TAG = GuideActivity.class.getSimpleName();
    private int[] mImageIds = new int[]{R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};
    private ViewPager mViewpager;
    private Button mStart;
    private LinearLayout mDots;
    private ImageView mReddot;
    private ArrayList<ImageView> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE);//去除标题栏
        setContentView( R.layout.activity_guide );

        initView();
    }

    private void initView() {
        mViewpager = (ViewPager) findViewById(R.id.guide_vp_viewpager);
        mStart = (Button) findViewById(R.id.guide_btn_start);
        mDots = (LinearLayout) findViewById(R.id.guide_ll_dots);
        mReddot = (ImageView) findViewById(R.id.guide_iv_reddot);
        //1.创建ImageView
        creatImageViewAndDot();

        //2.设置ViewPager的adapter，显示ImageView
        mViewpager.setAdapter( new Myadapter() );

        //3.监听ViewPager的界面切换监听，当界面切换到第三个界面的时候显示开始体验按钮，切换到其他界面不显示
        mViewpager.setOnPageChangeListener( onPageChangeListener );

        //设置按钮的点击事件
        mStart.setOnClickListener(this);

    }

    /**
     * 创建图片和点的操作
     */
    private void creatImageViewAndDot() {
        list = new ArrayList<>();
        list.clear();//严谨性的操作，保证每次集合中都没有以前的数据

        //1.将图片存放到相应的imageView中，将imageView设置给viewpager展示
        for (int i = 0; i < mImageIds.length; i++) {
            //根据图片的张数，创建相应个数的imageview
            createImageView(i);
            //根据图片的张数，创建相应个数的点
            createDot();
        }

    }

    /**
     * 创建点的操作
     */
    private void createDot() {
        ImageView point = new ImageView( this );
        point.setBackgroundResource( R.drawable.shape_guide_dot_bg );
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams( LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT );
        params.rightMargin=10;
        point.setLayoutParams( params );
        mDots.addView( point );
    }

    /**
     * 创建ImageView
     * @param i
     */
    private void createImageView(int i) {
        ImageView imageView = new ImageView( this );
        imageView.setBackgroundResource( mImageIds[i] );
        list.add( imageView );
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.guide_btn_start:
                //保存不是第一次进入的状态，方便splash界面切换的时候进行判断
                SharedPreferencesTool.saveBoolean( this, Constants.ISFIRSTENTER,false );
                //跳转到首页
                Intent intent = new Intent( this, HomeActivity.class );
                startActivity( intent );
                //移除界面，方便在首页点击返回键的时候不会回到引导界面，直接是退出应用程序
                finish();
                break;

            default:
                break;
        }
    }

    /**
     * viewpager的adapter
     */
    private class Myadapter extends PagerAdapter{
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view==object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            //根据条目的索引获取要显示添加的imageView
            ImageView imageView = list.get( position );
            //添加给viewpager的显示
            container.addView( imageView );
            //添加什么view对象，返回什么view对象，方便显示判断
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
//            super.destroyItem( container, position, object );
            container.removeView( (View) object );
        }
    }

    /**
     * ViewPager界面切换监听操作
     */
    private OnPageChangeListener onPageChangeListener=new OnPageChangeListener() {
        //当界面切换调用的方法
        //position : 当前界面的索引
        //positionOffset : 移动的百分比,从界面的右边到左边依次从0开始，到最左边的时候就是100%,但是如果切换到新的界面，百分比会重新从0开始计算
        //positionOffsetPixels : 移动的像素
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            //当界面切换的时候，同步的进行红色点的移动操作
            Log.d( TAG, "onPageScrolled:"+position+":"+positionOffset );
            //红色点移动的距离 = 20 * positionOffset
            //第二个界面的时候是+1*20；第三个界面的时候是+2*20
            int leftmargin = (int) (20 * positionOffset)+position * 20;
            //获取红色点的layoutparams
            RelativeLayout.LayoutParams layoutParams = (android.widget.RelativeLayout.LayoutParams) mReddot.getLayoutParams();
            layoutParams.leftMargin = leftmargin;
            mReddot.setLayoutParams(layoutParams);//将更改过的属性重新设置给红色点
        }

        //当界面切换完成调用的方法
        //position : 当前切换完成的界面的索引
        @Override
        public void onPageSelected(int position) {
            //当切换到第三个界面的时候，显示开始体验按钮，切换到其他界面，隐藏开始体验按钮
            if(position==list.size()-1){
                mStart.setVisibility( View.VISIBLE );
            }else {
                mStart.setVisibility( View.GONE );
            }

        }

        //当界面切换状态改变的是调用的方法
        @Override
        public void onPageScrollStateChanged(int position) {

        }
    };
}
