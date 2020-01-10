package com.example.promise;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class PromiseChange extends AppCompatActivity {
    String id, userphonenumber, otherphonenumber,  pdate, phour, pmin, ptext, pid;
    TextView name;//상대번호 저장 변수
    EditText date, time, txt1;
    Button btn1;
    AlertDialog alertDialog;
    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promise_change);

        name = (TextView)findViewById(R.id.name);
        date = (EditText)findViewById(R.id.date);
        time = (EditText)findViewById(R.id.time);
        txt1 = (EditText)findViewById(R.id.txt1);
        btn1 = (Button)findViewById(R.id.btn1);

        Intent intent = getIntent();
        id = intent.getStringExtra("id");
        userphonenumber = intent.getStringExtra("userphonenumber");
        otherphonenumber = intent.getStringExtra("otherphonenumber");
        pid = intent.getStringExtra("pid");

        name.setText(otherphonenumber);
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(PromiseChange.this, myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(PromiseChange.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                    /*    String state = "AM";
                        pstate = "AM";
                        // 선택한 시간이 12를 넘을경우 "PM"으로 변경 및 -12시간하여 출력 (ex : PM 6시 30분)
                        if (selectedHour > 12) {
                            selectedHour -= 12;
                            state = "PM";
                            pstate ="PM";
                        }*/
                        // EditText에 출력할 형식 지정
                        time.setText(selectedHour + "시 " + selectedMinute + "분");
                        System.out.println(selectedHour);
                        phour = Integer.toString(selectedHour);
                        if(selectedMinute <10){
                            pmin = "0"+Integer.toString(selectedMinute);
                        }
                        else {
                            pmin = Integer.toString(selectedMinute);
                        }
                    }
                }, hour, minute, false); // true의 경우 24시간 형식의 TimePicker 출현
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new android.app.AlertDialog.Builder(PromiseChange.this).create();
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
                        promisechange();
                        Intent intent = new Intent(PromiseChange.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                alertDialog.show();
            }
        });
    }

    private void updateLabel() {
        String myFormat = "yyyyMMdd";    // 출력형식   2018/11/28
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        date.setText(sdf.format(myCalendar.getTime()));
    }

    public void promisechange(){
        pdate = date.getText().toString();
        ptext = txt1.getText().toString();
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success){
                        Intent intent = new Intent(PromiseChange.this,MainActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(PromiseChange.this, "사용자가 앱을 설치하지 않았습니다. 사용자에게 앱 설치를 요청페이지로 이동합니다..", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(PromiseChange.this,SendSMS.class);
                        startActivity(intent);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };

        PromiseChange_validate ValidateRequest = new PromiseChange_validate(id, userphonenumber, otherphonenumber, pdate, phour, pmin, ptext, pid, responseListener);
        RequestQueue queue = Volley.newRequestQueue(PromiseChange.this);
        queue.add(ValidateRequest);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
