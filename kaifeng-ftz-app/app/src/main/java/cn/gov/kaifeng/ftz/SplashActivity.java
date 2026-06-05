package cn.gov.kaifeng.ftz;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        ImageView logoView = findViewById(R.id.splash_logo);
        TextView titleView = findViewById(R.id.splash_title);
        TextView subtitleView = findViewById(R.id.splash_subtitle);

        // Logo 缩放+淡入动画
        AnimationSet logoAnim = new AnimationSet(true);
        ScaleAnimation scale = new ScaleAnimation(
            0.7f, 1f, 0.7f, 1f,
            Animation.RELATIVE_TO_SELF, 0.5f,
            Animation.RELATIVE_TO_SELF, 0.5f
        );
        AlphaAnimation fadeIn = new AlphaAnimation(0f, 1f);
        scale.setDuration(700);
        fadeIn.setDuration(700);
        logoAnim.addAnimation(scale);
        logoAnim.addAnimation(fadeIn);
        logoAnim.setFillAfter(true);
        logoView.startAnimation(logoAnim);

        // 文字延迟淡入
        AlphaAnimation textFade = new AlphaAnimation(0f, 1f);
        textFade.setDuration(600);
        textFade.setStartOffset(500);
        textFade.setFillAfter(true);
        titleView.startAnimation(textFade);

        AlphaAnimation subtitleFade = new AlphaAnimation(0f, 1f);
        subtitleFade.setDuration(600);
        subtitleFade.setStartOffset(800);
        subtitleFade.setFillAfter(true);
        subtitleView.startAnimation(subtitleFade);

        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
            finish();
        }, SPLASH_DURATION);
    }
}
