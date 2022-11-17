package com.example.AppIndustry.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.AppIndustry.R;
import com.example.AppIndustry.utils.ServerProperties;
import com.example.AppIndustry.utils.components.CustomSensor;
import com.example.AppIndustry.utils.components.CustomSlider;
import com.example.AppIndustry.utils.components.CustomSwitch;

import java.util.ArrayList;

public class MainDashboard extends AppCompatActivity {
    static boolean connected = true;
    static ArrayList<CustomSwitch> switches;
    static ArrayList<CustomSensor> sensors;
    static ArrayList<CustomSlider> sliders;
    static ArrayList<CustomSlider> dropdowns;
    Button loginBtn;

    public static void clearArrs() {
        switches = new ArrayList<>();
        sensors = new ArrayList<>();
        sliders = new ArrayList<>();
        dropdowns = new ArrayList<>();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_dashboard);
        switches = new ArrayList<>();
        sensors = new ArrayList<>();
        sliders = new ArrayList<>();

        ConnectionUseCase.client.envia(
                "CF#"
        );
        try{
            Thread.sleep(ServerProperties.SERVER_QUERY_DELAY * 2);
        } catch (Exception e){

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
                ConnectionUseCase.client = null;
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
            ArrayList<CustomSlider> _sliders
    ){
        switches = _switches;
        sensors = _sensors;
        sliders = _sliders;

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

    private void updateSliders(){
        LinearLayout linearLayout = findViewById(R.id.dashboard_layout_sliders);
        for (int slider = 0; slider < sliders.size(); slider++){
            SeekBar _slider = new SeekBar(this);
            _slider.setTag(R.id.componentId, sliders.get(slider).getId());
            _slider.setMax(sliders.get(slider).getMax());
            _slider.setProgress(sliders.get(slider).getDef());
            linearLayout.addView(_slider);
        }
    }
    private void updateSwithces(){
        LinearLayout linearLayout = findViewById(R.id.dashboard_layout_switches);
        for (int switchIt = 0; switchIt < switches.size(); switchIt++){
            Switch _switch = new Switch(this);
            _switch.setChecked(Boolean.parseBoolean(switches.get(switchIt).getDef()));
            linearLayout.addView(_switch);
        }
    }
    private void updateDropdowns(){
        LinearLayout linearLayout = findViewById(R.id.dashboard_layout_dropdown);
        for (int dropdown = 0; dropdown < sliders.size(); dropdown++){
            Spinner _spinner = new Spinner(this);
            _spinner.setEnabled(true);
            linearLayout.addView(_spinner);
        }
    }

}
