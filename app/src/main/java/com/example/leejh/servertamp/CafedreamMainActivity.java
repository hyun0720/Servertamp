package com.example.leejh.servertamp;

import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.tech.NfcF;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;

import static com.example.leejh.servertamp.R.id.TV;

/**
 * Created by Lee jh on 2017-11-23.
 */

public class CafedreamMainActivity extends AppCompatActivity {
    TextView mTextView;
    NfcAdapter mNfcAdapter; // NFC 어댑터
    PendingIntent mPendingIntent; // 수신받은 데이터가 저장된 인텐트
    IntentFilter[] mIntentFilters; // 인텐트 필터
    String[][] mNFCTechLists;
    private LinearLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLayout = (LinearLayout) findViewById(R.id.Linear);
        mLayout.setBackgroundColor(Color.parseColor("#e0bd91"));

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(0xFFcd5c5c)); //액션바색깔

        mTextView = (TextView)findViewById(TV);
        // NFC 어댑터를 구한다
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);
        // NFC 어댑터가 null 이라면 칩이 존재하지 않는 것으로 간주
        if( mNfcAdapter == null ) {
            mTextView.setText("이 핸드폰은 NFC를 사용할 수 없습니다.");
            return;
        }

        mTextView.setText("NFC 태그를 찍으세요.");

        // NFC 데이터 활성화에 필요한 인텐트를 생성
        Intent intent = new Intent(this, getClass());
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        mPendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        // NFC 데이터 활성화에 필요한 인텐트 필터를 생성
        IntentFilter iFilter = new IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
        try {
            iFilter.addDataType("*/*");
            mIntentFilters = new IntentFilter[] { iFilter };
        } catch (Exception e) {
            mTextView.setText("Make IntentFilter error");
        }
        mNFCTechLists = new String[][] { new String[] { NfcF.class.getName() } };

        Button button1 = (Button) findViewById(R.id.button1);
        Button button2 = (Button) findViewById(R.id.button2);
        Button button3 = (Button) findViewById(R.id.button3);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                Intent intent_act = new Intent(getApplicationContext(), CafedreamPhoneServer.class);
                startActivity(intent_act);
            }

        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                Intent intent_act = new Intent(getApplicationContext(), CafedreamNfcServer.class);
                startActivity(intent_act);
            }

        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                Intent intent_act = new Intent(getApplicationContext(), CafedreamPhoneCouponUse.class);
                startActivity(intent_act);
            }

        });
    }

    public void onResume() {
        super.onResume();
        // 앱이 실행될때 NFC 어댑터를 활성화 한다
        if( mNfcAdapter != null )
            mNfcAdapter.enableForegroundDispatch(this, mPendingIntent, mIntentFilters, mNFCTechLists);

        // NFC 태그 스캔으로 앱이 자동 실행되었을때
        if( NfcAdapter.ACTION_NDEF_DISCOVERED.equals(getIntent().getAction()) )
            // 인텐트에 포함된 정보를 분석해서 화면에 표시
            onNewIntent(getIntent());
    }

    public void onPause() {
        super.onPause();
        // 앱이 종료될때 NFC 어댑터를 비활성화 한다
        if( mNfcAdapter != null )
            mNfcAdapter.disableForegroundDispatch(this);
    }

    // NFC 태그 정보 수신 함수. 인텐트에 포함된 정보를 분석해서 화면에 표시
    @Override
    public void onNewIntent(Intent intent) {
        // 인텐트에서 액션을 추출
        String action = intent.getAction();
        // 인텐트에서 태그 정보 추출
        String tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG).toString();
        String strMsg = action + "\n\n" + tag;
        // 액션 정보와 태그 정보를 화면에 출력
        mTextView.setText(strMsg);

        // 인텐트에서 NDEF 메시지 배열을 구한다
        Parcelable[] messages = intent.getParcelableArrayExtra(
                NfcAdapter.EXTRA_NDEF_MESSAGES);
        if(messages == null) return;

        for(int i=0; i < messages.length; i++)
            // NDEF 메시지를 화면에 출력
            showMsg((NdefMessage)messages[i]);
    }

    // NDEF 메시지를 화면에 출력
    public void showMsg(NdefMessage mMessage) {
        String strMsg = "", strRec="";
        // NDEF 메시지에서 NDEF 레코드 배열을 구한다
        NdefRecord[] recs = mMessage.getRecords();
        for (int i = 0; i < recs.length; i++) {
            // 개별 레코드 데이터를 구한다
            NdefRecord record = recs[i];
            byte[] payload = record.getPayload();
            // 레코드 데이터 종류가 텍스트 일때
            if( Arrays.equals(record.getType(), NdefRecord.RTD_TEXT) ) {
                // 버퍼 데이터를 인코딩 변환
                strRec = byteDecoding(payload);
                strRec =  strRec;
            }
            strMsg += (strRec);

            mTextView.setText(strRec);

            //NFC로 쿠폰 쓸 때, 카페이름으로 구분해서 디비에 stamps 갯수 빼기
            // NDEF 메시지를 화면에 출력
            final String phonenumber2 = mTextView.getText().toString();

            final int stamps2 = 10;

            Response.Listener<String> responseListener = new Response.Listener<String>() {
                public void onResponse(String response) {
                    try {
                        JSONObject jsonResponse = new JSONObject(response);
                        boolean success = jsonResponse.getBoolean("success");
                        if (success) {
                            Toast.makeText(CafedreamMainActivity.this, "쿠폰을 사용했습니다!", Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getApplicationContext(), CafedreamMainActivity.class);
                            intent.putExtra("phonenumber", phonenumber2);
                            intent.putExtra("stamps", stamps2);

                        } else {
                            Toast.makeText(CafedreamMainActivity.this, "쿠폰 사용에 실패했습니다 ㅠㅠ ", Toast.LENGTH_SHORT).show();

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            };
            CafedreamStampUseRequest cafedreamStampUseRequest = new CafedreamStampUseRequest(phonenumber2, stamps2 + "", responseListener);
            RequestQueue queue = Volley.newRequestQueue(CafedreamMainActivity.this);
            queue.add(cafedreamStampUseRequest);
        }
    }


    // 버퍼 데이터를 디코딩해서 String 으로 변환
    public String byteDecoding(byte[] buf) {
        String strText="";
        String textEncoding = ((buf[0] & 0200) == 0) ? "UTF-8" : "UTF-16";
        int langCodeLen = buf[0] & 0077;

        try {
            strText = new String(buf, langCodeLen + 1,
                    buf.length - langCodeLen - 1, textEncoding);
        } catch(Exception e) {
            Log.d("tag1", e.toString());
        }
        return strText;
    }
}

