package com.example.filemanagersimple.utils;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

import com.example.filemanagersimple.R;

public class CustomDialog extends Dialog {
    private CustomDialogCallBack callBack;
    public CustomDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.custom_dialog_layout);

        findViewById(R.id.btn_internal).setOnClickListener(v -> {
            callBack.moveToInternalStorage();
            dismiss();
        });

        findViewById(R.id.btn_external).setOnClickListener(v -> {
            callBack.moveToExternalStorage();
            dismiss();
        });
    }

    public interface CustomDialogCallBack {
        void moveToInternalStorage();
        void moveToExternalStorage();
    }

    public void setCallBack(CustomDialogCallBack callBack) {
        this.callBack = callBack;
    }
}
