package cn.jzvd.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.bumptech.glide.Glide;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

/**
 * Created by Nathen on 2017/11/2.
 */

public class ActivityApiRotationVideoSize extends AppCompatActivity {

    JzvdStd myJzvdStd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("RotationAndVideoSize");
        setContentView(R.layout.activity_api_rotation_videosize);

        myJzvdStd = findViewById(R.id.jz_video);
        myJzvdStd.setUp(VideoConstant.videoUrls[0][7], VideoConstant.videoTitles[0][7]
                , JzvdStd.SCREEN_WINDOW_NORMAL);
        Glide.with(this)
                .load(VideoConstant.videoThumbs[0][7])
                .into(myJzvdStd.thumbImageView);
        // The Point IS
        myJzvdStd.videoRotation = 180;
    }


    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_ADAPTER);
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        super.onBackPressed();
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

    public void clickRotationToNinety(View view) {
        Jzvd.setTextureViewRotation(90);
    }

    public void clickVideoImageDisplayFillParent(View view) {
        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_PARENT);
    }

    public void clickVideoImageDisplayFillCrop(View view) {
        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_FILL_SCROP);
    }

    public void clickVideoImageDiaplayOriginal(View view) {
        Jzvd.setVideoImageDisplayType(Jzvd.VIDEO_IMAGE_DISPLAY_TYPE_ORIGINAL);
    }
}
