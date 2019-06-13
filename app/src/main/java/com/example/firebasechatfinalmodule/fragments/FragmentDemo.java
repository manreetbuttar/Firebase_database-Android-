package com.example.firebasechatfinalmodule.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.firebasechatfinalmodule.R;
import com.example.firebasechatfinalmodule.utils.MySharedPreference;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.Objects;

import static android.app.Activity.RESULT_OK;
import static com.example.firebasechatfinalmodule.utils.MySharedPreference.USER_ID;
import static com.example.firebasechatfinalmodule.utils.MySharedPreference.USER_Name;
import static com.example.firebasechatfinalmodule.utils.MySharedPreference.USER_PROFILE_IMAGE;

public class FragmentDemo extends Fragment implements View.OnClickListener {

    View view;
    ImageView iv_uploadimage;
    private final int PICK_IMAGE_REQUEST = 72;
    private Uri filePath;


    //upload media files
    //Firebase
    FirebaseStorage storage;


    StorageReference storageReference;
    MySharedPreference mySharedPreference;


    //update profile pic
    DatabaseReference userInfo;

    //user profile view
    ImageView iv_userprofile;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view= inflater.inflate(R.layout.fragment_profile, container, false);
        return  view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initUIDes();
        listeners();
    }

    private void listeners() {
        iv_uploadimage.setOnClickListener(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void initUIDes() {
        iv_uploadimage=view.findViewById(R.id.iv_uploadimage);
        iv_userprofile=view.findViewById(R.id.iv_userprofile);

        // shared preference
        mySharedPreference=new MySharedPreference(getActivity());

        // storage references
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();


        //user profile
        userInfo = FirebaseDatabase.getInstance().getReference().child("Registered User");

        if(!mySharedPreference.getString(USER_PROFILE_IMAGE).equals("")){
            Glide.with(Objects.requireNonNull(getActivity()))
                    .load(mySharedPreference.getString(USER_PROFILE_IMAGE))
                    .placeholder(R.drawable.ic_launcher_background)
                    .error(R.drawable.ic_launcher_background)
                    .into(iv_userprofile);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case  R.id.iv_uploadimage:
                Intent intent = new Intent();
                intent.setType("*/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
                break;


        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null ){
            filePath = data.getData();
            uploadImage();

        }
    }

    private void uploadImage() {

        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(getActivity());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            final StorageReference ref = storageReference.child("profile_images/"+ mySharedPreference.getString(USER_Name)+"_"+mySharedPreference.getString(USER_ID));
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @RequiresApi(api = Build.VERSION_CODES.KITKAT)
                                @Override
                                public void onSuccess(Uri uri) {
                                    Log.e("Tuts+", "uri: " + uri.toString());
                                    userInfo.child(mySharedPreference.getString(USER_ID)).child("profile").setValue(uri.toString());
                                    Glide.with(Objects.requireNonNull(getActivity()))
                                            .load(uri)
                                            .placeholder(R.drawable.ic_launcher_background)
                                            .error(R.drawable.ic_launcher_background)
                                            .into(iv_userprofile);
                                    mySharedPreference.putString(USER_PROFILE_IMAGE,uri.toString());
                                }
                            });
                            Toast.makeText(getActivity(), "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getActivity(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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

}
