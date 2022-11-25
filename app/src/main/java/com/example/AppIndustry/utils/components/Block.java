package com.example.AppIndustry.utils.components;

import java.util.ArrayList;

public class Block {

    String blockName;

    ArrayList<CustomSwitch> block_switches;
    ArrayList<CustomSlider> block_sliders;
    ArrayList<CustomSensor> block_sensors;
    ArrayList<CustomDropdown> block_dropdowns;

    public Block(
            String blockName,
            ArrayList<CustomSwitch> block_switches,
            ArrayList<CustomSlider> block_sliders,
            ArrayList<CustomSensor> block_sensors,
            ArrayList<CustomDropdown> block_dropdowns
    ) {
        this.blockName = blockName;
        this.block_switches = block_switches;
        this.block_sliders = block_sliders;
        this.block_sensors = block_sensors;
        this.block_dropdowns = block_dropdowns;
    }

    public String getBlockName() {
        return blockName;
    }
    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }
    public ArrayList<CustomSwitch> getBlock_switches() {
        return block_switches;
    }
    public void setBlock_switches(ArrayList<CustomSwitch> block_switches) {
        this.block_switches = block_switches;
    }
    public ArrayList<CustomSlider> getBlock_sliders() {
        return block_sliders;
    }
    public void setBlock_sliders(ArrayList<CustomSlider> block_sliders) {
        this.block_sliders = block_sliders;
    }
    public ArrayList<CustomSensor> getBlock_sensors() {
        return block_sensors;
    }
    public void setBlock_sensors(ArrayList<CustomSensor> block_sensors) {
        this.block_sensors = block_sensors;
    }
    public ArrayList<CustomDropdown> getBlock_dropdowns() {
        return block_dropdowns;
    }
    public void setBlock_dropdowns(ArrayList<CustomDropdown> block_dropdowns) {
        this.block_dropdowns = block_dropdowns;
    }
}
