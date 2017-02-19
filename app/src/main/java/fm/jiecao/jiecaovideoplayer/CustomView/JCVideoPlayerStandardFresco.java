package fm.jiecao.jiecaovideoplayer.CustomView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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

import fm.jiecao.jcvideoplayer_lib.JCUserActionStandard;
import fm.jiecao.jcvideoplayer_lib.JCUtils;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jiecaovideoplayer.R;

//import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Just replace thumb from ImageView to SimpleDraweeView
 * Created by Nathen
 * On 2016/05/01 22:59
 */
public class JCVideoPlayerStandardFresco extends JCVideoPlayer {

    public ImageView backButton;
    public ProgressBar bottomProgressBar, loadingProgressBar;
    public TextView titleTextView;
    //    public SimpleDraweeView thumbImageView;
    public ImageView tinyBackImageView;

    protected static Timer DISSMISS_CONTROL_VIEW_TIMER;
    protected DismissControlViewTimerTask mDismissControlViewTimerTask;

    protected static JCUserActionStandard JC_USER_EVENT_STANDARD;

    public JCVideoPlayerStandardFresco(Context context) {
        super(context);
    }

    public JCVideoPlayerStandardFresco(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        bottomProgressBar = (ProgressBar) findViewById(R.id.bottom_progress);
        titleTextView = (TextView) findViewById(R.id.title);
        backButton = (ImageView) findViewById(R.id.back);
//        thumbImageView = (SimpleDraweeView) findViewById(R.id.thumb);
        loadingProgressBar = (ProgressBar) findViewById(R.id.loading);
        tinyBackImageView = (ImageView) findViewById(R.id.back_tiny);

//        thumbImageView.setOnClickListener(this);
        backButton.setOnClickListener(this);
        tinyBackImageView.setOnClickListener(this);

    }

