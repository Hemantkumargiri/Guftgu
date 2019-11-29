package com.guftgue.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.guftgue.R;

public class SplashActivity extends AppCompatActivity {

    private Handler handler;
    private Runnable runnable;
    private ImageView mImglogo;
    private String user_id;
    private String mobile;
    private RelativeLayout mLayoutSplashBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mImglogo=findViewById(R.id.imgLogo);
        mLayoutSplashBack = findViewById(R.id.layoutSplashBack);
        final Animation animation_1 = AnimationUtils.loadAnimation(getBaseContext(),R.anim.rotate);
        final Animation animation_2 = AnimationUtils.loadAnimation(getBaseContext(),R.anim.anti_rotate);
        final Animation animation_3 = AnimationUtils.loadAnimation(getBaseContext(),R.anim.abc_fade_out);
        final Animation animation_4 = AnimationUtils.loadAnimation(getBaseContext(),R.anim.fade_in);
        mImglogo.startAnimation(animation_2);
        animation_2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mLayoutSplashBack.startAnimation(animation_3);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mImglogo.startAnimation(animation_1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        animation_1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                mLayoutSplashBack.startAnimation(animation_4);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                //mLayoutSplashBack.startAnimation(animation_3);
                Intent i = new Intent(getBaseContext(), LoginActivity.class);
                startActivity(i);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        /*mImglogo.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {
            @Override
            public void onViewAttachedToWindow(View v) {
                ViewAnimator.animate(v)
                        .newsPaper()
                        .start();
            }
            @Override
            public void onViewDetachedFromWindow(View v) {

            }
        });*/
        handler = new Handler();
         runnable = new Runnable() {
            @Override
            public void run() {


                SharedPreferences sharedPreferences = getSharedPreferences("login", MODE_PRIVATE);
                user_id = sharedPreferences.getString("user_id", "");
                mobile = sharedPreferences.getString("phone", "");
                Log.d("user_id", "----------------" + user_id);


                if (!TextUtils.isEmpty(user_id.trim())) {
                    /*if (!TextUtils.isEmpty(mobile.trim())) {
                        startActivity(new Intent(SplashActivity.this, NewDashboard.class)
                        .putExtra("main","main2"));
                        finish();
                    }*/
                } /*else {
                    startActivity(new Intent(SplashActivity.this, WelcomeActivity.class));
                    finish();
                }*/
            }
        };
        handler.postDelayed(runnable, 3000);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (handler != null) {
            handler.removeCallbacks(runnable);
        }


    }

}
