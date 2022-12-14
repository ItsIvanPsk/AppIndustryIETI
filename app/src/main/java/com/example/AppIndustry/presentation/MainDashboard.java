package com.example.AppIndustry.presentation;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.AppIndustry.R;
import com.example.AppIndustry.data.WebSockets;
import com.example.AppIndustry.presentation.dialog.ServerDisconectedDialog;
import com.example.AppIndustry.utils.ServerProperties;
import com.example.AppIndustry.utils.components.Block;
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
    static ArrayList<Block> blocks = new ArrayList<>();
    static ArrayList<String> blockNameList = new ArrayList<>();


    Button loginBtn;
    boolean running = false;
    static Integer blockCant;

    public static void clearArrays() {
        switches = new ArrayList<>();
        sensors = new ArrayList<>();
        sliders = new ArrayList<>();
        dropdowns = new ArrayList<>();
    }

    public static void setBlockCant(Integer _blockCant) {
        blockCant = _blockCant;
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
        WebSockets.updateDashActivity(this);
        loginBtn = findViewById(R.id.dashboard_login_button);

        try{
            ConnectionUseCase.ws.envia(
                    "CF#"
            );
            Thread.sleep(ServerProperties.SERVER_QUERY_DELAY);
        }catch (Exception e){ }

        generateUI();

        setupListeners();
    }

    public void generateUI(){

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(new Runnable() {
            @Override
            public void run() {
                getAllBlocks();
                sepComponents();
                updateUI();
            }
        });
    }


    @SuppressLint("ResourceAsColor")
    private void updateUI() {
        ScrollView scroll = findViewById(R.id.dashboard_scroll);
        scroll.removeAllViews();
        LinearLayout components = new LinearLayout(this);
        components.setOrientation(LinearLayout.VERTICAL);
        scroll.addView(components);

        for (int block = 0; block < blocks.size(); block++) {

            LinearLayout blockLayout = new LinearLayout(this);
            blockLayout.setOrientation(LinearLayout.VERTICAL);
            blockLayout.setBackgroundResource(R.drawable.block_bg);

            LinearLayout.LayoutParams blockLayoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams. MATCH_PARENT ,
                    LinearLayout.LayoutParams. WRAP_CONTENT ) ;
            blockLayoutParams.setMargins( 30 , 20 , 30 , 0 ) ;

            LinearLayout blockHeader = new LinearLayout(this);
            TextView blockTitle = new TextView(this);
            blockTitle.setText(blocks.get(block).getBlockName());

            LinearLayout.LayoutParams headertitle = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams. MATCH_PARENT ,
                    LinearLayout.LayoutParams. WRAP_CONTENT ) ;
            headertitle.setMargins( 30 , 20 , 30 , 0 ) ;

            blockTitle.setPadding(10,20,10,20);
            blockTitle.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            blockTitle.setTextAppearance(R.style.BlockHeader);

            blockHeader.addView(blockTitle, headertitle);
            blockLayout.addView(blockHeader);

            for (int sw = 0; sw < blocks.get(block).getBlock_switches().size(); sw++) {
                LinearLayout switchLayout = new LinearLayout(this);
                switchLayout.setOrientation(LinearLayout.HORIZONTAL);
                switchLayout.setPadding(10,30,10,30);
                TextView tv = new TextView(this);
                tv.setText(blocks.get(block).getBlock_switches().get(sw).getLabel());
                tv.setTextAppearance(R.style.ControlHeader);
                LinearLayout.LayoutParams switchParamstv = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams. WRAP_CONTENT ,
                        LinearLayout.LayoutParams. WRAP_CONTENT ) ;
                switchParamstv.setMargins( 30 , 20 , 30 , 0 );
                Switch _switch = new Switch(this);
                _switch.setChecked(Boolean.parseBoolean(blocks.get(block).getBlock_switches().get(sw).getDef()));
                _switch.setPadding(0,10,0,10);
                _switch.setTag(R.id.componentId, blocks.get(block).getBlock_switches().get(sw).getId());
                _switch.setOnCheckedChangeListener(
                        (compoundButton, state) -> onComponentChanges(
                                "SW",
                                (Integer) _switch.getTag(R.id.componentId),
                                _switch.isChecked(),
                                null)
                );
                LinearLayout.LayoutParams switchParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams. MATCH_PARENT ,
                        LinearLayout.LayoutParams. WRAP_CONTENT ) ;
                switchParams.setMargins( 30 , 20 , 30 , 0 );
                switchLayout.addView(tv, switchParamstv);
                switchLayout.addView(_switch, switchParams);
                blockLayout.addView(switchLayout);
            }

            for (int ss = 0; ss < blocks.get(block).getBlock_sensors().size(); ss++) {
                LinearLayout inner_linearLayout = new LinearLayout(this);
                inner_linearLayout.setOrientation(LinearLayout.VERTICAL);
                TextView tv = new TextView(this);
                tv.setText(blocks.get(block).getBlock_sensors().get(ss).getLabel());
                tv.setTextAppearance(R.style.ControlHeader);
                LinearLayout.LayoutParams paramstv = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams. MATCH_PARENT ,
                        LinearLayout.LayoutParams. WRAP_CONTENT ) ;
                paramstv.setMargins( 30 , 20 , 30 , 0 );
                TextView _sensor = new TextView(this);

                String sensorText =
                        "Thresholdlow Temp: "
                                + blocks.get(block).getBlock_sensors().get(ss).getThresholdlow()
                                + blocks.get(block).getBlock_sensors().get(ss).getUnits() + "\n"
                                + "Thresholdhigh Temp: "
                                + blocks.get(block).getBlock_sensors().get(ss).getThresholdhigh()
                                + blocks.get(block).getBlock_sensors().get(ss).getUnits()
                                + "\n" + "\n";
                _sensor.setTag(R.id.componentId, blocks.get(block).getBlock_sensors().get(ss).getId());
                _sensor.setText(sensorText);

                LinearLayout.LayoutParams sensorParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams. WRAP_CONTENT ,
                        LinearLayout.LayoutParams. WRAP_CONTENT ) ;
                sensorParams.setMargins( 30 , 20 , 30 , 0 );

                inner_linearLayout.addView(tv, paramstv);
                inner_linearLayout.addView(_sensor, sensorParams);
                blockLayout.addView(inner_linearLayout);
            }

            for (int sl = 0; sl < blocks.get(block).getBlock_sliders().size(); sl++) {
                LinearLayout inner_linearLayout = new LinearLayout(this);
                inner_linearLayout.setOrientation(LinearLayout.VERTICAL);
                TextView tv = new TextView(this);
                tv.setText(blocks.get(block).getBlock_sliders().get(sl).getLabel());
                tv.setTextAppearance(R.style.ControlHeader);
                LinearLayout.LayoutParams paramstv = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams. MATCH_PARENT ,
                        LinearLayout.LayoutParams. WRAP_CONTENT ) ;
                paramstv.setMargins( 30 , 20 , 30 , 0 );
                SeekBar _slider = new SeekBar(this);
                _slider.setTag(R.id.componentId, blocks.get(block).getBlock_sliders().get(sl).getId());
                _slider.setTag(R.id.blockID, blocks.get(block).getBlock_sliders().get(sl).getBlockName());
                _slider.setMin(blocks.get(block).getBlock_sliders().get(sl).getMin());
                _slider.setMax(blocks.get(block).getBlock_sliders().get(sl).getMax());
                _slider.setProgress(blocks.get(block).getBlock_sliders().get(sl).getDef());
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

                LinearLayout.LayoutParams paramsSlider = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams. WRAP_CONTENT ,
                        LinearLayout.LayoutParams. WRAP_CONTENT ) ;
                paramsSlider.setMargins( 30 , 20 , 30 , 0 );
                paramsSlider.width = 800;

                inner_linearLayout.addView(tv, paramstv);
                inner_linearLayout.addView(_slider, paramsSlider);
                blockLayout.addView(inner_linearLayout);
            }

            for (int dd = 0; dd < blocks.get(block).getBlock_dropdowns().size(); dd++) {
                LinearLayout inner_linearLayout = new LinearLayout(this);
                inner_linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                ArrayList<String> spinner_options = new ArrayList<>();

                TextView tv = new TextView(this);
                tv.setText(blocks.get(block).getBlock_dropdowns().get(dd).getText());
                tv.setTextAppearance(R.style.ControlHeader);
                LinearLayout.LayoutParams paramstv = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams. WRAP_CONTENT ,
                        LinearLayout.LayoutParams. WRAP_CONTENT ) ;
                paramstv.setMargins( 30 , 20 , 30 , 0 );

                Spinner _spinner = new Spinner(this);
                _spinner.setEnabled(true);
                _spinner.setPadding(0,10,0,10);
                _spinner.setTag(R.id.componentId, blocks.get(block).getBlock_dropdowns().get(dd).getId());
                _spinner.setTag("bug init");
                for (int i = 0; i < blocks.get(block).getBlock_dropdowns().get(dd).getOptions().size() ; i++){
                    spinner_options.add(blocks.get(block).getBlock_dropdowns().get(dd).getOptions().get(i).getLabel());
                }
                ArrayAdapter<String> spinnerArrayAdapter
                        = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, spinner_options);
                _spinner.setAdapter(spinnerArrayAdapter);
                _spinner.setSelection(blocks.get(block).getBlock_dropdowns().get(dd).getDef() - 1);
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

                LinearLayout.LayoutParams spinnerParams = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams. WRAP_CONTENT ,
                        LinearLayout.LayoutParams. WRAP_CONTENT ) ;
                spinnerParams.setMargins( 30 , 20 , 30 , 0 );

                inner_linearLayout.addView(tv, paramstv);
                inner_linearLayout.addView(_spinner, spinnerParams);
                blockLayout.addView(inner_linearLayout);
            }

            components.addView(blockLayout, blockLayoutParams);
        }
    }

    public void sepComponents(){
        for (int block = 0; block < blocks.size(); block++) {
            String bName = blocks.get(block).getBlockName();
            for (int sw = 0; sw < switches.size(); sw++) {
                if(switches.get(sw).getBlockName().equals(bName)){
                    blocks.get(block)
                            .getBlock_switches()
                            .add(switches.get(sw));
                }
            }
            for (int sl = 0; sl < sliders.size(); sl++) {
                if(sliders.get(sl).getBlockName().equals(bName)){
                    blocks.get(block)
                            .getBlock_sliders()
                            .add(sliders.get(sl));
                }
            }
            for (int ss = 0; ss < sensors.size(); ss++) {
                if(sensors.get(ss).getBlockName().equals(bName)){
                    blocks.get(block)
                            .getBlock_sensors()
                            .add(sensors.get(ss));
                }
            }
            for (int dd = 0; dd < dropdowns.size(); dd++) {
                if(dropdowns.get(dd).getBlockName().equals(bName)){
                    blocks.get(block)
                            .getBlock_dropdowns()
                            .add(dropdowns.get(dd));
                }
            }
        }
    }

    public void printBlock(){
        for (int block = 0; block < blocks.size(); block++) {
            System.out.println("Blocks");
            System.out.println(blocks.get(block).getBlockName().toString());
            System.out.println(blocks.get(block).getBlock_switches().toString());
            System.out.println(blocks.get(block).getBlock_sliders().toString());
            System.out.println(blocks.get(block).getBlock_sensors().toString());
            System.out.println(blocks.get(block).getBlock_dropdowns().toString());
        }
    }

    public void getAllBlocks(){

        blockNameList.clear();
        blocks.clear();

        for (int sw = 0; sw < switches.size(); sw++) {
            if(!blockNameList.contains(switches.get(sw).getBlockName())){
                blockNameList.add(switches.get(sw).getBlockName());
                blocks.add(new Block(
                        switches.get(sw).getBlockName(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>()
                ));
            }
        }

        for (int sl = 0; sl < sliders.size(); sl++) {
            if(!blockNameList.contains(sliders.get(sl).getBlockName())){
                blockNameList.add(sliders.get(sl).getBlockName());
                blocks.add(new Block(
                        sliders.get(sl).getBlockName(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>()
                ));
            }
        }

        for (int ss = 0; ss < sensors.size(); ss++) {
            if(!blockNameList.contains(sensors.get(ss).getBlockName())){
                blockNameList.add(sensors.get(ss).getBlockName());
                blocks.add(new Block(
                        sensors.get(ss).getBlockName(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>()
                ));
            }
        }

        for (int dd = 0; dd < dropdowns.size(); dd++) {
            if(!blockNameList.contains(dropdowns.get(dd).getBlockName())){
                blockNameList.add(dropdowns.get(dd).getBlockName());
                blocks.add(new Block(
                        dropdowns.get(dd).getBlockName(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>(),
                        new ArrayList<>()
                ));
            }
        }
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
