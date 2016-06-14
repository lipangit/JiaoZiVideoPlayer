package fm.jiecao.jcvideoplayer_lib;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Nathen
 * On 2016/04/18 16:15
 */
public class JCVideoPlayerStandard extends JCVideoPlayer {

  public ImageView backButton;
  public ProgressBar bottomProgressBar, loadingProgressBar;
  public TextView titleTextView;
  public ImageView thumbImageView;
  public ImageView coverImageView;

  protected static Timer DISSMISS_CONTROL_VIEW_TIMER;
  protected static JCBuriedPointStandard JC_BURIED_POINT_STANDARD;

  public JCVideoPlayerStandard(Context context) {
    super(context);
  }

  public JCVideoPlayerStandard(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void init(Context context) {
    super.init(context);
    bottomProgressBar = (ProgressBar) findViewById(R.id.bottom_progressbar);
    titleTextView = (TextView) findViewById(R.id.title);
    backButton = (ImageView) findViewById(R.id.back);
    thumbImageView = (ImageView) findViewById(R.id.thumb);
    coverImageView = (ImageView) findViewById(R.id.cover);
    loadingProgressBar = (ProgressBar) findViewById(R.id.loading);

    thumbImageView.setOnClickListener(this);
    backButton.setOnClickListener(this);

  }

  @Override
  public boolean setUp(String url, Object... objects) {
    if (objects.length == 0) return false;
    if (super.setUp(url, objects)) {
      titleTextView.setText(objects[0].toString());
      if (mIfCurrentIsFullscreen) {
        fullscreenButton.setImageResource(R.drawable.jc_shrink);
      } else {
        fullscreenButton.setImageResource(R.drawable.jc_enlarge);
        backButton.setVisibility(View.GONE);
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
  protected void setStateAndUi(int state) {
    super.setStateAndUi(state);
    switch (mCurrentState) {
      case CURRENT_STATE_NORMAL:
        changeUiToNormal();
        break;
      case CURRENT_STATE_PREPAREING:
        changeUiToShowUiPrepareing();
        startDismissControlViewTimer();
        break;
      case CURRENT_STATE_PLAYING:
        changeUiToShowUiPlaying();
        startDismissControlViewTimer();
        break;
      case CURRENT_STATE_PAUSE:
        changeUiToShowUiPause();
        cancelDismissControlViewTimer();
        break;
      case CURRENT_STATE_ERROR:
        changeUiToError();
        break;
      case CURRENT_STATE_AUTO_COMPLETE:
        changeUiToShowUiComplete();
        cancelDismissControlViewTimer();
        bottomProgressBar.setProgress(100);
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
            int progress = mResultTimePosition * 100 / (duration == 0 ? 1 : duration);
            bottomProgressBar.setProgress(progress);
          }
          if (!mChangePosition && !mChangeVolume) {
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
  public void onClick(View v) {
    super.onClick(v);
    int i = v.getId();
    if (i == R.id.thumb) {
      if (TextUtils.isEmpty(mUrl)) {
        Toast.makeText(getContext(), getResources().getString(R.string.no_url), Toast.LENGTH_SHORT).show();
        return;
      }
      if (mCurrentState == CURRENT_STATE_NORMAL) {
        if (!JCUtils.isWifiConnected(getContext()) && !WIFI_TIP_DIALOG_SHOWED) {
          AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
          builder.setMessage(getResources().getString(R.string.tips_not_wifi));
          builder.setPositiveButton(getResources().getString(R.string.tips_not_wifi_confirm), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              dialog.dismiss();
              startPlayLocic();
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
          return;
        }
        startPlayLocic();
      } else if (mCurrentState == CURRENT_STATE_AUTO_COMPLETE) {
        onClickUiToggle();
      }
    } else if (i == R.id.surface_container) {
      if (JC_BURIED_POINT_STANDARD != null && isCurrentMediaListener()) {
        if (mIfCurrentIsFullscreen) {
          JC_BURIED_POINT_STANDARD.onClickBlankFullscreen(mUrl, mObjects);
        } else {
          JC_BURIED_POINT_STANDARD.onClickBlank(mUrl, mObjects);
        }
      }
      startDismissControlViewTimer();
    } else if (i == R.id.back) {
      backFullscreen();
    }
  }

  private void startPlayLocic() {
    if (JC_BURIED_POINT_STANDARD != null) {
      JC_BURIED_POINT_STANDARD.onClickStartThumb(mUrl, mObjects);
    }
    prepareVideo();
    startDismissControlViewTimer();
  }

  private void onClickUiToggle() {
    if (mCurrentState == CURRENT_STATE_PREPAREING) {
      if (bottomContainer.getVisibility() == View.VISIBLE) {
        changeUiToClearUiPrepareing();
      } else {
        changeUiToShowUiPrepareing();
      }
    } else if (mCurrentState == CURRENT_STATE_PLAYING) {
      if (bottomContainer.getVisibility() == View.VISIBLE) {
        changeUiToClearUiPlaying();
      } else {
        changeUiToShowUiPlaying();
      }
    } else if (mCurrentState == CURRENT_STATE_PAUSE) {
      if (bottomContainer.getVisibility() == View.VISIBLE) {
        changeUiToClearUiPause();
      } else {
        changeUiToShowUiPause();
      }
    } else if (mCurrentState == CURRENT_STATE_AUTO_COMPLETE) {
      if (bottomContainer.getVisibility() == View.VISIBLE) {
        changeUiToClearUiComplete();
      } else {
        changeUiToShowUiComplete();
      }
    }
  }

  @Override
  protected void setProgressAndTime(int progress, int secProgress, int currentTime, int totalTime) {
    super.setProgressAndTime(progress, secProgress, currentTime, totalTime);
    if (progress != 0) bottomProgressBar.setProgress(progress);
    if (secProgress != 0) bottomProgressBar.setSecondaryProgress(secProgress);
  }

  @Override
  protected void resetProgressAndTime() {
    super.resetProgressAndTime();
    bottomProgressBar.setProgress(0);
    bottomProgressBar.setSecondaryProgress(0);
  }

  //Unified management Ui
  private void changeUiToNormal() {
    topContainer.setVisibility(View.VISIBLE);
    bottomContainer.setVisibility(View.INVISIBLE);
    startButton.setVisibility(View.VISIBLE);
    loadingProgressBar.setVisibility(View.INVISIBLE);
    thumbImageView.setVisibility(View.VISIBLE);
    coverImageView.setVisibility(View.VISIBLE);
    bottomProgressBar.setVisibility(View.INVISIBLE);
    updateStartImage();
  }

  private void changeUiToShowUiPrepareing() {
    topContainer.setVisibility(View.VISIBLE);
    bottomContainer.setVisibility(View.VISIBLE);
    startButton.setVisibility(View.INVISIBLE);
    loadingProgressBar.setVisibility(View.VISIBLE);
    thumbImageView.setVisibility(View.INVISIBLE);
    coverImageView.setVisibility(View.VISIBLE);
    bottomProgressBar.setVisibility(View.INVISIBLE);
  }

  private void changeUiToClearUiPrepareing() {
    topContainer.setVisibility(View.INVISIBLE);
    bottomContainer.setVisibility(View.INVISIBLE);
    startButton.setVisibility(View.INVISIBLE);
    thumbImageView.setVisibility(View.INVISIBLE);
    bottomProgressBar.setVisibility(View.INVISIBLE);
    coverImageView.setVisibility(View.VISIBLE);
  }

  private void changeUiToShowUiPlaying() {
    topContainer.setVisibility(View.VISIBLE);
    bottomContainer.setVisibility(View.VISIBLE);
    startButton.setVisibility(View.VISIBLE);
    loadingProgressBar.setVisibility(View.INVISIBLE);
    thumbImageView.setVisibility(View.INVISIBLE);
    coverImageView.setVisibility(View.INVISIBLE);
    bottomProgressBar.setVisibility(View.INVISIBLE);
    updateStartImage();
  }

  private void changeUiToShowUiComplete() {
    topContainer.setVisibility(View.VISIBLE);
    bottomContainer.setVisibility(View.VISIBLE);
    startButton.setVisibility(View.VISIBLE);
    loadingProgressBar.setVisibility(View.INVISIBLE);
    thumbImageView.setVisibility(View.VISIBLE);
    coverImageView.setVisibility(View.INVISIBLE);
    bottomProgressBar.setVisibility(View.INVISIBLE);
    updateStartImage();
  }

  private void changeUiToClearUiPlaying() {
    changeUiToClearUi();
    bottomProgressBar.setVisibility(View.VISIBLE);
  }

  private void changeUiToShowUiPause() {
    topContainer.setVisibility(View.VISIBLE);
    bottomContainer.setVisibility(View.VISIBLE);
    startButton.setVisibility(View.VISIBLE);
    loadingProgressBar.setVisibility(View.INVISIBLE);
    thumbImageView.setVisibility(View.INVISIBLE);
    coverImageView.setVisibility(View.INVISIBLE);
    bottomProgressBar.setVisibility(View.INVISIBLE);
    updateStartImage();
  }

  private void changeUiToClearUiPause() {
    changeUiToClearUi();
    bottomProgressBar.setVisibility(View.VISIBLE);
  }

  private void changeUiToClearUi() {
    topContainer.setVisibility(View.INVISIBLE);
    bottomContainer.setVisibility(View.INVISIBLE);
    startButton.setVisibility(View.INVISIBLE);
    loadingProgressBar.setVisibility(View.INVISIBLE);
    thumbImageView.setVisibility(View.INVISIBLE);
    coverImageView.setVisibility(View.INVISIBLE);
    bottomProgressBar.setVisibility(View.INVISIBLE);
  }

  private void changeUiToClearUiComplete() {
    topContainer.setVisibility(View.INVISIBLE);
    bottomContainer.setVisibility(View.INVISIBLE);
    startButton.setVisibility(View.VISIBLE);
    loadingProgressBar.setVisibility(View.INVISIBLE);
    thumbImageView.setVisibility(View.VISIBLE);
    coverImageView.setVisibility(View.INVISIBLE);
    bottomProgressBar.setVisibility(View.VISIBLE);
    updateStartImage();
  }

  private void changeUiToError() {
    topContainer.setVisibility(View.INVISIBLE);
    bottomContainer.setVisibility(View.INVISIBLE);
    startButton.setVisibility(View.VISIBLE);
    loadingProgressBar.setVisibility(View.INVISIBLE);
    thumbImageView.setVisibility(View.INVISIBLE);
    coverImageView.setVisibility(View.VISIBLE);
    bottomProgressBar.setVisibility(View.INVISIBLE);
    updateStartImage();
  }

  private void updateStartImage() {
    if (mCurrentState == CURRENT_STATE_PLAYING) {
      startButton.setImageResource(R.drawable.jc_click_pause_selector);
    } else if (mCurrentState == CURRENT_STATE_ERROR) {
      startButton.setImageResource(R.drawable.jc_click_error_selector);
    } else {
      startButton.setImageResource(R.drawable.jc_click_play_selector);
    }
  }

  private void startDismissControlViewTimer() {
    cancelDismissControlViewTimer();
    DISSMISS_CONTROL_VIEW_TIMER = new Timer();
    DISSMISS_CONTROL_VIEW_TIMER.schedule(new TimerTask() {
      @Override
      public void run() {
        if (getContext() != null && getContext() instanceof Activity) {
          ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
              if (mCurrentState != CURRENT_STATE_NORMAL
                && mCurrentState != CURRENT_STATE_ERROR
                && mCurrentState != CURRENT_STATE_AUTO_COMPLETE) {
                bottomContainer.setVisibility(View.INVISIBLE);
                topContainer.setVisibility(View.INVISIBLE);
                bottomProgressBar.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.INVISIBLE);
              }
            }
          });
        }
      }
    }, 2500);
  }

  private void cancelDismissControlViewTimer() {
    if (DISSMISS_CONTROL_VIEW_TIMER != null) {
      DISSMISS_CONTROL_VIEW_TIMER.cancel();
      DISSMISS_CONTROL_VIEW_TIMER = null;
    }
  }

  public static void setJcBuriedPointStandard(JCBuriedPointStandard jcBuriedPointStandard) {
    JC_BURIED_POINT_STANDARD = jcBuriedPointStandard;
    JCVideoPlayer.setJcBuriedPoint(jcBuriedPointStandard);
  }

//  @Override
//  public void onCompletion() {
//    super.onCompletion();
//    cancelDismissControlViewTimer();
//  }

  @Override
  protected void onMediaInfoBuffering(boolean statues) {
    super.onMediaInfoBuffering(statues);
    loadingProgressBar.setVisibility(statues ? View.VISIBLE : View.INVISIBLE);
    startButton.setVisibility(!statues ? View.VISIBLE : View.INVISIBLE);
  }
}
