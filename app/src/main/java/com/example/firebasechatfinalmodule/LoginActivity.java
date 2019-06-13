package com.example.firebasechatfinalmodule;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.firebasechatfinalmodule.model.LoginModel;
import com.example.firebasechatfinalmodule.utils.MySharedPreference;

import static com.example.firebasechatfinalmodule.utils.MySharedPreference.USER_ID;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {


    FirebaseDatabaseManager firebaseDatabaseManager;
    EditText username,password;
    Button signIn,register;
    MySharedPreference mySharedPreference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
        listeners();
    }

    private void listeners() {
        signIn.setOnClickListener(this);
    }

    private void initUI() {
        mySharedPreference=new MySharedPreference(this);
        firebaseDatabaseManager=new FirebaseDatabaseManager(this);
        register=findViewById(R.id.register);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);
        signIn=findViewById(R.id.signIn);

    }

    private LoginModel setData(){
        LoginModel loginModel=new LoginModel();
        loginModel.setEmail(username.getText().toString());
        loginModel.setPassword(password.getText().toString());
        return  loginModel;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.signIn:
                LoginModel loginModel=setData();
                if(mySharedPreference.getString(USER_ID).equals("")){

                }else {
                    // table name is Registered User is because the fileds like email and password is validated from Registered User Table which is filled in registered time....
                    firebaseDatabaseManager.login("Registered User",mySharedPreference.getString(USER_ID),loginModel);

                    //After sucessfull login
                    Intent intent = new Intent(this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                }
                break;

        }
    }
}
