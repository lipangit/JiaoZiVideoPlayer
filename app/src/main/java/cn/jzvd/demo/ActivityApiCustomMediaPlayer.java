package cn.jzvd.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.LinkedHashMap;

import cn.jzvd.JZMediaSystem;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;
import cn.jzvd.demo.CustomMediaPlayer.CustomMediaPlayerAssertFolder;
import cn.jzvd.demo.CustomMediaPlayer.JZMediaIjkplayer;

/**
 * Created by Nathen on 2017/11/23.
 */

public class ActivityApiCustomMediaPlayer extends AppCompatActivity implements View.OnClickListener {
    Button mChangeToIjk, mChangeToSystemMediaPlayer;
    JZVideoPlayerStandard jzVideoPlayerStandard;

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

        mChangeToIjk.setOnClickListener(this);
        mChangeToSystemMediaPlayer.setOnClickListener(this);

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
        Picasso.with(this)
                .load("http://jzvd-pic.nathen.cn/jzvd-pic/1bb2ebbe-140d-4e2e-abd2-9e7e564f71ac.png")
                .into(jzVideoPlayerStandard.thumbImageView);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change_to_ijkplayer:
                JZVideoPlayer.setMediaInterface(new JZMediaIjkplayer());
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                break;
            case R.id.change_to_system_mediaplayer:
                JZVideoPlayer.setMediaInterface(new JZMediaSystem());
                Toast.makeText(this, "success", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        JZVideoPlayer.setMediaInterface(new CustomMediaPlayerAssertFolder());//进入此页面修改MediaInterface，让此页面的jzvd正常工作
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
        JZVideoPlayer.setMediaInterface(new JZMediaSystem());
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
