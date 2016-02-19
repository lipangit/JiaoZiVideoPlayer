#节操视频播放器

真正实现Android的全屏功能，励志成为Android平台使用最广泛的视频播放控件

##使用
添加布局
```html
<fm.jiecao.jcvideoplayer_lib.JCVideoPlayer
                android:id="@+id/videocontroller1"
                android:layout_width="match_parent"
                android:layout_height="200dp" />
```

设置视频地址、缩略图地址、标题
```java
JCVideoPlayer videoController = (JCVideoPlayer) findViewById(R.id.videocontroller);
videoController.setUp("http://2449.vod.myqcloud.com/2449_43b6f696980311e59ed467f22794e792.f20.mp4",
                "http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640",
                "嫂子别摸我");
```

其他接口，停止所有视频
```
JCVideoPlayer.releaseAllVideo();
```
    
##效果

![Demo Screenshot][1]

效果视频 : http://v.youku.com/v_show/id_XMTQ2NzUwOTcyNA==.html?firsttime=0&from=y1.4-2

[1]: ./effect.gif
