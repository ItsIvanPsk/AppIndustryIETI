package com.example.AppIndustry.presentation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;


import androidx.appcompat.app.AppCompatActivity;
import com.example.AppIndustry.R;
import com.example.AppIndustry.utils.components.CustomSensor;
import com.example.AppIndustry.utils.components.CustomSlider;
import com.example.AppIndustry.utils.components.CustomSwitch;

import java.util.ArrayList;

public class MainDashboard extends AppCompatActivity {

    static ArrayList<CustomSwitch> switches;
    static ArrayList<CustomSensor> sensors;
    static ArrayList<CustomSlider> sliders;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_dashboard);
        switches = new ArrayList<CustomSwitch>();
        sensors = new ArrayList<CustomSensor>();
        sliders = new ArrayList<CustomSlider>();
    }

    public static void setArrays(
            ArrayList<CustomSwitch> _switches,
            ArrayList<CustomSensor> _sensors,
            ArrayList<CustomSlider> _sliders
    ){
        switches = _switches;
        sensors = _sensors;
        sliders = _sliders;
    }

    private void updateSliders(){
        LinearLayout linearLayout = findViewById(R.id.dashboard_layout_spinners);
        for (int slider = 0; slider < sliders.size(); slider++){

        }
    }
    private void updateSensors(){

    }
    private void updateSwitches(){

    }
}
