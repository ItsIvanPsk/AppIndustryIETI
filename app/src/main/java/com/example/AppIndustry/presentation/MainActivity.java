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
    boolean running = false;

    //WebSockets ws;

    public void setRunning(boolean _running){
        this.running = _running;
    }

    public boolean getRunning(){
        return this.running;
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        WebSockets.updateMainActivity(this);

        button = findViewById(R.id.main_btn_login);
        serverInput = findViewById(R.id.main_input_server);
        usernameInput = findViewById(R.id.main_input_username);
        passwordInput = findViewById(R.id.main_input_password);

        setButtonListeners();
        running = true;
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
                        // ws = new WebSockets();
                        System.out.println(serverInput.getText().toString());
                        ConnectionUseCase.ws.connecta();
                        System.out.println("Conectado");
                        Thread.sleep(2000);
                        ConnectionUseCase.ws.envia(
                                "UV#" + usernameInput.getText().toString() + "#" + passwordInput.getText().toString()
                        );
                    }catch (Exception e){
                        System.out.println("Exception");
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
            //ConnectionUseCase.ws.wsDisconnect();
            startActivity(intent);
        } else {
            Log.i("SERVER_RESPONSE", "Incorrect Username or Username don't found");
        }
    }

    public void setValidated(boolean val){
        validated = val;
    }

    /*
        public WebSockets getWS() {
        return this.ws;
    }
     */


    @Override
    protected void onDestroy() {
        running = false;
        super.onDestroy();
    }

}