package com.promise.promising;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.promising.R;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import org.json.JSONObject;
import static com.android.volley.VolleyLog.TAG;

public class Login extends Activity {
    EditText userphonenumber, userpassword;
    Button login, signup;
    CheckBox id_store, auto_login;
    String user_phonenumber, user_password;
    String token;

    SharedPreferences shared;
    SharedPreferences.Editor editor;
    private long time= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        shared = getSharedPreferences("Mypref", Context.MODE_PRIVATE);

        userphonenumber = (EditText) findViewById(R.id.userphonenumber);
        userpassword = (EditText) findViewById(R.id.userpassword);
        login = (Button) findViewById(R.id.login);
        signup = (Button) findViewById(R.id.signup);
        id_store = (CheckBox) findViewById(R.id.id_store);
        auto_login = (CheckBox) findViewById(R.id.auto_login);


        if (shared.getBoolean("Auto_Login_enabled", false)) {
            userphonenumber.setText(shared.getString("userphonenumber", ""));
            userpassword.setText(shared.getString("PW", ""));
            auto_login.setChecked(true);
        }

        if (shared.getBoolean("Auto_Login_enabled2", false)) {
            userphonenumber.setText(shared.getString("userphonenumber", ""));
            id_store.setChecked(true);
        }

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
                        //System.out.println(token);
                    }
                });

        auto_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor = shared.edit();
                if(isChecked){
                    String user_phonenumber = userphonenumber.getText().toString();
                    String pw = userpassword.getText().toString();
                    editor.putString("userphonenumber", user_phonenumber);
                    editor.putString("PW", pw);
                    editor.putBoolean("Auto_Login_enabled", true);
                    editor.commit();
                }else{
                    editor.remove("Auto_Login_enabled");
                    editor.commit();
                }
            }
        });

        id_store.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor = shared.edit();
                if(isChecked){
                    String user_phonenumber = userphonenumber.getText().toString();
                    editor.putString("userphonenumber", user_phonenumber);
                    editor.putBoolean("Auto_Login_enabled2", true);
                    editor.commit();
                }else{
                    editor.remove("Auto_Login_enabled2");
                    editor.commit();
                }
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user_phonenumber = userphonenumber.getText().toString();
                user_password = userpassword.getText().toString();

                if(user_phonenumber.isEmpty()){
                    Toast.makeText(Login.this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(user_password.isEmpty()){
                    Toast.makeText(Login.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    login(user_phonenumber, user_password);
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });
    }

    public void login(final String user_phonenumber, final String user_password){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    SharedPreferences shared = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("userphonenumber",user_phonenumber);
                    editor.putString("userpassword",user_password);
                    editor.commit();

                    if(success){
                        Intent intent = new Intent(Login.this,MainActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(Login.this, "아이디와 비밀번호를 확인하세요.", Toast.LENGTH_LONG).show();
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };

        login_validateRequest ValidateRequest = new login_validateRequest(user_phonenumber, user_password, responseListener);
        RequestQueue queue = Volley.newRequestQueue(Login.this);
        queue.add(ValidateRequest);
    }

    @Override
    public void onBackPressed(){
        if(System.currentTimeMillis()-time>=2000){
            time=System.currentTimeMillis();
            Toast.makeText(getApplicationContext(),"뒤로 버튼을 한번 더 누르면 종료합니다.",Toast.LENGTH_SHORT).show();
        }else if(System.currentTimeMillis()-time<2000){
            finish();
        }
    }

}