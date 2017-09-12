<a href="https://github.com/lipangit/JiaoZiVideoPlayer" target="_blank"><img src="https://user-images.githubusercontent.com/2038071/29994158-7e65546c-8ffb-11e7-80fd-c630e2a36135.png" style="max-width:100%;"></a>
--
<p align="center">
<a href="http://developer.android.com/index.html"><img src="https://img.shields.io/badge/platform-android-green.svg"></a>
<a href="http://search.maven.org/#artifactdetails%7Ccn.jzvd%7Cjiaozivideoplayer%7C5.8.2%7Caar"><img src="https://img.shields.io/badge/Maven%20Central-6.0.0-green.svg"></a>
<a href="http://choosealicense.com/licenses/mit/"><img src="https://img.shields.io/badge/license-MIT-green.svg"></a>
<a href="https://android-arsenal.com/details/1/3269"><img src="https://img.shields.io/badge/Android%20Arsenal-jiaozivideoplayer-green.svg?style=true"></a>
</p>

#### 立志成为Android平台使用最广泛的视频播放控件  Q群:490442439 验证信息:jzvd

目前认为安卓中视频点播的终极状态是系统的MediaPlayer，其他的播放引擎并不是未来的趋势，虽然系统的MediaPlayer也有缺点我认为我们应该做出一些让步，如果系统的MediaPlayer没有重大缺陷将不会考虑替换它。

[英文文档](https://github.com/lipangit/JiaoZiVideoPlayer)           [工作计划](https://github.com/lipangit/JiaoZiVideoPlayer/projects/2)

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
10. 小于 110kb
11. [Home键暂停，返回继续播放](https://github.com/lipangit/JiaoZiVideoPlayer/blob/develop/app/src/main/java/cn/jzvd/demo/ApiActivity.java#L117)

## 效果

**[jiaozivideoplayer-6.0.0.apk](https://github.com/lipangit/JieCaoVideoPlayer/releases/download/v6.0.0/jiaozivideoplayer-6.0.0.apk)**

![Demo Screenshot][1]

## 使用

即便是自定义UI，或者对Library有过修改，也是这五步骤来使用播放器。

1.添加类库
```gradle
compile 'cn.jzvd:jiaozivideoplayer:6.0.0'
```

[或直接下载jar包](https://github.com/lipangit/JiaoZiVideoPlayer/releases/tag/v6.0.0) 不建议

2.添加布局
```xml
<cn.jzvd.JZVideoPlayerStandard
    android:id="@+id/videoplayer"
    android:layout_width="match_parent"
    android:layout_height="200dp"/>
```

3.设置视频地址、缩略图地址、标题
```java
JZVideoPlayerStandard jzVideoPlayerStandard = (JZVideoPlayerStandard) findViewById(R.id.videoplayer);
jzVideoPlayerStandard.setUp("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4"
                            , JZVideoPlayerStandard.SCREEN_LAYOUT_NORMAL, "嫂子闭眼睛");
jzVideoPlayerStandard.thumbImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
```

4.在`Activity`中
```java
@Override
public void onBackPressed() {
    if (JZVideoPlayer.backPress()) {
        return;
    }
    super.onBackPressed();
}
@Override
protected void onPause() {
    super.onPause();
    JZVideoPlayer.releaseAllVideos();
}
```

5.在`AndroidManifest.xml`中
```
<activity
    android:name=".MainActivity"
    android:configChanges="orientation|screenSize|keyboardHidden"
    android:screenOrientation="portrait" /> <!-- or android:screenOrientation="landscape"-->
```

## [Wiki](https://github.com/lipangit/JiaoZiVideoPlayer/wiki)

1. 讲解demo中已经有的例子

2. 讲解如何根据特定的需求自定义更多的功能和效果

3. [工作分流]() 因为老臣精力有限，需要大家的帮助，把自己的工作做了简单的拆分，希望大家踊跃报名，作为回报，会展示有贡献朋友的github和博客

* 群管理  --  首先是群内活跃成员，工作内容：管理群成员，整理群文件，宣传微信群等

* 问题解答  --  对库的使用有实践的应用，能解答常见的使用问题，耐心的帮助新手入门。工作内容：每周至少回答两个群里的问题，每周至少解决一个issue

* wiki整理  --  老臣对wiki理解不深，表达能力不强，写作能力一般，我觉得现在的wiki没法看，想用库的人很难从wiki获得有效的帮助，废话太多逻辑复杂，希望和大家讨论

* 发版  --  走发版流程

* 添加功能和bug修复 --  主要的代码工作

* 技术难点调研  --  好多问题老臣不会，希望得到大家的帮助

4. [任务发布]() 因为老臣水平有限，精力有限，思路局限，希望大家能贡献功能、修复bug、技术难点和大家讨论



## 打赏

![打赏][2]

## License MIT

Copyright (c) 2015-2016 李盼 Nathen

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

[1]: https://user-images.githubusercontent.com/2038071/29037042-7e4a1c6e-7bd4-11e7-8e25-5408d138abcd.jpg
[2]: https://user-images.githubusercontent.com/2038071/29978804-45c321ba-8f75-11e7-9040-776d3b6dca1f.jpg