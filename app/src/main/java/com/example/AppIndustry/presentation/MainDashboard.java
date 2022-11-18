package com.example.AppIndustry.presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.AppIndustry.R;
import com.example.AppIndustry.data.WebSockets;
import com.example.AppIndustry.presentation.dialog.ServerDisconectedDialog;
import com.example.AppIndustry.utils.ServerProperties;
import com.example.AppIndustry.utils.components.CustomDropdown;
import com.example.AppIndustry.utils.components.CustomSensor;
import com.example.AppIndustry.utils.components.CustomSlider;
import com.example.AppIndustry.utils.components.CustomSwitch;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainDashboard extends AppCompatActivity {
    static boolean connected = true;
    static ArrayList<CustomSwitch> switches;
    static ArrayList<CustomSensor> sensors;
    static ArrayList<CustomSlider> sliders;
    static ArrayList<CustomDropdown> dropdowns;
    Button loginBtn;
    WebSockets ws;
    boolean running = false;

    public boolean getRunning(){
        return this.running;
    }

    public static void clearArrs() {
        switches = new ArrayList<>();
        sensors = new ArrayList<>();
        sliders = new ArrayList<>();
        dropdowns = new ArrayList<>();
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_dashboard);
        switches = new ArrayList<>();
        sensors = new ArrayList<>();
        sliders = new ArrayList<>();

        loginBtn = findViewById(R.id.dashboard_login_button);

        WebSockets.updateDashActivity(this);

        try{
            ws = new WebSockets();
            ws.connecta();
            System.out.println("MainDashboard: Conectado");
            Thread.sleep(ServerProperties.SERVER_QUERY_DELAY);
            ws.envia(
                    "CF#"
            );
            Thread.sleep(ServerProperties.SERVER_QUERY_DELAY);
        }catch (Exception e){
            System.out.println("Exception");
        }

        updateSliders();
        updateSwithces();
        updateDropdowns();
        setupListeners();
    }

    private void setupListeners() {

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                startActivity(intent);
            }
        });

    }

    public static void setStateConnected(boolean state){
        connected = state;
    }

    public static void setArrays(
            ArrayList<CustomSwitch> _switches,
            ArrayList<CustomSensor> _sensors,
            ArrayList<CustomSlider> _sliders,
            ArrayList<CustomDropdown> _dropdowns
    ){
        switches = _switches;
        sensors = _sensors;
        sliders = _sliders;
        dropdowns = _dropdowns;

        for (int i = 0; i < sensors.size(); i++){
            System.out.println(sensors.get(i).toString());
        }
        for (int i = 0; i < sliders.size(); i++){
            System.out.println(sliders.get(i).toString());
        }
        for (int i = 0; i < switches.size(); i++){
            System.out.println(switches.get(i).toString());
        }
        for (int i = 0; i < dropdowns.size(); i++){
            System.out.println(dropdowns.get(i).toString());
        }
    }

    private void updateSliders(){
        LinearLayout linearLayout = findViewById(R.id.dashboard_layout_sliders);
        for (int slider = 0; slider < sliders.size(); slider++){
            SeekBar _slider = new SeekBar(this);
            _slider.setTag(R.id.componentId, sliders.get(slider).getId());
            _slider.setMax(sliders.get(slider).getMax());
            _slider.setProgress(sliders.get(slider).getDef());
            _slider.setPadding(0,10,0,10);
            linearLayout.addView(_slider);
        }
    }
    private void updateSwithces(){
        LinearLayout linearLayout = findViewById(R.id.dashboard_layout_switches);
        for (int switchIt = 0; switchIt < switches.size(); switchIt++){
            Switch _switch = new Switch(this);
            _switch.setChecked(Boolean.parseBoolean(switches.get(switchIt).getDef()));
            _switch.setPadding(0,10,0,10);
            linearLayout.addView(_switch);
        }
    }
    private void updateDropdowns(){
        System.out.println("spi size = " + dropdowns.size());
        LinearLayout linearLayout = findViewById(R.id.dashboard_layout_dropdown);
        for (int dropdown = 0; dropdown < dropdowns.size(); dropdown++){
            ArrayList<String> spinner_options = new ArrayList<>();
            Spinner _spinner = new Spinner(this);
            _spinner.setEnabled(true);
            _spinner.setPadding(0,10,0,10);

            for (int i = 0; i < dropdowns.get(dropdown).getOptions().size() ; i++){
                spinner_options.add(dropdowns.get(dropdown).getOptions().get(i).getLabel());
            }
            ArrayAdapter<String> spinnerArrayAdapter
                    = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, spinner_options);
            _spinner.setAdapter(spinnerArrayAdapter);

            linearLayout.addView(_spinner);

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        running = false;
    }
}
