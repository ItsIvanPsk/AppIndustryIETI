package com.example.AppIndustry.presentation.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.util.Log;

import androidx.fragment.app.DialogFragment;

public class UserNotFoundDialog extends DialogFragment {

    public static Dialog userNotFound(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("App Industry");
        builder.setCancelable(false);
        builder.setMessage("The introduced username or passoword does not exist. Please type a correct username")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("SERVER_REPONSE","Incorrect username/password");
                    }
                });
        return builder.create();
    }

}
