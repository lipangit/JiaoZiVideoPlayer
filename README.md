<a href="https://github.com/lipangit/JieCaoVideoPlayer" target="_blank"><img src="https://github.com/lipangit/JieCaoVideoPlayer/releases/download/v4.8.0/logo2x.png" style="max-width:100%;"></a>
--
<p align="center">
<a href="http://developer.android.com/index.html"><img src="https://img.shields.io/badge/platform-android-green.svg"></a>
<a href="http://search.maven.org/#artifactdetails%7Cfm.jiecao%7Cjiecaovideoplayer%7C4.6.3%7Caar"><img src="https://img.shields.io/badge/Maven%20Central-5.5.1-green.svg"></a>
<a href="http://choosealicense.com/licenses/mit/"><img src="https://img.shields.io/badge/license-MIT-green.svg"></a>
<a href="https://android-arsenal.com/details/1/3269"><img src="https://img.shields.io/badge/Android%20Arsenal-jiecaovideoplayer-green.svg?style=true"></a>
</p>

* This project need translators, mother language is english, you can change everything edit readme, release note, formate variable and annotation.

####Ambition is become the most widely used video playback control. Q群:490442439 验证信息:jcvd

I think the final solution for play video in android is android.media.MediaPlayer, other player is not in the trend, even if the android.media.MediaPlayer have disavantages we should make concession, if android.media.MediaPlayer have defect we will think to change other player.

[中文文档](README-ZH.md)           [WorkPlan](https://github.com/lipangit/JieCaoVideoPlayer/projects/2)

## Features

1. Video fullscreen and float tiny window
2. Completely custom ui
3. In `ListView`、`ViewPager` and `ListView`、`ViewPager` and `Fragment` and other nested fragments and views situation, it works well
4. Gestrues to modify progress and volume
5. Adaptive to the screen size, where at least the width or length of the video is adaptive while the other  is centered on the screen
6. It will not disturb or change the playing state when entering or exiting fullscreen
7. Gravity sensor auto fullscreen
8. WebView with local video control
9. [Support https and rtsp](https://developer.android.com/guide/topics/media/media-formats.html)
10. Less than 100kb

## Effect

**[jiecaovideoplayer-5.5.1.apk](https://github.com/lipangit/JieCaoVideoPlayer/releases/download/v5.5.1/jiecaovideoplayer-5.5.1.apk)**

![Demo Screenshot][1]

## Usage

Even the custom UI, or has changed to the Library, is also the five steps to use the player.

1.Import library
```gradle
compile 'fm.jiecao:jiecaovideoplayer:5.5.1'
```

[Or download lib](https://github.com/lipangit/JieCaoVideoPlayer/releases/tag/v5.5.1)

2.Add JCVideoPlayer in your layout
```xml
<fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard
    android:id="@+id/videoplayer"
    android:layout_width="match_parent"
    android:layout_height="200dp"/>
```

3.Set the video uri, video thumb url and video title
```java
JCVideoPlayerStandard jcVideoPlayerStandard = (JCVideoPlayerStandard) findViewById(R.id.videoplayer);
jcVideoPlayerStandard.setUp("http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4"
                            , JCVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "嫂子闭眼睛");
jcVideoPlayerStandard.thumbImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
```

4.In `Activity`
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

5.In AndroidManifest.xml
```
<activity
    android:name=".MainActivity"
    android:configChanges="orientation|screenSize|keyboardHidden"
    android:screenOrientation="portrait" /> <!-- or android:screenOrientation="landscape"-->
```

####[Other APIs](https://github.com/lipangit/JieCaoVideoPlayer/wiki/API)

####[Custom UI](https://github.com/lipangit/JieCaoVideoPlayer/wiki/%E8%87%AA%E5%AE%9A%E4%B9%89UI)

##[Contributors](https://github.com/lipangit/JieCaoVideoPlayer/graphs/contributors)

##License MIT

Copyright (c) 2015-2016 节操精选 http://jiecao.fm

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

[1]: https://github.com/lipangit/JieCaoVideoPlayer/releases/download/v5.3/j9.jpg

