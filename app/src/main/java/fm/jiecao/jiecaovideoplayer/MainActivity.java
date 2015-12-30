package fm.jiecao.jiecaovideoplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import de.greenrobot.event.EventBus;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.VideoEvents;

public class MainActivity extends AppCompatActivity {
    JCVideoPlayer videoController1, videoController2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        videoController1 = (JCVideoPlayer) findViewById(R.id.videocontroller1);
        videoController1.setUp("http://2449.vod.myqcloud.com/2449_43b6f696980311e59ed467f22794e792.f20.mp4",
                "http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640",
                "嫂子别摸我", false);

        videoController2 = (JCVideoPlayer) findViewById(R.id.videocontroller2);
        videoController2.setUp("http://2449.vod.myqcloud.com/2449_a80a72289b1211e5a28d6dc08193c3c9.f20.mp4",
                "http://cos.myqcloud.com/1000264/qcloud_video_attachment/842646334/vod_cover/cover1449294460.jpg",
                "嫂子还摸我", false, false);

    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    public void onEventMainThread(VideoEvents event) {
        if (event.type == VideoEvents.POINT_START_ICON) {
            Log.i("Video Event", "POINT_START_ICON");
        } else if (event.type == VideoEvents.POINT_START_THUMB) {
            Log.i("Video Event", "POINT_START_THUMB");
        } else if (event.type == VideoEvents.POINT_STOP) {
            Log.i("Video Event", "POINT_STOP");
        } else if (event.type == VideoEvents.POINT_STOP_FULLSCREEN) {
            Log.i("Video Event", "POINT_STOP_FULLSCREEN");
        } else if (event.type == VideoEvents.POINT_RESUME) {
            Log.i("Video Event", "POINT_RESUME");
        } else if (event.type == VideoEvents.POINT_RESUME_FULLSCREEN) {
            Log.i("Video Event", "POINT_RESUME_FULLSCREEN");
        } else if (event.type == VideoEvents.POINT_CLICK_BLANK) {
            Log.i("Video Event", "POINT_CLICK_BLANK");
        } else if (event.type == VideoEvents.POINT_CLICK_BLANK_FULLSCREEN) {
            Log.i("Video Event", "POINT_CLICK_BLANK_FULLSCREEN");
        } else if (event.type == VideoEvents.POINT_CLICK_SEEKBAR) {
            Log.i("Video Event", "POINT_CLICK_SEEKBAR");
        } else if (event.type == VideoEvents.POINT_CLICK_SEEKBAR_FULLSCREEN) {
            Log.i("Video Event", "POINT_CLICK_SEEKBAR_FULLSCREEN");
        } else if (event.type == VideoEvents.POINT_AUTO_COMPLETE) {
            Log.i("Video Event", "POINT_AUTO_COMPLETE");
        } else if (event.type == VideoEvents.POINT_AUTO_COMPLETE_FULLSCREEN) {
            Log.i("Video Event", "POINT_AUTO_COMPLETE_FULLSCREEN");
        } else if (event.type == VideoEvents.POINT_ENTER_FULLSCREEN) {
            Log.i("Video Event", "POINT_ENTER_FULLSCREEN");
        } else if (event.type == VideoEvents.POINT_QUIT_FULLSCREEN) {
            Log.i("Video Event", "POINT_QUIT_FULLSCREEN");
        }
//        Log.i("Video Event", "type : " + event.type);
    }
}
