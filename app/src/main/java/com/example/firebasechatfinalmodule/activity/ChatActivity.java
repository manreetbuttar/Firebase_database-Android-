package com.example.firebasechatfinalmodule.activity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.firebasechatfinalmodule.CustomViews;
import com.example.firebasechatfinalmodule.FirebaseDatabaseManager;
import com.example.firebasechatfinalmodule.R;
import com.example.firebasechatfinalmodule.adapter.ChatHistoryAdapter;
import com.example.firebasechatfinalmodule.model.ActiveUser;
import com.example.firebasechatfinalmodule.model.MessageBody;
import com.example.firebasechatfinalmodule.utils.MySharedPreference;
import com.example.firebasechatfinalmodule.utils.clickListeners.RecyclerviewLongPressListeners;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.MessagingAnalytics;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.example.firebasechatfinalmodule.Utils.getCurrentTimeStamp;
import static com.example.firebasechatfinalmodule.utils.MySharedPreference.USER_ID;

public class ChatActivity extends AppCompatActivity implements View.OnClickListener, RecyclerviewLongPressListeners {

    // other user details
    String userID = "";
    String user_image = "";
    String username = "";

    //User header
    ImageView iv_userprofile;
    TextView tv_username;
    FirebaseDatabaseManager firebaseDatabaseManager;

    MySharedPreference mySharedPreference;


    HashMap<String, MessageBody> mMapMesagesIDs = new HashMap<>();
    ArrayList<String> mMessagesIDList = new ArrayList<>();
    //CHAT LIST
    private List<Object> mChatList;


    //adapter
    ChatHistoryAdapter chatHistoryAdapter;
    RecyclerView recyclerview;

    //child event listeners
    private ChildEventListener mListener;


    //send message to other user variables
    ImageView back, send;
    EditText ed_message;

    // For Media Sending Icon
    ImageView iv_media;

    CustomViews customViews;

    private final int PICK_IMAGE_REQUEST = 71;
    private final int REQUEST_CODE_OPEN_DOCUMENT = 23;

    private Uri filePath;

    // online offline status
    TextView tv_userstatus;
    ImageView iv_userstatus;


