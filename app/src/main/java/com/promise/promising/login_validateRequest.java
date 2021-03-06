package com.promise.promising;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class login_validateRequest extends StringRequest {
    //final static private String URL = "https://appointment.kr/promise-php/promise/login.php";
    final static private String URL = "https://scv0319.cafe24.com/weall/promise/login.php";

    private Map<String, String> parameters;
    public login_validateRequest(String userphonenumber,String userpassword, Response.Listener<String> listener){
        super(Request.Method.POST, URL, listener, null);//해당 URL에 POST방식으로 파마미터들을 전송함
        parameters = new HashMap<>();
        parameters.put("userphonenumber", userphonenumber);
        parameters.put("userpassword", userpassword);
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
