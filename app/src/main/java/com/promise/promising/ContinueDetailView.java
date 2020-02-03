package com.promise.promising;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.promising.R;
import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.promising.R;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import com.android.promising.R;

public class ContinueDetailView extends AppCompatActivity {
    Button btn1,btn2, btn3;
    TextView name, date, time, txt1, txt2, txt3;
    AlertDialog alertDialog;
    String id, userphonenumber, otherphonenumber, text, endweekend, restweek, agreement, agreement1, pid, pend;
    int state=0;
    String myJSON;
    ListAdapter adapter;
    JSONArray peoples = null;
    ArrayList<HashMap<String, String>> personList;
    int count = 0;  //컬럼 수 계산

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_continue_detail_view);

        btn1 = (Button)findViewById(R.id.btn1); //수정버튼
        btn2 = (Button)findViewById(R.id.btn2); //삭제버튼
        btn3 = (Button)findViewById(R.id.btn3); //완료버튼
        name = (TextView)findViewById(R.id.name);   //이름
        date = (TextView)findViewById(R.id.date);   //날짜
        time = (TextView)findViewById(R.id.time);   //시간
        txt1 = (TextView)findViewById(R.id.txt1);   //내용
        txt2 = (TextView)findViewById(R.id.txt2);   //나의 승인
        txt3 = (TextView)findViewById(R.id.txt3);   //상대 승인
        personList = new ArrayList<HashMap<String, String>>();

        //전 액티비티에서 값들 받오오는 부분
        Intent intent = getIntent();
        userphonenumber = intent.getStringExtra("userphonenumber");      //나의 폰번호
        id = intent.getStringExtra("id");                                   //나의 약속번호
        otherphonenumber = intent.getStringExtra("otherphonenumber");   //상대방 폰번호
        pid = intent.getStringExtra("pid");                                 //상대약속번호

        //String url = "https://appointment.kr/promise-php/promise/detailinfo.php?userphonenumber="+userphonenumber+"&otherphonenumber="+otherphonenumber+"&id="+id+"&pid="+pid+"";
        String url = "https://scv0319.cafe24.com/weall/promise/detailinfo.php?userphonenumber="+userphonenumber+"&otherphonenumber="+otherphonenumber+"&id="+id+"&pid="+pid+"";
        getData(url);
        //받아온 값들을 UI에 설정
        //name.setText(otherphonenumber);
        //date.setText(endweekend);
        //time.setText(status+phour+"시"+pmin+"분");
        //txt1.setText(text);

        //약속 수정
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new android.app.AlertDialog.Builder(ContinueDetailView.this).create();
                alertDialog.setTitle("약속 수정");
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
                        Intent intent = new Intent(ContinueDetailView.this, MainActivity.class);
                        process(state);
                        startActivity(intent);
                    }
                });
                alertDialog.show();
            }
        });

        //약속 삭제
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new android.app.AlertDialog.Builder(ContinueDetailView.this).create();
                alertDialog.setTitle("약속 삭제");
                alertDialog.setMessage("약속을 삭제하시겠습니까?");
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
                        state = 2;
                        Intent intent = new Intent(ContinueDetailView.this, MainActivity.class);
                        process(state);
                        startActivity(intent);
                    }
                });
                alertDialog.show();
            }
        });

        //약속 완료
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new android.app.AlertDialog.Builder(ContinueDetailView.this).create();
                alertDialog.setTitle("약속 완료");
                alertDialog.setMessage("약속을 완료하시겠습니까?");
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
                        state = 3;
                        Intent intent = new Intent(ContinueDetailView.this, MainActivity.class);
                        process(state);
                        startActivity(intent);
                    }
                });
                alertDialog.show();
            }
        });
    }

    public void showList(){
        try{
            JSONObject jsonObject = new JSONObject(myJSON);
            peoples = jsonObject.getJSONArray("response");

            //JSON 배열 길이만큼 반복문을 실행
            for(int i = 0; i< peoples.length(); i++){
                JSONObject object = peoples.getJSONObject(count);
                id = object.getString("id");
                otherphonenumber = object.getString("otherphonenumber");
                text = object.getString("text");
                endweekend = object.getString("endweekend");
                restweek = object.getString("restweek");
                agreement = object.getString("agreement");
                agreement1 = object.getString("agreement1");
                pend = object.getString("pend");
                pid = object.getString("pid");
                HashMap<String, String> persons = new HashMap<>();


                name.setText(otherphonenumber);
                date.setText(endweekend.substring(0,10));
                time.setText(endweekend.substring(11,13)+"시"+endweekend.substring(14,16)+"분");
                txt1.setText(text);

                //나의 약속승인여부
                if(agreement.equals("0")) {
                    txt2.setText("승인X");
                }
                else{
                    txt2.setText("승인O");
                    btn1.setEnabled(false);
                    btn2.setEnabled(false);
                }

                //상대 승인여부
                if(agreement1.equals("0")) {
                    txt3.setText("승인X");
                }
                else{
                    txt3.setText("승인O");
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //데이터베이스에서 데이터 가져오는 메소드
    public void getData(String url) {
        class GetDataJSON extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();
                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }

                    return sb.toString().trim();

                } catch (Exception e) {
                    return null;
                }
            }

            @Override
            protected void onPostExecute(String result) {
                myJSON = result;
                showList();
            }
        }
        GetDataJSON g = new GetDataJSON();
        g.execute(url);
    }

    public String doYearMonthDay() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm", Locale.KOREA);
        Date date = new Date();
        String currentDate = formatter.format(date);
        return currentDate;
    }

    public void process(int state) {
        if (state == 1) {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            Toast.makeText(ContinueDetailView.this, "약속승인 완료", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ContinueDetailView.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ContinueDetailView.this, "약속승인 오류 발생", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ContinueDetailView.this, nothingactivity.class);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            WaitDetail_validate ValidateRequest = new WaitDetail_validate(userphonenumber, otherphonenumber, id, pid, responseListener);
            RequestQueue queue = Volley.newRequestQueue(ContinueDetailView.this);
            queue.add(ValidateRequest);
        }
        else if(state == 2){
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            Toast.makeText(ContinueDetailView.this, "약속거절 완료", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ContinueDetailView.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ContinueDetailView.this, "약속거절 오류 발생", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ContinueDetailView.this, nothingactivity.class);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            WaitDetail_validate ValidateRequest = new WaitDetail_validate(userphonenumber, otherphonenumber, id, pid, "1", responseListener);
            RequestQueue queue = Volley.newRequestQueue(ContinueDetailView.this);
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
                            Toast.makeText(ContinueDetailView.this, "약속거절 완료", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ContinueDetailView.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(ContinueDetailView.this, "약속거절 오류 발생", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(ContinueDetailView.this, nothingactivity.class);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            WaitDetail_validate ValidateRequest = new WaitDetail_validate(userphonenumber, otherphonenumber, id, pid, "1", responseListener);
            RequestQueue queue = Volley.newRequestQueue(ContinueDetailView.this);
            queue.add(ValidateRequest);
        }
    }
}
