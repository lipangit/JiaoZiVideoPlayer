package cn.jzvd.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by Nathen on 16/7/31.
 */
public class PlayDirectlyActivity extends AppCompatActivity implements View.OnClickListener {
    Button mStartFullscreen, mStartTiny;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("PlayDirectlyWithoutLayout");
        setContentView(R.layout.activity_directly_play);

        mStartFullscreen = (Button) findViewById(R.id.fullscreen);
        mStartTiny = (Button) findViewById(R.id.tiny_window);

        mStartFullscreen.setOnClickListener(this);
        mStartTiny.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fullscreen:
                JZVideoPlayerStandard.startFullscreen(this, JZVideoPlayerStandard.class, VideoConstant.videoUrlList[6], "嫂子辛苦了");
                break;
            case R.id.tiny_window:
                Toast.makeText(PlayDirectlyActivity.this, "Comming Soon", Toast.LENGTH_SHORT).show();
                break;
        }
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

}
