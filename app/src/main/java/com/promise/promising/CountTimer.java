package com.promise.promising;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.Date;

public class CountTimer {
    public void Count(){
        ScheduledJob job = new ScheduledJob();
        Timer jobScheduler = new Timer();
        jobScheduler.scheduleAtFixedRate(job, 1000, 10000);
        try{
            Thread.sleep(20000);
        }catch (InterruptedException e){
            jobScheduler.cancel();
        }
    }
}

class ScheduledJob extends TimerTask{
    public static int count=0;

    public void run() {
        signup();
        System.out.println(count);
        count++;
    }

    public void signup() {
        String url = "https://scv0319.cafe24.com/weall/promise/alarm.php";
        //RequestQueue requestQueue = Volley.newRequestQueue(SignUp.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("Hitesh", "" + response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("Hitesh", "" + error);
                // Toast.makeText(SignUp.this, ""+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> stringMap = new HashMap<>();
                return stringMap;
            }
        };

        class Async_test extends AsyncTask<String,Void, String>{

            @Override
            protected String doInBackground(String... strings) {
                return null;
            }
        }
        //requestQueue.add(stringRequest);
    }
}

