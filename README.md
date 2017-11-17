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

[中文文档](README-ZH.md) [WorkPlan](https://github.com/lipangit/JiaoZiVideoPlayer/projects/2) [Weibo](http://weibo.com/2342820395/profile?topnav=1&wvr=6&is_all=1) [VideoTutorial](https://github.com/lipangit/JiaoZiVideoPlayer/wiki/%E8%A7%86%E9%A2%91%E6%95%99%E7%A8%8B)

## Features

1. You can completely customize the UI and any function
2. Support https and rtsp agreement, more agreement please click [here](https://developer.android.com/guide/topics/media/media-formats.html)
3. Less than 110kb
4. Can achieve full-screen playback, small window playback
5. The perfect list of sliding judgment
6. Full-screen work in multiple nested modes like ListView, ViewPager and ListView, ViewPager and Fragment
7. Can load, pause, play and other normal state into the full screen and exit full screen
8. A variety of video adaptation screen mode, can be covered full screen, you can cut full screen
9. Gravity sensing automatically enter full screen
10. Full screen gestures to modify the progress and volume
11. Home key to exit the interface to suspend the playback, return to the interface to continue playing
12. WebView Nested Local Video Controls

For small and medium-sized companies, I think the final solution to video in android is `android.media.MediaPlayer`, other players are not in the trend, even if the `android.media.MediaPlayer` has disavantages we should make concession, if `android.media.MediaPlayer` has defect we will consider to change other player.

## Effect

**[jiaozivideoplayer-6.1.2.apk](https://github.com/lipangit/JiaoZiVideoPlayer/releases/download/v6.1.2/jiaozivideoplayer-6.1.2.apk)**

![Demo Screenshot][1]

[List sliding automatically into the small window effect](http://weibo.com/tv/v/FtxpWgqmg?fid=1034:5cda6fc7f394b403d592bd9b1d5a9701)

## Usage

Even the custom UI, or has changed to the Library, is also the five steps to use the player.

1.Import library
```gradle
compile 'cn.jzvd:jiaozivideoplayer:6.1.2'
```

Or download [lib](https://github.com/lipangit/JiaoZiVideoPlayer/releases/tag/v6.1.2) (not recommended)

2.Add JZVideoPlayer in your layout
```xml
<cn.jzvd.JZVideoPlayerStandard
    android:id="@+id/videoplayer"
    android:layout_width="match_parent"
    android:layout_height="200dp"/>
```

3.Set the video uri, video thumb url and video title
```java
JZVideoPlayerStandard jzVideoPlayerStandard = (JZVideoPlayerStandard) findViewById(R.id.videoplayer);
jzVideoPlayerStandard.setUp("http://jzvd.nathen.cn/c6e3dc12a1154626b3476d9bf3bd7266/6b56c5f0dc31428083757a45764763b0-5287d2089db37e62345123a1be272f8b.mp4"
                            , JZVideoPlayerStandard.SCREEN_WINDOW_NORMAL, "饺子闭眼睛");
jzVideoPlayerStandard.thumbImageView.setImage("http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640");
```

4.In `Activity`
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

5.In AndroidManifest.xml
```
<activity
    android:name=".MainActivity"
    android:configChanges="orientation|screenSize|keyboardHidden"
    android:screenOrientation="portrait" /> <!-- or android:screenOrientation="landscape"-->
```

## [Wiki](https://github.com/lipangit/JiaoZiVideoPlayer/wiki)

* Simple use

1. [QuickStart](https://github.com/lipangit/JiaoZiVideoPlayer/wiki/QuickStart)
2. [In ListView](https://github.com/lipangit/JiaoZiVideoPlayer/wiki/%E5%88%97%E8%A1%A8%E6%92%AD%E6%94%BE)
3. [Tiny window](https://github.com/lipangit/JiaoZiVideoPlayer/wiki/%E5%B0%8F%E7%AA%97%E6%92%AD%E6%94%BE)
4. [Directly fullscreen](https://github.com/lipangit/JiaoZiVideoPlayer/wiki/%E7%9B%B4%E6%8E%A5%E5%85%A8%E5%B1%8F%E6%92%AD%E6%94%BE)
5. [API](https://github.com/lipangit/JiaoZiVideoPlayer/wiki/API%E7%9A%84%E4%BD%BF%E7%94%A8)

* Customize

1. [Custom code](https://github.com/lipangit/JiaoZiVideoPlayer/wiki/%E8%87%AA%E5%AE%9A%E4%B9%89%E4%BB%A3%E7%A0%81)
2. [Custom code sample](https://github.com/lipangit/JiaoZiVideoPlayer/wiki/%E8%87%AA%E5%AE%9A%E4%B9%89%E4%BB%A3%E7%A0%81%E7%A4%BA%E4%BE%8B)
3. [Custom UI](https://github.com/lipangit/JiaoZiVideoPlayer/wiki/%E8%87%AA%E5%AE%9A%E4%B9%89UI)
4. [Custom UI sample](https://github.com/lipangit/JiaoZiVideoPlayer/wiki/%E8%87%AA%E5%AE%9A%E4%B9%89UI%E7%A4%BA%E4%BE%8B)

[Common problem](https://github.com/lipangit/JiaoZiVideoPlayer/wiki/%E5%B8%B8%E8%A7%81%E9%97%AE%E9%A2%98)


## [Job diversion](https://github.com/lipangit/JiaoZiVideoPlayer/wiki#%E5%B7%A5%E4%BD%9C%E5%88%86%E6%B5%81) 

Need translator to make English native.

* [Group management](https://github.com/lipangit/JiaoZiVideoPlayer/wiki#%E7%BE%A4%E7%AE%A1%E7%90%86)

1. [熊晓清](http://blog.csdn.net/yaya_xiong) QQ:137048616
2. [Lionet](https://github.com/Lionet6?tab=repositories) QQ:2950527715
3. [montauk](https://github.com/hanmeimei888) QQ:958489121
4. [张展硕]() QQ:229431468

* [Questions and Answers](https://github.com/lipangit/JiaoZiVideoPlayer/wiki#%E9%97%AE%E9%A2%98%E8%A7%A3%E7%AD%94)

1. [熊晓清](http://blog.csdn.net/yaya_xiong) QQ:137048616
2. [の伤也快乐](https://github.com/jmhjmh) QQ:466278628

* [Wiki finishing](https://github.com/lipangit/JiaoZiVideoPlayer/wiki#wiki%E6%95%B4%E7%90%86)

* [Release version](https://github.com/lipangit/JiaoZiVideoPlayer/wiki#%E5%8F%91%E7%89%88)

## [Task release](https://github.com/lipangit/JiaoZiVideoPlayer/wiki#%E4%BB%BB%E5%8A%A1%E5%8F%91%E5%B8%83)

Need translator to make English native.

## [Reward](https://github.com/lipangit/JiaoZiVideoPlayer/wiki#%E6%89%93%E8%B5%8F%E5%92%8C%E5%92%A8%E8%AF%A2)

This project is dedicated to small and medium-sized app integrated video playback, greatly reducing development costs, if you save a ton of time, the proposed retail price: reward 76 USD. (Not expensive you brothers, get two years of the project than the female anchor song song it)

![Reward][2]

## License MIT

Copyright (c) 2015-2016 李盼 Nathen

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.

[1]: https://user-images.githubusercontent.com/2038071/31045150-a077cc8a-a5a2-11e7-8dc2-7a0e3a9f3e62.jpg
[2]: https://user-images.githubusercontent.com/2038071/29978804-45c321ba-8f75-11e7-9040-776d3b6dca1f.jpg
