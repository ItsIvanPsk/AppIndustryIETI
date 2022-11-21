package com.example.AppIndustry.presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.AppIndustry.R;
import com.example.AppIndustry.data.WebSockets;
import com.example.AppIndustry.utils.ServerProperties;
import com.example.AppIndustry.utils.components.CustomDropdown;
import com.example.AppIndustry.utils.components.CustomSensor;
import com.example.AppIndustry.utils.components.CustomSlider;
import com.example.AppIndustry.utils.components.CustomSwitch;

import java.util.ArrayList;

public class MainDashboard extends AppCompatActivity {
    static ArrayList<CustomSwitch> switches;
    static ArrayList<CustomSensor> sensors;
    static ArrayList<CustomSlider> sliders;
    static ArrayList<CustomDropdown> dropdowns;
    Button loginBtn;
    boolean running = false;

    public static void clearArrays() {
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
        dropdowns = new ArrayList<>();

        WebSockets.regAct(this);

        loginBtn = findViewById(R.id.dashboard_login_button);

        try{
            ConnectionUseCase.ws.envia(
                    "CF#"
            );
            Thread.sleep(ServerProperties.SERVER_QUERY_DELAY);
        }catch (Exception e){ }

        updateSliders();
        updateSwitches();
        updateDropdowns();
        updateSensors();
        setupListeners();

    }

    private void setupListeners() {

        loginBtn.setOnClickListener(view -> {
            ConnectionUseCase.ws.wsDisconnect();
            Intent intent = new Intent(MainDashboard.this, MainActivity.class);
            startActivity(intent);
        });

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
    }

    private void updateSliders(){
        LinearLayout linearLayout = findViewById(R.id.dashboard_layout_sliders);
        for (int slider = 0; slider < sliders.size(); slider++){
            TextView tv = new TextView(this);
            tv.setText(sliders.get(slider).getLabel());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tv.setTextAppearance(R.style.ControlHeader);
            }
            linearLayout.addView(tv);
            SeekBar _slider = new SeekBar(this);
            _slider.setTag(R.id.componentId, sliders.get(slider).getId());
            _slider.setMax(sliders.get(slider).getMax());
            _slider.setProgress(sliders.get(slider).getDef());
            _slider.setPadding(0,10,0,10);
            _slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) { }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {  }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    seekBar.getTag(R.id.componentId);
                    onComponentChanges(
                            "SL",
                            (Integer) seekBar.getTag(R.id.componentId),
                            null,
                            seekBar.getProgress()
                    );
                }
            });
            linearLayout.addView(_slider);
        }
    }
    private void updateSwitches(){
        LinearLayout linearLayout = findViewById(R.id.dashboard_layout_switches);
        for (int switchIt = 0; switchIt < switches.size(); switchIt++){
            LinearLayout inner_linearLayout = new LinearLayout(this);
            inner_linearLayout.setOrientation(LinearLayout.HORIZONTAL);
            TextView tv = new TextView(this);
            tv.setText(switches.get(switchIt).getLabel());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tv.setTextAppearance(R.style.ControlHeader);
            }
            Switch _switch = new Switch(this);
            _switch.setChecked(Boolean.parseBoolean(switches.get(switchIt).getDef()));
            _switch.setPadding(0,10,0,10);
            _switch.setTag(R.id.componentId, switches.get(switchIt).getId());
            _switch.setOnCheckedChangeListener(
                    (compoundButton, state) -> onComponentChanges(
                    "SW",
                    (Integer) _switch.getTag(R.id.componentId),
                    _switch.isChecked(),
                    null)
            );
            inner_linearLayout.addView(tv);
            inner_linearLayout.addView(_switch);
            linearLayout.addView(inner_linearLayout);
        }
    }
    private void updateDropdowns(){
        LinearLayout linearLayout = findViewById(R.id.dashboard_layout_dropdown);
        for (int dropdown = 0; dropdown < dropdowns.size(); dropdown++){
            ArrayList<String> spinner_options = new ArrayList<>();
            TextView tv = new TextView(this);
            tv.setText(dropdowns.get(dropdown).getText());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                tv.setTextAppearance(R.style.ControlHeader);
            }
            linearLayout.addView(tv);
            Spinner _spinner = new Spinner(this);
            _spinner.setEnabled(true);
            _spinner.setPadding(0,10,0,10);
            _spinner.setTag(R.id.componentId, dropdowns.get(dropdown).getId());
            _spinner.setTag("bug init");
            for (int i = 0; i < dropdowns.get(dropdown).getOptions().size() ; i++){
                spinner_options.add(dropdowns.get(dropdown).getOptions().get(i).getLabel());
            }
            ArrayAdapter<String> spinnerArrayAdapter
                    = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, spinner_options);
            _spinner.setAdapter(spinnerArrayAdapter);
            _spinner.setSelection(dropdowns.get(dropdown).getDef() - 1);
            _spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    if (adapterView.getTag().equals("bug init")) {
                        adapterView.setTag("bug resolved");
                    } else {
                        onComponentChanges("DD", (Integer) adapterView.getTag(R.id.componentId), null, adapterView.getSelectedItemPosition());
                    }
                }
                @Override
                public void onNothingSelected(AdapterView<?> adapterView) { }
            });
            linearLayout.addView(_spinner);
        }
    }
    private void updateSensors(){
        LinearLayout linearLayout = findViewById(R.id.dashboard_layout_sensors);
        for (int sensor = 0; sensor < sensors.size(); sensor++){
            TextView _sensor = new TextView(this);
            TextView sensorHeader = new TextView(this);
            sensorHeader.setText("Sensor " + (sensor + 1));
            _sensor.setTag(R.id.componentId, sensors.get(sensor).getId());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                sensorHeader.setTextAppearance(R.style.ControlHeader);
            }
            String sensorText =
                    "Thresholdlow Temp: "
                    + sensors.get(sensor).getThresholdlow() + sensors.get(sensor).getUnits() + "\n"
                    + "Thresholdhigh Temp: "
                    + sensors.get(sensor).getThresholdhigh() + sensors.get(sensor).getUnits()
                    + "\n" + "\n";
            _sensor.setText(sensorText);
            linearLayout.addView(sensorHeader);
            linearLayout.addView(_sensor);
        }
    }
    public void onComponentChanges(
            String componentType,
            Integer componentID,
            @Nullable Boolean state,
            @Nullable Integer progress
    ){
        switch (componentType) {
            case "SW":
                ConnectionUseCase.ws.envia("AC#" + componentType + "#" + componentID + "#" + state);
                break;
            case "SL":
                ConnectionUseCase.ws.envia("AC#" + componentType + "#" + componentID + "#" + progress);
                break;
            case "DD":
                ConnectionUseCase.ws.envia("AC#" + componentType + "#" + componentID + "#" + progress);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        running = false;
    }

}
