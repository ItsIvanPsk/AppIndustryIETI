package com.example.AppIndustry.presentation;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;


import androidx.appcompat.app.AppCompatActivity;
import com.example.AppIndustry.R;
import com.example.AppIndustry.data.WebSockets;
import com.example.AppIndustry.presentation.dialog.ServerDisconectedDialog;
import com.example.AppIndustry.utils.ServerProperties;
import com.example.AppIndustry.utils.components.CustomSensor;
import com.example.AppIndustry.utils.components.CustomSlider;
import com.example.AppIndustry.utils.components.CustomSwitch;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainDashboard extends AppCompatActivity implements ServerProperties {
    static boolean connected = true;
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

        /*
        ConnectionUseCase.client.envia(
                "CF#"
        );
         */

        setAdapters();

        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                if(!connected){
                    ServerDisconectedDialog.serverDisconected(MainDashboard.this);
                }
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
    }

    private void updateSliders(){
        LinearLayout linearLayout = findViewById(R.id.dashboard_layout_spinners);
        for (int slider = 0; slider < sliders.size(); slider++){

        }
    }
    private void updateSensors(){

    }
    private void setAdapters(){
        Spinner sp1 = findViewById(R.id.spinner1);
        Spinner sp2 = findViewById(R.id.spinner2);
        Spinner sp3 = findViewById(R.id.spinner3);
        ArrayList<String> spinnerArray = new ArrayList<>();
        spinnerArray.add("1");
        spinnerArray.add("2");
        spinnerArray.add("3");
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, R.layout.support_simple_spinner_dropdown_item, spinnerArray);
        sp1.setAdapter(spinnerArrayAdapter);
        sp2.setAdapter(spinnerArrayAdapter);
        sp3.setAdapter(spinnerArrayAdapter);
    }

}
