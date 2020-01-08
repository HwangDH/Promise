package com.example.promise;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RemoveRequest_validate extends StringRequest {
    final static private String URL = "https://scv0319.cafe24.com/weall/promise/removerequest.php";
    private Map<String, String> parameters;
    public RemoveRequest_validate(String userphonenumber, String otherphonenumber, String id, String pid, Response.Listener<String> listener){
        super(Request.Method.POST, URL, listener, null);//해당 URL에 POST방식으로 파마미터들을 전송함
        parameters = new HashMap<>();
        parameters.put("userphonenumber", userphonenumber);
        parameters.put("otherphonenumber", otherphonenumber);
        parameters.put("id", id);
        parameters.put("pid", pid);

        System.out.println(userphonenumber);
        System.out.println(otherphonenumber);
        System.out.println(id);
        System.out.println(pid);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
