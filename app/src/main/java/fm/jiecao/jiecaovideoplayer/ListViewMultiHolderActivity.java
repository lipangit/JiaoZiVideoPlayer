package fm.jiecao.jiecaovideoplayer;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by Nathen
 * On 2016/05/23 21:34
 */
public class ListViewMultiHolderActivity extends AppCompatActivity {
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview_content);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("MultiHolderListView");


        listView = (ListView) findViewById(R.id.listview);
        listView.setAdapter(new VideoListAdapter(this));
    }


    public class VideoListAdapter extends BaseAdapter {

        int[] videoIndexs = {0, 1, 1, 1, 1, 0, 1, 0, 1, 1, 0, 0, 1};//1 = jcvd, 0 = textView
        Context        context;
        LayoutInflater mInflater;

        public VideoListAdapter(Context context) {
            this.context = context;
            mInflater = LayoutInflater.from(context);
        }

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
            //This is the point
            if (convertView != null && convertView.getTag() != null && convertView.getTag() instanceof VideoHolder) {
                ((VideoHolder) convertView.getTag()).jcVideoPlayer.release();
            }


            if (videoIndexs[position] == 1) {
                VideoHolder viewHolder;
                if (convertView != null && convertView.getTag() != null && convertView.getTag() instanceof VideoHolder) {
                    viewHolder = (VideoHolder) convertView.getTag();
                } else {
                    viewHolder = new VideoHolder();
                    convertView = mInflater.inflate(R.layout.item_videoview, null);
                    viewHolder.jcVideoPlayer = (JCVideoPlayerStandard) convertView.findViewById(R.id.videoplayer);
                    convertView.setTag(viewHolder);
                }

                boolean setUp = viewHolder.jcVideoPlayer.setUp(
                        "http://gslb.miaopai.com/stream/ed5HCfnhovu3tyIQAiv60Q__.mp4", JCVideoPlayer.SCREEN_LAYOUT_LIST,
                        "嫂子快躲起来");
                if (setUp) {
                    ImageLoader.getInstance().displayImage("http://img4.jiecaojingxuan.com/2016/3/14/2204a578-609b-440e-8af7-a0ee17ff3aee.jpg",
                            viewHolder.jcVideoPlayer.thumbImageView);
                }
            } else {

                TextViewHolder textViewHolder;
                if (convertView != null && convertView.getTag() != null && convertView.getTag() instanceof TextViewHolder) {
                    textViewHolder = (TextViewHolder) convertView.getTag();
                } else {
                    textViewHolder = new TextViewHolder();
                    LayoutInflater mInflater = LayoutInflater.from(context);
                    convertView = mInflater.inflate(R.layout.item_textview, null);
                    textViewHolder.textView = (TextView) convertView.findViewById(R.id.textview);
                    convertView.setTag(textViewHolder);
                }

            }
            return convertView;
        }

        class VideoHolder {
            JCVideoPlayerStandard jcVideoPlayer;
        }

        class TextViewHolder {
            TextView textView;
        }
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
}
