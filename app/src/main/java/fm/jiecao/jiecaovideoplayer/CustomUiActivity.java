package fm.jiecao.jiecaovideoplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.nostra13.universalimageloader.core.ImageLoader;

import fm.jiecao.jcvideoplayer_lib.JCAbstractVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerSimple;
import fm.jiecao.jcvideoplayer_lib.JCFullScreenActivity;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import fm.jiecao.jiecaovideoplayer.View.JCVideoPlayerStandardWithShareButton;

/**
 * Created by Nathen
 * On 2016/04/15 11:25
 */
public class CustomUiActivity extends AppCompatActivity implements View.OnClickListener {
    JCVideoPlayerSimple jcVideoPlayerSimple;
    JCVideoPlayerStandard jcVideoPlayerStandard;
    JCVideoPlayerStandardWithShareButton jcVideoPlayerStandardWithShareButton;
    Button btnToFullscreen_simple, btnToFullscreen_standard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_ui);

        jcVideoPlayerSimple = (JCVideoPlayerSimple) findViewById(R.id.custom_videoplayer);
        jcVideoPlayerSimple.setUp("http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4");

        jcVideoPlayerStandard = (JCVideoPlayerStandard) findViewById(R.id.custom_videoplayer_standard);
        jcVideoPlayerStandard.setUp("http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4"
                , "嫂子干啥呢");
        ImageLoader.getInstance().displayImage("http://cos.myqcloud.com/1000264/qcloud_video_attachment/842646334/vod_cover/cover1458036374.jpg",
                jcVideoPlayerStandard.ivThumb);

        jcVideoPlayerStandardWithShareButton = (JCVideoPlayerStandardWithShareButton) findViewById(R.id.custom_videoplayer_standard_with_share_button);
        jcVideoPlayerStandardWithShareButton.setUp("http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4"
                , "嫂子闭眼睛");
        ImageLoader.getInstance().displayImage("http://cos.myqcloud.com/1000264/qcloud_video_attachment/842646334/vod_cover/cover1458036374.jpg",
                jcVideoPlayerStandardWithShareButton.ivThumb);

        btnToFullscreen_simple = (Button) findViewById(R.id.to_fullscreen_simple);
        btnToFullscreen_standard = (Button) findViewById(R.id.to_fullscreen_standard);
        btnToFullscreen_simple.setOnClickListener(this);
        btnToFullscreen_standard.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.to_fullscreen_simple:
                JCFullScreenActivity.toActivity(this,
                        "http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4",
                        JCVideoPlayerSimple.class, "嫂子真浪");
                break;
            case R.id.to_fullscreen_standard:
                JCFullScreenActivity.toActivity(this,
                        "http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4",
                        JCVideoPlayerStandard.class, "嫂子真牛逼");
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCAbstractVideoPlayer.releaseAllVideos();
    }
}
