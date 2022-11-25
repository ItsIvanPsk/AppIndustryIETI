package com.example.AppIndustry.utils.components;

public class CustomOption {
    private int id;
    private String label;

    public CustomOption(int _id, String _label){
        this.id = _id;
        this.label = _label;
    }

    public int getId(){
        return this.id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getLabel(){
        return this.label;
    }
    public void setLabel(String _label) {
        this.label = _label;
    }

    @Override
    public String toString() {
        return "CustomOption{" +
                "id=" + id +
                ", label='" + label + '\'' +
                '}';
    }
}
