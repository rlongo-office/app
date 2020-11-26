package com.webrpg.app.dao.datatable;

public class AttributeForm {
    private String name;
    private String hasvalue;
    private String parentid;

    public AttributeForm(String name, String hasvalue, String parentid) {
        this.name = name;
        this.hasvalue = hasvalue;
        this.parentid = parentid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHasvalue() {
        return hasvalue;
    }

    public void setHasvalue(String hasvalue) {
        this.hasvalue = hasvalue;
    }

    public String getParentid() {
        return parentid;
    }

    public void setParentid(String parentid) {
        this.parentid = parentid;
    }

    @Override
    public String toString() {
        return "AttributeForm{" +
                "name='" + name + '\'' +
                ", hasvalue='" + hasvalue + '\'' +
                ", parentid='" + parentid + '\'' +
                '}';
    }
}
