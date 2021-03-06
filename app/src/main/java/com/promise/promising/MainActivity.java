package com.promise.promising;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
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
import java.util.Calendar;
import java.util.HashMap;

public class MainActivity extends Activity {
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
    String [] pend;
    long backKeyPressedTime;
    static Boolean check= true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
        //String url = "https://appointment.kr/promise-php/promise/promiseinfo.php?userphonenumber="+userphonenumber+"";
        String url = "https://scv0319.cafe24.com/weall/promise/promiseinfo.php?userphonenumber="+userphonenumber+"";

        getData(url);

        //약속생성 버튼 클릭 시
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new AlertDialog.Builder(MainActivity.this).create();
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
                        Intent intent = new Intent(MainActivity.this, CreatePromise.class);
                        intent.putExtra("userphonenumber", userphonenumber);
                        startActivity(intent);
                    }
                });
                alertDialog.show();
            }
        });

        //동의 대기 버튼 클릭 시
        agreement_wait.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AgreementWaitList.class);
                intent.putExtra("userphonenumber", userphonenumber);
                startActivity(intent);
            }
        });

        //약속 진행 중 버튼 클릭 시
        agreement_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AgreementContinueList.class);
                intent.putExtra("userphonenumber", userphonenumber);
                startActivity(intent);
            }
        });

        //약속 완료 버튼 클릭 시
        agreement_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AgreementFinishList.class);
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
            pend = new String[peoples.length()];
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
                persons.put("pend", pend[count]);


                personList.add(persons);

                adapter = new SimpleAdapter(
                        MainActivity.this, personList, R.layout.promise_list,
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


   /* public void alarm() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            Toast.makeText(MainActivity.this, "1", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "2", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this, nothingactivity.class);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            ValidateRequest ValidateRequest = new ValidateRequest(userphonenumber, "1", responseListener);
            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
            queue.add(ValidateRequest);
    }*/

  /*  public void Count(){
        ScheduledJob job = new ScheduledJob();
        Timer jobScheduler = new Timer();
        jobScheduler.scheduleAtFixedRate(job, 1000, 10000);
        try{
            Thread.sleep(20000);
        }catch (InterruptedException e){
            jobScheduler.cancel();
        }
    }

    class ScheduledJob extends TimerTask {
        public void run() {
            //signup("01097753356");
            System.out.println(count);
            alarm();
        }
        public void alarm() {
            Response.Listener<String> responseListener = new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");
                        if (success) {
                            Toast.makeText(MainActivity.this, "1", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this, MainActivity.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "2", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(MainActivity.this, nothingactivity.class);
                            startActivity(intent);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };
            ValidateRequest ValidateRequest = new ValidateRequest(userphonenumber, "1", responseListener);
            RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
            queue.add(ValidateRequest);
    }
}*/


}

