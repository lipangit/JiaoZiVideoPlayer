package cn.jzvd.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.squareup.picasso.Picasso;

import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by Nathen on 2017/10/31.
 */

public class ActivityTinyWindow extends AppCompatActivity implements View.OnClickListener {

    JZVideoPlayerStandard mJzVideoPlayerStandard;
    Button mBtnTinyWindow, mBtnTinyWindowListView, mBtnTinyWindowListViewMultiHolder, mBtnTinyWindowRecycle, mBtnTinyWindowRecycleMultiHolder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("TinyWindow");
        setContentView(R.layout.activity_tiny_window);

        mJzVideoPlayerStandard = findViewById(R.id.jz_video);
        mJzVideoPlayerStandard.setUp("http://jzvd.nathen.cn/342a5f7ef6124a4a8faf00e738b8bee4/cf6d9db0bd4d41f59d09ea0a81e918fd-5287d2089db37e62345123a1be272f8b.mp4"
                , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "饺子快长大");
        Picasso.with(this)
                .load("http://jzvd-pic.nathen.cn/jzvd-pic/1bb2ebbe-140d-4e2e-abd2-9e7e564f71ac.png")
                .into(mJzVideoPlayerStandard.thumbImageView);

        mBtnTinyWindow = findViewById(R.id.tiny_window);
        mBtnTinyWindowListView = findViewById(R.id.auto_tiny_list_view);
        mBtnTinyWindowListViewMultiHolder = findViewById(R.id.auto_tiny_list_view_multi_holder);
        mBtnTinyWindowRecycle = findViewById(R.id.auto_tiny_list_view_recycleview);
        mBtnTinyWindowRecycleMultiHolder = findViewById(R.id.auto_tiny_list_view_recycleview_multiholder);
        mBtnTinyWindow.setOnClickListener(this);
        mBtnTinyWindowListView.setOnClickListener(this);
        mBtnTinyWindowListViewMultiHolder.setOnClickListener(this);
        mBtnTinyWindowRecycle.setOnClickListener(this);
        mBtnTinyWindowRecycleMultiHolder.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tiny_window:
                mJzVideoPlayerStandard.startWindowTiny();
                break;
            case R.id.auto_tiny_list_view:
                startActivity(new Intent(this, ActivityTinyWindowListViewNormal.class));
                break;
            case R.id.auto_tiny_list_view_multi_holder:
                startActivity(new Intent(this, ActivityTinyWindowListViewMultiHolder.class));
                break;
            case R.id.auto_tiny_list_view_recycleview:
                startActivity(new Intent(this, ActivityTinyWindowRecycleView.class));
                break;
            case R.id.auto_tiny_list_view_recycleview_multiholder:
                startActivity(new Intent(this, ActivityTinyWindowRecycleViewMultiHolder.class));
                break;
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
