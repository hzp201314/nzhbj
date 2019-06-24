package com.hzp.nzhbj.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hzp.nzhbj.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * created by hzp on 2019/6/23 16:17
 * 作者：codehan
 * 描述：自定义上下拉刷新ListView 具有下拉刷新头和上拉刷新脚
 */
public class PullToRefreshListView extends ListView implements OnScrollListener {

    private View headerView;
    private LinearLayout mRefreshLlHeaderrootview;
    private LinearLayout mRefreshLlHeaderview;
    private ImageView mRefreshIvArrow;
    private TextView mRefreshTvText;
    private TextView mRefreshTvTime;
    private ProgressBar mRefreshPbLoading;
    private int refreshLlHeaderviewMeasuredHeight;
    private View viewPager;
    private RotateAnimation up;
    private RotateAnimation down;
    private int downY=-1;

    /*下拉刷新*/
    private static final int PULL_DOWN=1;
    /*松开刷新*/
    private static final int REFRESH=2;
    /*正在刷新*/
    private static final int REFRESHING=3;
    private int CURRENTSTATE=PULL_DOWN;
    private View footerView;
    private int footerViewMeasuredHeight;
    /*是否加载显示更多标示*/
    private boolean isLoadMore=false;


    public PullToRefreshListView(Context context) {
//        super( context );
        this(context,null);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs) {
//        super( context, attrs );
        this(context,attrs,-1);
    }

