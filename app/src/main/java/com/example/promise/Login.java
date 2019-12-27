package com.example.promise;

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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class Login extends Activity {
    EditText userid, userpasswod;
    Button login, signup;
    CheckBox id_store, auto_login;
    String user_id, user_password;
    String token;

    SharedPreferences shared;
    SharedPreferences.Editor editor;
    private long time= 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        shared = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
        userid = (EditText)findViewById(R.id.userid);
        userpasswod =(EditText)findViewById(R.id.userpassword);
        login = (Button)findViewById(R.id.login);
        signup = (Button)findViewById(R.id.signup);
        id_store = (CheckBox)findViewById(R.id.id_store);
        auto_login = (CheckBox)findViewById(R.id.auto_login);


        if(shared.getBoolean("Auto_Login_enabled", false)){
            userid.setText(shared.getString("ID", ""));
            userpasswod.setText(shared.getString("PW", ""));
            auto_login.setChecked(true);
        }

        if(shared.getBoolean("Auto_Login_enabled2", false)){
            userid.setText(shared.getString("ID",""));
            id_store.setChecked(true);
        }

       /* FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "getInstanceId failed", task.getException());
                            return;
                        }
                        // Get new Instance ID token
                        token = task.getResult().getToken();
                    }
                });
*/
        auto_login.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor = shared.edit();
                if(isChecked){
                    String id = userid.getText().toString();
                    String pw = userpasswod.getText().toString();
                    editor.putString("ID", id);
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
                    String id = userid.getText().toString();
                    editor.putString("ID", id);
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
                user_id = userid.getText().toString();
                user_password = userpasswod.getText().toString();

                if(user_id.isEmpty()){
                    Toast.makeText(Login.this, "아이디를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else if(user_password.isEmpty()){
                    Toast.makeText(Login.this, "비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                else{
                    login(user_id, user_password);
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

    public void login(final String user_id, final String user_password){
        String url = "https://scv0319.cafe24.com/weall/promise/login.php";
        RequestQueue requestQueue = Volley.newRequestQueue(Login.this);
        /*StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
           public void onResponse(String response)  {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);

                    String userid2 = jsonObject1.getString("userid");
                    String userpassword2 = jsonObject1.getString("userpassword");
                    SharedPreferences shared = getSharedPreferences("Mypref", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = shared.edit();
                    editor.putString("userid",userid2);
                    editor.putString("userpassword",userpassword2);
                    editor.commit();

                    Intent intent = new Intent(Login.this,MainActivity.class);
                    startActivity(intent);

                }
                catch (JSONException e) {
                    Toast.makeText(Login.this, "아이디와 비밀번호를 정확하게 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("HiteshURLerror",""+error);
            }
        });
        requestQueue.add(stringRequest);*/
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("result");
                    JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                    String userid2 = jsonObject1.getString("userid");
                    String userpassword2 = jsonObject1.getString("userpassword");

                    System.out.println(userid2);
                    System.out.println(userpassword2);
                    if(userid2.equals(user_id) && userpassword2.equals(user_password)) {
                        Intent intent = new Intent(Login.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
                catch (JSONException e) {
                    Toast.makeText(Login.this, "아이디와 비밀번호를 정확하게 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Hitesh",""+error);
                Toast.makeText(Login.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> stringMap = new HashMap<>();
                stringMap.put("userid",user_id);
                stringMap.put("userpassword",user_password);
                return stringMap;
            }
        };

        requestQueue.add(stringRequest);
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