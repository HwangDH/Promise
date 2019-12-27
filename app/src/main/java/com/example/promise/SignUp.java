package com.example.promise;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
import androidx.viewpager.widget.PagerAdapter;

import static com.android.volley.VolleyLog.TAG;

public class SignUp extends AppCompatActivity {
    AlertDialog alertdialog;
    EditText userid, userpassword, userpassword2, username, userage, phonenumber;
    TextView signup, already;
    Button id_check;

    int num=0;
    private boolean validate = false;
    String user_id, user_id2;
    String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //XML변수 설정
        userid= (EditText) findViewById(R.id.userid);
        userpassword= (EditText) findViewById(R.id.userpassword);
        userpassword2 = (EditText)findViewById(R.id.userpassword2);
        username= (EditText) findViewById(R.id.username);
        userage= (EditText) findViewById(R.id.userage);
        phonenumber = (EditText)findViewById(R.id.phonenumber);
        id_check = (Button) findViewById(R.id.id_check);
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

                        // Log and toast
                        //String msg = getString(R.string.msg_token_fmt, token);
                        //Log.d(TAG, msg);
                        //Toast.makeText(Signup.this, token, Toast.LENGTH_SHORT).show();
                        System.out.println(token);
                    }
                });

        id_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_id= userid.getText().toString();
                user_id2 = user_id;
                if (user_id.isEmpty()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                    alertdialog = builder.setMessage("ID is empty")
                            .setPositiveButton("확인", null)
                            .create();
                    alertdialog.show();
                }
                else{
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try{
                                //Toast.makeText(Signup.this, response, Toast.LENGTH_LONG).show();
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");

                                if(success){//사용할 수 있는 아이디라면
                                    num=1;
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                                    alertdialog = builder.setMessage("you can use ID")
                                            .setPositiveButton("OK", null)
                                            .create();
                                    alertdialog.show();
                                    //System.out.println(num);
                                    validate = true;//검증완료
                                }else{//사용할 수 없는 아이디라면`
                                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                                    alertdialog = builder.setMessage("already used ID")
                                            .setNegativeButton("OK", null)
                                            .create();
                                    alertdialog.show();
                                    userid.setText("");
                                }

                            }catch(Exception e){
                                e.printStackTrace();
                            }
                        }
                    };
                    ValidateRequest ValidateRequest = new ValidateRequest(user_id, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(SignUp.this);
                    queue.add(ValidateRequest);
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
                        //SharedPreferences.Editor editor = shared.edit();
                        //editor.clear();
                        //editor.commit();
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
                String sid = userid.getText().toString();
                String spassword = userpassword.getText().toString();
                String sname = username.getText().toString();
                String sage = userage.getText().toString();
                String sphonenumber = phonenumber.getText().toString();
                String stoken = token;

                String password1 = userpassword.getText().toString();
                String password2 = userpassword2.getText().toString();

                //일치하지 않는다면
                if(PasswordCheck(password1, password2)==false){
                    userpassword.setText("");
                    userpassword2.setText("");
                    Toast.makeText(SignUp.this, "비밀번호가 일치하지않습니다.", Toast.LENGTH_SHORT).show();
                }
                //하나라도 정보를 입력하지 않으면
                else if(sid.isEmpty() ||spassword.isEmpty()||sname.isEmpty()||sage.isEmpty()){
                    Toast.makeText(SignUp.this, "Fill all details", Toast.LENGTH_SHORT).show();
                }
                //아이디 중복검사를 하지 않았을 경우
                else if(num==0){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                    alertdialog = builder.setMessage("아이디 중복검사를 해주세요.")
                            .setPositiveButton("OK", null)
                            .create();
                    alertdialog.show();
                }
                //중복검사 후 아이디 변경한 뒤 중복검사를 하지 않았을 경우
                else if(!user_id.equals(sid)){
                    AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                    alertdialog = builder.setMessage("아이디 중복검사를 해주세요.")
                            .setPositiveButton("OK", null)
                            .create();
                    alertdialog.show();
                }
                //모든것이 정상적으로 된다면
                else {
                    if(PasswordCheck(password1, password2)){
                        //sigup함수 호출 - 회원가입 승인
                        signup(sid,spassword,sname,sage, sphonenumber, stoken );
                        SharedPreferences preferences = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.clear();
                        editor.putString("userid",sid);
                        editor.putString("userpassword",spassword);
                        editor.putString("username",sname);
                        editor.putString("userage",sage);
                        editor.commit();
                        AlertDialog.Builder builder = new AlertDialog.Builder(SignUp.this);
                        alertdialog = builder.setMessage("회원가입을 축하합니다.")
                                .setPositiveButton("OK", null)
                                .create();
                        alertdialog.show();
                        show();
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
    public void signup(final String userid, final String userpassword,final String username, final String userage, final String phonenumber, final String token){
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
                stringMap.put("userid",userid);
                stringMap.put("userpassword",userpassword);
                stringMap.put("username",username);
                stringMap.put("userage",userage);
                stringMap.put("phonenumber",phonenumber);
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