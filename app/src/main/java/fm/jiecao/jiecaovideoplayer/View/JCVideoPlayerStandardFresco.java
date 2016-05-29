package fm.jiecao.jiecaovideoplayer.View;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.Timer;
import java.util.TimerTask;

import fm.jiecao.jcvideoplayer_lib.JCBuriedPointStandard;
import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jiecaovideoplayer.R;

/**
 * Created by Nathen
 * On 2016/05/01 22:59
 */
public class JCVideoPlayerStandardFresco extends JCVideoPlayer {

  protected ImageView ivBack;
  protected ProgressBar pbBottom, pbLoading;
  protected TextView tvTitle;
  public SimpleDraweeView ivThumb;
  protected ImageView ivCover;

  private static Timer mDismissControlViewTimer;
  private static JCBuriedPointStandard jc_BuriedPointStandard;

  public JCVideoPlayerStandardFresco(Context context) {
    super(context);
  }

  public JCVideoPlayerStandardFresco(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void init(Context context) {
    super.init(context);
    pbBottom = (ProgressBar) findViewById(R.id.bottom_progressbar);
    tvTitle = (TextView) findViewById(R.id.title);
    ivBack = (ImageView) findViewById(R.id.back);
    ivThumb = (SimpleDraweeView) findViewById(R.id.thumb);
    ivCover = (ImageView) findViewById(R.id.cover);
    pbLoading = (ProgressBar) findViewById(R.id.loading);

    ivThumb.setOnClickListener(this);
    ivBack.setOnClickListener(this);

  }

  @Override
  public boolean setUp(String url, Object... objects) {
    if (super.setUp(url, objects)) {
      tvTitle.setText(objects[0].toString());
      if (mIfCurrentIsFullscreen) {
        fullscreenButton.setImageResource(R.drawable.jc_shrink);
      } else {
        fullscreenButton.setImageResource(R.drawable.jc_enlarge);
        ivBack.setVisibility(View.GONE);
      }
      return true;
    }
    return false;
  }

  @Override
  public int getLayoutId() {
    return R.layout.jc_layout_standard_fresco;
  }

  @Override
  protected void setStateAndUi(int state) {
    super.setStateAndUi(state);
    switch (mCurrentState) {
      case CURRENT_STATE_NORMAL:
        changeUiToNormal();
        cancelDismissControlViewTimer();
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
    }
  }

  @Override
  public boolean onTouch(View v, MotionEvent event) {
    switch (event.getAction()) {
      case MotionEvent.ACTION_DOWN:
        cancelDismissControlViewTimer();
        break;
      case MotionEvent.ACTION_UP:
        startDismissControlViewTimer();
        break;
    }
    return super.onTouch(v, event);
  }

  @Override
  public void onClick(View v) {
    super.onClick(v);
    int i = v.getId();
    if (i == R.id.thumb) {
      if (mCurrentState == CURRENT_STATE_NORMAL) {
        if (jc_BuriedPointStandard != null) {
          jc_BuriedPointStandard.onClickStartThumb(mUrl, mObjects);
        }
        prepareVideo();
        startDismissControlViewTimer();
      }
    } else if (i == R.id.surface_container) {
      if (jc_BuriedPointStandard != null && JCMediaManager.instance().listener == this) {
        if (mIfCurrentIsFullscreen) {
          jc_BuriedPointStandard.onClickBlankFullscreen(mUrl, mObjects);
        } else {
          jc_BuriedPointStandard.onClickBlank(mUrl, mObjects);
        }
      }
      onClickUiToggle();
      startDismissControlViewTimer();
    } else if (i == R.id.back) {
      backFullscreen();
    }
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
    }
  }

  @Override
  protected void setProgressAndTime(int progress, int secProgress, int currentTime, int totalTime) {
    super.setProgressAndTime(progress, secProgress, currentTime, totalTime);
    if (progress != 0) pbBottom.setProgress(progress);
    if (secProgress != 0) pbBottom.setSecondaryProgress(secProgress);
  }

  @Override
  protected void resetProgressAndTime() {
    super.resetProgressAndTime();
    pbBottom.setProgress(0);
    pbBottom.setSecondaryProgress(0);
  }

  //Unified management Ui
  private void changeUiToNormal() {
    topContainer.setVisibility(View.VISIBLE);
    bottomContainer.setVisibility(View.INVISIBLE);
    startButton.setVisibility(View.VISIBLE);
    pbLoading.setVisibility(View.INVISIBLE);
    ivThumb.setVisibility(View.VISIBLE);
    ivCover.setVisibility(View.VISIBLE);
    pbBottom.setVisibility(View.INVISIBLE);
    updateStartImage();
  }

