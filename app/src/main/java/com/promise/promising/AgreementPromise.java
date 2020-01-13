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

public class AgreementPromise extends AppCompatActivity {
    String userphonenumber, id, otherphonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement_promise);

        Intent intent = getIntent();
        userphonenumber = intent.getStringExtra("userphonenumber");
        id = intent.getStringExtra("id");//상대 약속번호
        otherphonenumber = intent.getStringExtra("id");//상대 약속번호
        sendPromise();
    }

    public void sendPromise(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success){
                        Toast.makeText(AgreementPromise.this, "승인 완료", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AgreementPromise.this,MainActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(AgreementPromise.this, "승인 오류 발생", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AgreementPromise.this,MainActivity.class);
                        startActivity(intent);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        AgreementPromise_validateRequest ValidateRequest = new AgreementPromise_validateRequest(userphonenumber, id, otherphonenumber,responseListener);
        RequestQueue queue = Volley.newRequestQueue(AgreementPromise.this);
        queue.add(ValidateRequest);
    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}
