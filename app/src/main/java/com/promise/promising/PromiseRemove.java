package com.promise.promising;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.android.promising.R;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class PromiseRemove extends AppCompatActivity {
    String userphonenumber, otherphonenumber, id, pid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_promise_remove);

        Intent intent = getIntent();
        userphonenumber = intent.getStringExtra("userphonenumber");
        otherphonenumber = intent.getStringExtra("otherphonenumber");
        id = intent.getStringExtra("id");
        pid = intent.getStringExtra("pid");

        RemovePromise();
    }

    public void RemovePromise(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success){
                        Toast.makeText(PromiseRemove.this, "승인 완료", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(PromiseRemove.this,MainActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(PromiseRemove.this, "승인 오류 발생", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(PromiseRemove.this,MainActivity.class);
                        startActivity(intent);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        PromiseRemove_validateRequest ValidateRequest = new PromiseRemove_validateRequest(userphonenumber, otherphonenumber, id, pid,responseListener);
        RequestQueue queue = Volley.newRequestQueue(PromiseRemove.this);
        queue.add(ValidateRequest);
    }
}
