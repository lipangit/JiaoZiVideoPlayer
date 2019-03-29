<a href="https://github.com/lipangit/JiaoZiVideoPlayer" target="_blank"><p align="center"><img src="https://user-images.githubusercontent.com/2038071/42033014-0bf1c0b0-7b0e-11e8-811d-7639bcd294eb.png" alt="JiaoZiVideoPlayer" height="150px"></p></a>
--
<p align="center">
<a href="http://developer.android.com/index.html"><img src="https://img.shields.io/badge/platform-android-green.svg"></a>
<a href="http://search.maven.org/#artifactdetails%7Ccn.jzvd%7Cjiaozivideoplayer%7C5.8.2%7Caar"><img src="https://img.shields.io/badge/Maven%20Central-7.0_preview-green.svg"></a>
<a href="http://choosealicense.com/licenses/mit/"><img src="https://img.shields.io/badge/license-MIT-green.svg"></a>
<a href="https://android-arsenal.com/details/1/3269"><img src="https://img.shields.io/badge/Android%20Arsenal-jiaozivideoplayer-green.svg?style=true"></a>
</p>

高度自定义的安卓视频播放器

Q群: 490442439 2群: 761899104 验证信息:jzvd

## 主要特点

1. 可以完全自定义UI和任何功能
2. 一行代码切换播放引擎，支持的视频格式和协议取决于播放引擎，[android.media.MediaPlayer](https://developer.android.com/guide/topics/media/media-formats.html) [ijkplayer](https://github.com/Bilibili/ijkplayer)
3. 完美检测列表滑动
4. 可实现全屏播放，小窗播放
5. 能在`ListView`、`ViewPager`和`ListView`、`ViewPager`和`Fragment`等多重嵌套模式下全屏工作
6. 可以在加载、暂停、播放等各种状态中正常进入全屏和退出全屏
7. 多种视频适配屏幕的方式，可铺满全屏，可以全屏剪裁
8. 重力感应自动进入全屏
9. 全屏后手势修改进度和音量
10. Home键退出界面暂停播放，返回界面继续播放
11. WebView嵌套本地视频控件
12. demo中添加视频缓存的例子
13. 倍速播放

## 使用步骤

1. 通读ReadMe
2. 下载安装demo apk [jiaozivideoplayer-7.0_preview.apk](https://github.com/lipangit/JiaoZiVideoPlayer/releases/download/v6.4.2/jiaozivideoplayer-7.0_preview.apk)，各个页面都进入一次，各个按钮点一次
3. 下载调试develop分支，有针对性的通过效果找到实现的源码
4. 看[自定义相关的WIKI](https://github.com/lipangit/JiaoZiVideoPlayer/wiki)，实现自己的需求

* [入门文档 1](https://www.jianshu.com/p/4c187a09b838)
* [入门文档 2](https://shimo.im/docs/xj5F85W1gqEEBXRJ)

## 效果

![Demo Screenshot][1]

[列表滑动自动进入小窗的效果](http://weibo.com/tv/v/FtxpWgqmg?fid=1034:5cda6fc7f394b403d592bd9b1d5a9701)

## 使用

即便是自定义UI，或者对Library有过修改，也是这五步骤来使用播放器。

1.添加类库
```gradle
compile 'cn.jzvd:jiaozivideoplayer:7.0_preview'
```

或直接下载 [aar](https://github.com/lipangit/JiaoZiVideoPlayer/releases/tag/v6.4.2) (不建议)

2.添加布局
```xml
<cn.jzvd.JzvdStd
    android:id="@+id/videoplayer"
    android:layout_width="match_parent"
    android:layout_height="200dp"/>
```

3.设置视频地址、缩略图地址、标题
```java
JzvdStd jzvdStd = (JzvdStd) findViewById(R.id.videoplayer);
jzvdStd.setUp("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4"
                            , "饺子闭眼睛", Jzvd.SCREEN_WINDOW_NORMAL);
jzvdStd.thumbImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
```

4.在`Activity`中
```java
@Override
public void onBackPressed() {
    if (Jzvd.backPress()) {
        return;
    }
    super.onBackPressed();
}
@Override
protected void onPause() {
    super.onPause();
    Jzvd.releaseAllVideos();
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

* 常规使用

1. [QuickStart](https://github.com/lipangit/JiaoZiVideoPlayer/wiki/QuickStart)
2. [列表播放](https://github.com/lipangit/JiaoZiVideoPlayer/wiki/%E5%88%97%E8%A1%A8%E6%92%AD%E6%94%BE)
3. [小窗播放](https://github.com/lipangit/JiaoZiVideoPlayer/wiki/%E5%B0%8F%E7%AA%97%E6%92%AD%E6%94%BE)
4. [直接全屏播放](https://github.com/lipangit/JiaoZiVideoPlayer/wiki/%E7%9B%B4%E6%8E%A5%E5%85%A8%E5%B1%8F%E6%92%AD%E6%94%BE)
5. [API](https://github.com/lipangit/JiaoZiVideoPlayer/wiki/API%E7%9A%84%E4%BD%BF%E7%94%A8)

* 自定义

1. [自定义代码](https://github.com/lipangit/JiaoZiVideoPlayer/wiki/%E8%87%AA%E5%AE%9A%E4%B9%89%E4%BB%A3%E7%A0%81)
2. [自定义代码示例](https://github.com/lipangit/JiaoZiVideoPlayer/wiki/%E8%87%AA%E5%AE%9A%E4%B9%89%E4%BB%A3%E7%A0%81%E7%A4%BA%E4%BE%8B)
3. [自定义UI](https://github.com/lipangit/JiaoZiVideoPlayer/wiki/%E8%87%AA%E5%AE%9A%E4%B9%89UI)
4. [自定义UI示例](https://github.com/lipangit/JiaoZiVideoPlayer/wiki/%E8%87%AA%E5%AE%9A%E4%B9%89UI%E7%A4%BA%E4%BE%8B)

[常见问题](https://github.com/lipangit/JiaoZiVideoPlayer/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98)


## [工作分流](https://github.com/lipangit/JiaoZiVideoPlayer/wiki#%E5%B7%A5%E4%BD%9C%E5%88%86%E6%B5%81) 

老臣精力能力有限，希望和志同道合的朋友一起把项目做好，感兴趣的同学随时和我报名

* [群管理](https://github.com/lipangit/JiaoZiVideoPlayer/wiki#%E7%BE%A4%E7%AE%A1%E7%90%86)

1. [熊晓清](http://blog.csdn.net/yaya_xiong) QQ:137048616
2. [Lionet](https://github.com/Lionet6?tab=repositories) QQ:2950527715
3. [montauk](https://github.com/hanmeimei888) QQ:958489121
4. [张展硕]() QQ:229431468

* [问题解答](https://github.com/lipangit/JiaoZiVideoPlayer/wiki#%E9%97%AE%E9%A2%98%E8%A7%A3%E7%AD%94)

1. [熊晓清](http://blog.csdn.net/yaya_xiong) QQ:137048616
2. [の伤也快乐](https://github.com/jmhjmh) QQ:466278628
3. [吴亚男]() QQ:623562486

* [wiki整理](https://github.com/lipangit/JiaoZiVideoPlayer/wiki#wiki%E6%95%B4%E7%90%86)

* [发版](https://github.com/lipangit/JiaoZiVideoPlayer/wiki#%E5%8F%91%E7%89%88)

## [任务发布](https://github.com/lipangit/JiaoZiVideoPlayer/wiki#%E4%BB%BB%E5%8A%A1%E5%8F%91%E5%B8%83)

## [打赏](https://github.com/lipangit/JiaoZiVideoPlayer/wiki#%E6%89%93%E8%B5%8F%E5%92%8C%E5%92%A8%E8%AF%A2)

![打赏][2]

## License MIT

Copyright (c) 2015-2018 李盼 Nathen

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

[1]: https://user-images.githubusercontent.com/2038071/31045150-a077cc8a-a5a2-11e7-8dc2-7a0e3a9f3e62.jpg
[2]: https://user-images.githubusercontent.com/2038071/29978804-45c321ba-8f75-11e7-9040-776d3b6dca1f.jpg