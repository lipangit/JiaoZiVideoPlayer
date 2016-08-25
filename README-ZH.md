<a href="https://github.com/lipangit/JieCaoVideoPlayer" target="_blank"><img src="https://raw.githubusercontent.com/lipangit/JieCaoVideoPlayer/develop/screenshots/logo2x.png" style="max-width:100%;"></a>
--
<p align="center">
<a href="http://developer.android.com/index.html"><img src="https://img.shields.io/badge/platform-android-green.svg"></a>
<a href="http://search.maven.org/#artifactdetails%7Cfm.jiecao%7Cjiecaovideoplayer%7C4.6.2%7Caar"><img src="https://img.shields.io/badge/Maven%20Central-4.6.2-green.svg"></a>
<a href="http://choosealicense.com/licenses/mit/"><img src="https://img.shields.io/badge/license-MIT-green.svg"></a>
<a href="https://android-arsenal.com/details/1/3269"><img src="https://img.shields.io/badge/Android%20Arsenal-jiecaovideoplayer-green.svg?style=true"></a>
</p>

立志成为Android平台使用最广泛的视频播放控件  Q群:490442439 验证信息:jcvd

##主要特点
1. 视频全屏播放和浮层小窗播放
2. 可以完全自定义UI
3. 能在`ListView`、`ViewPager`和`ListView`、`ViewPager`和`Fragment`等多重嵌套模式下全屏工作
4. 手势修改进度和音量
5. 视频大小的屏幕适配，宽或长至少有两个对边是充满屏幕的，另外两个方向居中
6. 可以在加载、暂停、播放等各种状态中正常进入全屏和退出全屏
7. [支持hls,rtsp](https://github.com/Bilibili/ijkplayer)
8. 设置http头信息

##效果

**[jiecaovideoplayer-4.6.2-demo.apk](https://raw.githubusercontent.com/lipangit/jiecaovideoplayer/develop/downloads/jiecaovideoplayer-4.6.2-demo.apk)**

![Demo Screenshot][1]

##使用

1.添加类库
```gradle
compile 'fm.jiecao:jiecaovideoplayer:4.6.2'
```

或直接下载

* [jiecaovideoplayer-4.6.2.aar](https://raw.githubusercontent.com/lipangit/jiecaovideoplayer/develop/downloads/jiecaovideoplayer-4.6.2.aar)
* [jiecaovideoplayer-4.6.2-javadoc.jar](https://raw.githubusercontent.com/lipangit/jiecaovideoplayer/develop/downloads/jiecaovideoplayer-4.6.2-javadoc.jar)
* [jiecaovideoplayer-4.6.2-sources.jar](https://raw.githubusercontent.com/lipangit/jiecaovideoplayer/develop/downloads/jiecaovideoplayer-4.6.2-sources.jar)

2.添加布局
```xml
<fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard
    android:id="@+id/custom_videoplayer_standard"
    android:layout_width="match_parent"
    android:layout_height="200dp"/>
```

3.设置视频地址、缩略图地址、标题
```java
JCVideoPlayerStandard jcVideoPlayerStandard = (JCVideoPlayerStandard) findViewById(R.id.custom_videoplayer_standard);
jcVideoPlayerStandard.setUp("http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4"
                            , JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, "嫂子闭眼睛");
jcVideoPlayerStandard.thumbImageView.setThumbInCustomProject("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
```

4.在`Activity`中
```java
@Override
public void onBackPressed() {
    if (JCVideoPlayer.backPress()) {
        return;
    }
    super.onBackPressed();
}
@Override
protected void onPause() {
    super.onPause();
    JCVideoPlayer.releaseAllVideos();
}
```

####其他接口

直接进入全屏
```java
JCVideoPlayerStandard.startFullscreen(this, JCVideoPlayerStandard.class, "http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4", "嫂子辛苦了");
```

####混淆
```
-keep class tv.danmaku.ijk.** { *; }
-dontwarn tv.danmaku.ijk.**
```

播放Assets文件夹下的视频,请先拷贝到本地路径再播放.[亲测](https://github.com/Bilibili/ijkplayer/issues/1013)如果直接传参数IMediaDataSource,只停留在第一帧画面上并且后台会报错

##[自定义UI](./README_CUSTOM_UI-ZH.md)

##贡献者

[Nathen](https://github.com/lipangit) [Derlio](https://github.com/derlio) [zhangzzqq](https://github.com/zhangzzqq) [carmelo-ruota](https://github.com/carmelo-ruota) [wxxsw](https://github.com/wxxsw) [Miguel Aragues](https://github.com/Maragues) [e16din](https://github.com/e16din)

## License MIT

Copyright (c) 2015-2016 节操精选 http://jiecao.fm

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

[1]: ./screenshots/j7.jpg
