package fm.jiecao.jiecaovideoplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import fm.jiecao.jcvideoplayer_lib.JCVideoView;

public class MainActivity extends AppCompatActivity {
    JCVideoView videoController1, videoController2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        videoController1 = (JCVideoView) findViewById(R.id.videocontroller1);
        videoController1.setUp("http://2449.vod.myqcloud.com/2449_43b6f696980311e59ed467f22794e792.f20.mp4",
                "http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640",
                "嫂子别摸我", false);

        videoController2 = (JCVideoView) findViewById(R.id.videocontroller2);
        videoController2.setUp("http://2449.vod.myqcloud.com/2449_a80a72289b1211e5a28d6dc08193c3c9.f20.mp4",
                "http://cos.myqcloud.com/1000264/qcloud_video_attachment/842646334/vod_cover/cover1449294460.jpg",
                "嫂子还摸我", false, false);

    }
}
