package com.example.AppIndustry.data;

import com.example.AppIndustry.presentation.MainActivity;
import com.example.AppIndustry.presentation.MainDashboard;
import com.example.AppIndustry.utils.components.CustomDropdown;
import com.example.AppIndustry.utils.components.CustomOption;
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

    public static WebSocketClient client;

    public void envia(String message) {
        try {
            client.send(message);
        } catch (WebsocketNotConnectedException e) {
            System.out.println("Connexió perduda ...");
        }

    }

    public void connecta(String server) {
        try {
            client = new WebSocketClient(new URI("ws://" + server + ":" + "8888"), (Draft) new Draft_6455()) {
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
                            System.out.println("onMessage: " + message);
                            configParser(message);
                            break;
                    }
                }

                @Override
                public void onOpen(ServerHandshake handshake) {
                    System.out.println("Connected to: " + getURI());
                    client.send("Hola");
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
        ArrayList<CustomDropdown> dropdowns = new ArrayList<CustomDropdown>();
        System.out.println("Este es el mensaje: " + message);
        String[] components = message.split("%%");
        System.out.println(components.length);
        for (int i = 0; i < components.length; i++){
            String componentID = components[i].substring(0,2);
            String[] attr = components[i].split("#");
            System.out.println(componentID);
            if(componentID.equals("SW")){
                switches.add(
                        new CustomSwitch(
                                Integer.parseInt(attr[1]),
                                attr[2],
                                attr[3]
                        )
                );
            } else if(componentID.equals("SL")){
                sliders.add(
                        new CustomSlider(
                                Integer.parseInt(attr[1]),
                                Integer.parseInt(attr[2]),
                                Integer.parseInt(attr[3]),
                                Integer.parseInt(attr[4]),
                                Integer.parseInt(attr[5]),
                                attr[6]
                        )
                );
            } else if(componentID.equals("SS")){
                sensors.add(
                        new CustomSensor(
                                Integer.parseInt(attr[1]),
                                attr[2],
                                attr[3],
                                attr[4],
                                attr[5]
                        )
                );
            } else if(componentID.equals("DD")){
                ArrayList<CustomOption> opts = new ArrayList<>();
                System.out.println("AQUI IVAN: " + attr[3]);
                String[] aux_opt = attr[3].split(",");
                for(int k = 0; k < aux_opt.length; k++){
                    String[] optionsValues = aux_opt[k].split("//");
                    /*
                    opts.add(
                            new CustomOption(
                                Integer.parseInt(optionsValues[0]), optionsValues[1]
                            )
                    );
                     */
                }

                dropdowns.add(
                        new CustomDropdown(
                                Integer.parseInt(attr[1]),
                                Integer.parseInt(attr[2]),
                                opts
                        )
                );
            }
        }

        MainDashboard.setArrays(switches,sensors,sliders);
    }

    public void userValidation (String message) {
        System.out.println(message);
        String[] user = message.split("#");
        if(user[3].equals("true")){ MainActivity.setValidated(true); }
        else { MainActivity.setValidated(false); }
    }

}
