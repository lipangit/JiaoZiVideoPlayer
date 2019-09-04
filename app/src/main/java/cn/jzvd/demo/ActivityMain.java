package cn.jzvd.demo;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.bumptech.glide.Glide;

import cn.jzvd.Jzvd;
import cn.jzvd.demo.CustomJzvd.MyJzvdStd;

/**
 * Created by Nathen on 16/7/22.
 */
public class ActivityMain extends AppCompatActivity {

    MyJzvdStd myJzvdStd;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myJzvdStd = findViewById(R.id.jz_video);
        myJzvdStd.setUp("http://jzvd.nathen.cn/342a5f7ef6124a4a8faf00e738b8bee4/cf6d9db0bd4d41f59d09ea0a81e918fd-5287d2089db37e62345123a1be272f8b.mp4"
                , "饺子快长大");
        Glide.with(this).load("http://jzvd-pic.nathen.cn/jzvd-pic/1bb2ebbe-140d-4e2e-abd2-9e7e564f71ac.png").into(myJzvdStd.thumbImageView);

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

    public void clickApi(View view) {
        startActivity(new Intent(ActivityMain.this, ActivityApi.class));
    }

    public void clickListView(View view) {
        startActivity(new Intent(ActivityMain.this, ActivityListView.class));
    }

    public void clickTinyWindow(View view) {
        startActivity(new Intent(ActivityMain.this, ActivityTinyWindow.class));
    }

    public void clickDirectPlay(View view) {
        startActivity(new Intent(ActivityMain.this, ActivityDirectPlay.class));
    }

    public void clickWebView(View view) {
        startActivity(new Intent(ActivityMain.this, ActivityWebView.class));
    }

}
