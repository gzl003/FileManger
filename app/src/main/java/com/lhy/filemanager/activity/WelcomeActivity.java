package com.lhy.filemanager.activity;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.lhy.filemanager.R;

public class WelcomeActivity extends AppCompatActivity {

    public static final int START_DELAY = 200;
    public static final int DURATION = 3000;

    private ImageView anim_img;
    private TextView text_welcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        anim_img = (ImageView) findViewById(R.id.image_welcome);
        text_welcome = (TextView) findViewById(R.id.text_welcome);

        showEnterAnim();
    }

    private void showEnterAnim() {

        ObjectAnimator scaleXAnimimg = ObjectAnimator.ofFloat(anim_img, "scaleX", 1.0f, 1.5f);
        scaleXAnimimg.setDuration(DURATION);
        scaleXAnimimg.setStartDelay(START_DELAY);
        ObjectAnimator scaleYAnimimg = ObjectAnimator.ofFloat(anim_img, "scaleY", 1.0f, 1.3f);
        scaleYAnimimg.setDuration(DURATION);
        scaleYAnimimg.setStartDelay(START_DELAY);


        AnimatorSet animSet = new AnimatorSet();
        animSet.play(scaleXAnimimg).with(scaleYAnimimg);
        animSet.setInterpolator(new AccelerateDecelerateInterpolator());
        animSet.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                text_welcome.setAlpha(0f);
                text_welcome.setVisibility(View.VISIBLE);
                ObjectAnimator alphaAnimimg = ObjectAnimator.ofFloat(text_welcome, "alpha", 0f, 1f);
                alphaAnimimg.setDuration(1500);
                alphaAnimimg.start();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //动画结束
                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                        finish();
                    }
                }, 3000);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animSet.start();
    }
}
