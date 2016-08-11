package fm.jiecao.jiecaovideoplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.nostra13.universalimageloader.core.ImageLoader;

import fm.jiecao.jcvideoplayer_lib.JCBuriedPoint;
import fm.jiecao.jcvideoplayer_lib.JCBuriedPointStandard;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerSimple;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by Nathen on 16/7/22.
 */
public class MainActiivty extends AppCompatActivity implements View.OnClickListener {

    JCVideoPlayerStandard jcVideoPlayerStandard;
    JCVideoPlayerSimple   jcVideoPlayerSimple;

    Button aboutListView, aboutUI, playDirectly;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        aboutUI = (Button) findViewById(R.id.play_directly_without_layout);
        aboutListView = (Button) findViewById(R.id.about_listview);
        playDirectly = (Button) findViewById(R.id.about_ui);

        aboutListView.setOnClickListener(this);
        aboutUI.setOnClickListener(this);
        playDirectly.setOnClickListener(this);

        jcVideoPlayerSimple = (JCVideoPlayerSimple) findViewById(R.id.simple_demo);
        jcVideoPlayerSimple.setUp("http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8"
                , JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, "嫂子在家吗");

        jcVideoPlayerStandard = (JCVideoPlayerStandard) findViewById(R.id.jc_video);
        jcVideoPlayerStandard.setUp("http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4"
                , JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, "嫂子坐这");
        ImageLoader.getInstance().displayImage("http://cos.myqcloud.com/1000264/qcloud_video_attachment/842646334/vod_cover/cover1458036374.jpg",
                jcVideoPlayerStandard.thumbImageView);

        findViewById(R.id.tiny_win).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jcVideoPlayerStandard.startWindowTiny();
            }
        });

        JCVideoPlayer.setJcBuriedPoint(new MyJCBuriedPointStandard());

    }

    class MyJCBuriedPointStandard implements JCBuriedPointStandard {

        @Override
        public void onEvent(int type, String url, int screen, Object... objects) {
            switch (type) {
                case JCBuriedPoint.ON_CLICK_START_ICON:
                    Log.i("Buried_Point", "ON_CLICK_START_ICON" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCBuriedPoint.ON_CLICK_START_ERROR:
                    Log.i("Buried_Point", "ON_CLICK_START_ERROR" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCBuriedPoint.ON_CLICK_START_AUTO_COMPLETE:
                    Log.i("Buried_Point", "ON_CLICK_START_AUTO_COMPLETE" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCBuriedPoint.ON_CLICK_PAUSE:
                    Log.i("Buried_Point", "ON_CLICK_PAUSE" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCBuriedPoint.ON_CLICK_RESUME:
                    Log.i("Buried_Point", "ON_CLICK_RESUME" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCBuriedPoint.ON_SEEK_POSITION:
                    Log.i("Buried_Point", "ON_SEEK_POSITION" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCBuriedPoint.ON_AUTO_COMPLETE:
                    Log.i("Buried_Point", "ON_AUTO_COMPLETE" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCBuriedPoint.ON_ENTER_FULLSCREEN:
                    Log.i("Buried_Point", "ON_ENTER_FULLSCREEN" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCBuriedPoint.ON_QUIT_FULLSCREEN:
                    Log.i("Buried_Point", "ON_QUIT_FULLSCREEN" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCBuriedPoint.ON_ENTER_TINYSCREEN:
                    Log.i("Buried_Point", "ON_ENTER_TINYSCREEN" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCBuriedPoint.ON_QUIT_TINYSCREEN:
                    Log.i("Buried_Point", "ON_QUIT_TINYSCREEN" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCBuriedPoint.ON_TOUCH_SCREEN_SEEK_VOLUME:
                    Log.i("Buried_Point", "ON_TOUCH_SCREEN_SEEK_VOLUME" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCBuriedPoint.ON_TOUCH_SCREEN_SEEK_POSITION:
                    Log.i("Buried_Point", "ON_TOUCH_SCREEN_SEEK_POSITION" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;

                case JCBuriedPointStandard.ON_CLICK_START_THUMB:
                    Log.i("Buried_Point", "ON_CLICK_START_THUMB" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                case JCBuriedPointStandard.ON_CLICK_BLANK:
                    Log.i("Buried_Point", "ON_CLICK_BLANK" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " url is : " + url + " screen is : " + screen);
                    break;
                default:
                    Log.i("Buried_Point", "unknow");
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.play_directly_without_layout:
                startActivity(new Intent(MainActiivty.this, PlayDirectlyActivity.class));
                break;
            case R.id.about_listview:
                startActivity(new Intent(MainActiivty.this, ListViewActivity.class));
                break;
            case R.id.about_ui:
                startActivity(new Intent(MainActiivty.this, UIActivity.class));
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
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

}
