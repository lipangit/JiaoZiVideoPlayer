#自定义UI

    参照JCVideoPlayerSimple，JCVideoPlayerStandard，JCVideoPlayerStandardWithShareButton
    
1. 继承JCVideoPlayer，JCVideoPlayer中包含所有关于播放的代码，不包含任何UI的代码
2. getLayoutId();布局的id
3. Override init(Context context);这里初始化自定义控件
4. Override setStateAndUi(int state);是重点，自定义各个状态的UI,如果在其他地方修改UI应该保持谨慎
5. Override onTouch(),onClick();如果有需要
6. 在自定义UI之后，应该依然按照[README.md](./README-ZH.md)中的4步骤来调用播放器

关于控件id的问题，在Base simple ui example中的所有最基础的id是写死在JCVideoPlayer中的，下面的控件id已经findViewById过了不需要再findViewById

| 控件        | id           |
| ------------- |:-------------:| 
| 开始按钮       | start         |
| 全屏按钮       | fullscreen    |
| 进度条         | progress      |
| 总共时间       | total         |
| 下方layout    | layout_bottom  |
| 上方layout    |    layout_top  |
| SurfaceView的容器 | surface_container  |

如果想快速的集成视频播放功能，建议参照JCVideoPlayerStandardWithShareButton来做，它继承JCVideoPlayerStandard，JCVideoPlayerStandard的目标是和今日头条的细节保持一致

如果想完全自定义UI，参照JCVideoPlayerStandard

如果在JCVideoPlayer之外写任何关于控制视频的代码不是我们的初衷，请提issue