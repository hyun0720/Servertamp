package com.example.leejh.servertamp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lee jh on 2017-11-12.
 */

public class GaeunRequest extends StringRequest {
    final static private String URL = "http://zzcandy.cafe24.com/gaeun.php";
    private Map<String, String> parameters;

    public GaeunRequest(String phonenumber,  Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("phonenumber",phonenumber);


    }
    public  Map<String,String> getParams(){
        return parameters;
    }

}
