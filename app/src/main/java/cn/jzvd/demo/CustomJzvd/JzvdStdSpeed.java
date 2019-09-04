package cn.jzvd.demo.CustomJzvd;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import cn.jzvd.JzvdStd;
import cn.jzvd.demo.R;

public class JzvdStdSpeed extends JzvdStd {
    TextView tvSpeed;
    int currentSpeedIndex = 2;

    public JzvdStdSpeed(Context context) {
        super(context);
    }

    public JzvdStdSpeed(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        tvSpeed = findViewById(R.id.tv_speed);
        tvSpeed.setOnClickListener(this);
    }

    public void setScreenNormal() {
        super.setScreenNormal();
        tvSpeed.setVisibility(View.GONE);
    }

    @Override
    public void setScreenFullscreen() {
        super.setScreenFullscreen();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M)
            tvSpeed.setVisibility(View.VISIBLE);

        if (jzDataSource.objects == null) {
            Object[] object = {2};
            jzDataSource.objects = object;
            currentSpeedIndex = 2;
        } else {
            currentSpeedIndex = (int) jzDataSource.objects[0];
        }
        if (currentSpeedIndex == 2) {
            tvSpeed.setText("倍速");
        } else {
            tvSpeed.setText(getSpeedFromIndex(currentSpeedIndex) + "X");
        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.tv_speed) {//0.5 0.75 1.0 1.25 1.5 1.75 2.0
            if (currentSpeedIndex == 6) {
                currentSpeedIndex = 0;
            } else {
                currentSpeedIndex += 1;
            }
            mediaInterface.setSpeed(getSpeedFromIndex(currentSpeedIndex));
            tvSpeed.setText(getSpeedFromIndex(currentSpeedIndex) + "X");
            jzDataSource.objects[0] = currentSpeedIndex;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.layout_std_speed;
    }

    private float getSpeedFromIndex(int index) {
        float ret = 0f;
        if (index == 0) {
            ret = 0.5f;
        } else if (index == 1) {
            ret = 0.75f;
        } else if (index == 2) {
            ret = 1.0f;
        } else if (index == 3) {
            ret = 1.25f;
        } else if (index == 4) {
            ret = 1.5f;
        } else if (index == 5) {
            ret = 1.75f;
        } else if (index == 6) {
            ret = 2.0f;
        }
        return ret;
    }

}
