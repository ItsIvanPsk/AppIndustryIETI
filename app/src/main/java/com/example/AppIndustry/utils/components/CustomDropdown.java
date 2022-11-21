package com.example.AppIndustry.utils.components;

import java.util.ArrayList;

public class CustomDropdown {
    private int id, def;
    String text;
    private ArrayList<CustomOption> options = new ArrayList<>();

    public void setDef(int def) { this.def = def; }
    public int getDef(){ return this.def; }
    public void setId(int _id) {
        this.id = _id;
    }
    public int getId() {
        return id;
    }
    public String getText(){
        return this.text;
    }
    public ArrayList<CustomOption> getOptions(){ return this.options; }

    public CustomDropdown(
            int _id,
            int _def,
            String _text,
            ArrayList<CustomOption> _options
    ){
        this.id = _id;
        this.def = _def;
        this.text = _text;
        this.options = _options;
    }
}
