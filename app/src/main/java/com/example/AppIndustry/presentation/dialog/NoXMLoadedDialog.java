package com.example.AppIndustry.presentation.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;

import androidx.fragment.app.DialogFragment;

import com.example.AppIndustry.presentation.MainActivity;

public class NoXMLoadedDialog extends DialogFragment {

    public static Dialog noXMLoaded(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setTitle("App Industry");
        builder.setMessage("The server has not a config loaded.")
                .setPositiveButton("Ok", (dialog, id) -> {
                    Intent intent = new Intent(activity, MainActivity.class);
                    activity.startActivity(intent);
                });
        return builder.create();
    }

}
