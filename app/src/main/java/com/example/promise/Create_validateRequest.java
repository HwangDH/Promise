package com.example.promise;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class Create_validateRequest extends StringRequest {
    final static private String URL = "https://scv0319.cafe24.com/weall/promise/sendpromise.php";
    private Map<String, String> parameters;
    public Create_validateRequest(String userid,String name, String date, String status, String hour, String min, String text, Response.Listener<String> listener){
        super(Request.Method.POST, URL, listener, null);//해당 URL에 POST방식으로 파마미터들을 전송함
        parameters = new HashMap<>();
        parameters.put("userid", userid);
        parameters.put("name", name);
        parameters.put("date", date);
        parameters.put("status", status);
        parameters.put("hour", hour);
        parameters.put("min", min);
        parameters.put("text", text);
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
