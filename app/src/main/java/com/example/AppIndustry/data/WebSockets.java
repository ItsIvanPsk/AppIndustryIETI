package com.example.AppIndustry.data;



import com.example.AppIndustry.presentation.MainActivity;
import com.example.AppIndustry.utils.ServerProperties;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class WebSockets implements ServerProperties {

    WebSocketClient client;

    public void envia(String user) {
        try {
            client.send(user);
        } catch (WebsocketNotConnectedException e) {
            System.out.println("Connexió perduda ...");
            connecta();
        }

    }

    public void connecta() {
        try {
            client = new WebSocketClient(new URI(SERVER_URIs), (Draft) new Draft_6455()) {
                @Override
                public void onMessage(String message) {
                    String token = message.substring(0,2);
                    System.out.println(token);
                    if (token.equals("UV")){
                        userValidation(message);
                    }
                }

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
            System.out.println("Error: " + SERVER_URIs + " no és una direcció URI de WebSocket vàlida");
        }
    }


    public void userValidation (String message) {
        String[] user = message.split("#");
        if(user[3].equals("true")){ MainActivity.setValidated(true); }
        else { MainActivity.setValidated(false); }
    }

}
