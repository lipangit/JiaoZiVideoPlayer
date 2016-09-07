package fm.jiecao.jcvideoplayer_lib;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Nathen
 * On 2016/04/18 16:15
 */
public class JCVideoPlayerStandard extends JCVideoPlayer {

    protected static Timer DISMISS_CONTROL_VIEW_TIMER;

    public ImageView   backButton;
    public ProgressBar bottomProgressBar, loadingProgressBar;
    public TextView  titleTextView;
    public ImageView thumbImageView;
    public ImageView coverImageView;
    public ImageView tinyBackImageView;

    private static Bitmap  pauseSwitchCoverBitmap = null;
    private static boolean isRefreshCover         = false;

    protected DismissControlViewTimerTask mDismissControlViewTimerTask;


    public JCVideoPlayerStandard(Context context) {
        super(context);
    }

    public JCVideoPlayerStandard(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        bottomProgressBar = (ProgressBar) findViewById(R.id.bottom_progressbar);
        titleTextView = (TextView) findViewById(R.id.title);
        backButton = (ImageView) findViewById(R.id.back);
        thumbImageView = (ImageView) findViewById(R.id.thumb);
        coverImageView = (ImageView) findViewById(R.id.cover);
        loadingProgressBar = (ProgressBar) findViewById(R.id.loading);
        tinyBackImageView = (ImageView) findViewById(R.id.back_tiny);

        thumbImageView.setOnClickListener(this);
        backButton.setOnClickListener(this);
        tinyBackImageView.setOnClickListener(this);

    }

    @Override
    public boolean setUp(String url, int screen, Object... objects) {
        if (objects.length == 0) return false;
        if (super.setUp(url, screen, objects)) {
            if (pauseSwitchCoverBitmap != null && coverImageView.getBackground() == null) {
                coverImageView.setBackgroundColor(Color.parseColor("#222222"));//防止在复用的时候导致，闪一下上次暂停切换缓存的图的问题
            }
            titleTextView.setText(objects[0].toString());
            if (currentScreen == SCREEN_WINDOW_FULLSCREEN) {
                fullscreenButton.setImageResource(R.drawable.jc_shrink);
                backButton.setVisibility(View.VISIBLE);
                tinyBackImageView.setVisibility(View.INVISIBLE);
            } else if (currentScreen == SCREEN_LAYOUT_LIST) {
                fullscreenButton.setImageResource(R.drawable.jc_enlarge);
                backButton.setVisibility(View.GONE);
                tinyBackImageView.setVisibility(View.INVISIBLE);
            } else if (currentScreen == SCREEN_WINDOW_TINY) {
                tinyBackImageView.setVisibility(View.VISIBLE);
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
            }
            return true;
        }
        return false;
    }

    @Override
    public int getLayoutId() {
        return R.layout.jc_layout_standard;
    }

