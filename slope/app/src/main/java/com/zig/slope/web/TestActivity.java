package com.zig.slope.web;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.zig.slope.R;

public class TestActivity extends AppCompatActivity {
    private static final float PERCENT = 100;
    private ImageView ivIndex;
    private TextView tvRatio;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ivIndex = (ImageView) findViewById(R.id.iv_index);
        tvRatio = (TextView) findViewById(R.id.tv_ratio);
        tvRatio.setText(PERCENT + "");
        ivRotate(PERCENT);
    }

    private void ivRotate(double percent) {
        double percentOffset = percent > 100 ? 100 : percent;
        RotateAnimation rotateAnimation = new RotateAnimation(270f, 270f+180 * ((int) percentOffset / 100f),
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1.0f);
        rotateAnimation.setDuration(15000);
        rotateAnimation.setFillAfter(true);
        ivIndex.startAnimation(rotateAnimation);
    }
}
