package cn.jzvd.demo;

import android.app.Activity;
import android.os.Bundle;

import com.squareup.picasso.Picasso;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by Nathen on 2017/9/19.
 */

public class ActivityApiExtendsNormal extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extends_normal);
        JZVideoPlayerStandard jzVideoPlayerStandard = findViewById(R.id.videoplayer);
        jzVideoPlayerStandard.setUp(VideoConstant.videoUrlList[0]
                , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "饺子不信");
        Picasso.with(this)
                .load(VideoConstant.videoThumbList[0])
                .into(jzVideoPlayerStandard.thumbImageView);
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
}
