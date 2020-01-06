package com.example.promise;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;


public class DetailView extends AppCompatActivity {
    Button btn1,btn2, btn3, btn4;
    TextView name, date, time, txt1, txt2, txt3, txt4;
    AlertDialog alertDialog;
    String id, userphonenumber, otherphonenumber, text, endweekend, phour, pmin, agreement, agreement1, status, pid;
    long backKeyPressedTime;

    String myJSON;
    ListAdapter adapter;
    JSONArray peoples = null;
    ArrayList<HashMap<String, String>> personList;
    int count = 0;  //컬럼 수 계산

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_view);

        btn1 = (Button)findViewById(R.id.btn1); //확인버튼
        btn2 = (Button)findViewById(R.id.btn2); //승인버튼
        btn3 = (Button)findViewById(R.id.btn3); //수정버튼
        btn4 = (Button)findViewById(R.id.btn4); //삭제버튼
        name = (TextView)findViewById(R.id.name);   //이름
        date = (TextView)findViewById(R.id.Date);   //날짜
        time = (TextView)findViewById(R.id.Time);   //시간
        txt1 = (TextView)findViewById(R.id.txt1);   //내용
        txt2 = (TextView)findViewById(R.id.txt2);   //나의 승인
        txt3 = (TextView)findViewById(R.id.txt3);   //상대 승인
        txt4 = (TextView)findViewById(R.id.txt4);   //마감 여부

        personList = new ArrayList<HashMap<String, String>>();

        //전 액티비티에서 값들 받오오는 부분
        Intent intent = getIntent();
        userphonenumber = intent.getStringExtra("userphonenumber");      //나의 폰번호
        id = intent.getStringExtra("id");                                   //나의 약속번호
        otherphonenumber = intent.getStringExtra("otherphonenumber");   //상대방 폰번호
        pid = intent.getStringExtra("pid");                                 //상대약속번호

        String url = "https://scv0319.cafe24.com/weall/promise/detailinfo.php?userphonenumber="+userphonenumber+"&otherphonenumber="+otherphonenumber+"&id="+id+"&pid="+pid+"";
        getData(url);
        //받아온 값들을 UI에 설정
        //name.setText(otherphonenumber);
        //date.setText(endweekend);
        //time.setText(status+phour+"시"+pmin+"분");
        //txt1.setText(text);

        //확인 버튼 클릭 시
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new android.app.AlertDialog.Builder(DetailView.this).create();
                alertDialog.setTitle("약속 확인");
                alertDialog.setMessage("약속리스트 화면으로 되돌아 가시겠습니까?");
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
                        Intent intent = new Intent(DetailView.this, MainActivity.class);

                        startActivity(intent);
                    }
                });
                alertDialog.show();
            }
        });

        //약속 승인 버튼 클릭 시
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(agreement.equals("0")){
                    alertDialog = new android.app.AlertDialog.Builder(DetailView.this).create();
                    alertDialog.setTitle("약속 승인");
                    alertDialog.setMessage("약속을 승인하시겠습니까?");
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
                            Intent intent = new Intent(DetailView.this, AgreementPromise.class);
                            //intent.putExtra("userphonenumber", userphonenumber);
                            //intent.putExtra("id", id);
                            intent.putExtra("userphonenumber", userphonenumber);
                            intent.putExtra("id", id);
                            startActivity(intent);
                        }
                    });
                    alertDialog.show();
                }
                else{
                    Toast.makeText(DetailView.this, "이미 승인 상태입니다.", Toast.LENGTH_LONG).show();
                }
            }
        });

        //약속수정 버튼 클릭 시
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new android.app.AlertDialog.Builder(DetailView.this).create();
                alertDialog.setTitle("약속 수정");
                alertDialog.setMessage("약속수정 화면으로 가시겠습니까?");
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
                        Intent intent = new Intent(DetailView.this, ChangePromise.class);
                        startActivity(intent);
                    }
                });
                alertDialog.show();
            }
        });

        //약속삭제 버튼 클릭 시
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog = new android.app.AlertDialog.Builder(DetailView.this).create();
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
                        Intent intent = new Intent(DetailView.this, AgreementPromise.class);
                        startActivity(intent);
                    }
                });
                alertDialog.show();
            }
        });
    }

    //데이터베이스에 가져온 데이터를 저장하는 메소드
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
                phour = object.getString("phour");
                pmin = object.getString("pmin");
                agreement = object.getString("agreement");
                agreement1 = object.getString("agreement1");
                //System.out.println("agreement1 : "+ agreement1);
                status = object.getString("status");
                pid = object.getString("pid");
                HashMap<String, String> persons = new HashMap<>();
                //count++;
                //System.out.println(count);
                name.setText(otherphonenumber);
                date.setText(endweekend);
                time.setText(phour+"시"+pmin+"분");
                txt1.setText(text);

                //나의 약속승인여부
                if(agreement.equals("0")) {
                    txt2.setText("승인X");
                }
                else{
                    txt2.setText("승인O");
                }

                //상대 승인여부
                if(agreement1.equals("0")) {
                    txt3.setText("승인X");
                }
                else{
                    txt3.setText("승인O");
                }

                txt4.setText(status);       //마감 여부
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

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

}