    @Override
    public void setUp(String url, int screen, Object... objects) {
        super.setUp(url, screen, objects);
        if (objects.length == 0) return;
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
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_standard_fresco;
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
                        onClickUiToggle();
                    }
                    break;
            }
        } else if (id == R.id.bottom_seek_progress) {
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
            if (JC_USER_EVENT_STANDARD != null && isCurrentJcvd()) {
//                if (mIfCurrentIsFullscreen) {
//                    JC_USER_EVENT_STANDARD.onClickBlankFullscreen(url, objects);
//                } else {
//                    JC_USER_EVENT_STANDARD.onClickBlank(url, objects);
//                }
            }
            startDismissControlViewTimer();
        } else if (i == R.id.back) {
            backPress();
        } else if (i == R.id.back_tiny) {
            backPress();
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
        startDismissControlViewTimer();
    }

    private void startPlayLogic() {
        onEvent(JCUserActionStandard.ON_CLICK_START_THUMB);
        prepareMediaPlayer();
        startDismissControlViewTimer();
    }

    private void onClickUiToggle() {
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

//    @Override
//    public void setProgressAndTime(int progress, int secProgress, int currentTime, int totalTime) {
//        super.setProgressAndTime(progress, secProgress, currentTime, totalTime);
//        if (progress != 0) bottomProgressBar.setProgress(progress);
//        if (secProgress != 0) bottomProgressBar.setSecondaryProgress(secProgress);
//    }

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

    private void changeUiToPreparingClear() {
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

    private void changeUiToPlayingShow() {
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

    private void changeUiToPlayingClear() {
        switch (currentScreen) {
            case SCREEN_LAYOUT_LIST:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.INVISIBLE, View.INVISIBLE, View.VISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    private void changeUiToPauseShow() {
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

    private void changeUiToPauseClear() {
        switch (currentScreen) {
            case SCREEN_LAYOUT_LIST:
                setAllControlsVisible(View.INVISIBLE, View.VISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_FULLSCREEN:
                setAllControlsVisible(View.INVISIBLE, View.VISIBLE, View.INVISIBLE,
                        View.INVISIBLE, View.INVISIBLE, View.INVISIBLE, View.INVISIBLE);
                break;
            case SCREEN_WINDOW_TINY:
                break;
        }

    }

    private void changeUiToPlayingBufferingShow() {
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

    private void changeUiToPlayingBufferingClear() {
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

    private void changeUiToCompleteShow() {
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

    private void changeUiToCompleteClear() {
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

    private void changeUiToError() {
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

    private void setAllControlsVisible(int topCon, int bottomCon, int startBtn, int loadingPro,
                                       int thumbImg, int coverImg, int bottomPro) {
        topContainer.setVisibility(topCon);
        bottomContainer.setVisibility(bottomCon);
        startButton.setVisibility(startBtn);
        loadingProgressBar.setVisibility(loadingPro);
//        thumbImageView.setVisibility(thumbImg);
        bottomProgressBar.setVisibility(bottomPro);
    }

    private void updateStartImage() {
        if (currentState == CURRENT_STATE_PLAYING) {
            startButton.setImageResource(R.drawable.jc_click_pause_selector);
        } else if (currentState == CURRENT_STATE_ERROR) {
            startButton.setImageResource(R.drawable.jc_click_error_selector);
        } else {
            startButton.setImageResource(R.drawable.jc_click_play_selector);
        }
    }

    protected Dialog mProgressDialog;
    protected ProgressBar mDialogProgressBar;
    protected TextView mDialogSeekTime;
    protected TextView mDialogTotalTime;
    protected ImageView mDialogIcon;

    @Override
    public void showProgressDialog(float deltaX, String seekTime, int seekTimePosition, String totalTime, int totalTimeDuration) {
        super.showProgressDialog(deltaX, seekTime, seekTimePosition, totalTime, totalTimeDuration);
        if (mProgressDialog == null) {
            View localView = LayoutInflater.from(getContext()).inflate(R.layout.jc_dialog_progress, null);
            mDialogProgressBar = ((ProgressBar) localView.findViewById(R.id.duration_progressbar));
            mDialogSeekTime = ((TextView) localView.findViewById(R.id.tv_current));
            mDialogTotalTime = ((TextView) localView.findViewById(R.id.tv_duration));
            mDialogIcon = ((ImageView) localView.findViewById(R.id.duration_image_tip));
            mProgressDialog = new Dialog(getContext(), R.style.jc_style_dialog_progress);
            mProgressDialog.setContentView(localView);
            mProgressDialog.getWindow().addFlags(Window.FEATURE_ACTION_BAR);
            mProgressDialog.getWindow().addFlags(32);
            mProgressDialog.getWindow().addFlags(16);
            mProgressDialog.getWindow().setLayout(-2, -2);
            WindowManager.LayoutParams localLayoutParams = mProgressDialog.getWindow().getAttributes();
            localLayoutParams.gravity = Gravity.CENTER;
            mProgressDialog.getWindow().setAttributes(localLayoutParams);
        }
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }

        mDialogSeekTime.setText(seekTime);
        mDialogTotalTime.setText(" / " + totalTime);
        mDialogProgressBar.setProgress(seekTimePosition * 100 / totalTimeDuration);
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


    protected Dialog mVolumeDialog;
    protected ProgressBar mDialogVolumeProgressBar;

    @Override
    public void showVolumeDialog(float deltaY, int volumePercent) {
        super.showVolumeDialog(deltaY, volumePercent);
        if (mVolumeDialog == null) {
            View localView = LayoutInflater.from(getContext()).inflate(R.layout.jc_dialog_volume, null);
            mDialogVolumeProgressBar = ((ProgressBar) localView.findViewById(R.id.volume_progressbar));
            mVolumeDialog = new Dialog(getContext(), R.style.jc_style_dialog_progress);
            mVolumeDialog.setContentView(localView);
            mVolumeDialog.getWindow().addFlags(8);
            mVolumeDialog.getWindow().addFlags(32);
            mVolumeDialog.getWindow().addFlags(16);
            mVolumeDialog.getWindow().setLayout(-2, -2);
            WindowManager.LayoutParams localLayoutParams = mVolumeDialog.getWindow().getAttributes();
            localLayoutParams.gravity = Gravity.CENTER;
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

    private void startDismissControlViewTimer() {
        cancelDismissControlViewTimer();
        DISSMISS_CONTROL_VIEW_TIMER = new Timer();
        mDismissControlViewTimerTask = new DismissControlViewTimerTask();
        DISSMISS_CONTROL_VIEW_TIMER.schedule(mDismissControlViewTimerTask, 2500);
    }

    private void cancelDismissControlViewTimer() {
        if (DISSMISS_CONTROL_VIEW_TIMER != null) {
            DISSMISS_CONTROL_VIEW_TIMER.cancel();
        }
        if (mDismissControlViewTimerTask != null) {
            mDismissControlViewTimerTask.cancel();
        }

    }

    protected class DismissControlViewTimerTask extends TimerTask {

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
                            bottomProgressBar.setVisibility(View.VISIBLE);
                            startButton.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            }
        }
    }

}
