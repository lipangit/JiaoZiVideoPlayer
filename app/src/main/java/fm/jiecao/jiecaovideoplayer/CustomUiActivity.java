package fm.jiecao.jiecaovideoplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.nostra13.universalimageloader.core.ImageLoader;

import fm.jiecao.jcvideoplayer_lib.JCAbstractVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCDemoVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCFullScreenActivity;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerJinRiTouTIao;

/**
 * Created by Nathen
 * On 2016/04/15 11:25
 */
public class CustomUiActivity extends AppCompatActivity implements View.OnClickListener {
    JCDemoVideoPlayer jcDemoVideoPlayer;
    JCVideoPlayerJinRiTouTIao jcVideoPlayerJinRiTouTIao;
    Button btnToFullscreen_demo, btnToFullscreen_jinritoutiao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_ui);

        jcDemoVideoPlayer = (JCDemoVideoPlayer) findViewById(R.id.custom_videoplayer);
        jcDemoVideoPlayer.setUp("http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4");

        jcVideoPlayerJinRiTouTIao = (JCVideoPlayerJinRiTouTIao) findViewById(R.id.custom_videoplayer_jinritoutiao);
        jcVideoPlayerJinRiTouTIao.setUp("http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4"
                , "嫂子干啥呢");
        ImageLoader.getInstance().displayImage("http://cos.myqcloud.com/1000264/qcloud_video_attachment/842646334/vod_cover/cover1458036374.jpg",
                jcVideoPlayerJinRiTouTIao.ivThumb);

        btnToFullscreen_demo = (Button) findViewById(R.id.to_fullscreen_simple_demo);
        btnToFullscreen_jinritoutiao = (Button) findViewById(R.id.to_fullscreen_jinritoutiao);
        btnToFullscreen_demo.setOnClickListener(this);
        btnToFullscreen_jinritoutiao.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.to_fullscreen_simple_demo:
                JCAbstractVideoPlayer jcAbstractVideoPlayer = new JCDemoVideoPlayer(this);
                JCFullScreenActivity.toActivity(this,
                        "http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4",
                        jcAbstractVideoPlayer.getClass(), "嫂子真浪");
                break;
            case R.id.to_fullscreen_jinritoutiao:
                JCAbstractVideoPlayer jCVideoPlayerJinRiTouTIao = new JCVideoPlayerJinRiTouTIao(this);
                JCFullScreenActivity.toActivity(this,
                        "http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4",
                        jCVideoPlayerJinRiTouTIao.getClass(), "嫂子真牛逼");
                break;
        }
    }
}
