package com.example.AppIndustry.utils.components;

public class CustomSwitch {
    private int id, blockID;
    private String def, label;

    public CustomSwitch(
            int id,
            int blockID,
            String def,
            String label
    ) {
        this.id = id;
        this.blockID = blockID;
        if (def.equals("on")){
            this.def = "true";
        } else{
            this.def = "false";
        }
        this.label = label;
    }

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setBlockID(int _blockID) {
        this.blockID = _blockID;
    }
    public int getBlockID() {
        return blockID;
    }
    public String getDef() {
        return def;
    }
    public void setDef(String deault) {
        this.def = deault;
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
                ", def='" + def + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
