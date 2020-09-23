package com.speant.delivery.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.crashlytics.android.Crashlytics;
import com.speant.delivery.Common.SessionManager;
import com.speant.delivery.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

public class ActivitySplash extends AppCompatActivity {

    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    private int progressStatus = 0;
    private Handler handler = new Handler();
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);

        sessionManager = new SessionManager(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.colorPrimary));
        }

        new Handler().postDelayed(new Runnable() {

            // Using handler with postDelayed called runnable run method
            @Override
            public void run() {
                if (sessionManager.isLoggedIn()){
                    Intent i = new Intent(ActivitySplash.this, MainActivity.class);
                    startActivity(i);
                }else {
                    Intent i = new Intent(ActivitySplash.this, ActivityLogin.class);
                    startActivity(i);
                }
                // close this activity
                finish();
            }
        }, 3 * 1000); // wait for 2 seconds

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus < 101) {
                    // Update the progress status
                    progressStatus += 25;

                    // Try to sleep the thread for 20 milliseconds
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    // Update the progress bar
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBar.setProgress(progressStatus);
                        }
                    });
                }
            }
        }).start(); // Start the operation
    }
}
