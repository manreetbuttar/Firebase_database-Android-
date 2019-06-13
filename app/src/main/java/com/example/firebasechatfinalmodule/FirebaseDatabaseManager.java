package com.example.firebasechatfinalmodule;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.example.firebasechatfinalmodule.model.ActiveUser;
import com.example.firebasechatfinalmodule.model.LoginModel;
import com.example.firebasechatfinalmodule.model.MessageBody;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;


import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.HashMap;
import java.util.UUID;

import static com.example.firebasechatfinalmodule.Utils.getCurrentTimeStamp;


public class FirebaseDatabaseManager {

    Context mContext;

    //firebase storage
    //upload media files
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;

    //child event listeners
    private ChildEventListener mListener;


    // database ederence
    DatabaseReference chattingDatabase;

    HashMap<String, HashMap<String,String>> mMapUserInfo=new HashMap<>();


    public FirebaseDatabaseManager(Context mContext) {
        this.mContext = mContext;
        // Storage to upload media
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        chattingDatabase= FirebaseDatabase.getInstance().getReference();
    }


    public void register(String tablename, final String unique_id, final Object tableFields) {
        DatabaseReference databasename = FirebaseDatabase.getInstance().getReference();
        databasename.child(tablename).child(unique_id).setValue(tableFields);
    }


    public void register(String tablename, final String unique_id, final HashMap tableFields) {
        DatabaseReference databasename = FirebaseDatabase.getInstance().getReference();
        databasename.child(tablename).child(tablename).child(unique_id).setValue(tableFields);
        Intent intent = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(intent);
        Toast.makeText(mContext, "Registered SucessFullly", Toast.LENGTH_SHORT).show();

    }

    public void login(String tablename, String child, final LoginModel mAuthMap) {
        DatabaseReference databasename = FirebaseDatabase.getInstance().getReference().child(tablename).child(child);
        databasename.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String email = (String) dataSnapshot.child("email").getValue();
                String newemail = mAuthMap.getEmail();
                if (email.equals(newemail)) {
                    Toast.makeText(mContext, "UserExist", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, "User Not Exist", Toast.LENGTH_SHORT).show();
                    return;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void login(String tablename, String child, final HashMap mAuthMap) {
        DatabaseReference databasename = FirebaseDatabase.getInstance().getReference().child(tablename).child(child);
        databasename.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String email = (String) dataSnapshot.child("email").getValue();
                String newemail = (String) mAuthMap.get("email");
                assert email != null;
                if (email.equals(newemail)) {
                    Toast.makeText(mContext, "UserExist", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, HomeActivity.class);
                    mContext.startActivity(intent);
                } else {
                    Toast.makeText(mContext, "User Not Exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void isUserActive(String tabelname, String UserId, Object status) {
        DatabaseReference databasename = FirebaseDatabase.getInstance().getReference();
        databasename.child(tabelname).
                child(UserId).
                setValue(status);
    }


    public  void getAllUsers(String tablename) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(tablename);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                mMapUserInfo = (HashMap<String, HashMap<String, String>>)dataSnapshot.getValue();
                EventBus.getDefault().post(mMapUserInfo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    public void getAllMessagesToUser(String tablename, String your_Id,String user_id){

        chattingDatabase.child(tablename)
                .child(your_Id)
                .child(user_id)
                .orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                EventBus.getDefault().post(dataSnapshot);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        mListener = chattingDatabase.child(tablename)
                    .child(your_Id)
                    .child(user_id)
                    .orderByKey().addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                            MessageBody message = dataSnapshot.getValue(MessageBody.class);
                            EventBus.getDefault().post(message);
                        }

                        @Override
                        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                        }

                        @Override
                        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

    }


    public void sendTextMessages(String tablename,String your_id,String otheruser_id,String unique_messageID,Object mMesaageBody) {

        chattingDatabase.child(tablename).
                child(your_id).
                child(otheruser_id).
                child(unique_messageID).
                setValue(mMesaageBody);
        chattingDatabase.child(tablename).
                child(otheruser_id).
                child(your_id).
                child(unique_messageID).
                setValue(mMesaageBody);
    }


    public void changeUseronlineOfflineStatus(String tablename,String yourID, Object statusDeatils){
        DatabaseReference databaseReference =FirebaseDatabase.getInstance().getReference();
        databaseReference.child(tablename).child(yourID).setValue(statusDeatils);
    }

    public void sendMediaMessage(Uri filePath,Context context,String tablename,String your_ID,String otheruserID) {

        if(filePath != null)
        {
            File parent_file=new File(filePath.toString());
            final ProgressDialog progressDialog = new ProgressDialog(context);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();
            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.e("Tuts+", "uri: " + uri.toString());
                                    if(parent_file.toString().contains("video")||uri.toString().contains("mp4")||uri.toString().contains("3gp")){
                                        sendDataWithVideo(uri,tablename,your_ID,otheruserID);
                                    }else {
                                        sendDataWithImage(uri,tablename,your_ID,otheruserID);
                                    }


                                }
                            });
                            Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(context, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }


    public void sendDataWithVideo(Uri uri,String tablename,String yourID,String otheruser_ID){
        DatabaseReference databaseReference =FirebaseDatabase.getInstance().getReference().child(tablename);
        MessageBody messageBody = new MessageBody();
        messageBody.setFrom(yourID);
        messageBody.setTime(getCurrentTimeStamp());
        messageBody.setSeen("false");
        messageBody.setType("video");
        messageBody.setMessage(uri.toString());
        databaseReference.
                child(yourID).
                child(otheruser_ID).
                push().
                setValue(messageBody);
        databaseReference.
                child(otheruser_ID).
                child(yourID).
                push().
                setValue(messageBody);
    }

    private void sendDataWithImage(Uri uri,String tablename,String yourID,String otheruser_ID){
        DatabaseReference databaseReference =FirebaseDatabase.getInstance().getReference().child(tablename);
        MessageBody messageBody = new MessageBody();
        messageBody.setFrom(yourID);
        messageBody.setTime(getCurrentTimeStamp());
        messageBody.setSeen("false");
        messageBody.setType("image");
        messageBody.setMessage(uri.toString());
        databaseReference.
                child(yourID).
                child(otheruser_ID).
                push().
                setValue(messageBody);
        databaseReference.
                child(yourID).
                child(otheruser_ID).
                push().
                setValue(messageBody);
    }



    public void getActiveUsers(String tablename,String otheruserID) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference().child(tablename);
        databaseReference.child(otheruserID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ActiveUser activeUser = dataSnapshot.getValue(ActiveUser.class);
                EventBus.getDefault().post(activeUser);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
