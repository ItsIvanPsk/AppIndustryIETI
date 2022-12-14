package com.example.AppIndustry.utils.components;

public class CustomSwitch {
    private int id;
    private String blockName, def, label;

    public CustomSwitch(
            int id,
            String blockName,
            String def,
            String label
    ) {
        this.id = id;
        this.blockName = blockName;
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
    public void setBlockName(String _blockName) {
        this.blockName = _blockName;
    }
    public String getBlockName() {
        return blockName;
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
                ", blockName='" + blockName + '\'' +
                ", def='" + def + '\'' +
                ", label='" + label + '\'' +
                '}';
    }
}
