package com.example.AppIndustry.utils.components;

public class CustomSensor {
    private int id;
    private String units, thresholdlow, thresholdhigh, label;

    public CustomSensor(int id, String units, String thresholdlow, String thresholdhigh, String label) {
        this.id = id;
        this.units = units;
        this.thresholdlow = thresholdlow;
        this.thresholdhigh = thresholdhigh;
        this.label = label;
    }

    @Override
    public String toString() {
        return "CustomSensor{" +
                "id=" + id +
                ", units='" + units + '\'' +
                ", thresholdlow='" + thresholdlow + '\'' +
                ", thresholdhigh='" + thresholdhigh + '\'' +
                ", label='" + label + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUnits() {
        return units;
    }

    public void setUnits(String units) {
        this.units = units;
    }

    public String getThresholdlow() {
        return thresholdlow;
    }

    public void setThresholdlow(String thresholdlow) {
        this.thresholdlow = thresholdlow;
    }

    public String getThresholdhigh() {
        return thresholdhigh;
    }

    public void setThresholdhigh(String thresholdhigh) {
        this.thresholdhigh = thresholdhigh;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}