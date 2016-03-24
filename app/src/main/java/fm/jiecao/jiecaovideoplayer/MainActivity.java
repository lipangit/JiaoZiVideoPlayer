package fm.jiecao.jiecaovideoplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.nostra13.universalimageloader.core.ImageLoader;

import de.greenrobot.event.EventBus;
import fm.jiecao.jcvideoplayer_lib.JCFullScreenActivity;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.VideoEvents;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    JCVideoPlayer videoController1, videoController2, videoController3;
    Button btnToList, btnToListViewpager, btnToFullscreen, btnToChangecolor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /**
         * 如果没有埋点需求，可以不用注册eventbus
         * <br>
         * if you do not want to get Buried Point , you do not need regist eventbus here
         */
        EventBus.getDefault().register(this);

        videoController1 = (JCVideoPlayer) findViewById(R.id.videocontroller1);
        videoController1.setUp("http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4",
                "嫂子别摸我");
        ImageLoader.getInstance().displayImage("http://cos.myqcloud.com/1000264/qcloud_video_attachment/842646334/vod_cover/cover1458036374.jpg",
                videoController1.ivThumb);

        videoController2 = (JCVideoPlayer) findViewById(R.id.videocontroller2);
        videoController2.setUp("http://2449.vod.myqcloud.com/2449_bfbbfa3cea8f11e5aac3db03cda99974.f20.mp4",//
                "嫂子还摸我", false);
        ImageLoader.getInstance().displayImage("http://p.qpic.cn/videoyun/0/2449_bfbbfa3cea8f11e5aac3db03cda99974_1/640",
                videoController1.ivThumb);

        videoController3 = (JCVideoPlayer) findViewById(R.id.videocontroller3);
        videoController3.setUp("http://121.40.64.47/resource/mp3/music_yangguang3.mp3",
                "嫂子别闹");
        ImageLoader.getInstance().displayImage("http://p.qpic.cn/videoyun/0/2449_38e65894d9e211e5b0e0a3699ca1d490_1/640",
                videoController1.ivThumb);

        btnToList = (Button) findViewById(R.id.to_list_activity);
        btnToListViewpager = (Button) findViewById(R.id.to_list_viewpager_activity);
        btnToFullscreen = (Button) findViewById(R.id.to_fullscreen);
        btnToChangecolor = (Button) findViewById(R.id.to_changecolor_activity);

        btnToList.setOnClickListener(this);
        btnToListViewpager.setOnClickListener(this);
        btnToFullscreen.setOnClickListener(this);
        btnToChangecolor.setOnClickListener(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }


    public void onEventMainThread(VideoEvents event) {
        if (event.type == VideoEvents.POINT_START_ICON) {
            Log.i("Video Event", "POINT_START_ICON" + " title is : " + event.obj + " url is : " + event.obj1);
        } else if (event.type == VideoEvents.POINT_START_THUMB) {
            Log.i("Video Event", "POINT_START_THUMB" + " title is : " + event.obj + " url is : " + event.obj1);
        } else if (event.type == VideoEvents.POINT_STOP) {
            Log.i("Video Event", "POINT_STOP" + " title is : " + event.obj + " url is : " + event.obj1);
        } else if (event.type == VideoEvents.POINT_STOP_FULLSCREEN) {
            Log.i("Video Event", "POINT_STOP_FULLSCREEN" + " title is : " + event.obj + " url is : " + event.obj1);
        } else if (event.type == VideoEvents.POINT_RESUME) {
            Log.i("Video Event", "POINT_RESUME" + " title is : " + event.obj + " url is : " + event.obj1);
        } else if (event.type == VideoEvents.POINT_RESUME_FULLSCREEN) {
            Log.i("Video Event", "POINT_RESUME_FULLSCREEN" + " title is : " + event.obj + " url is : " + event.obj1);
        } else if (event.type == VideoEvents.POINT_CLICK_BLANK) {
            Log.i("Video Event", "POINT_CLICK_BLANK" + " title is : " + event.obj + " url is : " + event.obj1);
        } else if (event.type == VideoEvents.POINT_CLICK_BLANK_FULLSCREEN) {
            Log.i("Video Event", "POINT_CLICK_BLANK_FULLSCREEN" + " title is : " + event.obj + " url is : " + event.obj1);
        } else if (event.type == VideoEvents.POINT_CLICK_SEEKBAR) {
            Log.i("Video Event", "POINT_CLICK_SEEKBAR" + " title is : " + event.obj + " url is : " + event.obj1);
        } else if (event.type == VideoEvents.POINT_CLICK_SEEKBAR_FULLSCREEN) {
            Log.i("Video Event", "POINT_CLICK_SEEKBAR_FULLSCREEN" + " title is : " + event.obj + " url is : " + event.obj1);
        } else if (event.type == VideoEvents.POINT_AUTO_COMPLETE) {
            Log.i("Video Event", "POINT_AUTO_COMPLETE" + " title is : " + event.obj + " url is : " + event.obj1);
        } else if (event.type == VideoEvents.POINT_AUTO_COMPLETE_FULLSCREEN) {
            Log.i("Video Event", "POINT_AUTO_COMPLETE_FULLSCREEN" + " title is : " + event.obj + " url is : " + event.obj1);
        } else if (event.type == VideoEvents.POINT_ENTER_FULLSCREEN) {
            Log.i("Video Event", "POINT_ENTER_FULLSCREEN" + " title is : " + event.obj + " url is : " + event.obj1);
        } else if (event.type == VideoEvents.POINT_QUIT_FULLSCREEN) {
            Log.i("Video Event", "POINT_QUIT_FULLSCREEN" + " title is : " + event.obj + " url is : " + event.obj1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.to_list_activity:
                startActivity(new Intent(MainActivity.this, ListActivity.class));
                break;
            case R.id.to_list_viewpager_activity:
                startActivity(new Intent(MainActivity.this, ListViewpagerActivity.class));
                break;
            case R.id.to_fullscreen:
                JCFullScreenActivity.toActivity(this,
                        "http://gslb.miaopai.com/stream/ed5HCfnhovu3tyIQAiv60Q__.mp4",
                        "嫂子躺下");

//                JCVideoPlayer.toFullscreenActivity(this,
//                        "http://gslb.miaopai.com/stream/ed5HCfnhovu3tyIQAiv60Q__.mp4",
//                        "http://img4.jiecaojingxuan.com/2016/3/14/2204a578-609b-440e-8af7-a0ee17ff3aee.jpg",
//                        "嫂子躺下");
                break;
            case R.id.to_changecolor_activity:
                startActivity(new Intent(this, SetSkinActivity.class));
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
