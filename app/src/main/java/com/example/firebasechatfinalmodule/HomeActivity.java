package com.example.firebasechatfinalmodule;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.firebasechatfinalmodule.adapter.MyFragmentPagerAdapter;
import com.example.firebasechatfinalmodule.model.ActiveUser;
import com.example.firebasechatfinalmodule.utils.MySharedPreference;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import static com.example.firebasechatfinalmodule.Utils.getCurrentTimeStamp;
import static com.example.firebasechatfinalmodule.utils.MySharedPreference.USER_ID;

public class HomeActivity extends AppCompatActivity {

    ViewPager mviewPager;
    MyFragmentPagerAdapter mFragmentPagerAdapter;
    TabLayout mtabLayout;
    MySharedPreference mySharedPreference;
    FirebaseDatabaseManager firebaseDatabaseManager;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        isUserActive();
    }

    private void init() {
        mviewPager=findViewById(R.id.viewPager);
        firebaseDatabaseManager=new FirebaseDatabaseManager(this);
        mySharedPreference = new MySharedPreference(this);

        //---ADDING ADAPTER FOR FRAGMENTS IN VIEW PAGER----
        mFragmentPagerAdapter=new MyFragmentPagerAdapter(getSupportFragmentManager());
        mviewPager.setAdapter(mFragmentPagerAdapter);

        //---SETTING TAB LAYOUT WITH VIEW PAGER
        mtabLayout=findViewById(R.id.tabLayout);
        mtabLayout.setupWithViewPager(mviewPager);

    }



// If you want to update the user is active after login , this method is called
    public void isUserActive(){
        ActiveUser activeUser=new ActiveUser ();
        activeUser.setStatus ("online");
        activeUser.setTimestamp (getCurrentTimeStamp());
        firebaseDatabaseManager.isUserActive("ActiveUser",mySharedPreference.getString(USER_ID),activeUser);

    }


    // And If you want to update the user is offline after destroying or closing the App , this method is called
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActiveUser activeUser=new ActiveUser ();
        activeUser.setStatus ("offline");
        activeUser.setTimestamp (getCurrentTimeStamp());
        firebaseDatabaseManager.isUserActive("ActiveUser",mySharedPreference.getString(USER_ID),activeUser);

    }

}
