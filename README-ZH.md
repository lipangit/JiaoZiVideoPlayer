<a href="https://github.com/lipangit/JieCaoVideoPlayer" target="_blank"><img src="https://user-images.githubusercontent.com/2038071/29994158-7e65546c-8ffb-11e7-80fd-c630e2a36135.png" style="max-width:100%;"></a>
--
<p align="center">
<a href="http://developer.android.com/index.html"><img src="https://img.shields.io/badge/platform-android-green.svg"></a>
<a href="http://search.maven.org/#artifactdetails%7Cfm.jiecao%7Cjiecaovideoplayer%7C4.6.3%7Caar"><img src="https://img.shields.io/badge/Maven%20Central-5.8.1-green.svg"></a>
<a href="http://choosealicense.com/licenses/mit/"><img src="https://img.shields.io/badge/license-MIT-green.svg"></a>
<a href="https://android-arsenal.com/details/1/3269"><img src="https://img.shields.io/badge/Android%20Arsenal-jiecaovideoplayer-green.svg?style=true"></a>
</p>

#### 立志成为Android平台使用最广泛的视频播放控件  Q群:490442439 验证信息:jcvd

目前认为安卓中视频点播的终极状态是系统的MediaPlayer，其他的播放引擎并不是未来的趋势，虽然系统的MediaPlayer也有缺点我认为我们应该做出一些让步，如果系统的MediaPlayer没有重大缺陷将不会考虑替换它。

[英文文档](https://github.com/lipangit/JieCaoVideoPlayer)           [工作计划](https://github.com/lipangit/JieCaoVideoPlayer/projects/2)

## 主要特点

1. 视频全屏播放和浮层小窗播放
2. 可以完全自定义UI
3. 能在`ListView`、`ViewPager`和`ListView`、`ViewPager`和`Fragment`等多重嵌套模式下全屏工作
4. 手势修改进度和音量
5. 视频大小的屏幕适配，宽或长至少有两个对边是充满屏幕的，另外两个方向居中
6. 可以在加载、暂停、播放等各种状态中正常进入全屏和退出全屏
7. 重力感应自动全屏
8. WebView嵌套本地视频控件
9. [支持https和rtsp](https://developer.android.com/guide/topics/media/media-formats.html)
10. 小于 100kb

## 效果

**[jiecaovideoplayer-5.8.1.apk](https://github.com/lipangit/JieCaoVideoPlayer/releases/download/v5.8.1/jiecaovideoplayer-5.8.1.apk)**

![Demo Screenshot][1]

## 使用

即便是自定义UI，或者对Library有过修改，也是这五步骤来使用播放器。

1.添加类库
```gradle
compile 'fm.jiecao:jiecaovideoplayer:5.8.1'
```

[或直接下载jar包](https://github.com/lipangit/JieCaoVideoPlayer/releases/tag/v5.8.1) 不建议

2.添加布局
```xml
<fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard
    android:id="@+id/videoplayer"
    android:layout_width="match_parent"
    android:layout_height="200dp"/>
```

3.设置视频地址、缩略图地址、标题
```java
JCVideoPlayerStandard jcVideoPlayerStandard = (JCVideoPlayerStandard) findViewById(R.id.videoplayer);
jcVideoPlayerStandard.setUp("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4"
                            , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "嫂子闭眼睛");
jcVideoPlayerStandard.thumbImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
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

5.在`AndroidManifest.xml`中
```
<activity
    android:name=".MainActivity"
    android:configChanges="orientation|screenSize|keyboardHidden"
    android:screenOrientation="portrait" /> <!-- or android:screenOrientation="landscape"-->
```

## [Wiki](https://github.com/lipangit/JieCaoVideoPlayer/wiki)

## 打赏

![打赏][2]

## License MIT

Copyright (c) 2015-2016 李盼 Nathen

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

[1]: https://user-images.githubusercontent.com/2038071/29037042-7e4a1c6e-7bd4-11e7-8e25-5408d138abcd.jpg
[2]: https://user-images.githubusercontent.com/2038071/29978804-45c321ba-8f75-11e7-9040-776d3b6dca1f.jpg