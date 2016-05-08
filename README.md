# JieCao Video Player  

[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html) 
[![Maven Central](https://img.shields.io/badge/Maven%20Central-3.0-green.svg)](http://search.maven.org/#artifactdetails%7Cfm.jiecao%7Cjiecaovideoplayer%7C3.0%7Caar) 
[![Licenses](https://img.shields.io/badge/license-MIT-green.svg)](http://choosealicense.com/licenses/mit/) 
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-jiecaovideoplayer-green.svg?style=true)](https://android-arsenal.com/details/1/3269)
[![GitHub stars](https://img.shields.io/github/stars/lipangit/jiecaovideoplayer.svg?style=social&label=Star)]()

Android truly full-screen capabilities, the Android platform to become the most widely used video playback controls

[中文文档](README-ZH.md)

## Features
1. Launching new Fullscreen Activity when playing video in fullscreen mode
2. Completely custom ui
3. In `ListView`、`ViewPager` and `ListView`、`ViewPager` and `Fragment` and other nested fragments and views situation, it works well
4. Video will be reset(pause) when it's scrolled out of the screen in `ListView` and `ViewPager`
5. Adaptive to the screen size, where at least the width or length of the video is adaptive while the other  is centered on the screen
6. It will not disturb or change the playing state when entering or exiting fullscreen
7. With limited storage, less than 50 kb

## Effect

**[jiecaovideoplayer-3.0-demo.apk](https://raw.githubusercontent.com/lipangit/jiecaovideoplayer/develop/downloads/jiecaovideoplayer-3.0-demo.apk)**

![Demo Screenshot][1]

Demo video : http://v.youku.com/v_show/id_XMTQ2NzUwOTcyNA==.html

## Usage
1.Import library
```gradle
compile 'fm.jiecao:jiecaovideoplayer:3.0'
```

Or download lib

* [jiecaovideoplayer-3.0.aar](https://raw.githubusercontent.com/lipangit/jiecaovideoplayer/develop/downloads/jiecaovideoplayer-3.0.aar)
* [jiecaovideoplayer-3.0-javadoc.jar](https://raw.githubusercontent.com/lipangit/jiecaovideoplayer/develop/downloads/jiecaovideoplayer-3.0-javadoc.jar)
* [jiecaovideoplayer-3.0-sources.jar](https://raw.githubusercontent.com/lipangit/jiecaovideoplayer/develop/downloads/jiecaovideoplayer-3.0-sources.jar)

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
jcVideoPlayerStandard.setUp("http://2449.vod.myqcloud.com/2449_bfbbfa3cea8f11e5aac3db03cda99974.f20.mp4"
                , "嫂子想我没");
jcVideoPlayerStandard.ivThumb.setThumbInCustomProject("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
```

4.Remember to invoke `JCVideoPlayer.releaseAllVideos();` in `onPause()` of `Fragment` or `Activity`

#### Other APIs

Invoke `JCFullScreenActivity.toActivity(...)` to enter fullscreen directly.
```java
JCFullScreenActivity.toActivity(this,
    "http://video.jiecao.fm/5/1/%E8%87%AA%E5%8F%96%E5%85%B6%E8%BE%B1.mp4",
    JCVideoPlayerStandard.class,
    "嫂子别摸我");
```

Control play button,if state is normal it will play, if state is playing it will pause
```java
jcVideoPlayerStandard.ivStart.performClick();
```

ProGuard
```
Need nothing
```

##[Custom UI](./README_CUSTOM_UI.md)

##Contributors

节操精选Android小组([Nathen](https://github.com/lipangit) [Derlio](https://github.com/derlio)) [zhangzzqq](https://github.com/zhangzzqq) [carmelo-ruota](https://github.com/carmelo-ruota)

## License MIT

Copyright (c) 2015-2016 节操精选 http://jiecao.fm

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

[1]: ./screenshots/j4.jpg

