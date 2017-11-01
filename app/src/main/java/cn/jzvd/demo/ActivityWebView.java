package cn.jzvd.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.AbsoluteLayout;

import com.squareup.picasso.Picasso;

import cn.jzvd.JZUtils;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by Nathen on 16/10/13.
 */

public class ActivityWebView extends AppCompatActivity {
    WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("WebView");
        setContentView(R.layout.activity_webview);
        mWebView = findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new JZCallBack(), "jzvd");
        mWebView.loadUrl("file:///android_asset/jzvd.html");
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public class JZCallBack {

        @JavascriptInterface
        public void adViewJiaoZiVideoPlayer(final int width, final int height, final int top, final int left, final int index) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (index == 0) {
                        JZVideoPlayerStandard webVieo = new JZVideoPlayerStandard(ActivityWebView.this);
                        webVieo.setUp(VideoConstant.videoUrlList[1],
                                JZVideoPlayer.SCREEN_WINDOW_LIST, "饺子骑大马");
                        Picasso.with(ActivityWebView.this)
                                .load(VideoConstant.videoThumbList[1])
                                .into(webVieo.thumbImageView);
                        ViewGroup.LayoutParams ll = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        AbsoluteLayout.LayoutParams layoutParams = new AbsoluteLayout.LayoutParams(ll);
                        layoutParams.y = JZUtils.dip2px(ActivityWebView.this, top);
                        layoutParams.x = JZUtils.dip2px(ActivityWebView.this, left);
                        layoutParams.height = JZUtils.dip2px(ActivityWebView.this, height);
                        layoutParams.width = JZUtils.dip2px(ActivityWebView.this, width);
                        mWebView.addView(webVieo, layoutParams);
                    } else {
                        JZVideoPlayerStandard webVieo = new JZVideoPlayerStandard(ActivityWebView.this);
                        webVieo.setUp(VideoConstant.videoUrlList[2],
                                JZVideoPlayer.SCREEN_WINDOW_LIST, "饺子失态了");
                        Picasso.with(ActivityWebView.this)
                                .load(VideoConstant.videoThumbList[2])
                                .into(webVieo.thumbImageView);
                        ViewGroup.LayoutParams ll = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        AbsoluteLayout.LayoutParams layoutParams = new AbsoluteLayout.LayoutParams(ll);
                        layoutParams.y = JZUtils.dip2px(ActivityWebView.this, top);
                        layoutParams.x = JZUtils.dip2px(ActivityWebView.this, left);
                        layoutParams.height = JZUtils.dip2px(ActivityWebView.this, height);
                        layoutParams.width = JZUtils.dip2px(ActivityWebView.this, width);
                        mWebView.addView(webVieo, layoutParams);
                    }

                }
            });

        }
    }
}
