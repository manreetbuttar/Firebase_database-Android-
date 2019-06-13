package com.example.firebasechatfinalmodule.adapter;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.example.firebasechatfinalmodule.R;
import com.example.firebasechatfinalmodule.utils.clickListeners.RecyclerViewClickListener;


import java.util.ArrayList;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.ChatViewHolder> {

    private Activity activity;
    private ArrayList<String> mUsername=new ArrayList<>();
    private ArrayList<String> mUserImage=new ArrayList<>();
    RecyclerViewClickListener recyclerViewClickListener;

    public UserListAdapter(Activity activity, ArrayList<String> mUsername, ArrayList<String> userimage, RecyclerViewClickListener recyclerViewClickListener) {
        this.activity = activity;
        this.mUsername = mUsername;
        this.mUserImage = userimage;
        this.recyclerViewClickListener=recyclerViewClickListener;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_chat, viewGroup, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ChatViewHolder chatViewHolder, final int i) {
        chatViewHolder.tv_username.setText(mUsername.get(i));
        Glide.with(activity)
                .load(mUserImage.get(i))
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(chatViewHolder.circleImageView);


        chatViewHolder.cont_goforchat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerViewClickListener.onClick(chatViewHolder.cont_goforchat,i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mUsername.size();
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {

         TextView tv_username,tv_lastmessage,tv_activetime;
         ConstraintLayout cont_goforchat;
         ImageView circleImageView;


        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_username=itemView.findViewById(R.id.tv_username);
            tv_lastmessage=itemView.findViewById(R.id.tv_lastmessage);
            tv_activetime=itemView.findViewById(R.id.tv_activetime);
            cont_goforchat=itemView.findViewById(R.id.cont_goforchat);
            circleImageView=itemView.findViewById(R.id.usersprofileimages);
        }

    }



}
