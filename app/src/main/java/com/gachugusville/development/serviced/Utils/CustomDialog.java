package com.gachugusville.development.serviced.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;

import androidx.appcompat.app.AlertDialog;

import com.gachugusville.development.serviced.R;

public class CustomDialog {
    Activity activity;
    AlertDialog dialog;

    public CustomDialog(Activity activity) {
        this.activity = activity;
    }

    @SuppressLint("InflateParams")
    public void startDialog() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(activity);
        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_custom, null));
        builder.setCancelable(false);
        dialog = builder.create();
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }
}
