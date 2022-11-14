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


public class MainActivity extends AppCompatActivity implements ServerProperties {

    EditText usernameInput, passwordInput;
    Button button;
    static boolean validated = false;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        button = findViewById(R.id.main_btn_login);
        usernameInput = findViewById(R.id.main_input_username);
        passwordInput = findViewById(R.id.main_input_password);

        ConnectionUseCase.client = new WebSockets();
        ConnectionUseCase.client.connecta();

        setButtonListeners();
    }

    public void setButtonListeners(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    view.setEnabled(false);
                    ConnectionUseCase.client.envia(
                            "UV#" + usernameInput.getText().toString() + "#" + passwordInput.getText().toString()
                    );
                    Thread.sleep(SERVER_QUERY_DELAY);
                    if (validated){
                        String username = usernameInput.getText().toString();
                        Intent intent = new Intent(MainActivity.this,MainDashboard.class);
                        intent.putExtra(username, username);
                        startActivity(intent);
                    } else {
                        Log.i("SERVER_RESPONSE", "Incorrect Username or Username don't found");
                        UserNotFoundDialog.userNotFound(MainActivity.this).show();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                view.setEnabled(true);
            }
        });
    }

    public static void setValidated(boolean val){
        validated = val;
    }

}