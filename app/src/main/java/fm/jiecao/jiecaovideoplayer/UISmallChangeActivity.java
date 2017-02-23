package fm.jiecao.jiecaovideoplayer;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.squareup.picasso.Picasso;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import fm.jiecao.jiecaovideoplayer.CustomView.JCVideoPlayerStandardAutoCompleteAfterFullscreen;
import fm.jiecao.jiecaovideoplayer.CustomView.JCVideoPlayerStandardShowShareButtonAfterFullscreen;
import fm.jiecao.jiecaovideoplayer.CustomView.JCVideoPlayerStandardShowTextureViewAfterAutoComplete;
import fm.jiecao.jiecaovideoplayer.CustomView.JCVideoPlayerStandardShowTitleAfterFullscreen;

/**
 * Created by Nathen on 16/7/31.
 */
public class UISmallChangeActivity extends AppCompatActivity {
    JCVideoPlayerStandardShowShareButtonAfterFullscreen jcVideoPlayerStandardWithShareButton;
    JCVideoPlayerStandardShowTitleAfterFullscreen jcVideoPlayerStandardShowTitleAfterFullscreen;
    JCVideoPlayerStandardShowTextureViewAfterAutoComplete jcVideoPlayerStandardShowTextureViewAfterAutoComplete;
    JCVideoPlayerStandardAutoCompleteAfterFullscreen jcVideoPlayerStandardAutoCompleteAfterFullscreen;

    JCVideoPlayerStandard jcVideoPlayerStandard_1_1, jcVideoPlayerStandard_16_9;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("SmallChangeUI");
        setContentView(R.layout.activity_ui_small_change);

        jcVideoPlayerStandardWithShareButton = (JCVideoPlayerStandardShowShareButtonAfterFullscreen) findViewById(R.id.custom_videoplayer_standard_with_share_button);
        jcVideoPlayerStandardWithShareButton.setUp("http://video.jiecao.fm/11/17/c/fxkR4gylyIZKeljem8xTvA__.mp4", JCVideoPlayer.SCREEN_LAYOUT_NORMAL
                , "嫂子想呼吸");
        Picasso.with(this)
                .load("http://img4.jiecaojingxuan.com/2016/11/17/6fc2ae91-36e2-44c5-bb10-29ae5d5c678c.png@!640_360")
                .into(jcVideoPlayerStandardWithShareButton.thumbImageView);


        jcVideoPlayerStandardShowTitleAfterFullscreen = (JCVideoPlayerStandardShowTitleAfterFullscreen) findViewById(R.id.custom_videoplayer_standard_show_title_after_fullscreen);
        jcVideoPlayerStandardShowTitleAfterFullscreen.setUp("http://video.jiecao.fm/11/18/xu/%E6%91%87%E5%A4%B4.mp4", JCVideoPlayer.SCREEN_LAYOUT_NORMAL
                , "嫂子想摇头");
        Picasso.with(this)
                .load("http://img4.jiecaojingxuan.com/2016/11/18/f03cee95-9b78-4dd5-986f-d162c06c385c.png@!640_360")
                .into(jcVideoPlayerStandardShowTitleAfterFullscreen.thumbImageView);

        jcVideoPlayerStandardShowTextureViewAfterAutoComplete = (JCVideoPlayerStandardShowTextureViewAfterAutoComplete) findViewById(R.id.custom_videoplayer_standard_show_textureview_aoto_complete);
        jcVideoPlayerStandardShowTextureViewAfterAutoComplete.setUp("http://video.jiecao.fm/11/18/c/I-KpaMJ-HMDfAy6tX2Jfag__.mp4", JCVideoPlayer.SCREEN_LAYOUT_NORMAL
                , "嫂子想旅行");
        Picasso.with(this)
                .load("http://img4.jiecaojingxuan.com/2016/11/18/e7ea659f-c3d2-4979-9ea5-f993b05e5930.png@!640_360")
                .into(jcVideoPlayerStandardShowTextureViewAfterAutoComplete.thumbImageView);

        jcVideoPlayerStandardAutoCompleteAfterFullscreen = (JCVideoPlayerStandardAutoCompleteAfterFullscreen) findViewById(R.id.custom_videoplayer_standard_aoto_complete);
        jcVideoPlayerStandardAutoCompleteAfterFullscreen.setUp("http://video.jiecao.fm/8/17/%E6%8A%AB%E8%90%A8.mp4", JCVideoPlayer.SCREEN_LAYOUT_NORMAL
                , "嫂子没来");
        Picasso.with(this)
                .load("http://img4.jiecaojingxuan.com/2016/8/17/f2dbd12e-b1cb-4daf-aff1-8c6be2f64d1a.jpg")
                .into(jcVideoPlayerStandardAutoCompleteAfterFullscreen.thumbImageView);

        jcVideoPlayerStandard_1_1 = (JCVideoPlayerStandardAutoCompleteAfterFullscreen) findViewById(R.id.jc_videoplayer_1_1);
        jcVideoPlayerStandard_1_1.setUp("http://video.jiecao.fm/8/17/%E6%8A%AB%E8%90%A8.mp4", JCVideoPlayer.SCREEN_LAYOUT_NORMAL
                , "嫂子有事吗");
        Picasso.with(this)
                .load("http://img4.jiecaojingxuan.com/2016/8/17/f2dbd12e-b1cb-4daf-aff1-8c6be2f64d1a.jpg")
                .into(jcVideoPlayerStandard_1_1.thumbImageView);
        jcVideoPlayerStandard_1_1.widthRatio = 1;
        jcVideoPlayerStandard_1_1.heightRatio = 1;

        jcVideoPlayerStandard_16_9 = (JCVideoPlayerStandardAutoCompleteAfterFullscreen) findViewById(R.id.jc_videoplayer_16_9);
        jcVideoPlayerStandard_16_9.setUp("http://video.jiecao.fm/8/17/%E6%8A%AB%E8%90%A8.mp4", JCVideoPlayer.SCREEN_LAYOUT_NORMAL
                , "嫂子来不了");
        Picasso.with(this)
                .load("http://img4.jiecaojingxuan.com/2016/8/17/f2dbd12e-b1cb-4daf-aff1-8c6be2f64d1a.jpg")
                .into(jcVideoPlayerStandard_16_9.thumbImageView);
        jcVideoPlayerStandard_16_9.widthRatio = 16;
        jcVideoPlayerStandard_16_9.heightRatio = 9;
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
}
