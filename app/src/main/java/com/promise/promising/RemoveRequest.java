package com.promise.promising;

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

import com.android.promising.R;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class RemoveRequest extends AppCompatActivity {
    AlertDialog alertDialog;
    Button btn1, btn2;
    TextView otherphonenumber, date, time, txt1, txt4;
    String otherphonenumber2, id, text, endweekend, restweek, status, pid;
    SharedPreferences shared;
    String userphonenumber;
    int state = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_remove_request);

        shared = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
        userphonenumber = shared.getString("userphonenumber", "");
        btn1 = (Button)findViewById(R.id.btn1); //확인버튼
        btn2 = (Button)findViewById(R.id.btn2);    //거절버튼
        txt1 = (TextView)findViewById(R.id.txt1);   //내용
        txt4 = (TextView)findViewById(R.id.txt4);   //마감 여부
        otherphonenumber = (TextView)findViewById(R.id.otherphonenumber);   //이름
        date = (TextView)findViewById(R.id.date);   //날짜
        time = (TextView)findViewById(R.id.time);   //시간

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        otherphonenumber2 = intent.getStringExtra("otherphonenumber");
        endweekend = intent.getStringExtra("endweekend");
        restweek = intent.getStringExtra("restweek");
        text =intent.getStringExtra("text");
        status = intent.getStringExtra("status");
        pid = intent.getStringExtra("pid");
        System.out.println(userphonenumber);
        otherphonenumber.setText(otherphonenumber2);
        date.setText(endweekend);
        time.setText(endweekend.substring(11,13)+"시"+endweekend.substring(14,16)+"분");
        txt1.setText(text);
        if(status.equals("1")){
            txt4.setText("마감기한 초과");
        }
        else {
            txt4.setText("마감기한 남음");
        }

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new android.app.AlertDialog.Builder(RemoveRequest.this).create();
                alertDialog.setTitle("약속 삭제확인");
                alertDialog.setMessage("약속을 삭제하시겠습니까?");
                alertDialog.setCancelable(false);

                //취소 버튼 클릭 시
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removerequest(state);
                        Intent intent = new Intent(RemoveRequest.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                alertDialog.show();
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new android.app.AlertDialog.Builder(RemoveRequest.this).create();
                alertDialog.setTitle("약속 삭제 거절");
                alertDialog.setMessage("약속삭제 요청을 거절 하시겠습니까?");
                alertDialog.setCancelable(false);

                //취소 버튼 클릭 시
                alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        state = 1;
                        removerequest(state);
                        Intent intent = new Intent(RemoveRequest.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                alertDialog.show();
            }
        });
    }

    public void removerequest(int state){
        if (state == 0) {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            Toast.makeText(RemoveRequest.this, "약속 삭제 완료", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RemoveRequest.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RemoveRequest.this, "약속 삭제 오류 발생", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RemoveRequest.this, nothingactivity.class);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            RemoveRequest_validate ValidateRequest = new RemoveRequest_validate(userphonenumber, otherphonenumber2, id, pid, responseListener);
            RequestQueue queue = Volley.newRequestQueue(RemoveRequest.this);
            queue.add(ValidateRequest);
        }
        else{
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            Toast.makeText(RemoveRequest.this, "약속 삭제 거절 완료", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RemoveRequest.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(RemoveRequest.this, "약속 삭제 거절 오류 발생", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(RemoveRequest.this, nothingactivity.class);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            RemoveRequest_validate ValidateRequest = new RemoveRequest_validate(otherphonenumber2, responseListener);
            RequestQueue queue = Volley.newRequestQueue(RemoveRequest.this);
            queue.add(ValidateRequest);
        }
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
