package com.example.AppIndustry.data;



import com.example.AppIndustry.presentation.MainActivity;
import com.example.AppIndustry.utils.components.CustomSensor;
import com.example.AppIndustry.utils.components.CustomSlider;
import com.example.AppIndustry.utils.components.CustomSwitch;
import com.example.AppIndustry.utils.ServerProperties;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.exceptions.WebsocketNotConnectedException;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

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

                    /*
                        UV -> User validation
                        CF -> DashboardConfig
                            DD -> Dropdown
                            SW -> Switch
                            SS -> Sensor
                            SL -> Slider
                     */
                    switch (token) {
                        case "UV":
                            userValidation(message);
                            break;
                        case "CF":
                            configParser(message);
                            break;
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

    private void configParser(String message) {
        ArrayList<CustomSwitch> switches = new ArrayList<CustomSwitch>();
        ArrayList<CustomSensor> sensors = new ArrayList<CustomSensor>();
        ArrayList<CustomSlider> sliders = new ArrayList<CustomSlider>();
        // message = "CF%%SW#0#LabelDefault#Label1%%SW#1#LabelDefault#Label2%%SS#2#ºC#5#10#Label";
        System.out.println(message);
        message = message.substring(4, message.length());
        String[] cfg = message.split("%%");
        for (int i = 1; i < cfg.length; i++){
            String[] sw = message.split("#");
            if (sw[0].equals("SW")){
                switches.add(
                        new CustomSwitch(
                                Integer.parseInt(sw[1]),
                                sw[2].toString(),
                                sw[3].substring(0, sw[3].length() - 4).toString()
                        )
                );
            } else if (sw[0].equals("SS")){
                sensors.add(
                        new CustomSensor(
                                Integer.parseInt(sw[1]),
                                sw[2].toString(),
                                sw[3],
                                sw[4],
                                sw[5]
                        )
                );
            } else if (sw[0].equals("SL")){
                sliders.add(
                        new CustomSlider(
                                Integer.parseInt(sw[1]),
                                Float.parseFloat(sw[2]),
                                Integer.parseInt(sw[3]),
                                Integer.parseInt(sw[4]),
                                Float.parseFloat(sw[5]),
                                sw[6]
                        )
                );
            }
        }
        for (int i = 0; i < sensors.size(); i++){
            System.out.println(sensors.get(i).toString());
        }
        for (int i = 0; i < sliders.size(); i++){
            System.out.println(sliders.get(i).toString());
        }
        for (int i = 0; i < switches.size(); i++){
            System.out.println(switches.get(i).toString());
        }
    }


    public void userValidation (String message) {
        System.out.println(message);
        String[] user = message.split("#");
        for (int i = 0; i < user.length; i++){
            System.out.println("USER - " + user[i]);
        }
        if(user[3].equals("true")){ MainActivity.setValidated(true); }
        else { MainActivity.setValidated(false); }
    }
}
