package com.example.AppIndustry.utils.components;

public class CustomSwitch {
    private int id;
    private String deault, label;

    public CustomSwitch(int id, String deault, String label) {
        this.id = id;
        this.deault = deault;
        this.label = label;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDeault() {
        return deault;
    }

    public void setDeault(String deault) {
        this.deault = deault;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @Override
    public String toString() {
        return "CustomSwitch{" +
                "id=" + id +
                ", deault='" + deault + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
