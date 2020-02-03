package com.promise.promising;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.promising.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONObject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import android.app.AlertDialog;
import androidx.annotation.NonNull;

import static com.android.volley.VolleyLog.TAG;

public class SignUp extends AppCompatActivity {
    AlertDialog alertdialog;
    EditText userphonenumber, userpassword, userpassword2, username, cetrification;
    TextView signup, already;
    Button btn1, btn2;
    RadioGroup radioGroup1, radioGroup2, radioGroup3;
    String number = "";//인증번호
    String token;
    String phone_cert = "0";
    String agreement1="0", agreement2="0", agreement3="0";
    private boolean validate = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //XML변수 설정
        userphonenumber= (EditText) findViewById(R.id.userphonenumber);
        cetrification = (EditText)findViewById(R.id.cetrification);
        userpassword= (EditText) findViewById(R.id.userpassword);
        userpassword2 = (EditText)findViewById(R.id.userpassword2);
        username= (EditText) findViewById(R.id.username);
        btn1 = (Button) findViewById(R.id.btn1);//인증번호 요청 버튼
        btn2 = (Button)findViewById(R.id.btn2); //인증확인 버튼
        radioGroup1 = (RadioGroup)findViewById(R.id.radioGroup1);
        radioGroup2 = (RadioGroup)findViewById(R.id.radioGroup2);
        radioGroup3 = (RadioGroup)findViewById(R.id.radioGroup3);
        signup = (TextView) findViewById(R.id.signup);
        already = (TextView) findViewById(R.id.already);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        token = task.getResult().getToken();

