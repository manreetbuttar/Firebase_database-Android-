package com.example.firebasechatfinalmodule;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;

import java.util.Objects;

public class CustomViews {

    Activity context;
    private final int PICK_IMAGE_REQUEST = 71;
    private final int REQUEST_CODE_OPEN_DOCUMENT=23;

    public CustomViews(Activity context) {
        this.context=context;
    }

    public void  openMediaDialog() {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.alert_openmedia);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
        Window window = dialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        wlp.gravity = Gravity.BOTTOM;
        window.setAttributes(wlp);
        LinearLayout ll_gallery=dialog.findViewById(R.id.ll_gallery);
        LinearLayout ll_document=dialog.findViewById(R.id.ll_document);
        ll_gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
                dialog.dismiss();

            }
        });
        ll_document.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseDocuments();
            }
        });
        dialog.show();

    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("*/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        context.startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    private void chooseDocuments(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        context.startActivityForResult(intent, REQUEST_CODE_OPEN_DOCUMENT);
    }

}
