package com.example.appindustry.presentation;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.appindustry.R;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {


    int port = 8888;
    String location = "10.0.2.2";
    String uri = "ws://" + location + ":" + port;
    WebSocketClient client;
    TextView textView;
    EditText inputText;

    @Override
    protected void onStart() {
        super.onStart();
        connecta();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

    }

    protected void envia (boolean salt) {
        String missatge = String.valueOf(inputText.getText()).replaceAll("\n", "");
        if (missatge.compareTo("") != 0) {
            if (salt) {
                textView.append(missatge + "\n");
            } else {
                textView.append(missatge);
            }
            inputText.setText("");
            try {
                client.send(missatge);
            } catch (WebsocketNotConnectedException e) {
                System.out.println("Connexió perduda ...");
                connecta();
            }
        }
    }

    public void connecta () {
        try {
            client = new WebSocketClient(new URI(uri), (Draft) new Draft_6455()) {
                @Override
                public void onMessage(String message) { onMessageListener(message); }

                @Override
                public void onOpen(ServerHandshake handshake) {
                    System.out.println("Connected to: " + getURI());
                }

                @Override
                public void onClose(int code, String reason, boolean remote) {
                    System.out.println("Disconnected from: " + getURI());
                }

                @Override
                public void onError(Exception ex) { ex.printStackTrace(); }
            };
            client.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.out.println("Error: " + uri + " no és una direcció URI de WebSocket vàlida");
        }
    }

    protected void onMessageListener (String message) {
        textView.append(message + "\n");
    }

}