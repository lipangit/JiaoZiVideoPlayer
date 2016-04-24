#节操视频播放器 

[![Platform](https://img.shields.io/badge/platform-android-green.svg)](http://developer.android.com/index.html) 
[![Maven Central](https://img.shields.io/badge/Maven%20Central-3.0_preview-green.svg)](http://search.maven.org/#artifactdetails%7Cfm.jiecao%7Cjiecaovideoplayer%7C3.0_preview%7Caar) 
[![Licenses](https://img.shields.io/badge/license-MIT-green.svg)](http://choosealicense.com/licenses/mit/) 
[![Android Arsenal](https://img.shields.io/badge/Android%20Arsenal-jiecaovideoplayer-green.svg?style=true)](https://android-arsenal.com/details/1/3269)
[![GitHub stars](https://img.shields.io/github/stars/lipangit/jiecaovideoplayer.svg?style=social&label=Star)]()

真正实现Android的全屏功能，立志成为Android平台使用最广泛的视频播放控件

##主要特点
1. 全屏时启动新`Activity`实现播放器真正的全屏功能
2. 能在`ListView`、`ViewPager`和`ListView`、`ViewPager`和`Fragment`等多重嵌套模式下全屏工作
3. `ListView`的拖拽和`ViewPager`的滑动时如果划出屏幕会自动重置视频
4. 视频大小的屏幕适配，宽或长至少有两个对边是充满屏幕的，另外两个方向居中
5. 可以在加载、暂停、播放等各种状态中正常进入全屏和退出全屏
6. 可以完全自定义UI
7. 播放MP3时显示缩略图片
8. 占用空间非常小，不到50k

##效果

**[jiecaovideoplayer-3.0_preview-demo.apk](https://raw.githubusercontent.com/lipangit/jiecaovideoplayer/develop/downloads/jiecaovideoplayer-3.0_preview-demo.apk)**

![Demo Screenshot][1]

视频演示 : http://v.youku.com/v_show/id_XMTQ2NzUwOTcyNA==.html?firsttime=0&from=y1.4-2

##使用
1.添加类库,稳定版是 fm.jiecao:jiecaovideoplayer:2.0_final
```gradle
compile 'fm.jiecao:jiecaovideoplayer:3.0_preview'
```

或直接下载

* [jiecaovideoplayer-3.0_preview.aar](https://raw.githubusercontent.com/lipangit/jiecaovideoplayer/develop/downloads/jiecaovideoplayer-3.0_preview.aar)
* [jiecaovideoplayer-3.0_preview-javadoc.jar](https://raw.githubusercontent.com/lipangit/jiecaovideoplayer/develop/downloads/jiecaovideoplayer-3.0_preview-javadoc.jar)
* [jiecaovideoplayer-3.0_preview-sources.jar](https://raw.githubusercontent.com/lipangit/jiecaovideoplayer/develop/downloads/jiecaovideoplayer-3.0_preview-sources.jar)

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
jcVideoPlayerStandard.setUp("http://2449.vod.myqcloud.com/2449_bfbbfa3cea8f11e5aac3db03cda99974.f20.mp4"
                , "嫂子想我没");
jcVideoPlayerStandard.ivThumb.setThumbInCustomProject("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
```

4.在包含播放器的`Fragment`或`Activity`的`onPause()`方法中调用`JCVideoPlayer.releaseAllVideos();`

####其他接口

直接进入全屏，比如在webview中视频播放的适配很麻烦很无头绪，调用此接口直接全屏播放
```java
JCFullScreenActivity.toActivity(this,
    "http://2449.vod.myqcloud.com/2449_43b6f696980311e59ed467f22794e792.f20.mp4",
    "嫂子别摸我");
```

用代码控制播放按钮的点击,如果是普通状态会播放视频，如果是播放中会暂停视频
```java
jcVideoPlayerStandard.ivStart.performClick();
```

####混淆
```
无需添加
```

##[自定义UI](./README_CUSTOM_UI-ZH.md)

##贡献者

节操精选Android小组([Nathen](https://github.com/lipangit) [Derlio](https://github.com/derlio)) [zhangzzqq](https://github.com/zhangzzqq) [carmelo-ruota](https://github.com/carmelo-ruota)

## License MIT

Copyright (c) 2015-2016 节操精选 http://jiecao.fm

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

[1]: ./screenshots/j3.jpg
