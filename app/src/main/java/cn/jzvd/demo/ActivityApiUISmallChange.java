package cn.jzvd.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.bumptech.glide.Glide;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import cn.jzvd.demo.CustomJzvd.JzvdStdAutoCompleteAfterFullscreen;
import cn.jzvd.demo.CustomJzvd.JzvdStdMp3;
import cn.jzvd.demo.CustomJzvd.JzvdStdShowShareButtonAfterFullscreen;
import cn.jzvd.demo.CustomJzvd.JzvdStdShowTextureViewAfterAutoComplete;
import cn.jzvd.demo.CustomJzvd.JzvdStdShowTitleAfterFullscreen;
import cn.jzvd.demo.CustomJzvd.JzvdStdSpeed;
import cn.jzvd.demo.CustomJzvd.JzvdStdVolumeAfterFullscreen;

/**
 * Created by Nathen on 16/7/31.
 */
public class ActivityApiUISmallChange extends AppCompatActivity {
    JzvdStdShowShareButtonAfterFullscreen jzvdStdWithShareButton;
    JzvdStdShowTitleAfterFullscreen jzvdStdShowTitleAfterFullscreen;
    JzvdStdShowTextureViewAfterAutoComplete jzvdStdShowTextureViewAfterAutoComplete;
    JzvdStdAutoCompleteAfterFullscreen jzvdStdAutoCompleteAfterFullscreen;
    JzvdStdVolumeAfterFullscreen jzvdStdVolumeAfterFullscreen;
    JzvdStdMp3 jzvdStdMp3;
    JzvdStdSpeed jzvdStdSpeed;

    JzvdStd jzvdStd_1_1, jzvdStd_16_9;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("SmallChangeUI");
        setContentView(R.layout.activity_ui_small_change);

        jzvdStdWithShareButton = findViewById(R.id.custom_videoplayer_standard_with_share_button);
        jzvdStdWithShareButton.setUp(VideoConstant.videoUrlList[3], "饺子想呼吸", Jzvd.SCREEN_NORMAL);
        Glide.with(this)
                .load(VideoConstant.videoThumbList[3])
                .into(jzvdStdWithShareButton.thumbImageView);


        jzvdStdShowTitleAfterFullscreen = findViewById(R.id.custom_videoplayer_standard_show_title_after_fullscreen);
        jzvdStdShowTitleAfterFullscreen.setUp(VideoConstant.videoUrlList[4], "饺子想摇头", Jzvd.SCREEN_NORMAL);
        Glide.with(this)
                .load(VideoConstant.videoThumbList[4])
                .into(jzvdStdShowTitleAfterFullscreen.thumbImageView);

        jzvdStdShowTextureViewAfterAutoComplete = findViewById(R.id.custom_videoplayer_standard_show_textureview_aoto_complete);
        jzvdStdShowTextureViewAfterAutoComplete.setUp(VideoConstant.videoUrlList[5], "饺子想旅行", Jzvd.SCREEN_NORMAL);
        Glide.with(this)
                .load(VideoConstant.videoThumbList[5])
                .into(jzvdStdShowTextureViewAfterAutoComplete.thumbImageView);

        jzvdStdAutoCompleteAfterFullscreen = findViewById(R.id.custom_videoplayer_standard_aoto_complete);
        jzvdStdAutoCompleteAfterFullscreen.setUp(VideoConstant.videoUrls[0][1], "饺子没来", Jzvd.SCREEN_NORMAL);
        Glide.with(this)
                .load(VideoConstant.videoThumbs[0][1])
                .into(jzvdStdAutoCompleteAfterFullscreen.thumbImageView);

        jzvdStd_1_1 = findViewById(R.id.jz_videoplayer_1_1);
        jzvdStd_1_1.setUp(VideoConstant.videoUrls[0][1], "饺子有事吗", Jzvd.SCREEN_NORMAL);
        Glide.with(this)
                .load(VideoConstant.videoThumbs[0][1])
                .into(jzvdStd_1_1.thumbImageView);
        jzvdStd_1_1.widthRatio = 1;
        jzvdStd_1_1.heightRatio = 1;

        jzvdStd_16_9 = findViewById(R.id.jz_videoplayer_16_9);
        jzvdStd_16_9.setUp(VideoConstant.videoUrls[0][1], "饺子来不了", Jzvd.SCREEN_NORMAL);
        Glide.with(this)
                .load(VideoConstant.videoThumbs[0][1])
                .into(jzvdStd_16_9.thumbImageView);
        jzvdStd_16_9.widthRatio = 16;
        jzvdStd_16_9.heightRatio = 9;

        jzvdStdVolumeAfterFullscreen = findViewById(R.id.jz_videoplayer_volume);
        jzvdStdVolumeAfterFullscreen.setUp(VideoConstant.videoUrls[0][1], "饺子摇摆", Jzvd.SCREEN_NORMAL);
        Glide.with(this)
                .load(VideoConstant.videoThumbs[0][1])
                .into(jzvdStdVolumeAfterFullscreen.thumbImageView);

        jzvdStdMp3 = findViewById(R.id.jz_videoplayer_mp3);
        jzvdStdMp3.setUp(VideoConstant.videoUrls[0][1],
                "饺子你听", Jzvd.SCREEN_NORMAL);
        Glide.with(this)
                .load(VideoConstant.videoThumbs[0][1])
                .into(jzvdStdMp3.thumbImageView);

        jzvdStdSpeed = findViewById(R.id.jz_videoplayer_speed);
        jzvdStdSpeed.setUp(VideoConstant.videoUrls[0][1],
                "饺子快点", Jzvd.SCREEN_NORMAL);
        Glide.with(this)
                .load(VideoConstant.videoThumbs[0][1])
                .into(jzvdStdSpeed.thumbImageView);

    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
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
