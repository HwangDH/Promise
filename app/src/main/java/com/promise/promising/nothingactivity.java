package com.promise.promising;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.android.promising.R;

//메인화면 새로고침을 위한 액티비티
public class nothingactivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nothingactivity);

        Intent intent = new Intent(nothingactivity.this,MainActivity.class);
        startActivity(intent);
    }
}
