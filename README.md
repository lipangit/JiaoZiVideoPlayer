<a href="https://github.com/lipangit/JieCaoVideoPlayer" target="_blank"><img src="https://raw.githubusercontent.com/lipangit/JieCaoVideoPlayer/develop/screenshots/logo2x.png" style="max-width:100%;"></a>
--
<p align="center">
<a href="http://developer.android.com/index.html"><img src="https://img.shields.io/badge/platform-android-green.svg"></a>
<a href="http://search.maven.org/#artifactdetails%7Cfm.jiecao%7Cjiecaovideoplayer%7C4.6.2%7Caar"><img src="https://img.shields.io/badge/Maven%20Central-4.6.2-green.svg"></a>
<a href="http://choosealicense.com/licenses/mit/"><img src="https://img.shields.io/badge/license-MIT-green.svg"></a>
<a href="https://android-arsenal.com/details/1/3269"><img src="https://img.shields.io/badge/Android%20Arsenal-jiecaovideoplayer-green.svg?style=true"></a>
</p>

* This project need translators, mother language is english, you can change everything edit readme, release note, formate variable and annotation.

Ambition is become the most widely used video playback control.


[中文文档](README-ZH.md)

## Features

1. Video fullscreen and float tiny window
2. Completely custom ui
3. In `ListView`、`ViewPager` and `ListView`、`ViewPager` and `Fragment` and other nested fragments and views situation, it works well
4. Gestrues to modify progress and volume
5. Adaptive to the screen size, where at least the width or length of the video is adaptive while the other  is centered on the screen
6. It will not disturb or change the playing state when entering or exiting fullscreen
7. [Support hls,rtsp](https://github.com/Bilibili/ijkplayer)
8. Put head data
9. Fullscreen on rotation (configChanges)

## Effect

**[jiecaovideoplayer-4.6.2-demo.apk](https://raw.githubusercontent.com/lipangit/jiecaovideoplayer/develop/downloads/jiecaovideoplayer-4.6.2-demo.apk)**

![Demo Screenshot][1]

## Usage

1.Import library
```gradle
compile 'fm.jiecao:jiecaovideoplayer:4.6.2'
```

Or download lib

* [jiecaovideoplayer-4.6.2.aar](https://raw.githubusercontent.com/lipangit/jiecaovideoplayer/develop/downloads/jiecaovideoplayer-4.6.2.aar)
* [jiecaovideoplayer-4.6.2-javadoc.jar](https://raw.githubusercontent.com/lipangit/jiecaovideoplayer/develop/downloads/jiecaovideoplayer-4.6.2-javadoc.jar)
* [jiecaovideoplayer-4.6.2-sources.jar](https://raw.githubusercontent.com/lipangit/jiecaovideoplayer/develop/downloads/jiecaovideoplayer-4.6.2-sources.jar)

2.Add JCVideoPlayer in your layout
```xml
<fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard
    android:id="@+id/custom_videoplayer_standard"
    android:layout_width="match_parent"
    android:layout_height="200dp"/>
```

3.Set the video uri, video thumb url and video title
```java
JCVideoPlayerStandard jcVideoPlayerStandard = (JCVideoPlayerStandard) findViewById(R.id.custom_videoplayer_standard);
jcVideoPlayerStandard.setUp("http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4"
                            , JCVideoPlayerStandard.SCREEN_LAYOUT_LIST, "嫂子闭眼睛");
jcVideoPlayerStandard.thumbImageView.setThumbInCustomProject("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
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

#### Other APIs

Start fullscreen directly.
```java
JCVideoPlayerStandard.startFullscreen(this, JCVideoPlayerStandard.class, "http://2449.vod.myqcloud.com/2449_22ca37a6ea9011e5acaaf51d105342e3.f20.mp4", "嫂子辛苦了");
```

Start fullscreen on device rotation.
Manifest
```xml
android:configChanges="keyboardHidden|orientation|screenSize"
```
Activity
```java
@Override
public void onConfigurationChanged(Configuration newConfig) {
    super.onConfigurationChanged(newConfig);
    switch (newConfig.orientation) {
        case Configuration.ORIENTATION_PORTRAIT:
            JCVideoPlayer.backPress();
            break;
        case Configuration.ORIENTATION_LANDSCAPE:
            if(JCVideoPlayerStandard.currentState == JCVideoPlayer.CURRENT_STATE_NORMAL) {
                JCVideoPlayerStandard.startPlayLocic();
            }
            JCVideoPlayerStandard.startWindowFullscreen(0);
            break;
    }
}
```
0 is rotation degree

ProGuard
```
-keep class tv.danmaku.ijk.** { *; }
-dontwarn tv.danmaku.ijk.**
```

Play video in assets you should copy to local path first.[Try by myself like this](https://github.com/Bilibili/ijkplayer/issues/1013)textureview will stop on first frame and error on logcat

##[Custom UI](./README_CUSTOM_UI.md)

##Contributors

[Nathen](https://github.com/lipangit) [Derlio](https://github.com/derlio) [zhangzzqq](https://github.com/zhangzzqq) [carmelo-ruota](https://github.com/carmelo-ruota) [wxxsw](https://github.com/wxxsw) [Miguel Aragues](https://github.com/Maragues) [e16din](https://github.com/e16din)

## License MIT

Copyright (c) 2015-2016 节操精选 http://jiecao.fm

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

[1]: ./screenshots/j7.jpg

