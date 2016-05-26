package fm.jiecao.jiecaovideoplayer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.HashMap;
import java.util.Map;

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
    btnToListActivity, btnToListViewPagerActivity, btnToMultiHolderActivity, btnToImageLoadActivity;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    getSupportActionBar().setTitle(getString(R.string.app_name_full));

    btnToFullscreen_simple = (Button) findViewById(R.id.to_fullscreen_simple);
    btnToFullscreen_standard = (Button) findViewById(R.id.to_fullscreen_standard);
    btnToListActivity = (Button) findViewById(R.id.to_list_activity);
    btnToListViewPagerActivity = (Button) findViewById(R.id.to_list_viewpager_activity);
    btnToImageLoadActivity = (Button) findViewById(R.id.to_loadimage_activity);
    btnToMultiHolderActivity = (Button) findViewById(R.id.to_multiholder_activity);

    btnToFullscreen_simple.setOnClickListener(this);
    btnToFullscreen_standard.setOnClickListener(this);
    btnToListActivity.setOnClickListener(this);
    btnToListViewPagerActivity.setOnClickListener(this);
    btnToImageLoadActivity.setOnClickListener(this);
    btnToMultiHolderActivity.setOnClickListener(this);

    Map<String, String> headData = new HashMap<>();
    headData.put("key1", "value1");
    headData.put("key2", "value2");
    jcVideoPlayerSimple = (JCVideoPlayerSimple) findViewById(R.id.custom_videoplayer);
    jcVideoPlayerSimple.setUp("http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8",
      headData);

    jcVideoPlayerStandard = (JCVideoPlayerStandard) findViewById(R.id.custom_videoplayer_standard);
    jcVideoPlayerStandard.setUp("http://2449.vod.myqcloud.com/2449_bfbbfa3cea8f11e5aac3db03cda99974.f20.mp4"
      , "嫂子想我没");
    ImageLoader.getInstance().displayImage("http://p.qpic.cn/videoyun/0/2449_bfbbfa3cea8f11e5aac3db03cda99974_1/640",
      jcVideoPlayerStandard.thumbImageView);

    jcVideoPlayerStandardWithShareButton = (JCVideoPlayerStandardShowShareButtonAfterFullscreen) findViewById(R.id.custom_videoplayer_standard_with_share_button);
    jcVideoPlayerStandardWithShareButton.setUp("http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4"
      , "嫂子闭眼睛");
    ImageLoader.getInstance().displayImage("http://cos.myqcloud.com/1000264/qcloud_video_attachment/842646334/vod_cover/cover1458036374.jpg",
      jcVideoPlayerStandardWithShareButton.thumbImageView);


    jcVideoPlayerStandardShowTitleAfterFullscreen = (JCVideoPlayerStandardShowTitleAfterFullscreen) findViewById(R.id.custom_videoplayer_standard_show_title_after_fullscreen);
    jcVideoPlayerStandardShowTitleAfterFullscreen.setUp("http://video.jiecao.fm/5/1/%E8%87%AA%E5%8F%96%E5%85%B6%E8%BE%B1.mp4"
      , "嫂子摸完没");
    ImageLoader.getInstance().displayImage("http://img4.jiecaojingxuan.com/2016/5/1/3430ec64-e6a7-4d8e-b044-9d408e075b7c.jpg",
      jcVideoPlayerStandardShowTitleAfterFullscreen.thumbImageView);

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
      case R.id.to_multiholder_activity:
        startActivity(new Intent(MainActivity.this, MultiHolderActivity.class));
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
      Log.i("Buried_Point", "onClickStartThumb" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " mUrl is : " + url);
    }

    @Override
    public void onClickBlank(String url, Object... objects) {
      Log.i("Buried_Point", "onClickBlank" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " mUrl is : " + url);
    }

    @Override
    public void onClickBlankFullscreen(String url, Object... objects) {
      Log.i("Buried_Point", "onClickBlankFullscreen" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " mUrl is : " + url);
    }

    @Override
    public void onClickStartIcon(String url, Object... objects) {
      Log.i("Buried_Point", "onClickStartIcon" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " mUrl is : " + url);
    }

    @Override
    public void onClickStartError(String url, Object... objects) {
      Log.i("Buried_Point", "onClickStartError" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " mUrl is : " + url);
    }

    @Override
    public void onClickStop(String url, Object... objects) {
      Log.i("Buried_Point", "onClickStop" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " mUrl is : " + url);
    }

    @Override
    public void onClickStopFullscreen(String url, Object... objects) {
      Log.i("Buried_Point", "onClickStopFullscreen" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " mUrl is : " + url);
    }

    @Override
    public void onClickResume(String url, Object... objects) {
      Log.i("Buried_Point", "onClickResume" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " mUrl is : " + url);
    }

    @Override
    public void onClickResumeFullscreen(String url, Object... objects) {
      Log.i("Buried_Point", "onClickResumeFullscreen" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " mUrl is : " + url);
    }

    @Override
    public void onClickSeekbar(String url, Object... objects) {
      Log.i("Buried_Point", "onClickSeekbar" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " mUrl is : " + url);
    }

    @Override
    public void onClickSeekbarFullscreen(String url, Object... objects) {
      Log.i("Buried_Point", "onClickSeekbarFullscreen" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " mUrl is : " + url);
    }

    @Override
    public void onAutoComplete(String url, Object... objects) {
      Log.i("Buried_Point", "onAutoComplete" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " mUrl is : " + url);
    }

    @Override
    public void onAutoCompleteFullscreen(String url, Object... objects) {
      Log.i("Buried_Point", "onAutoCompleteFullscreen" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " mUrl is : " + url);
    }

    @Override
    public void onEnterFullscreen(String url, Object... objects) {
      Log.i("Buried_Point", "onEnterFullscreen" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " mUrl is : " + url);
    }

    @Override
    public void onQuitFullscreen(String url, Object... objects) {
      Log.i("Buried_Point", "onQuitFullscreen" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " mUrl is : " + url);
    }

    @Override
    public void onTouchScreenSeekVolume(String url, Object... objects) {
      Log.i("Buried_Point", "onTouchScreenSeekVolume" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " mUrl is : " + url);
    }

    @Override
    public void onTouchScreenSeekPosition(String url, Object... objects) {
      Log.i("Buried_Point", "onTouchScreenSeekVolume" + " title is : " + (objects.length == 0 ? "" : objects[0]) + " mUrl is : " + url);
    }
  };

}
