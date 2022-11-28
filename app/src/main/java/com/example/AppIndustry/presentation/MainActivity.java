package com.example.AppIndustry.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import com.example.AppIndustry.R;
import com.example.AppIndustry.data.*;


public class MainActivity extends AppCompatActivity {

    EditText usernameInput, passwordInput, serverInput;
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
        serverInput = findViewById(R.id.main_input_server);
        usernameInput = findViewById(R.id.main_input_username);
        passwordInput = findViewById(R.id.main_input_password);

        WebSockets.updateMainActivity(this);

        setButtonListeners();
    }

    public void setButtonListeners(){
        button.setOnClickListener(view -> {
            if( !usernameInput.getText().toString().equals("")
                && !passwordInput.getText().toString().equals("")
                    && !serverInput.getText().toString().equals("")
            ) {
                try{
                    System.out.println(serverInput.getText().toString());
                    ConnectionUseCase.ws.connecta();
                    System.out.println("Connected");
                    Thread.sleep(1000);
                    ConnectionUseCase.ws.envia(
                            "UV#" + usernameInput.getText().toString() + "#" + passwordInput.getText().toString()
                    );
                }catch (Exception e){
                    System.out.println("Exception");
                }
            }
        });
    }

    public void navigate(){
        if (validated){
            String username = usernameInput.getText().toString();
            Intent intent = new Intent(MainActivity.this,MainDashboard.class);
            MainDashboard.clearArrays();
            intent.putExtra(username, username);
            startActivity(intent);
        } else {
            Log.i("SERVER_RESPONSE", "Incorrect Username or Username don't found");
        }
    }

    public void setValidated(boolean val){
        validated = val;
    }


}