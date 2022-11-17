package com.example.AppIndustry.utils.components;

public class CustomSlider {
    private int id, min, max, step, def;
    private String label;

    public CustomSlider(int id, int def, int min, int max, int step, String label) {
        this.id = id;
        this.def = def;
        this.min = min;
        this.max = max;
        this.step = step;
        this.label = label;
    }

    @Override
    public String toString() {
        return "CustomSlider{" +
                "id=" + getId() +
                ", min=" + getMin() +
                ", max=" + getMax() +
                ", def=" + getDef() +
                ", step=" + getStep() +
                ", label='" + getLabel() + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getMax() {
        return max;
    }

    public void setMax(int max) {
        this.max = max;
    }

    public int getDef() {
        return def;
    }

    public void setDef(int def) {
        this.def = def;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
