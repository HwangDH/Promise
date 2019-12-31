package com.example.promise;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AgreementPromise_validateRequest  extends StringRequest {
    final static private String URL = "https://scv0319.cafe24.com/weall/promise/agreementpromise.php";
    private Map<String, String> parameters;
    public AgreementPromise_validateRequest(String ptext, Response.Listener<String> listener){
        super(Request.Method.POST, URL, listener, null);//해당 URL에 POST방식으로 파마미터들을 전송함
        parameters = new HashMap<>();
        parameters.put("ptext", ptext);
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