    public PullToRefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super( context, attrs, defStyleAttr );
        addHeader();
        initAnimation();
        getTime();
        addFooter();
        setOnScrollListener(this);
    }

    /**
     * 添加刷新头+viewpager的布局作为listview的头条目
     */
    private void addHeader() {
        headerView = View.inflate( getContext(), R.layout.refresh_header, null );

        mRefreshLlHeaderrootview = (LinearLayout) headerView.findViewById(R.id.refresh_ll_headerrootview);

        //刷新头
        mRefreshLlHeaderview = (LinearLayout) headerView.findViewById(R.id.refresh_ll_headerview);
        mRefreshIvArrow = (ImageView) headerView.findViewById(R.id.refresh_iv_arrow);
        mRefreshPbLoading = (ProgressBar) headerView.findViewById(R.id.refresh_pb_loading);
        mRefreshTvText = (TextView) headerView.findViewById(R.id.refresh_tv_text);
        mRefreshTvTime = (TextView) headerView.findViewById(R.id.refresh_tv_time);

        // 隐藏刷新头
        mRefreshLlHeaderview.measure( 0,0 );
        refreshLlHeaderviewMeasuredHeight = mRefreshLlHeaderview.getMeasuredHeight();
        mRefreshLlHeaderview.setPadding( 0,-refreshLlHeaderviewMeasuredHeight,0,0 );

        addHeaderView( headerView );
    }

    /**
     * 添加viewpager到刷新头布局的操作
     * @param view
     */
    public void setViewPager(View view){
        viewPager = view;
        mRefreshLlHeaderrootview.addView( view );
    }

    /**
     * 初始化动画
     */
    private void initAnimation() {
        // 箭头向上
        up = new RotateAnimation( 0, -180, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f );
        up.setDuration( 500 );
        up.setFillAfter( true );// 保持动画结束的状态

        // 箭头向下
        down = new RotateAnimation( -180, -360, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f );
        up.setDuration( 500 );
        up.setFillAfter( true );// 保持动画结束的状态

    }

    /**
     * 设置刷新时间操作
     */
    private void getTime() {
        Date date = new Date();
        SimpleDateFormat simpleDateFormat=new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
        String time = simpleDateFormat.format( date );
        mRefreshTvTime.setText( time );

    }

    // 1.触摸事件
    // 2.下拉操作
    // 3.如果当前界面显示的第一个条目是listview的第一个条目，下拉显示刷新头
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d( "PullToRefreshListView", "onTouchEvent: listView的action_down" );
                downY = (int) ev.getY();// 因为事先在viewpager中给viewpager的显示的view对象设置了触摸事件，所以造成了按下的触摸事件被viewpager中显示的view对象处理了
                break;
            case MotionEvent.ACTION_MOVE:
                Log.d( "PullToRefreshListView", "onTouchEvent: listView的action_move" );
                //判断如果是正在刷新，停止下拉操作
                if(CURRENTSTATE==REFRESHING){
                    return true;
                }
                //如果listview在屏幕中的y坐标大于viewpager在屏幕y坐标，不能执行下拉操作
                int[] listviewLocation = new int[2];//元素1：x坐标；元素2：y坐标
                getLocationOnScreen( listviewLocation );//获取当前控件在屏幕中的坐标，保存到int类型的数组中
                int listviewY=listviewLocation[1];//获取取listview在屏幕中y坐标

                int[] viewPagerLocation = new int[2];
                viewPager.getLocationOnScreen( viewPagerLocation );
                int viewPagerY =viewPagerLocation[1];

                if(listviewY>viewPagerY){
                    downY=-1;
                    break;
                }
                if(downY==-1){
                    downY=(int)ev.getY();// 因为listview的按下的事件被拦截处理了，导致listview拿不到按下的坐标，所以取的时候和按下坐标相邻的移动的第一个点的坐标
                }
                int moveY=(int)ev.getY();
                if(moveY-downY>0&&getFirstVisiblePosition()==0){
                    //下拉显示刷新头
                    int paddingTop=moveY-downY-refreshLlHeaderviewMeasuredHeight;
                    mRefreshLlHeaderview.setPadding( 0,paddingTop,0,0 );

                    // 下拉刷新 -> 松开刷新
                    if(paddingTop>0&&CURRENTSTATE==PULL_DOWN){
                        CURRENTSTATE=REFRESH;
                        switchOption();
                    }

                    // 松开刷新 -> 下拉刷新
                    if(paddingTop<0&&CURRENTSTATE==REFRESH){
                        CURRENTSTATE=PULL_DOWN;
                        switchOption();
                    }
                    return true;

                }
                break;
            case MotionEvent.ACTION_UP:
                downY=-1;
                // 松开刷新 -> 正在刷新
                if(CURRENTSTATE==REFRESH){
                    CURRENTSTATE=REFRESHING;
                    // 显示刷新头
                    mRefreshLlHeaderview.setPadding( 0,0,0,0 );
                    switchOption();

                    //加载数据
                    if(listener !=null){
                        listener.refresh();
                    }

                }
                // 下拉刷新 -> 弹出刷新头
                if(CURRENTSTATE==PULL_DOWN){
                    mRefreshLlHeaderview.setPadding( 0,-refreshLlHeaderviewMeasuredHeight,0,0 );
                }
                break;

            default:
                break;
        }
        return super.onTouchEvent( ev );
    }

    /**
     * 根据状态改变控件的内容
     */
    private void switchOption() {
        switch (CURRENTSTATE) {
            case PULL_DOWN:
                mRefreshTvText.setText( "下拉刷新" );
                mRefreshIvArrow.startAnimation( down );
                break;
            case REFRESH:
                mRefreshTvText.setText( "松开刷新" );
                mRefreshIvArrow.startAnimation( up );
                break;
            case REFRESHING:
                mRefreshTvText.setText( "正在刷新" );
                mRefreshIvArrow.clearAnimation();
                mRefreshIvArrow.setVisibility( GONE );
                mRefreshPbLoading.setVisibility( VISIBLE );
                break;

        }
    }

    /**
     * 取消刷新操作
     */
    public void finish(){
        //正在刷新 -> 下拉刷新，隐藏刷新头
        if(CURRENTSTATE==REFRESHING){
            CURRENTSTATE=PULL_DOWN;
            mRefreshTvText.setText( "下拉刷新" );
            mRefreshPbLoading.setVisibility( GONE );
            mRefreshIvArrow.setVisibility( VISIBLE );
            mRefreshLlHeaderview.setPadding( 0,-refreshLlHeaderviewMeasuredHeight,0,0 );
        }

        //取消加载更多
        if(isLoadMore){
            footerView.setPadding( 0,0,0,-footerViewMeasuredHeight );
            isLoadMore=false;
        }
    }

    /**
     * 添加加载更多底部条目的操作
     */
    private void addFooter() {
        footerView = View.inflate( getContext(), R.layout.refresh_footer, null );

        footerView.measure( 0,0 );
        footerViewMeasuredHeight = footerView.getMeasuredHeight();
        footerView.setPadding( 0,0,0,-footerViewMeasuredHeight );

        addFooterView( footerView );

    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        //当滑动到listview的最后一个条目，并且停止滑动的时候，显示加载更多条目
        if(scrollState==OnScrollListener.SCROLL_STATE_IDLE&&getLastVisiblePosition()==getCount()-1&& isLoadMore ==false){
            isLoadMore=true;
            footerView.setPadding( 0,0,0,0 );
            setSelection( getCount()-1 );

            //加载数据
            if(listener!=null){
                listener.loadMore();
            }
        }
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }

    //创建下拉刷新数据和上拉加载数据的回调，方便界面调用实现
    public interface OnReFreshListener{
        /*下拉刷新*/
        public void refresh();

        /*加载更多*/
        public void loadMore();
    }
    private OnReFreshListener listener;
    public void setOnRefreshListener(OnReFreshListener listener){
        this.listener=listener;
    }
}
