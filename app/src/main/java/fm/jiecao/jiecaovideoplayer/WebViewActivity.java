package fm.jiecao.jiecaovideoplayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.AbsoluteLayout;

import com.squareup.picasso.Picasso;

import fm.jiecao.jcvideoplayer_lib.JCUtils;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by Nathen on 16/10/13.
 */

public class WebViewActivity extends AppCompatActivity {
    WebView mWebView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("AboutWebView");
        setContentView(R.layout.activity_webview);
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new JCCallBack(), "jcvd");
        mWebView.loadUrl("file:///android_asset/jcvd.html");
    }

    @Override
    public void onBackPressed() {
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
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

    public class JCCallBack {

        @JavascriptInterface
        public void adViewJieCaoVideoPlayer(final int width, final int height, final int top, final int left, final int index) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (index == 0) {
                        JCVideoPlayerStandard webVieo = new JCVideoPlayerStandard(WebViewActivity.this);
                        webVieo.setUp(VideoConstant.videoUrlList[1],
                                JCVideoPlayer.SCREEN_LAYOUT_LIST, "嫂子骑大马");
                        Picasso.with(WebViewActivity.this)
                                .load(VideoConstant.videoThumbList[1])
                                .into(webVieo.thumbImageView);
                        ViewGroup.LayoutParams ll = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        AbsoluteLayout.LayoutParams layoutParams = new AbsoluteLayout.LayoutParams(ll);
                        layoutParams.y = JCUtils.dip2px(WebViewActivity.this, top);
                        layoutParams.x = JCUtils.dip2px(WebViewActivity.this, left);
                        layoutParams.height = JCUtils.dip2px(WebViewActivity.this, height);
                        layoutParams.width = JCUtils.dip2px(WebViewActivity.this, width);
                        mWebView.addView(webVieo, layoutParams);
                    } else {
                        JCVideoPlayerStandard webVieo = new JCVideoPlayerStandard(WebViewActivity.this);
                        webVieo.setUp(VideoConstant.videoUrlList[2],
                                JCVideoPlayer.SCREEN_LAYOUT_LIST, "嫂子失态了");
                        Picasso.with(WebViewActivity.this)
                                .load(VideoConstant.videoThumbList[2])
                                .into(webVieo.thumbImageView);
                        ViewGroup.LayoutParams ll = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        AbsoluteLayout.LayoutParams layoutParams = new AbsoluteLayout.LayoutParams(ll);
                        layoutParams.y = JCUtils.dip2px(WebViewActivity.this, top);
                        layoutParams.x = JCUtils.dip2px(WebViewActivity.this, left);
                        layoutParams.height = JCUtils.dip2px(WebViewActivity.this, height);
                        layoutParams.width = JCUtils.dip2px(WebViewActivity.this, width);
                        mWebView.addView(webVieo, layoutParams);
                    }

                }
            });

        }
    }
}
