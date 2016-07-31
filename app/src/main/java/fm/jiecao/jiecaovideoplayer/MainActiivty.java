package fm.jiecao.jiecaovideoplayer;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.lang.reflect.Constructor;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerSimple;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by Nathen on 16/7/22.
 */
public class MainActiivty extends AppCompatActivity {

    JCVideoPlayerStandard jcVideoPlayerStandard;
    JCVideoPlayerSimple   jcVideoPlayerSimple;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        jcVideoPlayerSimple = (JCVideoPlayerSimple) findViewById(R.id.simple_demo);
        jcVideoPlayerSimple.setUp("http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8"
                , JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, "嫂子在家吗");

        jcVideoPlayerStandard = (JCVideoPlayerStandard) findViewById(R.id.jc_video);
        jcVideoPlayerStandard.setUp("http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4"
                , JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, "嫂子闭眼睛");
        ImageLoader.getInstance().displayImage("http://cos.myqcloud.com/1000264/qcloud_video_attachment/842646334/vod_cover/cover1458036374.jpg",
                jcVideoPlayerStandard.thumbImageView);

        findViewById(R.id.list_demo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActiivty.this, ListActivity.class));
            }
        });
        findViewById(R.id.play_directly_without_layout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActiivty.this, DirectlyPlayActivity.class));
            }
        });

        findViewById(R.id.ui_demo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActiivty.this, UiActivity.class));
            }
        });
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

}
