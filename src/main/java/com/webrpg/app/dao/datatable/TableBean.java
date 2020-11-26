package com.webrpg.app.dao.datatable;

import java.io.Serializable;
import java.util.List;

public class TableBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private int iTotalRecords;

    private int iTotalDisplayRecords;

    private String sEcho;

    private List data;

    public TableBean(int iTotalRecords, int iTotalDisplayRecords, String sEcho, List data) {
        super();
        this.iTotalRecords = iTotalRecords;
        this.iTotalDisplayRecords = iTotalDisplayRecords;
        this.sEcho = sEcho;
        this.data = data;
    }

    public int getiTotalRecords() {
        return iTotalRecords;
    }

    public void setiTotalRecords(int iTotalRecords) {
        this.iTotalRecords = iTotalRecords;
    }

    public int getiTotalDisplayRecords() {
        return iTotalDisplayRecords;
    }

    public void setiTotalDisplayRecords(int iTotalDisplayRecords) {
        this.iTotalDisplayRecords = iTotalDisplayRecords;
    }

    public String getsEcho() {
        return sEcho;
    }

    public void setsEcho(String sEcho) {
        this.sEcho = sEcho;
    }

    public List getData() {
        return data;
    }

    public void setData(List data) {
        this.data = data;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }


}
