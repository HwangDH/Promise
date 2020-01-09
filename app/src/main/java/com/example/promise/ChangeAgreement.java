package com.example.promise;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class ChangeAgreement extends AppCompatActivity {
    TextView name, date, time, txt1;
    Button btn1, btn2;
    SharedPreferences shared;
    AlertDialog alertDialog;
    String id, userphonenumber, otherphonenumber, text, endweekend, pid;
    int state = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_agreement);

        shared = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
        userphonenumber = shared.getString("userphonenumber", "");

        name = (TextView)findViewById(R.id.name);
        date = (TextView)findViewById(R.id.date);
        time = (TextView)findViewById(R.id.time);
        txt1 = (TextView)findViewById(R.id.txt1);

        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.btn2);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        otherphonenumber = intent.getStringExtra("otherphonenumber");
        endweekend = intent.getStringExtra("endweekend");
        text =intent.getStringExtra("text");
        pid = intent.getStringExtra("pid");

        name.setText(otherphonenumber);
        date.setText(endweekend.substring(0,4)+"년"+endweekend.substring(4,6)+"월"+endweekend.substring(6,8)+"일");
        time.setText(endweekend.substring(8,10)+"시"+endweekend.substring(10,12)+"분");
        txt1.setText(text);

        //약속 수정 확인 버튼
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new android.app.AlertDialog.Builder(ChangeAgreement.this).create();
                alertDialog.setTitle("약속 수정 확인");
                alertDialog.setMessage("약속을 수정하시겠습니까?");
                alertDialog.setCancelable(false);

                //취소 버튼 클릭 시
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

                //확인 버튼 클릭 시
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        state = 1;
                        removerequest(state);
                        Intent intent = new Intent(ChangeAgreement.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                alertDialog.show();
            }
        });

        //약속 승인 거절 버튼
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new android.app.AlertDialog.Builder(ChangeAgreement.this).create();
                alertDialog.setTitle("약속 승인 거절");
                alertDialog.setMessage("약속 수정을 거절하시겠습니까?");
                alertDialog.setCancelable(false);

                //취소 버튼 클릭 시
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

                //확인 버튼 클릭 시
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        state = 0;
                        removerequest(state);
                        Intent intent = new Intent(ChangeAgreement.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                alertDialog.show();
            }
        });
    }

    public void removerequest(int state) {
        if (state == 1) {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            Toast.makeText(ChangeAgreement.this, "약속 수정 완료", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ChangeAgreement.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ChangeAgreement.this, "약속 수정 오류 발생", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ChangeAgreement.this, nothingactivity.class);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            ChangeAgreement_validate ValidateRequest = new ChangeAgreement_validate(id, userphonenumber, otherphonenumber, endweekend, text, pid, responseListener);
            RequestQueue queue = Volley.newRequestQueue(ChangeAgreement.this);
            queue.add(ValidateRequest);
        }
        else {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            Toast.makeText(ChangeAgreement.this, "약속 수정 거절 완료", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ChangeAgreement.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ChangeAgreement.this, "약속 수정 거절 오류 발생", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ChangeAgreement.this, nothingactivity.class);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            ChangeAgreement_validate ValidateRequest = new ChangeAgreement_validate(otherphonenumber, responseListener);
            RequestQueue queue = Volley.newRequestQueue(ChangeAgreement.this);
            queue.add(ValidateRequest);
        }
    }
}
