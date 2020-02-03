package com.promise.promising;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class AgreementCancel_validateRequest extends StringRequest {
    final static private String URL = "https://scv0319.cafe24.com/weall/promise/agreementcancel.php";
    //final static private String URL = "https://appointment.kr/promise-php/promise/agreementcancel.php";
    private Map<String, String> parameters;
    public AgreementCancel_validateRequest(String userphonenumber, String id, String otherphonenumber, String pid, Response.Listener<String> listener){
        super(Request.Method.POST, URL, listener, null);//해당 URL에 POST방식으로 파마미터들을 전송함
        parameters = new HashMap<>();
        parameters.put("userphonenumber", userphonenumber);
        parameters.put("id", id);
        parameters.put("otherphonenumber", otherphonenumber);
        parameters.put("pid", pid);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
