package fm.jiecao.jiecaovideoplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import fm.jiecao.jcvideoplayer_lib.JCDemoVideoPlayer;

/**
 * Created by Nathen
 * On 2016/04/15 11:25
 */
public class CustomUiActivity extends AppCompatActivity {
    JCDemoVideoPlayer jcDemoVideoPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_ui);
        jcDemoVideoPlayer = (JCDemoVideoPlayer) findViewById(R.id.custom_videoplayer);
        jcDemoVideoPlayer.setUp(
                "嫂子别摸我", "http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4");

    }
}
