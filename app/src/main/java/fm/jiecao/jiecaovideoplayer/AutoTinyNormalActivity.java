package fm.jiecao.jiecaovideoplayer;

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

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

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

        JCVideoPlayerStandard jcVideoPlayerStandard = (JCVideoPlayerStandard) headerLayout.findViewById(R.id.jc_video);
        jcVideoPlayerStandard.setUp("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4"
                , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "嫂子坐这");
        Picasso.with(this)
                .load("http://cos.myqcloud.com/1000264/qcloud_video_attachment/842646334/vod_cover/cover1458036374.jpg")
                .into(jcVideoPlayerStandard.thumbImageView);

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
        if (JCVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
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
//        JCVideoPlayer.onScroll(firstVisibleItem, visibleItemCount, totalItemCount);
    }
}
