package cn.jzvd.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by Nathen on 2017/11/1.
 */

public class ActivityTinyWindowRecycleViewMultiHolder extends AppCompatActivity {

    RecyclerView recyclerView;
    AdapterRecyclerView videoAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("RecycleViewMultiHolderTinyWindow");
        setContentView(R.layout.activity_recyclerview_content);

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        videoAdapter = new AdapterRecyclerView();
        recyclerView.setAdapter(videoAdapter);
        recyclerView.addOnChildAttachStateChangeListener(new RecyclerView.OnChildAttachStateChangeListener() {
            @Override
            public void onChildViewAttachedToWindow(View view) {
                JZVideoPlayer.onChildViewAttachedToWindow(view, R.id.videoplayer);
            }

            @Override
            public void onChildViewDetachedFromWindow(View view) {
                JZVideoPlayer.onChildViewDetachedFromWindow(view);
            }
        });

    }

    public class AdapterRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        @Override
        public int getItemViewType(int position) {
            if (position != 4 && position != 7) {
                return 0;//非视频
            }
            return 1;//视频
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view;
            if (viewType == 1) {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_videoview, parent, false);
                return new VideoHolder(view);
            } else {
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_textview, parent, false);
                return new TextHolder(view);
            }
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            if (position != 4 && position != 7) {
                TextHolder textHolder = (TextHolder) holder;
            } else {
                VideoHolder videoHolder = (VideoHolder) holder;
                videoHolder.jzVideoPlayer.setUp(
                        VideoConstant.videoUrls[0][position], JZVideoPlayer.SCREEN_WINDOW_LIST,
                        VideoConstant.videoTitles[0][position]);
                videoHolder.jzVideoPlayer.positionInList = position;
                Picasso.with(ActivityTinyWindowRecycleViewMultiHolder.this)
                        .load(VideoConstant.videoThumbs[0][position])
                        .into(videoHolder.jzVideoPlayer.thumbImageView);
            }
        }

        @Override
        public int getItemCount() {
            return 10;
        }

        class VideoHolder extends RecyclerView.ViewHolder {
            JZVideoPlayerStandard jzVideoPlayer;

            public VideoHolder(View itemView) {
                super(itemView);
                jzVideoPlayer = itemView.findViewById(R.id.videoplayer);
            }
        }

        class TextHolder extends RecyclerView.ViewHolder {
            TextView textView;

            public TextHolder(View itemView) {
                super(itemView);
                textView = itemView.findViewById(R.id.textview);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        JZVideoPlayer.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
            return;
        }
        super.onBackPressed();
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
