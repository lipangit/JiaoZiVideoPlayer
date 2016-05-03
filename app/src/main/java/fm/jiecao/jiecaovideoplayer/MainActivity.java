package fm.jiecao.jiecaovideoplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.nostra13.universalimageloader.core.ImageLoader;

import fm.jiecao.jcvideoplayer_lib.JCBuriedPointStandard;
import fm.jiecao.jcvideoplayer_lib.JCFullScreenActivity;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerSimple;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import fm.jiecao.jiecaovideoplayer.View.JCVideoPlayerStandardShowShareButtonAfterFullscreen;
import fm.jiecao.jiecaovideoplayer.View.JCVideoPlayerStandardShowTitleAfterFullscreen;

/**
 * Created by Nathen
 * On 2016/04/15 11:25
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    JCVideoPlayerSimple jcVideoPlayerSimple;
    JCVideoPlayerStandard jcVideoPlayerStandard;
    JCVideoPlayerStandardShowShareButtonAfterFullscreen jcVideoPlayerStandardWithShareButton;
    JCVideoPlayerStandardShowTitleAfterFullscreen jcVideoPlayerStandardShowTitleAfterFullscreen;

    Button btnToFullscreen_simple, btnToFullscreen_standard,
            btnToListActivity, btnToListViewPagerActivity, btnToImageLoadActivity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnToFullscreen_simple = (Button) findViewById(R.id.to_fullscreen_simple);
        btnToFullscreen_standard = (Button) findViewById(R.id.to_fullscreen_standard);
        btnToListActivity = (Button) findViewById(R.id.to_list_activity);
        btnToListViewPagerActivity = (Button) findViewById(R.id.to_list_viewpager_activity);
        btnToImageLoadActivity = (Button) findViewById(R.id.to_loadimage_activity);
        btnToFullscreen_simple.setOnClickListener(this);
        btnToFullscreen_standard.setOnClickListener(this);
        btnToListActivity.setOnClickListener(this);
        btnToListViewPagerActivity.setOnClickListener(this);
        btnToImageLoadActivity.setOnClickListener(this);

        jcVideoPlayerSimple = (JCVideoPlayerSimple) findViewById(R.id.custom_videoplayer);
        jcVideoPlayerSimple.setUp("http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4", null);

        jcVideoPlayerStandard = (JCVideoPlayerStandard) findViewById(R.id.custom_videoplayer_standard);
        jcVideoPlayerStandard.setUp("http://2449.vod.myqcloud.com/2449_bfbbfa3cea8f11e5aac3db03cda99974.f20.mp4"
                , "嫂子想我没");
        ImageLoader.getInstance().displayImage("http://p.qpic.cn/videoyun/0/2449_bfbbfa3cea8f11e5aac3db03cda99974_1/640",
                jcVideoPlayerStandard.ivThumb);

        jcVideoPlayerStandardWithShareButton = (JCVideoPlayerStandardShowShareButtonAfterFullscreen) findViewById(R.id.custom_videoplayer_standard_with_share_button);
        jcVideoPlayerStandardWithShareButton.setUp("http://gslb.miaopai.com/stream/ed5HCfnhovu3tyIQAiv60Q__.mp4"
                , "嫂子闭眼睛");
        ImageLoader.getInstance().displayImage("http://img4.jiecaojingxuan.com/2016/3/14/2204a578-609b-440e-8af7-a0ee17ff3aee.jpg",
                jcVideoPlayerStandardWithShareButton.ivThumb);

        jcVideoPlayerStandardShowTitleAfterFullscreen = (JCVideoPlayerStandardShowTitleAfterFullscreen) findViewById(R.id.custom_videoplayer_standard_show_title_after_fullscreen);
        jcVideoPlayerStandardShowTitleAfterFullscreen.setUp("http://2449.vod.myqcloud.com/2449_43b6f696980311e59ed467f22794e792.f20.mp4"
                , "嫂子摸完没");
        ImageLoader.getInstance().displayImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640",
                jcVideoPlayerStandardShowTitleAfterFullscreen.ivThumb);

        JCVideoPlayerStandard.setJcBuriedPointStandard(jcBuriedPointStandard);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.to_fullscreen_simple:
                JCFullScreenActivity.toActivity(this,
                        "http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4",
                        JCVideoPlayerSimple.class, "嫂子真浪");
                break;
            case R.id.to_fullscreen_standard:
                JCFullScreenActivity.toActivity(this,
                        "http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4",
                        JCVideoPlayerStandard.class, "嫂子真牛逼");
                break;
            case R.id.to_list_activity:
                startActivity(new Intent(MainActivity.this, ListActivity.class));
                break;
            case R.id.to_list_viewpager_activity:
                startActivity(new Intent(MainActivity.this, ListViewpagerActivity.class));
                break;
            case R.id.to_loadimage_activity:
                startActivity(new Intent(MainActivity.this, LoadImageActivity.class));
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        JCVideoPlayer.releaseAllVideos();
    }

    JCBuriedPointStandard jcBuriedPointStandard = new JCBuriedPointStandard() {
        @Override
        public void onClickStartThumb(String url, Object... objects) {
            Log.i("Buried_Point", "onClickStartThumb" + " title is : " + (objects == null ? "" : objects[0]) + " url is : " + url);
        }

        @Override
        public void onClickBlank(String url, Object... objects) {
            Log.i("Buried_Point", "onClickBlank" + " title is : " + (objects == null ? "" : objects[0]) + " url is : " + url);
        }

        @Override
        public void onClickBlankFullscreen(String url, Object... objects) {
            Log.i("Buried_Point", "onClickBlankFullscreen" + " title is : " + (objects == null ? "" : objects[0]) + " url is : " + url);
        }

        @Override
        public void onClickStartIcon(String url, Object... objects) {
            Log.i("Buried_Point", "onClickStartIcon" + " title is : " + (objects == null ? "" : objects[0]) + " url is : " + url);
        }

        @Override
        public void onClickStartError(String url, Object... objects) {
            Log.i("Buried_Point", "onClickStartError" + " title is : " + (objects == null ? "" : objects[0]) + " url is : " + url);
        }

        @Override
        public void onClickStop(String url, Object... objects) {
            Log.i("Buried_Point", "onClickStop" + " title is : " + (objects == null ? "" : objects[0]) + " url is : " + url);
        }

        @Override
        public void onClickStopFullscreen(String url, Object... objects) {
            Log.i("Buried_Point", "onClickStopFullscreen" + " title is : " + (objects == null ? "" : objects[0]) + " url is : " + url);
        }

        @Override
        public void onClickResume(String url, Object... objects) {
            Log.i("Buried_Point", "onClickResume" + " title is : " + (objects == null ? "" : objects[0]) + " url is : " + url);
        }

        @Override
        public void onClickResumeFullscreen(String url, Object... objects) {
            Log.i("Buried_Point", "onClickResumeFullscreen" + " title is : " + (objects == null ? "" : objects[0]) + " url is : " + url);
        }

        @Override
        public void onClickSeekbar(String url, Object... objects) {
            Log.i("Buried_Point", "onClickSeekbar" + " title is : " + (objects == null ? "" : objects[0]) + " url is : " + url);
        }

        @Override
        public void onClickSeekbarFullscreen(String url, Object... objects) {
            Log.i("Buried_Point", "onClickSeekbarFullscreen" + " title is : " + (objects == null ? "" : objects[0]) + " url is : " + url);
        }

        @Override
        public void onAutoComplete(String url, Object... objects) {
            Log.i("Buried_Point", "onAutoComplete" + " title is : " + (objects == null ? "" : objects[0]) + " url is : " + url);
        }

        @Override
        public void onAutoCompleteFullscreen(String url, Object... objects) {
            Log.i("Buried_Point", "onAutoCompleteFullscreen" + " title is : " + (objects == null ? "" : objects[0]) + " url is : " + url);
        }

        @Override
        public void onEnterFullscreen(String url, Object... objects) {
            Log.i("Buried_Point", "onEnterFullscreen" + " title is : " + (objects == null ? "" : objects[0]) + " url is : " + url);
        }

        @Override
        public void onQuitFullscreen(String url, Object... objects) {
            Log.i("Buried_Point", "onQuitFullscreen" + " title is : " + (objects == null ? "" : objects[0]) + " url is : " + url);
        }
    };

}
