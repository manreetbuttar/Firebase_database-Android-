package com.example.firebase_database;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.widget.Toast;
import com.google.firebase.database.*;

import java.util.HashMap;

public class FirebaseDatabaseManager {

    public Context mContext;
    DatabaseReference databasename;


    public  FirebaseDatabaseManager(Context context){
        mContext = context;
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

    }

    public  void createTablewithChildAndFileds(String tablename, final String child, final HashMap tableFields){
        if(tableFields==null){
            Toast.makeText(mContext, "Something Went Wrong , please fill all the fields", Toast.LENGTH_SHORT).show();
            return;

        }
        databasename= FirebaseDatabase.getInstance().getReference().child(tablename);
         databasename.addListenerForSingleValueEvent(new ValueEventListener() {
             @Override
             public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                 String userID = databasename.push().getKey();
                 databasename.child(child).setValue(tableFields);
                 Toast.makeText(mContext, "SucessFully Register", Toast.LENGTH_SHORT).show();
                 //Intent intent=new Intent(mContext,LoginActivity.class);
                // mContext.startActivity(intent);
             }

             @Override
             public void onCancelled(@NonNull DatabaseError databaseError) {
             }
         });


    }



    public  void createTablewithMuttipleChildAndFileds(String tablename, final String child1,final String child2, final String child3, final HashMap tableFields){
        if(tableFields==null){
            Toast.makeText(mContext, "Something Went Wrong , please fill all the fields", Toast.LENGTH_SHORT).show();
            return;

        }
        databasename= FirebaseDatabase.getInstance().getReference().child(tablename);
        databasename.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                databasename.child(child1).child(child2).child(child3).setValue(tableFields);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });


    }



    public  void  authenticateUserWIthSignUp(String tablename, String child, final HashMap mAuthMap){
        databasename= FirebaseDatabase.getInstance().getReference().child(tablename).child(child);
        databasename.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
               String email=(String) dataSnapshot.child("email").getValue();
                String newemail = (String)mAuthMap.get("email");
                if(email.equals(newemail)){
                    Toast.makeText(mContext, "UserExist", Toast.LENGTH_SHORT).show();
                  //  Intent intent=new Intent(mContext,HomeActivity.class);
                   // mContext.startActivity(intent);
                }else {

                    Toast.makeText(mContext, "User Not Exist", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }



}
