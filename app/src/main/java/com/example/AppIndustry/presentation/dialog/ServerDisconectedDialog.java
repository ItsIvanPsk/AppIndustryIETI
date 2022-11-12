package com.example.AppIndustry.presentation.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

import androidx.fragment.app.DialogFragment;

public class ServerDisconectedDialog extends DialogFragment {
    public static Dialog serverDisconected(Activity activity) {
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setCancelable(false);
        builder.setTitle("App Industry");
        builder.setMessage("You have been disconected from the server, you will return to the login menu.\nPress OK to go back.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Log.i("SERVER_DISCONECTED","You have been disconected from the server");
                    }
                });
        return builder.create();
    }
}
