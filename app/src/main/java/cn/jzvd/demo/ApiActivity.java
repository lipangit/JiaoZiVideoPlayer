package cn.jzvd.demo;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;

import cn.jzvd.JZMediaManager;
import cn.jzvd.JZVideoPlayer;
import cn.jzvd.JZVideoPlayerManager;
import cn.jzvd.JZVideoPlayerSimple;
import cn.jzvd.JZVideoPlayerStandard;

/**
 * Created by Nathen on 16/7/31.
 */
public class ApiActivity extends AppCompatActivity implements View.OnClickListener {
    Button mSmallChange, mBigChange, mOrientation;
    JZVideoPlayerSimple mJzVideoPlayerSimple;
    JZVideoPlayerStandard mJzVideoPlayerStandard;
    JZVideoPlayer.JZAutoFullscreenListener mSensorEventListener;
    SensorManager mSensorManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setTitle("About Api");
        setContentView(R.layout.activity_api);

        mSmallChange = (Button) findViewById(R.id.small_change);
        mBigChange = (Button) findViewById(R.id.big_change);
        mOrientation = (Button) findViewById(R.id.orientation);

        mSmallChange.setOnClickListener(this);
        mBigChange.setOnClickListener(this);
        mOrientation.setOnClickListener(this);

        mJzVideoPlayerSimple = (JZVideoPlayerSimple) findViewById(R.id.simple_demo);
        mJzVideoPlayerSimple.setUp("http://devimages.apple.com/iphone/samples/bipbop/gear1/prog_index.m3u8"
                , JZVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "嫂子在家吗");


        mJzVideoPlayerStandard = (JZVideoPlayerStandard) findViewById(R.id.jz_video);
        LinkedHashMap map = new LinkedHashMap();
        map.put("高清", VideoConstant.videoUrlList[0]);
        map.put("标清", VideoConstant.videoUrls[0][6]);
        map.put("普清", VideoConstant.videoUrls[0][4]);
        mJzVideoPlayerStandard.setUp(map, 2
                , JZVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "嫂子不信");
        Picasso.with(this)
                .load(VideoConstant.videoThumbList[0])
                .into(mJzVideoPlayerStandard.thumbImageView);
        mJzVideoPlayerStandard.loop = true;
        mJzVideoPlayerStandard.headData = new HashMap<>();
        mJzVideoPlayerStandard.headData.put("key", "value");


//        JZVideoPlayer.SAVE_PROGRESS = false;
        /** Play video in local path, eg:record by system camera **/
//        cpAssertVideoToLocalPath();
//        mJzVideoPlayerStandard.setUp(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/local_video.mp4"
//                , JZVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "嫂子不信");
        /** Play video in assert, but not work now **/
//        mJzVideoPlayerStandard.setUp("file:///android_asset/local_video.mp4"
//                , JZVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "嫂子不信");

        /** ImageLoader **/
//        ImageLoader.getInstance().displayImage(VideoConstant.videoThumbs[0][1],
//                videoController1.thumbImageView);
        /** Glide **/
//        Glide.with(this)
//                .load(VideoConstant.videoThumbs[0][1])
//                .into(videoController1.thumbImageView);
        /** volley omit **/
        /** Fresco omit **/
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mSensorEventListener = new JZVideoPlayer.JZAutoFullscreenListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.small_change:
                startActivity(new Intent(ApiActivity.this, UISmallChangeActivity.class));
                break;
            case R.id.big_change:
                Toast.makeText(ApiActivity.this, "Comming Soon", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(ApiActivity.this, UIBigChangeActivity.class));
                break;
            case R.id.orientation:
                startActivity(new Intent(ApiActivity.this, OrientationActivity.class));
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Sensor accelerometerSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mSensorManager.registerListener(mSensorEventListener, accelerometerSensor, SensorManager.SENSOR_DELAY_NORMAL);
        //home back
        if (JZVideoPlayerManager.getCurrentJzvd() != null) {
            JZVideoPlayer jzvd = JZVideoPlayerManager.getCurrentJzvd();
            if (jzvd.currentState == JZVideoPlayer.CURRENT_STATE_PAUSE) {
                jzvd.onStatePlaying();
                JZMediaManager.instance().mediaPlayer.start();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(mSensorEventListener);
        JZVideoPlayer.clearSavedProgress(this, null);
        //home back
        if (JZVideoPlayerManager.getCurrentJzvd() != null) {
            JZVideoPlayer jzvd = JZVideoPlayerManager.getCurrentJzvd();
            jzvd.onStatePause();
            JZMediaManager.instance().mediaPlayer.pause();
        }
    }

    @Override
    public void onBackPressed() {
        if (JZVideoPlayer.backPress()) {
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

    public void cpAssertVideoToLocalPath() {
        try {
            InputStream myInput;
            OutputStream myOutput = new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath() + "/DCIM/Camera/local_video.mp4");
            myInput = this.getAssets().open("local_video.mp4");
            byte[] buffer = new byte[1024];
            int length = myInput.read(buffer);
            while (length > 0) {
                myOutput.write(buffer, 0, length);
                length = myInput.read(buffer);
            }

            myOutput.flush();
            myInput.close();
            myOutput.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
