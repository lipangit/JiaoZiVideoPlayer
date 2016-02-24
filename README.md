#节操视频播放器

真正实现Android的全屏功能，励志成为Android平台使用最广泛的视频播放控件，GitFlow流程开发develop分支是最新版本

##主要特点
1. 全屏时启动新Activity实现播放器真正的全屏功能
2. 可以在加载、暂停、播放等各种状态中正常进入全屏和退出全屏
3. 播放MP3时现实缩略图片
4. 能在ListView、ViewPager和ListView、ViewPager和Fragment的多重嵌套模式下全屏工作
5. ListView的拖拽和ViewPager的滑动时如果划出屏幕会自动重置视频
6. 根据自己应用的颜色风格换肤

##使用
引入类库
```java
compile 'fm.jiecao:jiecaovideoplayer:1.3'
```

添加布局
```html
<fm.jiecao.jcvideoplayer_lib.JCVideoPlayer
                android:id="@+id/videocontroller1"
                android:layout_width="match_parent"
                android:layout_height="200dp" />
```

设置视频地址、缩略图地址、标题
```java
JCVideoPlayer videoController = (JCVideoPlayer) findViewById(R.id.videocontroller);
videoController.setUp("http://2449.vod.myqcloud.com/2449_43b6f696980311e59ed467f22794e792.f20.mp4",
                "http://p.qpic.cn/videoyun/0/2449_43b6f696980311e59ed467f22794e792_1/640",
                "嫂子别摸我");
```

其他接口，停止所有视频
```java
JCVideoPlayer.releaseAllVideo();
```

设置皮肤，可以指定某个播放器的皮肤，也可以设置全局皮肤，优先级:某个播放器皮肤>全局皮肤>默认皮肤
```java
JCVideoPlayer.setGlobleSkin();//设置全局皮肤
videoController.setSkin();//设置这一个播放器的皮肤
```

在ListView和ViewPager中将视频移除屏幕外，会在onDetachedFromWindow时重置视频。

目标是在库外只需要添加布局，添加配置，其他的问题都在库内判断和操作。
    
    如果JCVideoPlayer这个View能检测到自己的Activity或者Fragment的生命周期JCVideoPlayer.releaseAllVideo();这个接口暴露出来都略显多余

##下载
 * **[jiecaovideoplayer-1.3-demo.apk](https://raw.githubusercontent.com/lipangit/jiecaovideoplayer/develop/downloads/jiecaovideoplayer-1.3-demo.apk)**
 * **[jiecaovideoplayer-1.3.aar](https://raw.githubusercontent.com/lipangit/jiecaovideoplayer/develop/downloads/jiecaovideoplayer-1.3.aar)**
 * **[jiecaovideoplayer-1.3-javadoc.jar](https://raw.githubusercontent.com/lipangit/jiecaovideoplayer/develop/downloads/jiecaovideoplayer-1.3-javadoc.jar)**
 * **[jiecaovideoplayer-1.3-sources.jar](https://raw.githubusercontent.com/lipangit/jiecaovideoplayer/develop/downloads/jiecaovideoplayer-1.3-sources.jar)**

##效果

![Demo Screenshot][1]

http://v.youku.com/v_show/id_XMTQ2NzUwOTcyNA==.html?firsttime=0&from=y1.4-2


[1]: ./effect.gif
