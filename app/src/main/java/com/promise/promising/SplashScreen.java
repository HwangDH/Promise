package com.promise.promising;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.android.promising.R;

public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        CountTimer ct = new CountTimer();
        ct.Count();
        Thread thread = new Thread() {

            @Override
            public void run() {
                try {
                    sleep(3000);
                    SharedPreferences shared = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
                    String val = shared.getString("userid", "");
                    if (val.length()==0) {
                        Intent intent = new Intent(SplashScreen.this, Login.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Intent intent = new Intent(SplashScreen.this, Login.class);
                        startActivity(intent);
                        finish();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

        };
        thread.start();
    }
}