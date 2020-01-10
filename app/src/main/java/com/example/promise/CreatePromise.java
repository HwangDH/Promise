package com.example.promise;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import org.json.JSONObject;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreatePromise extends AppCompatActivity {
    Button btn1, btn2;
    EditText otherphonenumber, Date, Time, txt1;
    AlertDialog alertdialog;
    Calendar myCalendar = Calendar.getInstance();
    String potherphonenumber, pdate, phour, pmin, ptext;

    DatePickerDialog.OnDateSetListener myDatePicker = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }
    };

    long backKeyPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_promise);

        btn1 = (Button)findViewById(R.id.btn1);
        btn2 = (Button)findViewById(R.id.addressbook);
        otherphonenumber = (EditText)findViewById(R.id.otherphonenumber);
        Date = (EditText)findViewById(R.id.date);
        Time = (EditText) findViewById(R.id.time);
        txt1 = (EditText)findViewById(R.id.txt1);

        Intent intent = getIntent();
        final String userphonenumber = intent.getStringExtra("userphonenumber");

        Date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(CreatePromise.this, myDatePicker, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(CreatePromise.this, new TimePickerDialog.OnTimeSetListener() {
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
                        Time.setText(selectedHour + "시 " + selectedMinute + "분");
                        System.out.println(selectedHour);
                        phour = Integer.toString(selectedHour);
                        pmin = Integer.toString(selectedMinute);
                    }
                }, hour, minute, false); // true의 경우 24시간 형식의 TimePicker 출현
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        //약속 보내기 버튼 클릭 시
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                potherphonenumber = otherphonenumber.getText().toString();
                pdate = Date.getText().toString();
                ptext = txt1.getText().toString();

                if(potherphonenumber.isEmpty() || pdate.isEmpty() || ptext.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(CreatePromise.this);
                    alertdialog = builder.setMessage("공백이 있습니다.")
                            .setPositiveButton("확인", null)
                            .create();
                    alertdialog.show();
                }
                else{
                    alertdialog = new AlertDialog.Builder(CreatePromise.this).create();
                    alertdialog.setTitle("약속 보내기");
                    alertdialog.setMessage("약속을 상대방에게 보내겠습니까?");
                    alertdialog.setCancelable(false);
                    alertdialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            alertdialog.dismiss();
                        }
                    });

                    alertdialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            sendPromise(userphonenumber, potherphonenumber, pdate, phour, pmin, ptext);
                            Intent intent = new Intent(CreatePromise.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });
                    alertdialog.show();
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setData(ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            Cursor cursor = null;
            if (data != null) {
                cursor = getContentResolver().query(data.getData(), new String[]{ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER}, null, null, null); }
            if (cursor != null) {
                cursor.moveToFirst();
                Log.e("cursor", "name : " + cursor.getString(0));
                Log.e("cursor", "number : " + cursor.getString(1));
                String number = cursor.getString(1);
                String number2 = number.replace("-", "");
                otherphonenumber.setText(number2);
                cursor.close();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updateLabel() {
        String myFormat = "yyyyMMdd";    // 출력형식   2018/11/28
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);

        Date.setText(sdf.format(myCalendar.getTime()));
    }

    public void sendPromise(final String userphonenumber, final String otherphonenumber, final String date, final String hour, final String min, final  String text){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success){
                        Intent intent = new Intent(CreatePromise.this,MainActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(CreatePromise.this, "사용자가 앱을 설치하지 않았습니다. 사용자에게 앱 설치를 요청페이지로 이동합니다..", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(CreatePromise.this,SendSMS.class);
                        startActivity(intent);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };

        Create_validateRequest ValidateRequest = new Create_validateRequest(userphonenumber, otherphonenumber, date, hour, min, text, responseListener);
        RequestQueue queue = Volley.newRequestQueue(CreatePromise.this);
        queue.add(ValidateRequest);
    }

    @Override
    public void onBackPressed() {
        //1번째 백버튼 클릭
        if(System.currentTimeMillis()>backKeyPressedTime+2000){
            backKeyPressedTime = System.currentTimeMillis();
            Toast.makeText(this, "한번 더 누르시면 앱을 종료합니다.", Toast.LENGTH_SHORT).show();
        }
        //2번째 백버튼 클릭 (종료)
        else{
            AppFinish();
        }
    }

    //앱종료
    public void AppFinish(){
        finish();
        System.exit(0);
        android.os.Process.killProcess(android.os.Process.myPid());
    }

}