    @Override
    protected void onStart() {
        super.onStart();
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(DataSnapshot messageList) {
        mMapMesagesIDs = (HashMap<String, MessageBody>) messageList.getValue();
        if (mMapMesagesIDs != null) {
            mMessagesIDList.addAll(mMapMesagesIDs.keySet());
        }
        for (DataSnapshot postSnapshot : messageList.getChildren()) {
            MessageBody messageRef = postSnapshot.getValue(MessageBody.class);
            mChatList.add(messageRef);
            chatHistoryAdapter.notifyDataSetChanged();
        }
        //setChildListenerNew();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(ActiveUser activeUser) {
        if (activeUser != null) {
            tv_userstatus.setText(activeUser.getStatus());
            if (activeUser.getStatus().equals("online")) {
                iv_userstatus.setImageResource(R.drawable.active_users);
                iv_userstatus.setVisibility(View.VISIBLE);
            } else if (activeUser.getStatus().equals("offline")) {
                iv_userstatus.setImageResource(R.drawable.offline_users);
                iv_userstatus.setVisibility(View.VISIBLE);
            } else {
                iv_userstatus.setVisibility(View.GONE);

            }
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageBody child_added_message) {
        mChatList.add(child_added_message);
        final int pos = mChatList.size() - 1;
        chatHistoryAdapter.notifyItemInserted(pos);
        recyclerview.postDelayed(() -> recyclerview.smoothScrollToPosition(pos), 10);
    }

    private void setRecyclerview() {
        chatHistoryAdapter = new ChatHistoryAdapter(mChatList, this, this);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setStackFromEnd(true);
        recyclerview.setLayoutManager(mLayoutManager);
        recyclerview.setAdapter(chatHistoryAdapter);
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatdemo);
        getIntentUser();
        initUI();
        listeners();
        setUserDeatils();
        setRecyclerview();
        getMessages();
        getActiveUser();

    }

    private void getActiveUser() {
        firebaseDatabaseManager.getActiveUsers("ActiveUser", userID);
    }

    private void listeners() {
        back.setOnClickListener(this);
        send.setOnClickListener(this);
        iv_media.setOnClickListener(this);
    }


    private void getIntentUser() {
        // intent to get all the deatils of other user
        userID = getIntent().getStringExtra("UserID");
        if (getIntent().getStringExtra("UserImage") != null) {
            user_image = getIntent().getStringExtra("UserImage");
        }
        if (getIntent().getStringExtra("Username") != null) {
            username = getIntent().getStringExtra("Username");
        }
    }

    private void initUI() {
        //initialize chat list
        mChatList = new ArrayList<>();

        //user profile header
        iv_userprofile = findViewById(R.id.iv_userprofile);
        tv_username = findViewById(R.id.tv_username);

        mySharedPreference = new MySharedPreference(this);

        // initialize the FirebaseManager  from library
        firebaseDatabaseManager = new FirebaseDatabaseManager(this);

        recyclerview = findViewById(R.id.recyclerview_chathistory);

        //send text messages  variables
        send = findViewById(R.id.iv_send);
        back = findViewById(R.id.iv_back);
        ed_message = findViewById(R.id.ed_message);

        //open media options
        iv_media = findViewById(R.id.iv_media);

        //pick media from customviews classes
        customViews = new CustomViews(this);

        // Offline online status
        tv_userstatus = findViewById(R.id.tv_userstatus);
        iv_userstatus = findViewById(R.id.iv_userstatus);


    }

    private void setUserDeatils() {
        //setup user header
        tv_username.setText(username);
        Glide.with(Objects.requireNonNull(this))
                .load(user_image)
                .placeholder(R.drawable.ic_launcher_background)
                .error(R.drawable.ic_launcher_background)
                .into(iv_userprofile);
    }


    private void getMessages() {
        firebaseDatabaseManager.getAllMessagesToUser("messages", mySharedPreference.getString(USER_ID), userID);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.iv_send:
                if (ed_message.getText().toString().equals("")) {
                    Toast.makeText(this, "Write something first", Toast.LENGTH_SHORT).show();
                    return;
                }
                // unique  id for message to perform operation at particular time
                String message_id = UUID.randomUUID().toString();
                // save the text message to the messages table
                firebaseDatabaseManager.sendTextMessages("messages", mySharedPreference.getString(USER_ID), userID, message_id, setTextMessage());
                ed_message.setText("");
                firebaseDatabaseManager.changeUseronlineOfflineStatus("ActiveUser", mySharedPreference.getString(USER_ID), updateOnlineStatus());
                //sendNotificationToUser();
                break;

            case R.id.iv_media:
                customViews.openMediaDialog();
                break;
        }
    }

    @Override
    public void onLongPress(@NotNull View view, int position) {

    }


    private MessageBody setTextMessage() {
        // Message body to set  all the properties of the messages according to the user requirment
        MessageBody messageBody = new MessageBody();
        messageBody.setFrom(mySharedPreference.getString(USER_ID));
        messageBody.setTime(getCurrentTimeStamp());
        messageBody.setSeen("false");
        messageBody.setType("text");
        messageBody.setMessage(ed_message.getText().toString());
        return messageBody;
    }


    private ActiveUser updateOnlineStatus() {
        ActiveUser activeUser = new ActiveUser();
        activeUser.setStatus("online");
        activeUser.setTimestamp(getCurrentTimeStamp());
        return activeUser;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                firebaseDatabaseManager.sendMediaMessage(filePath, this, "messages", mySharedPreference.getString(USER_ID), userID);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (requestCode == REQUEST_CODE_OPEN_DOCUMENT && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            firebaseDatabaseManager.sendMediaMessage(filePath, this, "messages", mySharedPreference.getString(USER_ID), userID);

        }

    }
}
