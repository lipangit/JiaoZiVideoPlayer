package fm.jiecao.jiecaovideoplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;

/**
 * Created by Nathen
 * On 2016/02/06 19:12
 */
public class ListActivity extends AppCompatActivity {
    ListView listView;
    String[] videoUrls = {"http://2449.vod.myqcloud.com/2449_43b6f696980311e59ed467f22794e792.f20.mp4",
            "http://2449.vod.myqcloud.com/2449_ded7b566b37911e5942f0b208e48548d.f20.mp4"};
    String[] videoThumbs = {"http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640",
            "http://p.qpic.cn/videoyun/0/2449_ded7b566b37911e5942f0b208e48548d_2/640"};
    String[] videoTitles = {"嫂子别摸我", "嫂子还摸我"};

    int[] videoIndexs = {0, 1, 1, 1, 1, 0, 1, 0, 1, 1, 0, 0, 1};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("列表中的视频");

        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(new MAdapter());
    }

    class MAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return videoIndexs.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (null == convertView) {
                viewHolder = new ViewHolder();
                LayoutInflater mInflater = LayoutInflater.from(ListActivity.this);
                convertView = mInflater.inflate(R.layout.item_list, null);
                viewHolder.jcVideoPlayer = (JCVideoPlayer) convertView.findViewById(R.id.videoplayer);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.jcVideoPlayer.setUp(
                    videoUrls[videoIndexs[position]],
                    videoThumbs[videoIndexs[position]],
                    videoTitles[videoIndexs[position]]);
            return convertView;
        }

        class ViewHolder {
            JCVideoPlayer jcVideoPlayer;
        }
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
