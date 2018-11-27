package com.example.leejh.servertamp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Lee jh on 2017-11-24.
 */

public class PandorothyPhoneServer extends Activity implements View.OnClickListener {

    private EditText mPhoneNumber;
    private TextView mOutputNumber;
    private TextView mOutputNumber_refined;
    private Button mBtn;
    private LinearLayout mLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_server);
        mLayout = (LinearLayout) findViewById(R.id.Phone);
        mLayout.setBackgroundColor(Color.parseColor("#594235"));

        mPhoneNumber = (EditText) findViewById(R.id.phone_number);
        mBtn = (Button) findViewById(R.id.getNumber);

        mBtn.setOnClickListener(this);
    }

    //핸드폰 번호 입력 확인 버튼  -> ***입력한 번호로 스템프 보내야함
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getNumber:
                final String phonenumber = mPhoneNumber.getText().toString();
                final int stamps = 1;


                if (phonenumber.equals("")) {
                    Toast toast = Toast.makeText(getApplicationContext(), "핸드폰 번호가 없습니다", Toast.LENGTH_SHORT); toast.show();

                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                Toast.makeText(PandorothyPhoneServer.this, "쿠폰 등록에 성공했습니다.", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(getApplicationContext(), GaeunPhoneServer.class);
                                intent.putExtra("phonenumber", phonenumber);
                                intent.putExtra("stamps", stamps);
                            } else {
                                Toast.makeText(PandorothyPhoneServer.this, "쿠폰 등록에 실패했습니다. ", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                PandorothyStampRequest pandorothyStampRequest = new PandorothyStampRequest(phonenumber, stamps +"" ,responseListener);
                RequestQueue queue = Volley.newRequestQueue(PandorothyPhoneServer.this);
                queue.add(pandorothyStampRequest);
        }
    }
}
