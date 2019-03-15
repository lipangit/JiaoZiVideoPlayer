package cn.jzvd.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import cn.jzvd.Jzvd;

/**
 * Created by Nathen on 2017/11/1.
 */

public class ActivityTinyWindowRecycleView extends AppCompatActivity {
    RecyclerView recyclerView;
    AdapterRecyclerViewVideo adapterVideoList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("RecyclerViewTinyWindow");
        setContentView(R.layout.activity_recyclerview_content);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapterVideoList = new AdapterRecyclerViewVideo(this);
        recyclerView.setAdapter(adapterVideoList);
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                if (Jzvd.CONTAINER_LIST.size() == 0) return;
                int vp = ((ViewGroup) view).indexOfChild(Jzvd.CONTAINER_LIST.getLast());
                System.out.println("fdsfdsafds 1");
                if (vp != -1 && Jzvd.CURRENT_JZVD != null) {
                    if (Jzvd.CURRENT_JZVD.currentScreen == Jzvd.SCREEN_WINDOW_TINY) {
                        System.out.println("fdsfdsafds 3");
                        Jzvd.backPress();
                    }
                }
//                Jzvd.onChildViewAttachedToWindow(view, R.id.videoplayer);
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                Jzvd jzvd = view.findViewById(R.id.videoplayer);
                if (jzvd != null && Jzvd.CURRENT_JZVD != null &&
                        jzvd.jzDataSource.containsTheUrl(Jzvd.CURRENT_JZVD.jzDataSource.getCurrentUrl())
                        && Jzvd.CURRENT_JZVD.currentScreen != Jzvd.SCREEN_WINDOW_TINY) {
                    if (Jzvd.CURRENT_JZVD.currentState == Jzvd.CURRENT_STATE_PAUSE) {
                        Jzvd.resetAllVideos();
                    } else {
                        Jzvd.CURRENT_JZVD.gotoScreenTiny();
                    }
                }
            }

//                Jzvd.onChildViewDetachedFromWindow(view, R.id.videoplayer);
        });

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
        Jzvd.resetAllVideos();
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
