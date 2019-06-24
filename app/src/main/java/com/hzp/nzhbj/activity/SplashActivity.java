package com.hzp.nzhbj.activity;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;

import com.hzp.nzhbj.R;
import com.hzp.nzhbj.tool.Constants;
import com.hzp.nzhbj.tool.SharedPreferencesTool;

public class SplashActivity extends Activity {
    private RelativeLayout mSplashRelRoot;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_splash );
        initView();
    }

    /**
     * 初始化控件
     */
    private void initView() {
        mSplashRelRoot = (RelativeLayout) findViewById(R.id.splash_rel_root);
        //执行动画的操作
        setAnimation();
    }

    /**
     * 执行界面动画
     */
    private void setAnimation() {
        //旋转
        RotateAnimation rotateAnimation=new RotateAnimation( 0,360, Animation.RELATIVE_TO_SELF,0.5f, Animation.RELATIVE_TO_SELF,0.5f );
        rotateAnimation.setDuration( 1000 );
        rotateAnimation.setFillAfter( true );//保持动画结束的状态

        //缩放
        //fromX : 起始控件的尺寸
        //toX : 结束控件的尺寸
        ScaleAnimation scaleAnimation=new ScaleAnimation( 0,1,0,1,Animation.RELATIVE_TO_SELF,05f,Animation.RELATIVE_TO_SELF,0.5f );
        scaleAnimation.setDuration( 1000 );
        scaleAnimation.setFillAfter( true );

        //渐变
        AlphaAnimation alphaAnimation=new AlphaAnimation( 0.0f,1.0f );
        alphaAnimation.setDuration( 1000 );
        alphaAnimation.setFillAfter( true );

        //组合动画
        //shareInterpolator : 是否使用同一动画插补器，true使用;false:不使用，各自使用各自的
        AnimationSet animationSet=new AnimationSet( false );
        animationSet.addAnimation( rotateAnimation );
        animationSet.addAnimation( scaleAnimation );
        animationSet.addAnimation( alphaAnimation );

        //执行动画
        mSplashRelRoot.startAnimation( animationSet );

        //动画执行结束，跳转到引导界面/首页
        //监听动画的执行操作
        animationSet.setAnimationListener( animationListener );



    }

    /**
     * 动画的监听
     */
    private Animation.AnimationListener animationListener=new Animation.AnimationListener() {
        //动画开始的调用
        @Override
        public void onAnimationStart(Animation animation) {

        }
        //动画结束调用的
        @Override
        public void onAnimationEnd(Animation animation) {
            enter();
        }
        //动画重复执行调用
        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    /**
     * 跳转界面的操作
     */
    private void enter() {
        //跳转引导界面/首页
        //问题：如何知道跳转到首页还是引导界面
        //跟手机卫士中手机防盗的跳转操作思路，获取是否是第一次进入的操作，如果是第一次进入，跳转到引导页面，
        // 如果不是第一次进入，跳转到首页
        boolean b = SharedPreferencesTool.getBoolean( SplashActivity.this, Constants.ISFIRSTENTER, true );
        if(b){
            //第一次进入，跳转到引导界面
            Intent intent = new Intent( SplashActivity.this, GuideActivity.class );
            startActivity( intent );
        }else {
            //不是第一次进入，跳转到首页
            Intent intent = new Intent( SplashActivity.this, HomeActivity.class );
            startActivity( intent );
        }
        //跳转界面完成，移除Splash界面
        finish();
    }
}
