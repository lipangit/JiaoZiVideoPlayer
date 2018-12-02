package cn.jzvd.demo;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.IOException;

import cn.jzvd.JZDataSource;
import cn.jzvd.JZMediaSystem;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import cn.jzvd.demo.CustomMediaPlayer.CustomMediaPlayerAssertFolder;
import cn.jzvd.demo.CustomMediaPlayer.JZExoPlayer;
import cn.jzvd.demo.CustomMediaPlayer.JZMediaIjkplayer;

/**
 * Created by Nathen on 2017/11/23.
 */

public class ActivityApiCustomMediaPlayer extends AppCompatActivity {
    JzvdStd jzvdStd;
    Handler handler = new Handler();//这里其实并不需要handler，为了防止播放中切换播放器引擎导致的崩溃，实际使用时一般不会遇到，可以随时调用JZVideoPlayer.setMediaInterface();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("CustomMediaPlayer");
        setContentView(R.layout.activity_api_custom_mediaplayer);

        jzvdStd = findViewById(R.id.videoplayer);

        JZDataSource jzDataSource = null;
        try {
            jzDataSource = new JZDataSource(getAssets().openFd("local_video.mp4"));
            jzDataSource.title = "饺子快长大";
        } catch (IOException e) {
            e.printStackTrace();
        }
        jzvdStd.setUp(jzDataSource, JzvdStd.SCREEN_WINDOW_NORMAL);
        Glide.with(this)
                .load("http://jzvd-pic.nathen.cn/jzvd-pic/1bb2ebbe-140d-4e2e-abd2-9e7e564f71ac.png")
                .into(jzvdStd.thumbImageView);

        Jzvd.setMediaInterface(new CustomMediaPlayerAssertFolder());//进入此页面修改MediaInterface，让此页面的jzvd正常工作
    }


    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
            return;
        }
        Jzvd.releaseAllVideos();
        handler.postDelayed(() -> Jzvd.setMediaInterface(new JZMediaSystem()), 1000);
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
                Jzvd.releaseAllVideos();
                handler.postDelayed(() -> Jzvd.setMediaInterface(new JZMediaSystem()), 1000);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    public void clickChangeToIjkplayer(View view) {
        Jzvd.releaseAllVideos();
        handler.postDelayed(() -> Jzvd.setMediaInterface(new JZMediaIjkplayer()), 1000);
        Toast.makeText(ActivityApiCustomMediaPlayer.this, "Change to Ijkplayer", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void clickChangeToSystemMediaPlayer(View view) {
        Jzvd.releaseAllVideos();
        handler.postDelayed(() -> Jzvd.setMediaInterface(new JZMediaSystem()), 1000);
        Toast.makeText(this, "Change to MediaPlayer", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void clickChangeToExo(View view) {
        Jzvd.releaseAllVideos();
        handler.postDelayed(() -> Jzvd.setMediaInterface(new JZExoPlayer()), 1000);
        Toast.makeText(this, "Change to ExoPlayer", Toast.LENGTH_SHORT).show();
        finish();
    }
}
