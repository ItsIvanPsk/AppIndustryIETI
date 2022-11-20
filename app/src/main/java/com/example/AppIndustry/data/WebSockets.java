package com.example.AppIndustry.data;

import android.app.Activity;
import android.os.Handler;
import android.os.Looper;

import com.example.AppIndustry.presentation.MainActivity;
import com.example.AppIndustry.presentation.MainDashboard;
import com.example.AppIndustry.presentation.dialog.ServerDisconectedDialog;
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

import java.lang.ref.WeakReference;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;

public class WebSockets {

    WebSocketClient client;

    static Activity currentAct;

    private static WeakReference<Activity> mainActivityRef;

    public static void updateMainActivity(Activity activity) {
        mainActivityRef = new WeakReference<>(activity);
    }

    public static void regAct(Activity activity){
        currentAct = activity;
    }

    public void envia(String message) {
        System.out.println("App sends: " + message);
        try {
            client.send(message);
        } catch (WebsocketNotConnectedException e) {
            System.out.println("No se ha podido enviar, no se ha encontrado la conexion con el websocket");
        }

    }

    public void connecta() {
        try {
            System.out.println("init connecta");
            client = new WebSocketClient(new URI(ServerProperties.SERVER_URIs), (Draft) new Draft_6455()) {
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
                    Handler handler = new Handler(Looper.getMainLooper());
                    handler.post(() -> ServerDisconectedDialog.serverDisconected(currentAct).show());
                }

                @Override
                public void onError(Exception ex) { ex.printStackTrace(); }
            };
            client.connect();
            System.out.println("Final client = new Connecta");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            System.out.println("Error: " + ServerProperties.SERVER_URIs + " no és una direcció URI de WebSocket vàlida");
        }
    }

    private void configParser(String message) {
        ArrayList<CustomSwitch> switches = new ArrayList<>();
        ArrayList<CustomSensor> sensors = new ArrayList<>();
        ArrayList<CustomSlider> sliders = new ArrayList<>();
        ArrayList<CustomDropdown> dropdowns = new ArrayList<>();
        ArrayList<CustomOption> opts;
        System.out.println("Este es el mensaje: " + message);

        String[] components = message.split("%%");

        System.out.println(components.length);

        for (String component : components) {
            String componentID = component.substring(0, 2);

            System.out.println(componentID);

            String[] attr = component.split("#");

            switch (componentID) {
                case "SW":
                    switches.add(
                            new CustomSwitch(
                                    Integer.parseInt(attr[1]),
                                    attr[2],
                                    attr[3]
                            )
                    );
                    break;
                case "SL":
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
                    break;
                case "SS":
                    sensors.add(
                            new CustomSensor(
                                    Integer.parseInt(attr[1]),
                                    attr[2],
                                    attr[3],
                                    attr[4],
                                    attr[5]
                            )
                    );
                    break;
                case "DD":
                    opts = new ArrayList<>();
                    String[] sepComas = attr[4].split(",");
                    for (int sepOpc = 0; sepOpc < sepComas.length; sepOpc++) {
                        if (sepOpc == sepComas.length - 1) {
                            sepComas[sepOpc] = sepComas[sepOpc].substring(1, sepComas[sepOpc].length() - 1);
                        } else {
                            sepComas[sepOpc] = sepComas[sepOpc].substring(1);
                        }
                    }

                    for (int sepOpc = 0; sepOpc < sepComas.length; sepOpc++) {
                        String[] options = sepComas[sepOpc].split("//");
                        opts.add(
                                new CustomOption(
                                        Integer.parseInt(options[0]),
                                        options[1]
                                )
                        );
                        System.out.println(opts.get(sepOpc).toString());
                    }

                    dropdowns.add(
                            new CustomDropdown(
                                    Integer.parseInt(attr[1]),
                                    Integer.parseInt(attr[2]),
                                    "",
                                    opts
                            )
                    );
                    break;
            }
        }

        MainDashboard.setArrays(switches,sensors,sliders,dropdowns);
    }

    public void userValidation (String message) {
        System.out.println(message);
        String[] user = message.split("#");
        MainActivity ma = (MainActivity) mainActivityRef.get();
        if(user[3].equals("true")){
            ma.setValidated(true);
            ma.navigate();
        }
        else { ma.setValidated(false); }
    }

    public void wsDisconnect(){
        client.onClose(5,"in",true);
    }

}
