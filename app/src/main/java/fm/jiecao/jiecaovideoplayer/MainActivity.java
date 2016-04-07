package fm.jiecao.jiecaovideoplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import fm.jiecao.jcvideoplayer_lib.JCBuriedPoint;
import fm.jiecao.jcvideoplayer_lib.JCFullScreenActivity;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    JCVideoPlayer videoController1, videoController2, videoController3;
    Button btnToList, btnToListViewpager, btnToFullscreen, btnToChangecolor, btnToLoadImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        videoController1 = (JCVideoPlayer) findViewById(R.id.videocontroller1);
        videoController1.setUp("http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4",
                "嫂子别摸我");
        ImageLoader.getInstance().displayImage("http://cos.myqcloud.com/1000264/qcloud_video_attachment/842646334/vod_cover/cover1458036374.jpg",
                videoController1.ivThumb);

        videoController2 = (JCVideoPlayer) findViewById(R.id.videocontroller2);
        videoController2.setUp("http://2449.vod.myqcloud.com/2449_bfbbfa3cea8f11e5aac3db03cda99974.f20.mp4",//
                "嫂子还摸我", false);
        ImageLoader.getInstance().displayImage("http://p.qpic.cn/videoyun/0/2449_bfbbfa3cea8f11e5aac3db03cda99974_1/640",
                videoController2.ivThumb);

        videoController3 = (JCVideoPlayer) findViewById(R.id.videocontroller3);
        videoController3.setUp("http://121.40.64.47/resource/mp3/music_yangguang3.mp3",
                "嫂子别闹");
        ImageLoader.getInstance().displayImage("http://p.qpic.cn/videoyun/0/2449_38e65894d9e211e5b0e0a3699ca1d490_1/640",
                videoController3.ivThumb);

        btnToList = (Button) findViewById(R.id.to_list_activity);
        btnToListViewpager = (Button) findViewById(R.id.to_list_viewpager_activity);
        btnToFullscreen = (Button) findViewById(R.id.to_fullscreen);
        btnToChangecolor = (Button) findViewById(R.id.to_changecolor_activity);
        btnToLoadImage = (Button) findViewById(R.id.to_list_loadimage_activity);

        btnToList.setOnClickListener(this);
        btnToListViewpager.setOnClickListener(this);
        btnToFullscreen.setOnClickListener(this);
        btnToChangecolor.setOnClickListener(this);
        btnToLoadImage.setOnClickListener(this);

        JCVideoPlayer.setJcBuriedPoint(jcBuriedPoint);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    //init buried point listener whenever you want. in application is also ok
    JCBuriedPoint jcBuriedPoint = new JCBuriedPoint() {
        @Override
        public void POINT_START_ICON(String title, String url) {
            Log.i("Buried_Point", "POINT_START_ICON" + " title is : " + title + " url is : " + url);
        }

        @Override
        public void POINT_START_THUMB(String title, String url) {
            Log.i("Buried_Point", "POINT_START_THUMB" + " title is : " + title + " url is : " + url);
        }

        @Override
        public void POINT_STOP(String title, String url) {
            Log.i("Buried_Point", "POINT_STOP" + " title is : " + title + " url is : " + url);
        }

        @Override
        public void POINT_STOP_FULLSCREEN(String title, String url) {
            Log.i("Buried_Point", "POINT_STOP_FULLSCREEN" + " title is : " + title + " url is : " + url);
        }

        @Override
        public void POINT_RESUME(String title, String url) {
            Log.i("Buried_Point", "POINT_RESUME" + " title is : " + title + " url is : " + url);
        }

        @Override
        public void POINT_RESUME_FULLSCREEN(String title, String url) {
            Log.i("Buried_Point", "POINT_RESUME_FULLSCREEN" + " title is : " + title + " url is : " + url);
        }

        @Override
        public void POINT_CLICK_BLANK(String title, String url) {
            Log.i("Buried_Point", "POINT_CLICK_BLANK" + " title is : " + title + " url is : " + url);
        }

        @Override
        public void POINT_CLICK_BLANK_FULLSCREEN(String title, String url) {
            Log.i("Buried_Point", "POINT_CLICK_BLANK_FULLSCREEN" + " title is : " + title + " url is : " + url);
        }

        @Override
        public void POINT_CLICK_SEEKBAR(String title, String url) {
            Log.i("Buried_Point", "POINT_CLICK_SEEKBAR" + " title is : " + title + " url is : " + url);
        }

        @Override
        public void POINT_CLICK_SEEKBAR_FULLSCREEN(String title, String url) {
            Log.i("Buried_Point", "POINT_CLICK_SEEKBAR_FULLSCREEN" + " title is : " + title + " url is : " + url);
        }

        @Override
        public void POINT_AUTO_COMPLETE(String title, String url) {
            Log.i("Buried_Point", "POINT_AUTO_COMPLETE" + " title is : " + title + " url is : " + url);
        }

        @Override
        public void POINT_AUTO_COMPLETE_FULLSCREEN(String title, String url) {
            Log.i("Buried_Point", "POINT_AUTO_COMPLETE_FULLSCREEN" + " title is : " + title + " url is : " + url);
        }

        @Override
        public void POINT_ENTER_FULLSCREEN(String title, String url) {
            Log.i("Buried_Point", "POINT_ENTER_FULLSCREEN" + " title is : " + title + " url is : " + url);
        }

        @Override
        public void POINT_QUIT_FULLSCREEN(String title, String url) {
            Log.i("Buried_Point", "POINT_QUIT_FULLSCREEN" + " title is : " + title + " url is : " + url);
        }
    };

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
                Toast.makeText(MainActivity.this, "coming soon", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(this, SetSkinActivity.class));
                break;
            case R.id.to_list_loadimage_activity:
                startActivity(new Intent(MainActivity.this, LoadImageActivity.class));
                break;
        }
    }

}
