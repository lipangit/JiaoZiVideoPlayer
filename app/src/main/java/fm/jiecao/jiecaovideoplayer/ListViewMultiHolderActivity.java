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
import com.squareup.picasso.Picasso;

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

        int[] viewtype = {0, 1, 0, 1, 0, 1, 1, 0, 0, 1};//1 = jcvd, 0 = textView

        Context        context;
        LayoutInflater mInflater;

        public VideoListAdapter(Context context) {
            this.context = context;
            mInflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            return viewtype.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //This is the point

            if (getItemViewType(position) == 1) {
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
                        VideoConstant.videoUrls[position], JCVideoPlayer.SCREEN_LAYOUT_LIST,
                        VideoConstant.videoTitles[position]);
                if (setUp) {
                    Picasso.with(ListViewMultiHolderActivity.this)
                            .load(VideoConstant.videoThumbs[position])
                            .into(viewHolder.jcVideoPlayer.thumbImageView);
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

        @Override
        public int getItemViewType(int position) {
            return viewtype[position];
        }

        @Override
        public int getViewTypeCount() {
            return 2;
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
