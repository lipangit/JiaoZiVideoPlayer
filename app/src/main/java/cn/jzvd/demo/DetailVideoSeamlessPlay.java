package cn.jzvd.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;

import com.bumptech.glide.Glide;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

import static cn.jzvd.Jzvd.SCREEN_FULLSCREEN;
import static cn.jzvd.Jzvd.STATE_AUTO_COMPLETE;
import static cn.jzvd.Jzvd.backPress;

/**
 * Created by yujunkui on 16/8/29.
 */
public class DetailVideoSeamlessPlay extends AppCompatActivity {
    private boolean isKeepPlaying;
    private FrameLayout playerContainer;
    private int currentPlayPosition;
    private JzvdStd videoPlayer;
    private String title, url, thumbUrl;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("DetailVideo");
        setContentView(R.layout.activity_detail_video);

        Intent intent = getIntent();
        currentPlayPosition = intent.getIntExtra("currentPlayPosition", 0);
        isKeepPlaying = getIntent().getBooleanExtra("isKeepPlaying", false);
        title = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        thumbUrl = getIntent().getStringExtra("thumbUrl");

        playerContainer = findViewById(R.id.container);
        videoPlayer = findViewById(R.id.video_player);

        initVideoPlayer();

    }

    private void initVideoPlayer() {
        // 无缝播放
        if (isKeepPlaying) {
            playerContainer.removeAllViews();
            JzvdStd player = (JzvdStd) Jzvd.CURRENT_JZVD;
            if (player != null) {
                player.attachToContainer(player, playerContainer);
                player.fullscreenButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (player.state == STATE_AUTO_COMPLETE) return;
                        //这里必须要传当前详情页的Context进去，因为当前的player是从列表页传过来的，
                        // 不传Context的话引用的Context仍然是列表页的，会引起功能失效。
                        if (player.screen == SCREEN_FULLSCREEN) {
                            player.backPress(DetailVideoSeamlessPlay.this);
                        } else {
                            player.gotoScreenFullscreenForResumePlay(DetailVideoSeamlessPlay.this);
                        }
                    }
                });
                player.backButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (player.screen == SCREEN_FULLSCREEN) {
                            player.backPress(DetailVideoSeamlessPlay.this);
                        }
                    }
                });
            }
            //正常播放
        } else {
            videoPlayer.setUp(url, title, Jzvd.SCREEN_NORMAL);
            Glide.with(DetailVideoSeamlessPlay.this).load(thumbUrl).into(videoPlayer.thumbImageView);
            videoPlayer.startVideo();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        JzvdStd.goOnPlayOnPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        JzvdStd.goOnPlayOnResume();
    }


    @Override
    public void onBackPressed() {
        //无缝播放的返回
        if (isKeepPlaying) {
            if (backPress(DetailVideoSeamlessPlay.this)) {
                return;
            }
            //这里可以用事件总线来做，现在一个就用sendBroadcast了
            Intent intent = new Intent(ActivityRecyclerViewSeamlessPlay.notification_detail_video_back_to_list);
            intent.putExtra("currentPlayPosition", currentPlayPosition);
            sendBroadcast(intent);
            //正常播放的返回
        } else {
            if (backPress()) {
                return;
            }
        }
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //无缝播放回到列表页之前不能停止视频
        if (!isKeepPlaying) {
            Jzvd.releaseAllVideos();
        }
    }

}
