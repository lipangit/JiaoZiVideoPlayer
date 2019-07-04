package cn.jzvd.demo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

import static cn.jzvd.Jzvd.backPress;

/**
 * Created by yujunkui on 16/8/29.
 */
public class ActivityRecyclerViewSeamlessPlay extends AppCompatActivity implements AdapterRecyclerViewSeamlessPlay.OnListListener {

    public static final String notification_detail_video_back_to_list = "notification_detail_video_back_to_list";
    RecyclerView recyclerView;
    AdapterRecyclerViewSeamlessPlay adapterVideoList;
    private boolean isKeepPlayingToDetail = false;  //无缝播放使用
    private DetailVideoBackReceiver detailVideoBackReceiver;
    private Handler handler = new Handler();
    private LinearLayoutManager linearLayoutManager;
    private int screenWidth, screenHeight;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("RecyclerView");
        setContentView(R.layout.activity_recyclerview_content);

        screenWidth = JZUtils.getScreenWidth(this);
        screenHeight = JZUtils.getScreenHeight(this);

        recyclerView = findViewById(R.id.recyclerview);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);

        adapterVideoList = new AdapterRecyclerViewSeamlessPlay(this);
        recyclerView.setAdapter(adapterVideoList);
        adapterVideoList.setOnListListener(this);

        //这样判断更严谨，video的部分一划出界面就释放资源.
        recyclerView.addOnScrollListener(scrollListener);
        initVideoBackReceiver();
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

    @Override
    public void onInfoClick(String title,String url, String thumbUrl,int position) {
        if (Jzvd.CURRENT_JZVD == null) {
            Intent intent = new Intent(this, DetailVideoSeamlessPlay.class);
            intent.putExtra("url", url);
            startActivity(intent);
        } else {
            isKeepPlayingToDetail = true;
            Intent intent = new Intent(this, DetailVideoSeamlessPlay.class);
            intent.putExtra("currentPlayPosition", position);
            intent.putExtra("isKeepPlaying", true);
            intent.putExtra("title", title);
            intent.putExtra("url", url);
            intent.putExtra("thumbUrl", thumbUrl);
            startActivity(intent);
        }
    }

    private RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
            if (Jzvd.CURRENT_JZVD != null) {
                if (!JZUtils.isVisible(screenWidth, screenHeight,Jzvd.CURRENT_JZVD)) {
                    Jzvd.releaseAllVideos();
                }
            }
        }
    };

    private void initVideoBackReceiver() {
        //不判断可能会接收多次
        if (detailVideoBackReceiver == null) {
            detailVideoBackReceiver = new DetailVideoBackReceiver();
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(notification_detail_video_back_to_list);
            registerReceiver(detailVideoBackReceiver, intentFilter);
        }
    }

    class DetailVideoBackReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            int currentPlayPosition = intent.getIntExtra("currentPlayPosition", -1);
            onPlayVideoBack(currentPlayPosition);
        }
    }

    //detail video back回调
    public void onPlayVideoBack(int currentPlayPosition) {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int first = linearLayoutManager.findFirstVisibleItemPosition();
                View view = recyclerView.getChildAt(currentPlayPosition - first);
                if (view != null) {
                    FrameLayout playerContainer = view.findViewById(R.id.container);
                    playerContainer.removeAllViews();
                    JzvdStd player = (JzvdStd) Jzvd.CURRENT_JZVD;
                    //这种情况是该视频在详情页正在播
                    if (player != null) {
                        player.attachToContainer(player,playerContainer);
                        //下面这行注释掉可在列表页继续无缝播放，但是全屏的时候由于该视频现在是详情页传过来的，
                        //现在引用的是详情页的Context，所以还会有问题，还需要切换Context。
                        Jzvd.releaseAllVideos();
                        //这种情况是该视频在详情页播完了
                    }else {
                        recyclerView.setAdapter(adapterVideoList);
                    }
                }
            }
        }, 500);
    }

    @Override
    public void onPause() {
        super.onPause();
        //无缝播放
        if (!isKeepPlayingToDetail) {
            JzvdStd.goOnPlayOnPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        isKeepPlayingToDetail = false;
        JzvdStd.goOnPlayOnResume();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
        Jzvd.releaseAllVideos();
        if (recyclerView != null) {
            recyclerView.removeOnScrollListener(scrollListener);
        }
        if (detailVideoBackReceiver != null) {
            unregisterReceiver(detailVideoBackReceiver);
        }
    }

    @Override
    public void onBackPressed() {
        if (backPress()) {
            return;
        }
        super.onBackPressed();
    }
}
