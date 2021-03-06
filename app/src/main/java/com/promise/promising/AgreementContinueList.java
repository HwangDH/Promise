package com.promise.promising;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.promising.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class AgreementContinueList extends AppCompatActivity {
    TextView name, date;
    Button btn1, agreement_wait, agreement_continue, agreement_finish;
    AlertDialog alertDialog;
    ListView listview;
    //데이터베이스에서 데이터 가져오기 위한 변수
    String myJSON;
    ListAdapter adapter;
    JSONArray peoples = null;
    ArrayList<HashMap<String, String>> personList;
    int count = 0;  //컬럼 수 계산
    SharedPreferences shared;
    //뒤로가기 연속 클릭 시간 계산 변수
    String userphonenumber;
    String [] id;
    String [] otherphonenumber;
    String [] endweekend;
    String [] pid;
    long backKeyPressedTime;
    static Boolean check= true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement_continue_list);

        //CountTimer ct = new CountTimer();
        //ct.Count();



        //약속상대와 날짜를 나타내기 위해 XML에서 가져온 변수
        name = (TextView)findViewById(R.id.name);
        date = (TextView)findViewById(R.id.date);

        //btn1 = 약속생성 버튼, btn2 = 약속수정버튼
        btn1 = (Button)findViewById(R.id.btn1);

        //동의 대기, 진행 중, 완료 버튼
        agreement_wait = (Button)findViewById(R.id.agreement_wait);
        agreement_continue = (Button)findViewById(R.id.agreement_continue);
        agreement_finish = (Button)findViewById(R.id.agreement_finish);

        //디비에 가져온 데이터 리스트뷰를 통해 띄우는 레이아웃
        listview = (ListView) findViewById(R.id.list);

        personList = new ArrayList<HashMap<String, String>>();
        //
        shared = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
        userphonenumber = shared.getString("userphonenumber", "");
      /*  while(check) {
            check = false;
            TimerTask tt = new TimerTask() {
                @Override
                public void run() {
                    alarm();
                }
            };
            Timer timer = new Timer();
            timer.schedule(tt, 0, 10000);
        }*/
        // Count();
        //Intent intent = getIntent();
        //userphonenumber = intent.getStringExtra("userphonenumber");

        //데이터베이스 호출
        //String url = "https://appointment.kr/promise-php/promise/agreecontinuelist.php?userphonenumber="+userphonenumber+"";
        String url = "https://scv0319.cafe24.com/weall/promise/agreecontinuelist.php?userphonenumber="+userphonenumber+"";
        getData(url);

        //약속생성 버튼 클릭 시
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(AgreementContinueList.this).create();
                alertDialog.setTitle("약속 생성");
                alertDialog.setMessage("약속을 생성하시겠습니까?");
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
                        Intent intent = new Intent(AgreementContinueList.this, CreatePromise.class);
                        intent.putExtra("userphonenumber", userphonenumber);
                        startActivity(intent);
                    }
                });
                alertDialog.show();
            }
        });

        //약속 목록 버튼 클릭 시
        agreement_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AgreementContinueList.this, MainActivity.class);
                intent.putExtra("userphonenumber", userphonenumber);
                startActivity(intent);
            }
        });

        //동의 대기 중 버튼 클릭 시
        agreement_wait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AgreementContinueList.this, AgreementWaitList.class);
                intent.putExtra("userphonenumber", userphonenumber);
                startActivity(intent);
            }
        });

        //약속 완료 버튼 클릭 시
        agreement_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AgreementContinueList.this, AgreementFinishList.class);
                intent.putExtra("userphonenumber", userphonenumber);
                startActivity(intent);
            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long ids) {
                Intent intent = new Intent(getApplicationContext(), DetailView.class);
                intent.putExtra("userphonenumber", userphonenumber);                //나의 폰번호
                intent.putExtra("id", id[position]);                                    //나의 약속번호
                intent.putExtra("otherphonenumber", otherphonenumber[position]);    //상대방 폰번호
                intent.putExtra("pid", pid[position]);                                  //상대약속번호
                startActivity(intent);
            }
        });
    }


    //데이터베이스에 가져온 데이터를 저장하는 메소드
    public void showList(){
        try{
            JSONObject jsonObject = new JSONObject(myJSON);
            peoples = jsonObject.getJSONArray("response");
            id=new String[peoples.length()];
            otherphonenumber=new String[peoples.length()];
            endweekend=new String[peoples.length()];
            pid=new String[peoples.length()];
            //JSON 배열 길이만큼 반복문을 실행
            while(count < peoples.length()){
                JSONObject object = peoples.getJSONObject(count);
                id[count] = object.getString("id");
                otherphonenumber[count] = object.getString("otherphonenumber");
                endweekend[count] = object.getString("endweekend");
                pid[count] = object.getString("pid");
                HashMap<String, String> persons = new HashMap<>();

                persons.put("id", id[count]);
                persons.put("otherphonenumber", otherphonenumber[count]);
                persons.put("endweekend", endweekend[count]);
                persons.put("pid", pid[count]);

                personList.add(persons);

                adapter = new SimpleAdapter(
                        AgreementContinueList.this, personList, R.layout.promise_list,
                        new String[] {"otherphonenumber", "endweekend"},
                        new int[] {R.id.name, R.id.date}
                );
                listview.setAdapter(adapter);
                count++;
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

    //뒤로가기 버튼 클릭 시 동작 함수
    @Override
    public void onBackPressed() {
    }
}
