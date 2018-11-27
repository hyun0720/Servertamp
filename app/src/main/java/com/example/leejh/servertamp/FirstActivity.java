package com.example.leejh.servertamp;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.abs_layout);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(0xFFcd5c5c)); //액션바색깔

        Button G = (Button) findViewById(R.id.Gaeun);
        Button C = (Button) findViewById(R.id.CafeDream);
        Button E = (Button) findViewById(R.id.Etang);
        Button P = (Button) findViewById(R.id.Pandorothy);


        G.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                Intent intent_act = new Intent(getApplicationContext(), GaeunMainActivity.class);
                startActivity(intent_act);
            }

        });

        C.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                Intent intent_act = new Intent(getApplicationContext(), CafedreamMainActivity.class);
                startActivity(intent_act);
            }

        });

        E.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                Intent intent_act = new Intent(getApplicationContext(), EtangMainActivity.class);
                startActivity(intent_act);
            }

        });


        P.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = getIntent();
                Intent intent_act = new Intent(getApplicationContext(), PandorothyMainActivity.class);
                startActivity(intent_act);
            }

        });

    }

}