                        //Toast.makeText(Signup.this, token, Toast.LENGTH_SHORT).show();

                    }
                });

        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rg_btn1){
                    agreement1 = "1";
                    Toast.makeText(SignUp.this, "동의하셨습니다..", Toast.LENGTH_SHORT).show();
                }
                else if(checkedId == R.id.rg_btn2){
                    Toast.makeText(SignUp.this, "동의하지 않으셨습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        radioGroup2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rg_btn3){
                    agreement2 = "1";
                    Toast.makeText(SignUp.this, "동의하셨습니다..", Toast.LENGTH_SHORT).show();
                }
                else if(checkedId == R.id.rg_btn4){
                    Toast.makeText(SignUp.this, "동의하지 않으셨습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        radioGroup3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if(checkedId == R.id.rg_btn5){
                    agreement3 = "1";
                    Toast.makeText(SignUp.this, "동의하셨습니다..", Toast.LENGTH_SHORT).show();
                }
                else if(checkedId == R.id.rg_btn6){
                    Toast.makeText(SignUp.this, "동의하지 않으셨습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        //btn1 = 인증번호 받기 버튼 클릭 시 처리 메소드 부분
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_phonenumber = userphonenumber.getText().toString();

                if (user_phonenumber.isEmpty()) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                    alertdialog = builder.setMessage("핸드폰 번호를 입력해주세요.")
                            .setPositiveButton("확인", null)
                            .create();
                    alertdialog.show();
                }

                //핸드폰 인증번호 받는 부분 구현 하는 메소드
                else {
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                String success = jsonResponse.getString("success");
                                number = success;
                                if (!success.isEmpty()) {//사용할 수 있는 아이디라면
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                                    alertdialog = builder.setMessage("인증 번호가 전송되었습니다.")
                                            .setPositiveButton("확인", null)
                                            .create();
                                    alertdialog.show();
                                    btn1.setActivated(false);
                                    validate = true;//검증완료
                                } else {//사용할 수 없는 아이디라면`
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                                    alertdialog = builder.setMessage("already used ID")
                                            .setNegativeButton("OK", null)
                                            .create();
                                    alertdialog.show();
                                    userphonenumber.setText("");
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    ValidateRequest ValidateRequest = new ValidateRequest(user_phonenumber, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(SignUp.this);
                    queue.add(ValidateRequest);
                }
            }
        });

        //btn2 = 인증번호 확인 버튼 클릭 시 처리 메소드 부분
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String snumber = cetrification.getText().toString();
                if(snumber.equals(number)){
                    phone_cert = "1";
                    userphonenumber.setFocusable(false);
                    cetrification.setFocusable(false);
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                    alertdialog = builder.setMessage("인증되었습니다.")
                            .setPositiveButton("확인", null)
                            .create();
                    alertdialog.show();
                    btn2.setEnabled(false);
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                    alertdialog = builder.setMessage("인증 실패했습니다. 다시 시도해주세요.")
                            .setPositiveButton("확인", null)
                            .create();
                    alertdialog.show();
                }
            }
        });

        already.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertdialog = new AlertDialog.Builder(SignUp.this).create();

                alertdialog.setTitle("Already have id?");
                alertdialog.setMessage("아이디를 가지고 계십니까?");
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
                        Intent intent = new Intent(SignUp.this, MainActivity.class);
                        startActivity(intent);
                    }
                });
                alertdialog.show();
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //사용자가 작성한 개인정보 데이터 저장 변수
                String sphonenumber = userphonenumber.getText().toString();
                String sphonecert = phone_cert;
                String spassword = userpassword.getText().toString();
                String sname = username.getText().toString();
                String sagreement1 = agreement1;
                String sagreement2 = agreement2;
                String sagreement3 = agreement3;
                String stoken = token;

                String password1 = userpassword.getText().toString();
                String password2 = userpassword2.getText().toString();

                // 비밀번호가 일치하지 않는다면
                if(PasswordCheck(password1, password2)==false){
                    userpassword.setText("");
                    userpassword2.setText("");
                    Toast.makeText(SignUp.this, "비밀번호가 일치하지않습니다.", Toast.LENGTH_SHORT).show();
                }
                //하나라도 정보를 입력하지 않으면
                else if(sphonenumber.isEmpty() ||spassword.isEmpty()|| sname.isEmpty()){
                    Toast.makeText(SignUp.this, "내용을 채워주세요.", Toast.LENGTH_SHORT).show();
                }
                //핸드폰 번호 인증하지 않았을 경우
                else if(sphonecert.equals("0")){
                    Toast.makeText(SignUp.this, "핸드폰 번호을 인증해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(sagreement1.equals("0") || sagreement2.equals("0")){
                    Toast.makeText(SignUp.this, "필수동의를 해주세요.", Toast.LENGTH_SHORT).show();
                }
                //모든것이 정상적으로 된다면
                else {
                    if(PasswordCheck(password1, password2)){
                        //sigup함수 호출 - 회원가입 승인
                        signup(sphonenumber,sphonecert,spassword,sname, sagreement1, sagreement2, sagreement3, stoken );
                        /*SharedPreferences preferences = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.putString("userphonenumber",sphonenumber);
                        editor.putString("userpassword",spassword);
                        editor.putString("username",sname);
                        editor.commit();*/

                        Toast.makeText(SignUp.this, "회원가입을 축하합니다.", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUp.this, Login.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(SignUp.this, "오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    public void show(){
        Intent intent = new Intent(SignUp.this,Login.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }

    //회원가입이 이뤄지는 함수-PHP호출과 데이터저장
    public void signup(final String userphonenumber, final String phonecert, final String userpassword, final String username, final String agreement1, final String agreement2, final String agreement3,final String token){
        //String url = "https://appointment.kr/promise-php/promise/signup.php";
        String url = "https://scv0319.cafe24.com/weall/promise/signup.php";
        RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Hitesh",""+response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Hitesh",""+error);
                Toast.makeText(SignUp.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> stringMap = new HashMap<>();
                stringMap.put("userphonenumber",userphonenumber);
                stringMap.put("phonecert",phonecert);
                stringMap.put("userpassword",userpassword);
                stringMap.put("username",username);
                stringMap.put("agreement1",agreement1);
                stringMap.put("agreement2",agreement2);
                stringMap.put("agreement3",agreement3);
                stringMap.put( "token", token );
                return stringMap;
            }
        };

        requestQueue.add(stringRequest);
    }

    public boolean PasswordCheck(String password1, String password2){
        if(password1.equals(password2)) {
            return true;
        }
        else{
            return false;
        }
    }
}