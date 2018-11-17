package com.laurensius_dede_suhardiman.smartairport;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ProgressBar;

public class SplashScreen extends AppCompatActivity {

    private ProgressBar pbSplash;
    private CountDownTimer countDownTimer;
    int i = 0 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        pbSplash = (ProgressBar)findViewById(R.id.pb_splash);
        pbSplash.setProgress(10);
        countDownTimer = new CountDownTimer(3000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                i++;
                pbSplash.setProgress((int)i*100/(3000/300));
            }

            @Override
            public void onFinish() {
                pbSplash.setProgress(100);
                Intent intent = new Intent(SplashScreen.this,SmartAirport.class);
                startActivity(intent);
                finish();
            }
        };
        countDownTimer.start();
    }
}
