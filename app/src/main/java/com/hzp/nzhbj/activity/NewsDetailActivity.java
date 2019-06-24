package com.hzp.nzhbj.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hzp.nzhbj.R;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.OnekeyShareTheme;

public class NewsDetailActivity extends Activity implements View.OnClickListener {

    private String mUrl;

    @ViewInject( R.id.titlebar_tv_title )
    private TextView mTitle;

    @ViewInject( R.id.titlebar_btn_menu )
    private ImageButton mMenu;

    @ViewInject( R.id.titlebar_btn_back )
    private ImageButton mBack;

    @ViewInject( R.id.titlebar_ll_shareandtextsize )
    private LinearLayout mTextSizeAndShare;

    @ViewInject( R.id.titlebar_btn_textsize )
    private ImageButton mTextSize;

    @ViewInject( R.id.titlebar_btn_share )
    private ImageButton mShare;

    @ViewInject(R.id.newsdetail_wb_webview)
    private WebView mWebView;

    @ViewInject(R.id.newsdetail_pb_loading)
    private ProgressBar mLoading;

    private WebSettings webSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState );
        requestWindowFeature( Window.FEATURE_NO_TITLE );
        setContentView( R.layout.activity_news_detail );

        // 接受传递过来的url
        mUrl = getIntent().getStringExtra( "url" );

        ViewUtils.inject( this );

        initView();
    }

    /**
     * 初始化控件状态
     */
    private void initView() {
        mTitle.setText("详情");
        mMenu.setVisibility( View.GONE);
        mBack.setVisibility(View.VISIBLE);
        mTextSizeAndShare.setVisibility(View.VISIBLE);

        // 通过webview显示网页
        mWebView.loadUrl( mUrl );// 根据网页的地址，加载显示网页
        // 设置webview操作
        webSettings=mWebView.getSettings();
        webSettings.setBuiltInZoomControls( true );// 显示放大/缩小按钮（wap网页不支持）
        webSettings.setUseWideViewPort( true );// 双击放大/缩小（wap网页不支持）
        webSettings.setJavaScriptEnabled( true );// 是否支持js操作

        // 监听webview加载网页的操作
        mWebView.setWebViewClient( new WebViewClient(){
            // 当网页打开的时候调用的方法
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                mLoading.setVisibility( View.VISIBLE );
                super.onPageStarted( view, url, favicon );
            }
            // 当网页加载完成的时候调用的方法
            @Override
            public void onPageFinished(WebView view, String url) {
                mLoading.setVisibility( View.GONE );
                super.onPageFinished( view, url );
            }
        } );

        mBack.setOnClickListener( this );
        mTextSize.setOnClickListener( this );
        mShare.setOnClickListener( this );


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.titlebar_btn_back:
                finish();
                break;
            case R.id.titlebar_btn_textsize:
                showSetTextSizeDialog();
                break;
            case R.id.titlebar_btn_share:
                showShare();
                break;
        }
    }

    /**
     * 分享的方法
     */
    private void showShare() {
        ShareSDK.initSDK( this );
        OnekeyShare oks = new OnekeyShare();
        // 关闭sso授权
        oks.disableSSOWhenAuthorize();

        oks.setTheme( OnekeyShareTheme.CLASSIC);

        // 分享时Notification的图标和文字 2.5.9以后的版本不调用此方法
        // oks.setNotification(R.drawable.ic_launcher,
        // getString(R.string.app_name));
        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle("标题");
        // titleUrl是标题的网络链接，仅在人人网和QQ空间使用
        oks.setTitleUrl("http://sharesdk.cn");
        // text是分享文本，所有平台都需要这个字段
        oks.setText("我是分享文本");
        // 分享网络图片，新浪微博分享网络图片需要通过审核后申请高级写入接口，否则请注释掉测试新浪微博
        //oks.setImageUrl("http://f1.sharesdk.cn/imgs/2014/02/26/owWpLZo_638x960.jpg");
        // imagePath是图片的本地路径，Linked-In以外的平台都支持此参数
        oks.setImagePath("/sdcard/text.jpg");//确保SDcard下面存在此张图片
        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl("http://sharesdk.cn");
        // comment是我对这条分享的评论，仅在人人网和QQ空间使用
        oks.setComment("我是测试评论文本");
        // site是分享此内容的网站名称，仅在QQ空间使用
        oks.setSite("ShareSDK");
        // siteUrl是分享此内容的网站地址，仅在QQ空间使用
        oks.setSiteUrl("http://sharesdk.cn");

        // 启动分享GUI
        oks.show(this);

    }

    private String [] items=new String[]{"超大号字体", "大号字体", "正常字体", "小号字体", "超小号字体"};

    private int textSize;
    /** 设置保存选中字体大小的索引 **/
    private int currentTextSize = 2;

    /**
     * 设置字体大小的dialog
     */
    private void showSetTextSizeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder( this );
        builder.setTitle( "字体设置" );

        // 设置单选按钮组
        // items : 单选按钮组的文本的数组
        // checkedItem : 默认选中按钮
        // 参数3：选中的点击事件
        builder.setSingleChoiceItems( items, currentTextSize, new DialogInterface.OnClickListener() {
            // which : 选中的按钮的索引
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 当选中按钮的时候，将按钮的索引保存起来
                textSize=which;
            }
        } );
        builder.setPositiveButton( "确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // 根据选中按钮的索引设置字体大小
                switch (textSize) {
                    case 0:
                        webSettings.setTextSize( WebSettings.TextSize.LARGEST );
                        break;
                    case 1:
                        webSettings.setTextSize( WebSettings.TextSize.LARGER );
                        break;
                    case 2:
                        webSettings.setTextSize( WebSettings.TextSize.NORMAL );
                        break;
                    case 3:
                        webSettings.setTextSize( WebSettings.TextSize.SMALLER );
                        break;
                    case 4:
                        webSettings.setTextSize( WebSettings.TextSize.SMALLEST );
                        break;
                }
                currentTextSize=textSize;
            }
        } );
        builder.setNegativeButton( "取消",null );
        builder.show();

    }
}
