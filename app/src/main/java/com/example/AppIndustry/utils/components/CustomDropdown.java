package com.example.AppIndustry.utils.components;

import java.util.ArrayList;

public class CustomDropdown {
    private int id, def;
    String text, blockName;
    private ArrayList<CustomOption> options;

    public void setId(int _id) {
        this.id = _id;
    }
    public int getId() {
        return id;
    }
    public void setBlockName(String _blockID) {
        this.blockName = _blockID;
    }
    public String getBlockName() {
        return blockName;
    }
    public void setDef(int def) { this.def = def; }
    public int getDef(){ return this.def; }
    public String getText(){
        return this.text;
    }
    public ArrayList<CustomOption> getOptions(){ return this.options; }

    public CustomDropdown(
            int _id,
            String _blockID,
            int _def,
            String _text,
            ArrayList<CustomOption> _options
    ){
        this.id = _id;
        this.blockName = _blockID;
        this.def = _def;
        this.text = _text;
        this.options = _options;
    }


    @Override
    public String toString() {
        return "CustomDropdown{" +
                "id=" + id +
                ", def=" + def +
                ", text='" + text + '\'' +
                ", blockName='" + blockName + '\'' +
                ", options=" + options +
                '}';
    }
}
