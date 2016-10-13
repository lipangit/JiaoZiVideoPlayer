package fm.jiecao.jiecaovideoplayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
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
        setContentView(R.layout.activity_webview);
        mWebView = (WebView) findViewById(R.id.webview);
        mWebView.getSettings().setJavaScriptEnabled(true);
        mWebView.addJavascriptInterface(new JCCallBack(), "jcvd");
        mWebView.loadUrl("file:///android_asset/jcvd.html");
    }

    public class JCCallBack {

        @JavascriptInterface
        public void adViewJieCaoVideoPlayer(final int width, final int height, final int top, final int bottom) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JCVideoPlayerStandard webVieo = new JCVideoPlayerStandard(WebViewActivity.this);
                    webVieo.setUp("http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4",
                            JCVideoPlayer.SCREEN_LAYOUT_LIST, "嫂子好困");
                    Picasso.with(WebViewActivity.this)
                            .load("http://cos.myqcloud.com/1000264/qcloud_video_attachment/842646334/vod_cover/cover1458036374.jpg")
                            .into(webVieo.thumbImageView);
                    ViewGroup.LayoutParams ll = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    AbsoluteLayout.LayoutParams layoutParams = new AbsoluteLayout.LayoutParams(ll);
                    layoutParams.y = JCUtils.dip2px(WebViewActivity.this, top);
                    layoutParams.height = JCUtils.dip2px(WebViewActivity.this, height);
                    mWebView.addView(webVieo, layoutParams);
                }
            });

        }
    }
}
