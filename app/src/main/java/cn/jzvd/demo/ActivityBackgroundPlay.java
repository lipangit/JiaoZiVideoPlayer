package cn.jzvd.demo;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * ClassName: ActivityBackgroundPlay <br/>
 * PackageName: cn.jzvd.demo <br/>
 * Create On: 8/7/18 8:42 PM <br/>
 * Site:https://github.XuQK <br/>
 * @author: XuQK <br/>
 */

public class ActivityBackgroundPlay extends AppCompatActivity {

    public static final String PLAYER_ACTION = "cn.jzvd.demo.ActivityBackgroundPlay.Notification";

    public static final String BUTTON_TAG = "cn.jzvd.demo.ActivityBackgroundPlay.Notification.buttonTag";
    public static final String PLAYER_PLAY = "cn.jzvd.demo.ActivityBackgroundPlay.Notification.play";
    public static final String PLAYER_PREVIOUS = "cn.jzvd.demo.ActivityBackgroundPlay.Notification.previous";
    public static final String PLAYER_NEXT = "cn.jzvd.demo.ActivityBackgroundPlay.Notification.next";
    public static final int PLAYER_NOTIFY_ID = 100;

    JZVideoPlayerStandard mPlayer;
    Button startBgPlay, stopBgPlay;
    boolean bgPlayEnable;
    Notification notify;
    RemoteViews remoteViews;
    int currentVideo;
    PlayerNotificationReceiver mPlayerReceiver;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_background_play);
        initNotificationChannelSys();

        mPlayer = findViewById(R.id.jz_video);
        startBgPlay = findViewById(R.id.open_background);
        startBgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!bgPlayEnable) {
                    showNotification();
                }

                startActivity(new Intent(Intent.ACTION_MAIN)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addCategory(Intent.CATEGORY_HOME));
            }
        });
        stopBgPlay = findViewById(R.id.close_background);
        stopBgPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bgPlayEnable = false;
                getManager().cancel(PLAYER_NOTIFY_ID);
            }
        });

        mPlayer.setUp(VideoConstant.videoUrlList[0], JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, VideoConstant.videoTitleList[0]);
        mPlayerReceiver = new PlayerNotificationReceiver();
    }

    @Override
    protected void onStart() {
        registerReceiver(mPlayerReceiver, new IntentFilter(PLAYER_ACTION));
        super.onStart();
    }

    @Override
    protected void onPause() {
        if (!bgPlayEnable) {
            JZVideoPlayer.releaseAllVideos();
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mPlayerReceiver);
        super.onDestroy();
    }

    public void showNotification() {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID_PLAYER);
        remoteViews = new RemoteViews(getPackageName(), R.layout.layout_notification_video_background_play);
        updateRemoteViewInfo();

        if (mPlayer.currentState == JZVideoPlayer.CURRENT_STATE_PLAYING) {
            remoteViews.setImageViewResource(R.id.iv_btn_play, R.drawable.app_ic_pause);
        } else {
            remoteViews.setImageViewResource(R.id.iv_btn_play, R.drawable.app_ic_play);
        }

        remoteViews.setImageViewResource(R.id.iv_btn_previous, R.drawable.app_ic_previous_video);
        remoteViews.setImageViewResource(R.id.iv_btn_next, R.drawable.app_ic_next_video);

        // 上一个按钮
        Intent previousIntent = new Intent(PLAYER_ACTION).putExtra(BUTTON_TAG, PLAYER_PREVIOUS);
        PendingIntent pIntentPrev = PendingIntent.getBroadcast(this, 1, previousIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.iv_btn_previous, pIntentPrev);

        // 播放/暂停一个按钮
        Intent playIntent = new Intent(PLAYER_ACTION).putExtra(BUTTON_TAG, PLAYER_PLAY);
        PendingIntent pIntentPlay = PendingIntent.getBroadcast(this, 2, playIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.iv_btn_play, pIntentPlay);

        // 下一个按钮
        Intent nextIntent = new Intent(PLAYER_ACTION).putExtra(BUTTON_TAG, PLAYER_NEXT);
        PendingIntent pIntentNext = PendingIntent.getBroadcast(this, 3, nextIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.iv_btn_next, pIntentNext);

        Intent resumeIntent = new Intent(this, ActivityBackgroundPlay.class);
        PendingIntent pIntentResume = PendingIntent.getActivity(this, 0, resumeIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        notify = builder.setContent(remoteViews)
                .setCustomBigContentView(remoteViews)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setColor(ContextCompat.getColor(this, R.color.colorPrimary))
                .setAutoCancel(false)
                .setContentIntent(pIntentResume)
                .setPriority(Notification.PRIORITY_MAX)
                .build();
        notify.flags = Notification.FLAG_ONGOING_EVENT;
        getManager().notify(PLAYER_NOTIFY_ID, notify);

        bgPlayEnable = true;
    }

    private void updateRemoteViewInfo() {
        if (remoteViews != null) {
            remoteViews.setTextViewText(R.id.tv_title, VideoConstant.videoTitleList[currentVideo]);
        }
    }

    public static final String CHANNEL_ID_PLAYER = "player";
    public static final String CHANNEL_LABEL_PLAYER = "JZ后台播放";

    public void initNotificationChannelSys() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            List<NotificationChannel> channels = new ArrayList<>();

            NotificationChannel playerChannel = new NotificationChannel(CHANNEL_ID_PLAYER,
                    CHANNEL_LABEL_PLAYER, NotificationManager.IMPORTANCE_DEFAULT);
            playerChannel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            channels.add(playerChannel);

            getManager().createNotificationChannels(channels);
        }
    }

    public NotificationManager getManager() {
        return (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
    }

    /**
     * 切换剧集，成功返回true
     * @param sort
     * @return
     */
    public boolean changeVideo(int sort) {
        if (sort < 0 || sort >= VideoConstant.videoUrlList.length) {
            return false;
        }

        currentVideo = sort;
        String url = VideoConstant.videoUrlList[sort];
        String name = VideoConstant.videoTitleList[sort];
        mPlayer.onCompletion();

        JZVideoPlayer.clearSavedProgress(this, url);
        mPlayer.setUp(url, JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, name);
        mPlayer.startButton.performClick();

        updateRemoteViewInfo();
        getManager().notify(PLAYER_NOTIFY_ID, notify);
        return true;
    }

    private void changePlayState() {
        if (mPlayer.currentState == JZVideoPlayer.CURRENT_STATE_PLAYING) {
            remoteViews.setImageViewResource(R.id.iv_btn_play, R.drawable.app_ic_pause);
        } else {
            remoteViews.setImageViewResource(R.id.iv_btn_play, R.drawable.app_ic_play);
        }

        if (bgPlayEnable) {
            getManager().notify(PLAYER_NOTIFY_ID, notify);
        }
    }

    class PlayerNotificationReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String tag = intent.getStringExtra(BUTTON_TAG);
            switch (tag) {
                case PLAYER_PREVIOUS:
                    if (!changeVideo(currentVideo - 1)) {
                        Toast.makeText(context, "已经是第一个了呢", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case PLAYER_PLAY:
                    mPlayer.startButton.performClick();
                    changePlayState();
                    break;
                case PLAYER_NEXT:
                    if (!changeVideo(currentVideo + 1)) {
                        Toast.makeText(context, "已经是最后一个啦", Toast.LENGTH_SHORT).show();
                    }
                    break;
                default:
            }
        }
    }
}
