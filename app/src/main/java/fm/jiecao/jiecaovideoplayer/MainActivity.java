package fm.jiecao.jiecaovideoplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import fm.jiecao.jcvideoplayer_lib.JCMediaPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoView;

public class MainActivity extends AppCompatActivity {
    JCVideoView videoController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        //TODO 初始化引擎
        JCMediaPlayer.init(DemoApplication.instance());
        videoController = (JCVideoView) findViewById(R.id.videocontroller);

        //TODO 给播放器设置url
        videoController.setUp("http://2449.vod.myqcloud.com/2449_43b6f696980311e59ed467f22794e792.f20.mp4",
                "http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640",
                "嫂子别摸我", false);
        //TODO 当播放器的播放按钮点击时，给引擎设置地址，开始缓冲视频

        //TODO 缓冲成功，设置holder播放视频

        //TODO 全屏的时候把全局的引擎拿过来换一个播放器播放


    }
}
