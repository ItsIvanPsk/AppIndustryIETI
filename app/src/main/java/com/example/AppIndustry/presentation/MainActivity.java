package com.example.AppIndustry.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.AppIndustry.R;
import com.example.AppIndustry.data.*;
import com.example.AppIndustry.presentation.dialog.UserNotFoundDialog;
import com.example.AppIndustry.utils.ServerProperties;

import org.java_websocket.client.WebSocketClient;

import java.lang.ref.WeakReference;


public class MainActivity extends AppCompatActivity {

    EditText usernameInput, passwordInput, serverInput;
    Button button;
    static boolean validated = false;
    static String uv = "";

    private static WeakReference<Activity> mActivityRef;
    public static void updateActivity(Activity activity) {
        mActivityRef = new WeakReference<Activity>(activity);
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ConnectionUseCase.client = null;
        button = findViewById(R.id.main_btn_login);
        serverInput = findViewById(R.id.main_input_server);
        usernameInput = findViewById(R.id.main_input_username);
        passwordInput = findViewById(R.id.main_input_password);

        setButtonListeners();
    }

    public void setButtonListeners(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if( !usernameInput.getText().equals("")
                    && !passwordInput.getText().equals("")
                        && !serverInput.getText().equals("")
                ) {
                    try{
                        ConnectionUseCase.client = new WebSockets();
                        System.out.println(serverInput.getText().toString());
                        ConnectionUseCase.client.connecta("10.0.2.2");
                        ConnectionUseCase.client.envia(
                                "UV#" + usernameInput.getText().toString() + "#" + passwordInput.getText().toString()
                        );
                    }catch (Exception e){
                        ConnectionUseCase.client = null;
                    }
                }
            }
        });
    }

    public void navigate(){
        if (validated){
            String username = usernameInput.getText().toString();
            Intent intent = new Intent(MainActivity.this,MainDashboard.class);
            MainDashboard.clearArrs();
            intent.putExtra(username, username);
            startActivity(intent);
        } else {
            Log.i("SERVER_RESPONSE", "Incorrect Username or Username don't found");
            UserNotFoundDialog.userNotFound(MainActivity.this).show();
        }
    }

    public static void setValidated(boolean val){
        validated = val;
    }

}