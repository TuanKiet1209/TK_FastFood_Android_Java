package com.example.foodorder.view.splash;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.example.foodorder.R;
import com.example.foodorder.view.BaseActivity;
import com.example.foodorder.view.main.MainActivity;

@SuppressLint("CustomSplashScreen")
public class SplashActivity extends BaseActivity implements SplashMVPView {

    private SplashPresenter mSplashPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        mSplashPresenter = new SplashPresenter(this);

        startMainActivity();
    }

    private void startMainActivity() {
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }, 2000);
    }
}