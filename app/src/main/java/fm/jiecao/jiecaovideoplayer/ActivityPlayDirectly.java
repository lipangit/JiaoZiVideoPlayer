package fm.jiecao.jiecaovideoplayer;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.lang.reflect.Constructor;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by Nathen on 16/7/31.
 */
public class ActivityPlayDirectly extends AppCompatActivity implements View.OnClickListener {
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
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);
                getSupportActionBar().setShowHideAnimationEnabled(false);
                getSupportActionBar().hide();

                ViewGroup vp = (ViewGroup) findViewById(Window.ID_ANDROID_CONTENT);
                View old = vp.findViewById(JCVideoPlayer.FULLSCREEN_ID);
                if (old != null) {
                    vp.removeView(old);
                }
                try {
                    Constructor<JCVideoPlayerStandard> constructor = JCVideoPlayerStandard.class.getConstructor(Context.class);
                    JCVideoPlayerStandard jcVideoPlayer = constructor.newInstance(this);
                    jcVideoPlayer.setId(JCVideoPlayerStandard.FULLSCREEN_ID);
                    WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                    int w = wm.getDefaultDisplay().getWidth();
                    int h = wm.getDefaultDisplay().getHeight();
                    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(h, w);
                    lp.setMargins((w - h) / 2, -(w - h) / 2, 0, 0);
                    vp.addView(jcVideoPlayer, lp);
                    jcVideoPlayer.setUp("http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4", JCVideoPlayerStandard.SCREEN_WINDOW_FULLSCREEN, "hello");
                    jcVideoPlayer.addTextureView();
                    jcVideoPlayer.setRotation(90);

                    jcVideoPlayer.startButton.performClick();

                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.tiny_window:
                Toast.makeText(ActivityPlayDirectly.this, "Comming Soon", Toast.LENGTH_SHORT).show();
                break;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

}
