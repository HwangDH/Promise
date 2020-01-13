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

public class AgreementCancel extends AppCompatActivity {

    String userphonenumber, id, otherphonenumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agreement_cancel);

        Intent intent = getIntent();
        userphonenumber = intent.getStringExtra("userphonenumber");
        id = intent.getStringExtra("id");//나의 약속번호
        otherphonenumber= intent.getStringExtra("otherphonenumber");
        agreement_cancel();
    }

    public void agreement_cancel(){
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if(success){
                        Toast.makeText(AgreementCancel.this, "승인 취소 완료", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AgreementCancel.this,MainActivity.class);
                        startActivity(intent);
                    }
                    else{
                        Toast.makeText(AgreementCancel.this, "승인 취소 오류 발생", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(AgreementCancel.this,MainActivity.class);
                        startActivity(intent);
                    }
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        };
        AgreementCancel_validateRequest ValidateRequest = new AgreementCancel_validateRequest(userphonenumber, id, otherphonenumber, responseListener);
        RequestQueue queue = Volley.newRequestQueue(AgreementCancel.this);
        queue.add(ValidateRequest);
    }
}
