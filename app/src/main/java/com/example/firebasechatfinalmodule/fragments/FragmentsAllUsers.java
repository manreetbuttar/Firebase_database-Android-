package com.example.firebasechatfinalmodule.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.firebasechatfinalmodule.FirebaseDatabaseManager;
import com.example.firebasechatfinalmodule.R;
import com.example.firebasechatfinalmodule.activity.ChatActivity;
import com.example.firebasechatfinalmodule.adapter.UserListAdapter;
import com.example.firebasechatfinalmodule.utils.MySharedPreference;
import com.example.firebasechatfinalmodule.utils.clickListeners.RecyclerViewClickListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;

import static com.example.firebasechatfinalmodule.utils.MySharedPreference.USER_ID;


public class FragmentsAllUsers extends Fragment implements RecyclerViewClickListener {

    View view;
    MySharedPreference mySharedPreference;
    RecyclerView recyclerview;
    FirebaseDatabaseManager firebaseDatabase;
    HashMap<String, HashMap<String,String>> mMapUserInfo=new HashMap<>();

    ArrayList<String> mUsername=new ArrayList<>();
    ArrayList<String> mUserID=new ArrayList<>();
    ArrayList<String> mUserImage=new ArrayList<>();
    UserListAdapter chatAdapter;
    String userID="";



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(HashMap<String, HashMap<String,String>>  userList) {
        if(userList!=null){
            mUsername.clear();
            mUserID.clear();
            if(userList.size()>0&&userList!=null){
                for (String key : userList.keySet()) {
                    if(!mySharedPreference.getString(USER_ID).equals(key)){
                        mUsername.add(userList.get(key).get("username"));
                        mUserImage.add(userList.get(key).get("profile"));
                    }

                }
                mUserID.addAll(userList.keySet());
                mUserID.remove(mySharedPreference.getString(USER_ID));
                chatAdapter.notifyDataSetChanged();
            }

        }

    }

    private void setRecyclerview(){
        chatAdapter=new UserListAdapter(getActivity(),mUsername,mUserImage,this);
        recyclerview.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.addItemDecoration(new DividerItemDecoration(getActivity(), LinearLayoutManager.VERTICAL));
        recyclerview.setItemAnimator(new DefaultItemAnimator());
        recyclerview.setAdapter(chatAdapter);

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_chat, container, false);
        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUi();
        setRecyclerview();
    }

    private void initUi() {
        mySharedPreference=new MySharedPreference(getActivity());
        recyclerview=view.findViewById(R.id.recyclerview);
        firebaseDatabase=new FirebaseDatabaseManager(getActivity());
        firebaseDatabase.getAllUsers("Registered User");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);

    }

    @Override
    public void onClick(@NotNull View view, int position) {
        Intent intent=new Intent(getActivity(), ChatActivity.class);
        intent.putExtra("UserID",mUserID.get(position));
        intent.putExtra("UserImage",mUserImage.get(position));
        intent.putExtra("Username",mUsername.get(position));
        startActivity(intent);
    }
}