  private void changeUiToShowUiPrepareing() {
    topContainer.setVisibility(View.VISIBLE);
    bottomContainer.setVisibility(View.VISIBLE);
    startButton.setVisibility(View.INVISIBLE);
    pbLoading.setVisibility(View.VISIBLE);
    ivThumb.setVisibility(View.INVISIBLE);
    ivCover.setVisibility(View.VISIBLE);
    pbBottom.setVisibility(View.INVISIBLE);
  }

  private void changeUiToClearUiPrepareing() {
//        changeUiToClearUi();
    topContainer.setVisibility(View.INVISIBLE);
    bottomContainer.setVisibility(View.INVISIBLE);
    startButton.setVisibility(View.INVISIBLE);
    ivThumb.setVisibility(View.INVISIBLE);
    pbBottom.setVisibility(View.INVISIBLE);
//        loadingProgressBar.setVisibility(View.VISIBLE);
    ivCover.setVisibility(View.VISIBLE);
  }

  private void changeUiToShowUiPlaying() {
    topContainer.setVisibility(View.VISIBLE);
    bottomContainer.setVisibility(View.VISIBLE);
    startButton.setVisibility(View.VISIBLE);
    pbLoading.setVisibility(View.INVISIBLE);
    ivThumb.setVisibility(View.INVISIBLE);
    ivCover.setVisibility(View.INVISIBLE);
    pbBottom.setVisibility(View.INVISIBLE);
    updateStartImage();
  }

  private void changeUiToClearUiPlaying() {
    changeUiToClearUi();
    pbBottom.setVisibility(View.VISIBLE);
  }

  private void changeUiToShowUiPause() {
    topContainer.setVisibility(View.VISIBLE);
    bottomContainer.setVisibility(View.VISIBLE);
    startButton.setVisibility(View.VISIBLE);
    pbLoading.setVisibility(View.INVISIBLE);
    ivThumb.setVisibility(View.INVISIBLE);
    ivCover.setVisibility(View.INVISIBLE);
    pbBottom.setVisibility(View.INVISIBLE);
    updateStartImage();
  }

  private void changeUiToClearUiPause() {
    changeUiToClearUi();
    pbBottom.setVisibility(View.VISIBLE);
  }

  private void changeUiToClearUi() {
    topContainer.setVisibility(View.INVISIBLE);
    bottomContainer.setVisibility(View.INVISIBLE);
    startButton.setVisibility(View.INVISIBLE);
    pbLoading.setVisibility(View.INVISIBLE);
    ivThumb.setVisibility(View.INVISIBLE);
    ivCover.setVisibility(View.INVISIBLE);
    pbBottom.setVisibility(View.INVISIBLE);
  }

  private void changeUiToError() {
    topContainer.setVisibility(View.INVISIBLE);
    bottomContainer.setVisibility(View.INVISIBLE);
    startButton.setVisibility(View.VISIBLE);
    pbLoading.setVisibility(View.INVISIBLE);
    ivThumb.setVisibility(View.INVISIBLE);
    ivCover.setVisibility(View.VISIBLE);
    pbBottom.setVisibility(View.INVISIBLE);
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
    mDismissControlViewTimer = new Timer();
    mDismissControlViewTimer.schedule(new TimerTask() {
      @Override
      public void run() {
        if (getContext() != null && getContext() instanceof Activity) {
          ((Activity) getContext()).runOnUiThread(new Runnable() {
            @Override
            public void run() {
              if (mCurrentState != CURRENT_STATE_NORMAL
                && mCurrentState != CURRENT_STATE_ERROR) {
                bottomContainer.setVisibility(View.INVISIBLE);
                topContainer.setVisibility(View.INVISIBLE);
                pbBottom.setVisibility(View.VISIBLE);
                startButton.setVisibility(View.INVISIBLE);
              }
            }
          });
        }
      }
    }, 2500);
  }

  private void cancelDismissControlViewTimer() {
    if (mDismissControlViewTimer != null) {
      mDismissControlViewTimer.cancel();
    }
  }

  public static void setJcBuriedPointStandard(JCBuriedPointStandard jcBuriedPointStandard) {
    jc_BuriedPointStandard = jcBuriedPointStandard;
    JCVideoPlayer.setJcBuriedPoint(jcBuriedPointStandard);
  }

}
