package cn.jzvd.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by Nathen on 16/8/23.
 */
public class AutoTinyNormalActivity extends AppCompatActivity implements AbsListView.OnScrollListener {
    ListView listView;
    LinearLayout headerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("AutoTinyNormal");
        setContentView(R.layout.activity_listview_content);

        listView = (ListView) findViewById(R.id.listview);
        headerLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.header_auto_tiny_normal, null);
        listView.addHeaderView(headerLayout);

        JZVideoPlayerStandard jzVideoPlayerStandard = (JZVideoPlayerStandard) headerLayout.findViewById(R.id.jz_video);
        jzVideoPlayerStandard.setUp(VideoConstant.videoUrlList[6]
                , JZVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "嫂子坐这");
        Picasso.with(this)
                .load(VideoConstant.videoThumbList[6])
                .into(jzVideoPlayerStandard.thumbImageView);

        Map<String, String> keyValuePair = new HashMap<>();
        keyValuePair.put("key", "list item");
        List<Map<String, String>> list = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            list.add(keyValuePair);
        }

        ListAdapter adapter = new SimpleAdapter(this, list,
                android.R.layout.simple_list_item_1, new String[]{"key"}, new int[]{android.R.id.text1});

        listView.setAdapter(adapter);
        listView.setOnScrollListener(this);
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

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
//        JZVideoPlayer.onScroll(firstVisibleItem, visibleItemCount, totalItemCount);
    }
}
