package com.example.leejh.servertamp;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Lee jh on 2017-11-24.
 */

public class PandorothyStampUseRequest extends StringRequest {
    final static private String URL = "http://zzcandy.cafe24.com/PandorothyPhoneCouponUse.php";
    private Map<String, String> parameters;

    public PandorothyStampUseRequest(String phonenumber, String stamps, Response.Listener<String> listener){
        super(Method.POST,URL,listener,null);
        parameters = new HashMap<>();
        parameters.put("phonenumber",phonenumber);
        parameters.put("stamps",stamps);



    }
    public  Map<String,String> getParams(){
        return parameters;
    }

}
