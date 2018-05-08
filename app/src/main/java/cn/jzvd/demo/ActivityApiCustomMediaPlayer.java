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
import java.util.LinkedHashMap;

import cn.jzvd.JZMediaSystem;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import cn.jzvd.demo.CustomMediaPlayer.CustomMediaPlayerAssertFolder;
import cn.jzvd.demo.CustomMediaPlayer.JZExoPlayer;
import cn.jzvd.demo.CustomMediaPlayer.JZMediaIjkplayer;

/**
 * Created by Nathen on 2017/11/23.
 */

public class ActivityApiCustomMediaPlayer extends AppCompatActivity implements View.OnClickListener {
    Button mChangeToIjk, mChangeToSystemMediaPlayer, mChangeToExo;
    JZVideoPlayerStandard jzVideoPlayerStandard;
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

        jzVideoPlayerStandard = findViewById(R.id.videoplayer);
        mChangeToIjk = findViewById(R.id.change_to_ijkplayer);
        mChangeToSystemMediaPlayer = findViewById(R.id.change_to_system_mediaplayer);
        mChangeToExo = findViewById(R.id.change_to_exo);

        mChangeToIjk.setOnClickListener(this);
        mChangeToSystemMediaPlayer.setOnClickListener(this);
        mChangeToExo.setOnClickListener(this);

        LinkedHashMap map = new LinkedHashMap();
        try {
            map.put(JZVideoPlayer.URL_KEY_DEFAULT, getAssets().openFd("local_video.mp4"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        Object[] dataSourceObjects = new Object[2];
        dataSourceObjects[0] = map;
        dataSourceObjects[1] = this;
        jzVideoPlayerStandard.setUp(dataSourceObjects, 0, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "饺子快长大");
        Glide.with(this)
                .load("http://jzvd-pic.nathen.cn/jzvd-pic/1bb2ebbe-140d-4e2e-abd2-9e7e564f71ac.png")
                .into(jzVideoPlayerStandard.thumbImageView);

        JZVideoPlayer.setMediaInterface(new CustomMediaPlayerAssertFolder());//进入此页面修改MediaInterface，让此页面的jzvd正常工作
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_to_ijkplayer:
                JZVideoPlayer.releaseAllVideos();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        JZVideoPlayer.setMediaInterface(new JZMediaIjkplayer());
                    }
                }, 1000);
                Toast.makeText(ActivityApiCustomMediaPlayer.this, "Change to Ijkplayer", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.change_to_system_mediaplayer:
                JZVideoPlayer.releaseAllVideos();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        JZVideoPlayer.setMediaInterface(new JZMediaSystem());
                    }
                }, 1000);
                Toast.makeText(this, "Change to MediaPlayer", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.change_to_exo:
                JZVideoPlayer.releaseAllVideos();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        JZVideoPlayer.setMediaInterface(new JZExoPlayer());
                    }
                }, 1000);
                Toast.makeText(this, "Change to ExoPlayer", Toast.LENGTH_SHORT).show();
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        JZVideoPlayer.releaseAllVideos();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                JZVideoPlayer.setMediaInterface(new JZMediaSystem());
            }
        }, 1000);
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                JZVideoPlayer.releaseAllVideos();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        JZVideoPlayer.setMediaInterface(new JZMediaSystem());
                    }
                }, 1000);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