    @Override
    public void setUiWitStateAndScreen(int state) {
        super.setUiWitStateAndScreen(state);
        switch (currentState) {
            case CURRENT_STATE_NORMAL:
                changeUiToNormal();
                break;
            case CURRENT_STATE_PREPARING:
                changeUiToPreparingShow();
                startDismissControlViewTimer();
                break;
            case CURRENT_STATE_PLAYING:
                changeUiToPlayingShow();
                startDismissControlViewTimer();
                break;
            case CURRENT_STATE_PAUSE:
                changeUiToPauseShow();
                cancelDismissControlViewTimer();
                break;
            case CURRENT_STATE_ERROR:
                changeUiToError();
                break;
            case CURRENT_STATE_AUTO_COMPLETE:
                changeUiToCompleteShow();
                cancelDismissControlViewTimer();
                bottomProgressBar.setProgress(100);
                break;
            case CURRENT_STATE_PLAYING_BUFFERING_START:
                changeUiToPlayingBufferingShow();
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        if (id == R.id.surface_container) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    break;
                case MotionEvent.ACTION_MOVE:
                    break;
                case MotionEvent.ACTION_UP:
                    startDismissControlViewTimer();
                    if (mChangePosition) {
                        int duration = getDuration();
                        int progress = mSeekTimePosition * 100 / (duration == 0 ? 1 : duration);
                        bottomProgressBar.setProgress(progress);
                    }
                    if (!mChangePosition && !mChangeVolume) {
                        onEvent(JCBuriedPointStandard.ON_CLICK_BLANK);
                        onClickUiToggle();
                    }
                    break;
            }
        } else if (id == R.id.progress) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    cancelDismissControlViewTimer();
                    break;
                case MotionEvent.ACTION_UP:
                    startDismissControlViewTimer();
                    break;
            }
        }
        return super.onTouch(v, event);
    }

    @Override
    public void addTextureView() {
        super.addTextureView();
        coverImageView.setRotation(JCMediaManager.instance().videoRotation);
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        int i = v.getId();
        if (i == R.id.thumb) {
            if (TextUtils.isEmpty(url)) {
                Toast.makeText(getContext(), getResources().getString(R.string.no_url), Toast.LENGTH_SHORT).show();
                return;
            }
            if (currentState == CURRENT_STATE_NORMAL) {
                if (!url.startsWith("file") && !JCUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {
                    showWifiDialog();
                    return;
                }
                startPlayLogic();
            } else if (currentState == CURRENT_STATE_AUTO_COMPLETE) {
                onClickUiToggle();
            }
        } else if (i == R.id.surface_container) {
            startDismissControlViewTimer();
        } else if (i == R.id.back) {
            backPress();
        } else if (i == R.id.back_tiny) {
            backPress();
        } else if (i == R.id.start) {
            if (currentState == CURRENT_STATE_NORMAL || currentState == CURRENT_STATE_PAUSE) {
                isRefreshCover = true;//播放会才会刷新图缓存
            }
        }
    }

    @Override
    public void prepareVideo() {
        coverImageView.setBackgroundColor(Color.parseColor("#222222"));
        coverImageView.setImageBitmap(null);
        super.prepareVideo();
    }

    @Override
    public void startWindowFullscreen() {
        obtainCover();
        super.startWindowFullscreen();
        if (currentState == CURRENT_STATE_PAUSE) {
            refreshCover(pauseSwitchCoverBitmap);
        }
    }

    private void obtainCover() {
        if (currentState == CURRENT_STATE_PAUSE) {
            if (isRefreshCover) {
                pauseSwitchCoverBitmap = JCMediaManager.textureView.getBitmap();
                isRefreshCover = false;
            }
        }
    }

    @Override
    public boolean goToOtherListener() {
        obtainCover();
        boolean b = super.goToOtherListener();
        if (currentState == CURRENT_STATE_PAUSE) {
            refreshCover(pauseSwitchCoverBitmap);
        }
        return b;
    }


    public void refreshCover(Bitmap bitmap) {
        if (pauseSwitchCoverBitmap != null) {
            JCVideoPlayerStandard jcVideoPlayerStandard = ((JCVideoPlayerStandard) JCVideoPlayerManager.listener());
            jcVideoPlayerStandard.coverImageView.setBackgroundColor(Color.parseColor("#000000"));
            jcVideoPlayerStandard.coverImageView.setImageBitmap(bitmap);
            jcVideoPlayerStandard.coverImageView.setVisibility(VISIBLE);
        }
    }

    @Override
    public void showWifiDialog() {
        super.showWifiDialog();
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage(getResources().getString(R.string.tips_not_wifi));
        builder.setPositiveButton(getResources().getString(R.string.tips_not_wifi_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                startPlayLogic();
                WIFI_TIP_DIALOG_SHOWED = true;
            }
        });
        builder.setNegativeButton(getResources().getString(R.string.tips_not_wifi_cancel), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        super.onStartTrackingTouch(seekBar);
        cancelDismissControlViewTimer();
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        super.onStopTrackingTouch(seekBar);
        if (currentState == CURRENT_STATE_PAUSE) {
            isRefreshCover = true;
        }
        startDismissControlViewTimer();
    }

    public void startPlayLogic() {
        prepareVideo();
        startDismissControlViewTimer();
        onEvent(JCBuriedPointStandard.ON_CLICK_START_THUMB);
    }

    public void onClickUiToggle() {
        if (currentState == CURRENT_STATE_PREPARING) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPreparingClear();
            } else {
                changeUiToPreparingShow();
            }
        } else if (currentState == CURRENT_STATE_PLAYING) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPlayingClear();
            } else {
                changeUiToPlayingShow();
            }
        } else if (currentState == CURRENT_STATE_PAUSE) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPauseClear();
            } else {
                changeUiToPauseShow();
            }
        } else if (currentState == CURRENT_STATE_AUTO_COMPLETE) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToCompleteClear();
            } else {
                changeUiToCompleteShow();
            }
        } else if (currentState == CURRENT_STATE_PLAYING_BUFFERING_START) {
            if (bottomContainer.getVisibility() == View.VISIBLE) {
                changeUiToPlayingBufferingClear();
            } else {
                changeUiToPlayingBufferingShow();
            }
        }
    }

    @Override
    public void setProgressAndTime(int progress, int secProgress, int currentTime, int totalTime) {
        super.setProgressAndTime(progress, secProgress, currentTime, totalTime);
        if (progress != 0) bottomProgressBar.setProgress(progress);
        if (secProgress != 0) bottomProgressBar.setSecondaryProgress(secProgress);
    }

    @Override
    public void resetProgressAndTime() {
        super.resetProgressAndTime();
        bottomProgressBar.setProgress(0);
        bottomProgressBar.setSecondaryProgress(0);
    }

    //Unified management Ui
    public void changeUiToNormal() {
        switch (currentScreen) {
            case SCREEN_LAYOUT_LIST:
                setAllControlsVisible(View.VISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.VISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.VISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.VISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }
    }

    public void changeUiToPreparingShow() {
        switch (currentScreen) {
            case SCREEN_LAYOUT_LIST:
                setAllControlsVisible(View.VISIBLE, View.VISIBLE, View.INVISIBLE,
                        View.VISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.VISIBLE, View.VISIBLE, View.INVISIBLE,
                        View.VISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void changeUiToPreparingClear() {
        switch (currentScreen) {
            case SCREEN_LAYOUT_LIST:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.VISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.VISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void changeUiToPlayingShow() {
        switch (currentScreen) {
            case SCREEN_LAYOUT_LIST:
                setAllControlsVisible(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void changeUiToPlayingClear() {
        switch (currentScreen) {
            case SCREEN_LAYOUT_LIST:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.VISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.VISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void changeUiToPauseShow() {
        switch (currentScreen) {
            case SCREEN_LAYOUT_LIST:
                setAllControlsVisible(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, coverImageView.getVisibility(), View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, coverImageView.getVisibility(), View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void changeUiToPauseClear() {
        switch (currentScreen) {
            case SCREEN_LAYOUT_LIST:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, coverImageView.getVisibility(), View.INVISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, coverImageView.getVisibility(), View.INVISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void changeUiToPlayingBufferingShow() {
        switch (currentScreen) {
            case SCREEN_LAYOUT_LIST:
                setAllControlsVisible(View.VISIBLE, View.VISIBLE, View.INVISIBLE,
                        View.VISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.VISIBLE, View.VISIBLE, View.INVISIBLE,
                        View.VISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void changeUiToPlayingBufferingClear() {
        switch (currentScreen) {
            case SCREEN_LAYOUT_LIST:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.VISIBLE, View.INVISIBLE, View.INVISIBLE, View.VISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.INVISIBLE,
                        View.VISIBLE, View.INVISIBLE, View.INVISIBLE, View.VISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void changeUiToCompleteShow() {
        switch (currentScreen) {
            case SCREEN_LAYOUT_LIST:
                setAllControlsVisible(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.VISIBLE, View.VISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void changeUiToCompleteClear() {
        switch (currentScreen) {
            case SCREEN_LAYOUT_LIST:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.VISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.VISIBLE, View.INVISIBLE, View.VISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void changeUiToError() {
        switch (currentScreen) {
            case SCREEN_LAYOUT_LIST:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.VISIBLE, View.INVISIBLE);
                updateStartImage();
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    public void setAllControlsVisible(int topCon, int bottomCon, int startBtn, int loadingPro,
                                      int thumbImg, int coverImg, int bottomPro) {
        topContainer.setVisibility(topCon);
        bottomContainer.setVisibility(bottomCon);
        startButton.setVisibility(startBtn);
        loadingProgressBar.setVisibility(loadingPro);
        thumbImageView.setVisibility(thumbImg);
        coverImageView.setVisibility(coverImg);
        bottomProgressBar.setVisibility(bottomPro);
    }

    public void updateStartImage() {
        if (currentState == CURRENT_STATE_PLAYING) {
            startButton.setImageResource(R.drawable.jc_click_pause_selector);
        } else if (currentState == CURRENT_STATE_ERROR) {
            startButton.setImageResource(R.drawable.jc_click_error_selector);
        } else {
            startButton.setImageResource(R.drawable.jc_click_play_selector);
        }
    }

    protected Dialog      mProgressDialog;
    protected ProgressBar mDialogProgressBar;
    protected TextView    mDialogSeekTime;
    protected TextView    mDialogTotalTime;
    protected ImageView   mDialogIcon;

    @Override
    public void showProgressDialog(float deltaX, String seekTime, int seekTimePosition, String totalTime, int totalTimeDuration) {
        super.showProgressDialog(deltaX, seekTime, seekTimePosition, totalTime, totalTimeDuration);
        if (mProgressDialog == null) {
            View localView = LayoutInflater.from(getContext()).inflate(fm.jiecao.jcvideoplayer_lib.R.layout.jc_progress_dialog, null);
            View content = localView.findViewById(R.id.content);
            content.setRotation(90);
            mDialogProgressBar = ((ProgressBar) localView.findViewById(fm.jiecao.jcvideoplayer_lib.R.id.duration_progressbar));
            mDialogSeekTime = ((TextView) localView.findViewById(fm.jiecao.jcvideoplayer_lib.R.id.tv_current));
            mDialogTotalTime = ((TextView) localView.findViewById(fm.jiecao.jcvideoplayer_lib.R.id.tv_duration));
            mDialogIcon = ((ImageView) localView.findViewById(fm.jiecao.jcvideoplayer_lib.R.id.duration_image_tip));
            mProgressDialog = new Dialog(getContext(), fm.jiecao.jcvideoplayer_lib.R.style.jc_style_dialog_progress);
            mProgressDialog.setContentView(localView);
            mProgressDialog.getWindow().addFlags(Window.FEATURE_ACTION_BAR);
            mProgressDialog.getWindow().addFlags(32);
            mProgressDialog.getWindow().addFlags(16);
            mProgressDialog.getWindow().setLayout(-2, -2);
            WindowManager.LayoutParams localLayoutParams = mProgressDialog.getWindow().getAttributes();
            localLayoutParams.gravity = Gravity.CENTER_VERTICAL | Gravity.RIGHT;
            localLayoutParams.x = getResources().getDimensionPixelOffset(fm.jiecao.jcvideoplayer_lib.R.dimen.jc_progress_dialog_margin_top) / 2;
            mProgressDialog.getWindow().setAttributes(localLayoutParams);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }

        mDialogSeekTime.setText(seekTime);
        mDialogTotalTime.setText(" / " + totalTime);
        mDialogProgressBar.setProgress(totalTimeDuration <= 0 ? 0 : (seekTimePosition * 100 / totalTimeDuration));
        if (deltaX > 0) {
            mDialogIcon.setBackgroundResource(R.drawable.jc_forward_icon);
        } else {
            mDialogIcon.setBackgroundResource(R.drawable.jc_backward_icon);
        }

    }

    @Override
    public void dismissProgressDialog() {
        super.dismissProgressDialog();
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }


    protected Dialog      mVolumeDialog;
    protected ProgressBar mDialogVolumeProgressBar;

    @Override
    public void showVolumeDialog(float deltaY, int volumePercent) {
        super.showVolumeDialog(deltaY, volumePercent);
        if (mVolumeDialog == null) {
            View localView = LayoutInflater.from(getContext()).inflate(R.layout.jc_volume_dialog, null);
            View content = localView.findViewById(R.id.content);
            content.setRotation(90);
            mDialogVolumeProgressBar = ((ProgressBar) localView.findViewById(R.id.volume_progressbar));
            mVolumeDialog = new Dialog(getContext(), R.style.jc_style_dialog_progress);
            mVolumeDialog.setContentView(localView);
            mVolumeDialog.getWindow().addFlags(8);
            mVolumeDialog.getWindow().addFlags(32);
            mVolumeDialog.getWindow().addFlags(16);
            mVolumeDialog.getWindow().setLayout(-2, -2);
            WindowManager.LayoutParams localLayoutParams = mVolumeDialog.getWindow().getAttributes();
            localLayoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
//            localLayoutParams.y = getContext().getResources().getDimensionPixelOffset(R.dimen.jc_volume_dialog_margin_left);
            mVolumeDialog.getWindow().setAttributes(localLayoutParams);
        }
        if (!mVolumeDialog.isShowing()) {
            mVolumeDialog.show();
        }

        mDialogVolumeProgressBar.setProgress(volumePercent);
    }

    @Override
    public void dismissVolumeDialog() {
        super.dismissVolumeDialog();
        if (mVolumeDialog != null) {
            mVolumeDialog.dismiss();
        }
    }

    public void startDismissControlViewTimer() {
        cancelDismissControlViewTimer();
        DISMISS_CONTROL_VIEW_TIMER = new Timer();
        mDismissControlViewTimerTask = new DismissControlViewTimerTask();
        DISMISS_CONTROL_VIEW_TIMER.schedule(mDismissControlViewTimerTask, 2500);
    }

    public void cancelDismissControlViewTimer() {
        if (DISMISS_CONTROL_VIEW_TIMER != null) {
            DISMISS_CONTROL_VIEW_TIMER.cancel();
        }
        if (mDismissControlViewTimerTask != null) {
            mDismissControlViewTimerTask.cancel();
        }

    }

    public class DismissControlViewTimerTask extends TimerTask {

        @Override
        public void run() {
            if (currentState != CURRENT_STATE_NORMAL
                    && currentState != CURRENT_STATE_ERROR
                    && currentState != CURRENT_STATE_AUTO_COMPLETE) {
                if (getContext() != null && getContext() instanceof Activity) {
                    ((Activity) getContext()).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            bottomContainer.setVisibility(View.INVISIBLE);
                            topContainer.setVisibility(View.INVISIBLE);
                            startButton.setVisibility(View.INVISIBLE);
                            if (currentScreen != SCREEN_WINDOW_TINY) {
                                bottomProgressBar.setVisibility(View.VISIBLE);
                            }
                        }
                    });
                }
            }
        }
    }
}
