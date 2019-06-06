package cn.jzvd.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;
import cn.jzvd.demo.CustomJzvd.JzvdStdTinyWindow;

/**
 * Created by Nathen on 2017/10/31.
 */

public class ActivityTinyWindow extends AppCompatActivity {

    JzvdStdTinyWindow jzvdStdTinyWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("TinyWindow");
        setContentView(R.layout.activity_tiny_window);

        jzvdStdTinyWindow = findViewById(R.id.jz_video);
        jzvdStdTinyWindow.setUp("http://jzvd.nathen.cn/342a5f7ef6124a4a8faf00e738b8bee4/cf6d9db0bd4d41f59d09ea0a81e918fd-5287d2089db37e62345123a1be272f8b.mp4", "饺子快长大"
                , JzvdStd.SCREEN_NORMAL);
        Glide.with(this)
                .load("http://jzvd-pic.nathen.cn/jzvd-pic/1bb2ebbe-140d-4e2e-abd2-9e7e564f71ac.png")
                .into(jzvdStdTinyWindow.thumbImageView);

    }


    @Override
    protected void onPause() {
        super.onPause();
        Jzvd.releaseAllVideos();
    }

    @Override
    public void onBackPressed() {
        if (Jzvd.backPress()) {
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


    public void clickTinyWindow(View view) {
        jzvdStdTinyWindow.gotoScreenTiny();
    }

    public void clickAutoTinyListViewRecyclerView(View view) {
        Toast.makeText(this, "comming soon", Toast.LENGTH_SHORT).show();
//        startActivity(new Intent(this, ActivityTinyWindowRecycleView.class));
    }

    public void clickAutoTinyListViewRecyclerViewMultiHolder(View view) {
        Toast.makeText(this, "comming soon", Toast.LENGTH_SHORT).show();
//        startActivity(new Intent(this, ActivityTinyWindowRecycleViewMultiHolder.class));
    }
}
