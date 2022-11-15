package com.example.AppIndustry.data;



import com.example.AppIndustry.presentation.MainActivity;
import com.example.AppIndustry.presentation.MainDashboard;
import com.example.AppIndustry.presentation.dialog.ServerDisconectedDialog;
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

public class WebSockets {

    WebSocketClient client;

    public void envia(String message) {
        try {
            client.send(message);
        } catch (WebsocketNotConnectedException e) {
            System.out.println("Connexió perduda ...");
        }

    }

    public void connecta(String server) {
        System.out.println(server);
        String uri = "ws://" + server + ":" + ServerProperties.SERVER_PORT;
        try {
            client = new WebSocketClient(new URI(uri), (Draft) new Draft_6455()) {
                @Override
                public void onMessage(String message) {
                    String token = message.substring(0,2);
                    System.out.println(token);

                    /*
                        UV# -> User validation
                        CF# -> DashboardConfig
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
                            System.out.println("HERE!");
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
                    MainDashboard.setStateConnected(false);
                }

                @Override
                public void onError(Exception ex) { ex.printStackTrace(); }
            };
            client.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.out.println("Error: " + ServerProperties.SERVER_URIs + " no és una direcció URI de WebSocket vàlida");
        }
    }

    private void configParser(String message) {
        ArrayList<CustomSwitch> switches = new ArrayList<CustomSwitch>();
        ArrayList<CustomSensor> sensors = new ArrayList<CustomSensor>();
        ArrayList<CustomSlider> sliders = new ArrayList<CustomSlider>();
        System.out.println(message);
        String[] cfg = message.split("%%");
        System.out.println(cfg[1].toString());
        for (int i = 1; i < cfg.length; i++){
            String[] sw = message.split("#");
            String type = cfg[i].substring(0,2);
            if(type.equals("SW")){
                System.out.println("SW!");
                switches.add(
                        new CustomSwitch(
                                Integer.parseInt(sw[1]),
                                sw[2].toString(),
                                sw[3].substring(0, sw[3].length() - 4).toString()
                        )
                );
                System.out.println(switches.toString());
            } else if (type.equals("SS")){
                System.out.println("SS!");
                sensors.add(
                        new CustomSensor(
                                Integer.parseInt(sw[1]),
                                sw[2].toString(),
                                sw[3],
                                sw[4],
                                sw[5]
                        )
                );
                System.out.println(switches.toString());
            }
        }
        /*
        for (int i = 1; i < cfg.length; i++){
            String[] sw = message.split("#");
            if (sw[i].equals("SW")){
                switches.add(
                        new CustomSwitch(
                                Integer.parseInt(sw[1]),
                                sw[2].toString(),
                                sw[3].substring(0, sw[3].length() - 4).toString()
                        )
                );
            } else if (sw[i].equals("SS")){
                sensors.add(
                        new CustomSensor(
                                Integer.parseInt(sw[1]),
                                sw[2].toString(),
                                sw[3],
                                sw[4],
                                sw[5]
                        )
                );
            } else if (sw[i].equals("SL")){
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
         */

        for (int i = 0; i < sensors.size(); i++){
            System.out.println(sensors.get(i).toString());
        }
        for (int i = 0; i < sliders.size(); i++){
            System.out.println(sliders.get(i).toString());
        }
        for (int i = 0; i < switches.size(); i++){
            System.out.println(switches.get(i).toString());
        }

        MainDashboard.setArrays(switches,sensors,sliders);
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
