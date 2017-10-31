package cn.jzvd.demo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by Nathen on 2017/10/31.
 */

public class TinyWindowActivity extends AppCompatActivity implements View.OnClickListener {

    JZVideoPlayerStandard mJzVideoPlayerStandard;
    Button mBtnTinyWindow;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tiny_window);

        mJzVideoPlayerStandard = findViewById(R.id.jz_video);
        mBtnTinyWindow = findViewById(R.id.tiny_window);
        mBtnTinyWindow.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tiny_window:
                mJzVideoPlayerStandard.startWindowTiny();
                break;
        }
    }
}
