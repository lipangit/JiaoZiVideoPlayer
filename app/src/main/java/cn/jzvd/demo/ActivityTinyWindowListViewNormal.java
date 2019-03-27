package cn.jzvd.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

import cn.jzvd.Jzvd;

/**
 * Created by Nathen on 2017/10/22.
 */

public class ActivityTinyWindowListViewNormal extends AppCompatActivity {

    ListView listView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("NormalListViewTinyWindow");
        setContentView(R.layout.activity_listview_normal_auto_tiny);

        listView = findViewById(R.id.listview);
        listView.setAdapter(new AdapterVideoList(this,
                VideoConstant.videoUrls[0],
                VideoConstant.videoTitles[0],
                VideoConstant.videoThumbs[0]));

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                int lastVisibleItem = firstVisibleItem + visibleItemCount;
                Jzvd jzvd = Jzvd.CURRENT_JZVD;
                if (jzvd == null) return;
                int currentPlayPosition = jzvd.positionInList;
                if (currentPlayPosition >= 0) {
                    if ((currentPlayPosition < firstVisibleItem || currentPlayPosition > (lastVisibleItem - 1))) {
                        if (jzvd.currentState == Jzvd.CURRENT_STATE_PAUSE) {
                            Jzvd.resetAllVideos();
                        } else {
                            Log.e("jzvd", "onScroll: out screen");
                            if (jzvd.currentScreen != Jzvd.SCREEN_WINDOW_TINY) {
                                jzvd.gotoScreenTiny();
                            }
                        }
                    } else {
                        if (jzvd.currentScreen == Jzvd.SCREEN_WINDOW_TINY) {
                            Log.e("jzvd", "onScroll: into screen");
                            Jzvd.backPress();
                        }
                    }
                }
            }
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
