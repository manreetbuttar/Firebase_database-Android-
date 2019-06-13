package com.example.firebasechatfinalmodule;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.firebasechatfinalmodule.model.RegisterUser;
import com.example.firebasechatfinalmodule.utils.MySharedPreference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import static com.example.firebasechatfinalmodule.utils.MySharedPreference.USER_ID;
import static com.example.firebasechatfinalmodule.utils.MySharedPreference.USER_Name;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    FirebaseDatabaseManager firebaseDatabaseManager;
    HashMap<String, String> mMapRegister = new HashMap<String, String>();
    EditText username, email, password, conf_password, phone;
    Button register,signIn;
    MySharedPreference mySharedPreference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regisetr);
        initUI();
        listeners();
    }

    private void listeners() {
    register.setOnClickListener(this);
    signIn.setOnClickListener(this);
    }

    private void initUI() {
        firebaseDatabaseManager=new FirebaseDatabaseManager(this);
        mySharedPreference=new MySharedPreference(this);
        username = findViewById(R.id.username);
        email =findViewById(R.id.email);
        password = findViewById(R.id.password);
        conf_password = findViewById(R.id.conf_password);
        phone = findViewById(R.id.phone);
        register = findViewById(R.id.registerfornewuser);
        signIn = findViewById(R.id.signIn);

    }

    private RegisterUser setData(){
        RegisterUser registerActivity=new RegisterUser();
        registerActivity.setUsername(username.getText().toString());
        registerActivity.setEmail(email.getText().toString());
        registerActivity.setPassword(password.getText().toString());
        registerActivity.setConfirm_password(conf_password.getText().toString());
        registerActivity.setPhone(phone.getText().toString());
        registerActivity.setProfile("");
        return registerActivity;


    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.registerfornewuser:
                if(username.getText().toString().equals("")&&email.getText().toString().equals("")&&password.getText().toString().equals("")&&phone.getText().toString().equals("")){
                    Toast.makeText(RegisterActivity.this, "Please Fill all the Fileds", Toast.LENGTH_SHORT).show();
                    return;
                }
                RegisterUser registerUser=setData();
                int random_number=Utils.generateRandomNumber();

                // Here  you can use model(pojo) class or HashMap according to your requirments.
                firebaseDatabaseManager.register("Registered User", String.valueOf(random_number),registerUser);
                mySharedPreference.putString(USER_ID,String.valueOf(random_number));
                mySharedPreference.putString(USER_Name,username.getText().toString());

                // Intent or Login Activity
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
                Toast.makeText(this, "Registered SucessFullly", Toast.LENGTH_SHORT).show();
                break;


            case R.id.signIn:
                Intent intent2 = new Intent(RegisterActivity.this, LoginActivity.class);
               startActivity(intent2);

                break;
        }
    }
}
