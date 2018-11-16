package cn.jzvd.demo;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import cn.jzvd.JZMediaManager;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdMgr;
import cn.jzvd.JzvdStd;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_IDLE;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

public class ActivityRecyclerViewAutoPlay extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterRecyclerViewVideo adapterVideoList;
    int firstVisibleItem, lastVisibleItem, visibleCount;
    LinearLayoutManager layoutManager;

    /**
     *   这里只实现了 滑动自动播放、
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view_auto_play);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("RecyclerViewAutoPlay");
        recyclerView = findViewById(R.id.recyclerview);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapterVideoList = new AdapterRecyclerViewVideo(this);
        recyclerView.setAdapter(adapterVideoList);
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {

            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                Jzvd jzvd = view.findViewById(R.id.videoplayer);
                if (jzvd != null && jzvd.jzDataSource.containsTheUrl(JZMediaManager.getCurrentUrl())) {
                    Jzvd currentJzvd = JzvdMgr.getCurrentJzvd();
                    if (currentJzvd != null && currentJzvd.currentScreen != Jzvd.SCREEN_WINDOW_FULLSCREEN) {
                        Jzvd.releaseAllVideos();
                    }
                }
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case SCROLL_STATE_IDLE: //滚动停止
                        autoPlayVideo(recyclerView);
                        break;
                    case SCROLL_STATE_DRAGGING: //手指拖动
                        break;
                    case SCROLL_STATE_SETTLING: //惯性滚动
                        break;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                firstVisibleItem = layoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = layoutManager.findLastVisibleItemPosition();
                visibleCount = lastVisibleItem - firstVisibleItem;
                //获取可见的最后一个view
                View lastChildView = recyclerView.getChildAt(
                        recyclerView.getChildCount() - 1);

                //获取可见的最后一个view的位置
                int lastChildViewPosition = recyclerView.getChildAdapterPosition(lastChildView);

                //判断lastPosition是不是最后一个position
                if (lastChildViewPosition == recyclerView.getLayoutManager().getItemCount() - 1) {
                    Toast.makeText(ActivityRecyclerViewAutoPlay.this, "滑动到底部了", Toast.LENGTH_SHORT).show();
                    //本来想进一步完善的 以后吧列表自动播放下一个bug解决完后一起写完
                    //这里存在的问题的当item全部加载完成后 第一个可见item 播放完成后不会播放身下的几个了 解决思路是 上面那个为题
                    //也有其他解决方式就是 记录当前播放item 播放完成后 在跳博放下一个
                }

            }

        });
    }

    private void autoPlayVideo(RecyclerView recyclerView) {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        for (int i = 0; i < visibleCount; i++) {
            if (layoutManager != null && layoutManager.getChildAt(i) != null && layoutManager.getChildAt(i).findViewById(R.id.videoplayer) != null) {
                JzvdStd jzvdStd = layoutManager.getChildAt(i).findViewById(R.id.videoplayer);
                Rect rect = new Rect();
                jzvdStd.getLocalVisibleRect(rect);
                int videoHeight = jzvdStd.getHeight();
                if (rect.top == 0 && rect.bottom == videoHeight) {
                    if (jzvdStd.currentState != jzvdStd.CURRENT_STATE_PLAYING) {  // 这里是为了解决  轻微滑动 会执行 jzvdStd.startButton.performClick(); 视频暂停  这里应该还有其他的判断条件根据项目实际情况来
                        jzvdStd.startButton.performClick();
                    }
                    return;
                }
            }
        }
        Jzvd.releaseAllVideos();
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