package fm.jiecao.jiecaovideoplayer;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import java.lang.reflect.Constructor;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by Nathen on 16/7/31.
 */
public class ActivityDirectlyPlay extends AppCompatActivity {
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


        findViewById(R.id.fullscreen).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                    Constructor<JCVideoPlayer> constructor = JCVideoPlayer.class.getConstructor(Context.class);
                    JCVideoPlayer jcVideoPlayer = constructor.newInstance(ActivityDirectlyPlay.this);
                    jcVideoPlayer.setId(JCVideoPlayer.FULLSCREEN_ID);
                    WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                    int w = wm.getDefaultDisplay().getWidth();
                    int h = wm.getDefaultDisplay().getHeight();
                    FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(h, w);
                    lp.setMargins((w - h) / 2, -(w - h) / 2, 0, 0);
                    vp.addView(jcVideoPlayer, lp);
                    jcVideoPlayer.setUp("http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4", JCVideoPlayerStandard.SCREEN_WINDOW_FULLSCREEN, "Hello");
                    jcVideoPlayer.addTextureView();
                    jcVideoPlayer.setRotation(90);
                    JCVideoPlayerManager.setListener(jcVideoPlayer);
                    jcVideoPlayer.startButton.performClick();
                } catch (InstantiationException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        findViewById(R.id.tiny_window).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
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
