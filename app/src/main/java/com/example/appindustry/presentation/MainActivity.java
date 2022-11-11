package com.example.AppIndustry.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.example.AppIndustry.R;
import com.example.AppIndustry.data.*;


public class MainActivity extends AppCompatActivity {

    WebSockets client;
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

        client = new WebSockets();
        client.connecta();

        setButtonListeners();
    }

    public void setButtonListeners(){
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                client.envia(
                        usernameInput.getText().toString() + "#" + passwordInput.getText().toString()
                );
                if (validated){
                    String username = usernameInput.getText().toString();
                    Intent intent = new Intent(MainActivity.this,MainDashboard.class);
                    intent.putExtra(username, username);
                    startActivity(intent);
                }
            }
        });
    }

    public static void setValidated(boolean val){
        validated = val;
    }



}