package com.webrpg.app.dao.datatable;

public class ColumnDef {
    private String className;
    private String targets;

    @Override
    public String toString() {
        return "ColumnDef [className=" + className + ", targets=" + targets + "]";
    }

    public ColumnDef(String className, String targets) {
        super();
        this.setClassName(className);
        this.setTargets(targets);
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTargets() {
        return targets;
    }

    public void setTargets(String targets) {
        this.targets = targets;
    }


}
