package com.promise.promising;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


public class ValidateRequest extends StringRequest {
    final static private String URL = "https://scv0319.cafe24.com/weall/promise/phone_check.php";
    final static private String URL2 = "https://scv0319.cafe24.com/weall/promise/alarm.php";

    private Map<String, String> parameters;

        public ValidateRequest(String userphonenumber, Response.Listener<String> listener){
            super(Method.POST, URL, listener, null);//해당 URL에 POST방식으로 파마미터들을 전송함
            parameters = new HashMap<>();
            parameters.put("userphonenumber", userphonenumber);
        }

        public ValidateRequest(String userphonenumber, String trash, Response.Listener<String> listener){
            super(Method.POST, URL2, listener, null);//해당 URL에 POST방식으로 파마미터들을 전송함
            parameters = new HashMap<>();
            parameters.put("userphonenumber", userphonenumber);
            parameters.put("trash", trash);
            System.out.println();
        }

        @Override
        protected Map<String, String> getParams() throws AuthFailureError {
            return parameters;
        }


}