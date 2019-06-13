package com.example.firebasechatfinalmodule.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.firebasechatfinalmodule.HomeActivity;
import com.example.firebasechatfinalmodule.R;
import com.example.firebasechatfinalmodule.RegisterActivity;
import com.example.firebasechatfinalmodule.utils.MySharedPreference;

import static com.example.firebasechatfinalmodule.utils.MySharedPreference.USER_ID;


public class SplashScreen extends AppCompatActivity {

    MySharedPreference mySharedPreference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mySharedPreference=new MySharedPreference(this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mySharedPreference.getString(USER_ID).equals("")){
                    startActivity(new Intent(SplashScreen.this, RegisterActivity.class));
                    finish();
                }
                else {

                    startActivity(new Intent(SplashScreen.this, HomeActivity.class));
                    finish();
                }
            }
        },800);
    }
}
