package com.example.firebasechatfinalmodule.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.firebasechatfinalmodule.fragments.FragmentChat;
import com.example.firebasechatfinalmodule.fragments.FragmentDemo;
import com.example.firebasechatfinalmodule.fragments.FragmentsAllUsers;

public class MyFragmentPagerAdapter extends FragmentPagerAdapter {

    public MyFragmentPagerAdapter(FragmentManager fragmentManager){
        super(fragmentManager);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                FragmentChat chat=new FragmentChat();
                return chat;
            case 1:
                FragmentsAllUsers allUsers=new FragmentsAllUsers();
                return allUsers;
            case 2:
                FragmentDemo fragmentDemo=new FragmentDemo();
                return fragmentDemo;
        }
        return null;
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position){
            case 0:
                return "Chats";
            case 1:
                return "Users";
            case 2:
                return "Demo";
            default:
                return null;
        }
    }

}