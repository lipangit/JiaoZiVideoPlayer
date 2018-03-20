package cn.jzvd.demo.mute;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;

import cn.jzvd.JZMediaInterface;
import cn.jzvd.JZMediaManager;
import cn.jzvd.JZUtils;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.demo.R;
import cn.jzvd.demo.mute.mediaInterface.ijk.JZMediaIjkplayerMute;
import cn.jzvd.demo.mute.mediaInterface.system.JZMediaSystemMute;

/**
 * 基于ActivityListViewRecyclerView 增加静音播放
 * by wangshiwei on 18/3/20.
 */
public class ActivityListViewRecyclerViewMute extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterRecyclerViewVideoMute adapterVideoList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("RecyclerView");
        setContentView(R.layout.activity_recyclerview_content);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapterVideoList = new AdapterRecyclerViewVideoMute(this);
        recyclerView.setAdapter(adapterVideoList);
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                JZVideoPlayer jzvd = view.findViewById(R.id.videoplayer);
                if (jzvd != null && JZUtils.dataSourceObjectsContainsUri(jzvd.dataSourceObjects, JZMediaManager.getCurrentDataSource())) {
                    // if(JZVideoPlayerManager.getCurrentJzvd().currentScreen != JZVideoPlayer.SCREEN_WINDOW_FULLSCREEN){
                    if (jzvd.currentScreen != JZVideoPlayer.SCREEN_WINDOW_FULLSCREEN) {
                        JZVideoPlayer.releaseAllVideos();
                    }
                }
            }
        });

        //
        // JZMediaManager内部默认设置了一个JZMediaSystem，但是并不支持静音功能
        // 设置一个此处设置一个支持静音的子类播放器引擎
        JZMediaManager jzMediaManager = JZMediaManager.instance();
        JZMediaInterface jzMediaInterface = jzMediaManager.jzMediaInterface;

        JZMediaSystemMute systemInterface = JZMediaSystemMute.getInstance();
        JZMediaIjkplayerMute ijkplayerInterface = JZMediaIjkplayerMute.getInstance();

        if (!(jzMediaInterface.equals(systemInterface)) || !(jzMediaInterface.equals(ijkplayerInterface))) {
            JZVideoPlayer.setMediaInterface(systemInterface);
        }
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