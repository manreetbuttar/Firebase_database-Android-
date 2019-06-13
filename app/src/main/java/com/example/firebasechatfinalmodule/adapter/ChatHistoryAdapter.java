package com.example.firebasechatfinalmodule.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.firebasechatfinalmodule.R;
import com.example.firebasechatfinalmodule.model.MessageBody;
import com.example.firebasechatfinalmodule.utils.MySharedPreference;
import com.example.firebasechatfinalmodule.utils.clickListeners.RecyclerviewLongPressListeners;


import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.firebasechatfinalmodule.Utils.getDateFromUTCTimestamp;
import static com.example.firebasechatfinalmodule.utils.MySharedPreference.USER_ID;

public class ChatHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    public static final int RIGHT_MSG = 0;
    public static final int LEFT_MSG = 1;
    public  static final int LEFT_IMAGE=2;
    public  static final int RIGHT_IMAGE=3;
    public  static final int LEFT_VIDEO=4;
    public  static final int RIGHT_VIDEO=5;
    MySharedPreference mySharedPreference;

    private List<Object> mChatList=new ArrayList<>();

    private Activity activity;
    private RecyclerviewLongPressListeners recyclerviewLongPressListeners;

    public ChatHistoryAdapter(List<Object> mChatList, Activity activity, RecyclerviewLongPressListeners recyclerviewLongPressListeners) {
        this.mChatList = mChatList;
        this.activity = activity;
        this.recyclerviewLongPressListeners=recyclerviewLongPressListeners;
        mySharedPreference=new MySharedPreference(activity);
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        switch (i) {

            case RIGHT_MSG:
                View v1 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sendertext, parent, false);
                return new MyViewHolderRightForText(v1);

            case LEFT_MSG:
                View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receivertext, parent, false);
                return new MyViewHolderLeftForText(v2);


            case LEFT_IMAGE:
                View v3 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receiverpicture, parent, false);
                return new MyViewHolderLeftForImage(v3);

            case RIGHT_IMAGE:
                View v4 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_senderpicture, parent, false);
                return new MyViewHolderRightForImage(v4);

            case LEFT_VIDEO:
                View v5 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_receivevideo, parent, false);
                return new MyViewHolderLeftForVideo(v5);

            case RIGHT_VIDEO:
                View v6 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sendvideo, parent, false);
                return new MyViewHolderRightForVideo(v6);

            default:
                View v7 = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sendertext, parent, false);
                return new MyViewHolderRightForText(v7);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case RIGHT_MSG:
                if(mChatList!=null&&mChatList.size()>0){
                    MessageBody senderModel = (MessageBody) mChatList.get(position);
                    MyViewHolderRightForText viewHolderRight = (MyViewHolderRightForText) holder;
                    viewHolderRight.tv_sendermessage.setText(senderModel.getMessage());
                    viewHolderRight.tv_mesagetime.setText(getDateFromUTCTimestamp(Long.parseLong(senderModel.getTime())).replace("AM", "am").replace("PM","pm"));
                    viewHolderRight.ll_senderparent.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                        recyclerviewLongPressListeners.onLongPress(viewHolderRight.ll_senderparent,position);
                        return true;
                        }
                    });
                }
                break;

            case LEFT_MSG:
                if(mChatList!=null&&mChatList.size()>0){
                    MessageBody receiverModel = (MessageBody) mChatList.get(position);
                    MyViewHolderLeftForText viewHolderLeft = (MyViewHolderLeftForText) holder;
                    viewHolderLeft.tv_receiver.setText(receiverModel.getMessage());
                    viewHolderLeft.tv_currentimeforreceiver.setText(getDateFromUTCTimestamp(Long.parseLong(receiverModel.getTime())).replace("AM", "am").replace("PM","pm"));
                    viewHolderLeft.rl_messageselectedforreceiver.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            recyclerviewLongPressListeners.onLongPress(viewHolderLeft.rl_messageselectedforreceiver,position);
                            return true;
                        }
                    });
                }

                break;



            case LEFT_IMAGE:
                if(mChatList!=null&&mChatList.size()>0){
                    MessageBody receiverModel = (MessageBody) mChatList.get(position);
                    MyViewHolderLeftForImage viewHolderLeftForImage=(MyViewHolderLeftForImage)holder;
                    Glide.with(activity)
                            .load(receiverModel.getMessage())
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_background)
                            .into(viewHolderLeftForImage.imageView);
                    viewHolderLeftForImage.ll_leftimageFullScreen.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                          showAlert(receiverModel.getMessage());
                    }
                    });
                    viewHolderLeftForImage. ll_leftimageFullScreen.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            recyclerviewLongPressListeners.onLongPress( viewHolderLeftForImage. ll_leftimageFullScreen,position);
                            return true;
                        }
                    });

                }
                break;




            case RIGHT_IMAGE:
                if(mChatList!=null&&mChatList.size()>0){
                    MessageBody receiverModel = (MessageBody) mChatList.get(position);
                    MyViewHolderRightForImage viewHolderLeftForImage=(MyViewHolderRightForImage)holder;
                    Glide.with(activity)
                            .load(receiverModel.getMessage())
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_background)
                            .into(viewHolderLeftForImage.imageViewleft);
                    viewHolderLeftForImage.ll_imageinviewascreen.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            showAlert(receiverModel.getMessage());
                        }
                    });
                    viewHolderLeftForImage.ll_imageinviewascreen.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            recyclerviewLongPressListeners.onLongPress(viewHolderLeftForImage.ll_imageinviewascreen,position);
                            return true;
                        }
                    });

                }
                break;


            case RIGHT_VIDEO:
                if(mChatList!=null&&mChatList.size()>0){
                    MessageBody receiverModel = (MessageBody) mChatList.get(position);
                    MyViewHolderRightForVideo viewHolderRightForVideo=(MyViewHolderRightForVideo)holder;
                    Glide.with(activity)
                            .load(receiverModel.getMessage())
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_background)
                            .into(viewHolderRightForVideo.imageViewforplaceright);
                     viewHolderRightForVideo.btn_playforsender.setOnClickListener(new View.OnClickListener() {
                         @Override
                         public void onClick(View v) {
                             Uri intentUri = Uri.parse(receiverModel.getMessage());
                             Intent intent = new Intent();
                             intent.setAction(Intent.ACTION_VIEW);
                             intent.setDataAndType(intentUri, "video/*");
                             activity.startActivity(intent);
                         }
                     });

                    viewHolderRightForVideo.rl_videoleft.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            recyclerviewLongPressListeners.onLongPress(viewHolderRightForVideo.rl_videoleft,position);
                            return true;
                        }
                    });

                }
                break;


            case LEFT_VIDEO:
                if(mChatList!=null&&mChatList.size()>0){
                    MessageBody receiverModel = (MessageBody) mChatList.get(position);
                    MyViewHolderLeftForVideo viewHolderLeftForVideo=(MyViewHolderLeftForVideo)holder;
                    Glide.with(activity)
                            .load(receiverModel.getMessage())
                            .placeholder(R.drawable.ic_launcher_background)
                            .error(R.drawable.ic_launcher_background)
                            .into(viewHolderLeftForVideo.imageViewforvideoleft);

                    viewHolderLeftForVideo.iv_buttonplayreceive.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Uri intentUri = Uri.parse(receiverModel.getMessage());
                            Intent intent = new Intent();
                            intent.setAction(Intent.ACTION_VIEW);
                            intent.setDataAndType(intentUri, "video/*");
                            activity.startActivity(intent);
                        }
                    });

                    viewHolderLeftForVideo.rl_selectedleftvideo.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View view) {
                            recyclerviewLongPressListeners.onLongPress(viewHolderLeftForVideo.rl_selectedleftvideo,position);
                            return true;
                        }
                    });


                }
                break;


        }
    }

    @Override
    public int getItemCount() {
        return mChatList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mChatList.get(position) instanceof MessageBody) {
            MessageBody chatMessage = (MessageBody) mChatList.get(position);
            if (chatMessage.getFrom().equals(mySharedPreference.getString(USER_ID))){
                if(chatMessage.getType().equals("image")){
                    return  RIGHT_IMAGE;
                }
                else  if(chatMessage.getType().equals("text")){
                    return RIGHT_MSG;
                }

                else if(chatMessage.getType().equals("video")){
                    return  RIGHT_VIDEO;
                }

            }

            else  {
                if(chatMessage.getType().equals("image")){
                    return  LEFT_IMAGE;
                }else if(chatMessage.getType().equals("text")){
                    return LEFT_MSG;
                }

                else if(chatMessage.getType().equals("video")){
                    return LEFT_VIDEO;
                }

            }
        }
             return RIGHT_MSG;
    }


    private void showAlert(String uri){
        final Dialog dialog = new Dialog(activity,android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        dialog.setContentView(R.layout.alert_fullscreenimageview);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);
        ImageView iv_fullimageView=dialog.findViewById(R.id.iv_fullimageView);
        ImageView iv_close=dialog.findViewById(R.id.iv_close);
        Glide.with(activity)
                .load(uri)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(iv_fullimageView);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }



    private class MyViewHolderLeftForText extends RecyclerView.ViewHolder {

        TextView tv_receiver,tv_currentimeforreceiver;
        RelativeLayout rl_messageselectedforreceiver;

        MyViewHolderLeftForText(View itemView) {
            super(itemView);
            tv_receiver = itemView.findViewById(R.id.tv_receiver);
            tv_currentimeforreceiver = itemView.findViewById(R.id.tv_currentimeforreceiver);
            rl_messageselectedforreceiver = itemView.findViewById(R.id.rl_messageselectedforreceiver);
        }

    }

    private class MyViewHolderLeftForImage extends RecyclerView.ViewHolder {

        ImageView imageView;
        LinearLayout ll_leftimageFullScreen;

        MyViewHolderLeftForImage(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.imageView);
            ll_leftimageFullScreen = itemView.findViewById(R.id.ll_leftimageFullScreen);
        }

    }

    private class MyViewHolderRightForText extends RecyclerView.ViewHolder {

        TextView tv_sendermessage,tv_mesagetime;
        RelativeLayout ll_senderparent;

        MyViewHolderRightForText(View itemView) {
            super(itemView);
            tv_sendermessage=itemView.findViewById(R.id.tv_sendermessage);
            tv_mesagetime=itemView.findViewById(R.id.tv_mesagetime);
            ll_senderparent=itemView.findViewById(R.id.ll_senderparent);

        }
    }



    private class MyViewHolderRightForImage extends RecyclerView.ViewHolder {
        ImageView imageViewleft;
        LinearLayout ll_imageinviewascreen;

        MyViewHolderRightForImage(View itemView) {
            super(itemView);
            imageViewleft=itemView.findViewById(R.id.imageViewright);
            ll_imageinviewascreen=itemView.findViewById(R.id.ll_imageinviewascreen);

        }
    }

    private class MyViewHolderRightForVideo extends RecyclerView.ViewHolder {

        ImageView btn_playforsender,imageViewforplaceright;
        RelativeLayout rl_videoleft;

        MyViewHolderRightForVideo(View itemView) {
            super(itemView);
            btn_playforsender=itemView.findViewById(R.id.btn_playforsender);
            imageViewforplaceright=itemView.findViewById(R.id.imageViewforplaceright);
            rl_videoleft=itemView.findViewById(R.id.rl_videoleft);
        }
    }


    private class MyViewHolderLeftForVideo extends RecyclerView.ViewHolder {
        ImageView iv_buttonplayreceive,imageViewforvideoleft;
        RelativeLayout rl_selectedleftvideo;


        MyViewHolderLeftForVideo(View itemView) {
            super(itemView);
            iv_buttonplayreceive=itemView.findViewById(R.id.iv_buttonplayreceive);
            imageViewforvideoleft=itemView.findViewById(R.id.imageViewforvideoleft);
            rl_selectedleftvideo=itemView.findViewById(R.id.rl_selectedleftvideo);


        }
    }


}