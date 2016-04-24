#Custom ui

    Referring JCVideoPlayerSimple, JCVideoPlayerStandard JCVideoPlayerStandardWith ShareButton
    
1. Inheritance JCVideoPlayer, JCVideoPlayer contains all the code on the player and does not contain any UI code
2. getLayoutId (); id layout
3. Override init (Context context); custom initialized here
4. Override setStateAndUi (int state); the focus, customize each state UI, if you modify the UI in other places should remain cautious
5. Override onTouch (), onClick (); if necessary
6. After the custom UI, you should still follow [README.md](./README.md) 4 steps to invoke Player

Control id on the problem, all in the most basic id Base simple ui example of death is written in JCVideoPlayer the following controls id has findViewById past do not need to findViewById

| control        | id           |
| ------------- |:-------------:| 
| start button       | start         |
| fullscreen button       | fullscreen    |
| progress         | progress      |
| current time         | current      |
| total time       | total         |
| bottom layout    | layout_bottom  |
| top layout    |    layout_top  |
| SurfaceView container | surface_container  |

If you want to quickly integrate video playback, we recommend referring JCVideoPlayerStandardWithShareButton do, it inherits JCVideoPlayerStandard, JCVideoPlayerStandard goal is and details of today's headlines are consistent

If you want to fully customize the UI, referring JCVideoPlayerStandard

If you write any code on the control of the video outside JCVideoPlayer not our intention, please mention the issue