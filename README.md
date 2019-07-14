<a href="https://github.com/lipangit/JiaoZiVideoPlayer" target="_blank"><p align="center"><img src="https://user-images.githubusercontent.com/2038071/42033014-0bf1c0b0-7b0e-11e8-811d-7639bcd294eb.png" alt="JiaoZiVideoPlayer" height="150px"></p></a>
--
<p align="center">
<a href="http://developer.android.com/index.html"><img src="https://img.shields.io/badge/platform-android-green.svg"></a>
<a href="http://search.maven.org/#artifactdetails%7Ccn.jzvd%7Cjiaozivideoplayer%7C5.8.2%7Caar"><img src="https://img.shields.io/badge/Maven%20Central-7.0.5-green.svg"></a>
<a href="http://choosealicense.com/licenses/mit/"><img src="https://img.shields.io/badge/license-MIT-green.svg"></a>
<a href="https://android-arsenal.com/details/1/3269"><img src="https://img.shields.io/badge/Android%20Arsenal-jiaozivideoplayer-green.svg?style=true"></a>
</p>

高度自定义的安卓视频播框架

## 置顶消息：

Q群:490442439, 2群:761899104, 验证信息:jzvd, 微信公众号:jzvdjzt，微信:lipanhelloworld，QQ:1066666651，[Telegram](https://t.me/jiaozitoken)

为了增加项目质量，促进项目进度，调用社群力量，方便社群管理，推出基于以太坊erc-20的数字通证[JiaoZiToken(JZT)(饺子Token)](https://github.com/lipangit/JZT)，通俗点理解，谁给饺子视频播放器写代码、出主意、解决用户问题、活跃社群关系、关注项目进展，就给谁饺子Token。将来会让项目更加丰富，更加精致，必定大有可为。

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

## 必读

#### 首先

1. 通读ReadMe
2. 下载安装demo apk [jiaozivideoplayer-7.0.5.apk](https://github.com/lipangit/JiaoZiVideoPlayer/releases/download/v7.0.5/jiaozivideoplayer-7.0.5.apk)，各个页面都进入一次，各个按钮点一次
3. 下载调试develop分支，有针对性的通过效果找到实现的源码
4. 继承JzvdStd，实现自己的需求

#### 必读文章

- [入门文档 1](https://juejin.im/entry/5cb838b5518825186d65430a)
- [入门文档 2](https://shimo.im/docs/xj5F85W1gqEEBXRJ)
- [Wiki](https://github.com/lipangit/JiaoZiVideoPlayer/wiki)，很久没更新了
- [English Wiki](https://github.com/felipetorres/VideoPlayer-Wiki)
- [Weibo](http://weibo.com/2342820395/profile?topnav=1&wvr=6&is_all=1)

- [公众号文章](https://github.com/lipangit/JiaoZiVideoPlayer/wiki/%E5%85%AC%E4%BC%97%E5%8F%B7%E6%96%87%E7%AB%A0)

#### 提问必读

- [常见问题](https://github.com/lipangit/JiaoZiVideoPlayer/wiki/常见问题)，查找已经解决的问题
- 有问题请到Issue提问，我会第一时间回复，然后加我QQ微信(请备注)。
- 到群里提问，请按照Issue模板，说清楚问题的情况，有管理员接应，解决不了的再找群主。

#### 参与项目必读

- [加入我们](https://github.com/lipangit/JiaoZiVideoPlayer/wiki/加入我们)
- [未解决问题](https://github.com/lipangit/JiaoZiVideoPlayer/wiki/未解决问题)，群主解决不了的，需要大家帮助的问题

## 效果

![Demo Screenshot][1]

## 使用

即便是自定义UI，或者对Library有过修改，把自定义的逻辑写到继承JzvdStd的类中，然后依然通过如下骤调用播放器。

#### 注意：
1.7.0版本之后要在JzvdStd外面包一层Layout
2.如果引入配置失败，根据失败的log检查是否添加了Java8的配置，或者升级环境到最新的稳定版

1.添加类库
```gradle
compile 'cn.jzvd:jiaozivideoplayer:7.0.5'
```

2.添加布局
```xml
<LinearLayout
    android:layout_width="match_parent"
    android:layout_height="200dp">
    <cn.jzvd.demo.CustomJzvd.MyJzvdStd
        android:id="@+id/jz_video"
        android:layout_width="match_parent"
        android:layout_height="200dp" />
</LinearLayout>
```

3.设置视频地址、缩略图地址、标题
```java
MyJzvdStd jzvdStd = (MyJzvdStd) findViewById(R.id.jz_video);
jzvdStd.setUp("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4"
                            , "饺子闭眼睛");
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

6.在`proguard-rules.pro`中按需添加
```
-keep public class cn.jzvd.JZMediaSystem {*; }
-keep public class cn.jzvd.demo.CustomMedia.CustomMedia {*; }
-keep public class cn.jzvd.demo.CustomMedia.JZMediaIjk {*; }
-keep public class cn.jzvd.demo.CustomMedia.JZMediaSystemAssertFolder {*; }

-keep class tv.danmaku.ijk.media.player.** {*; }
-dontwarn tv.danmaku.ijk.media.player.*
-keep interface tv.danmaku.ijk.media.player.** { *; }
```


## License MIT

Copyright (c) 2015-2019 李盼

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

[1]: https://user-images.githubusercontent.com/2038071/31045150-a077cc8a-a5a2-11e7-8dc2-7a0e3a9f3e62.jpg