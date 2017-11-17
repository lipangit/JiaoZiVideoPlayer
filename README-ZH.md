<a href="https://github.com/lipangit/JiaoZiVideoPlayer" target="_blank"><img src="https://user-images.githubusercontent.com/2038071/29994158-7e65546c-8ffb-11e7-80fd-c630e2a36135.png" style="max-width:100%;"></a>
--
<p align="center">
<a href="http://developer.android.com/index.html"><img src="https://img.shields.io/badge/platform-android-green.svg"></a>
<a href="http://search.maven.org/#artifactdetails%7Ccn.jzvd%7Cjiaozivideoplayer%7C5.8.2%7Caar"><img src="https://img.shields.io/badge/Maven%20Central-6.1.2-green.svg"></a>
<a href="http://choosealicense.com/licenses/mit/"><img src="https://img.shields.io/badge/license-MIT-green.svg"></a>
<a href="https://android-arsenal.com/details/1/3269"><img src="https://img.shields.io/badge/Android%20Arsenal-jiaozivideoplayer-green.svg?style=true"></a>
</p>

# [斗鱼直播](https://www.douyu.com/1667893)

周一到周六每天上午9:30开始直播写代码，回答大家的使用问题，请大家进入直播间后提问，直播期间不再手动打字回复，关注数到100的时候抽奖100元(两个50)

Q群:490442439 验证信息:jzvd

[英文文档](https://github.com/lipangit/JiaoZiVideoPlayer) [工作计划](https://github.com/lipangit/JiaoZiVideoPlayer/projects/2) [微博](http://weibo.com/2342820395/profile?topnav=1&wvr=6&is_all=1) [视频教程](https://github.com/lipangit/JiaoZiVideoPlayer/wiki/%E8%A7%86%E9%A2%91%E6%95%99%E7%A8%8B)

## 主要特点

1. 可以完全自定义UI和任何功能
2. 支持https和rtsp协议，更多协议请点[这里](https://developer.android.com/guide/topics/media/media-formats.html)
3. 小于 110kb
4. 可实现全屏播放，小窗播放
5. 完美的列表滑动的判断
6. 能在`ListView`、`ViewPager`和`ListView`、`ViewPager`和`Fragment`等多重嵌套模式下全屏工作
7. 可以在加载、暂停、播放等各种状态中正常进入全屏和退出全屏
8. 多种视频适配屏幕的方式，可铺满全屏，可以全屏剪裁
9. 重力感应自动进入全屏
10. 全屏后手势修改进度和音量
11. Home键退出界面暂停播放，返回界面继续播放
12. WebView嵌套本地视频控件

对于中小公司而言，目前认为安卓中视频点播的终极状态是系统的MediaPlayer，其他的播放引擎并不是未来的趋势，虽然系统的MediaPlayer也有缺点我认为我们应该做出一些让步，如果系统的MediaPlayer没有重大缺陷将不会考虑替换它。

## 效果

**[jiaozivideoplayer-6.1.2.apk](https://github.com/lipangit/JiaoZiVideoPlayer/releases/download/v6.1.2/jiaozivideoplayer-6.1.2.apk)**

![Demo Screenshot][1]

[列表滑动自动进入小窗的效果](http://weibo.com/tv/v/FtxpWgqmg?fid=1034:5cda6fc7f394b403d592bd9b1d5a9701)

## 使用

即便是自定义UI，或者对Library有过修改，也是这五步骤来使用播放器。

1.添加类库
```gradle
compile 'cn.jzvd:jiaozivideoplayer:6.1.2'
```

或直接下载[jar包](https://github.com/lipangit/JiaoZiVideoPlayer/releases/tag/v6.1.2) (不建议)

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
                            , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "饺子闭眼睛");
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

* [wiki整理](https://github.com/lipangit/JiaoZiVideoPlayer/wiki#wiki%E6%95%B4%E7%90%86)

* [发版](https://github.com/lipangit/JiaoZiVideoPlayer/wiki#%E5%8F%91%E7%89%88)

## [任务发布](https://github.com/lipangit/JiaoZiVideoPlayer/wiki#%E4%BB%BB%E5%8A%A1%E5%8F%91%E5%B8%83)

## [打赏](https://github.com/lipangit/JiaoZiVideoPlayer/wiki#%E6%89%93%E8%B5%8F%E5%92%8C%E5%92%A8%E8%AF%A2)

这项目是专门给中小app集成视频播放的，极大降低开发成本，如果给您节省了成吨的时间，建议零售价:打赏500元

![打赏][2]

## License MIT

Copyright (c) 2015-2016 李盼 Nathen

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

[1]: https://user-images.githubusercontent.com/2038071/31045150-a077cc8a-a5a2-11e7-8dc2-7a0e3a9f3e62.jpg
[2]: https://user-images.githubusercontent.com/2038071/29978804-45c321ba-8f75-11e7-9040-776d3b6dca1f.jpg