package com.webrpg.app.dao.datatable;

import java.io.Serializable;
import java.util.Map;

public class TableParameters implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer iDisplayStart;

    private Integer iDisplayLength;

    private Integer iColumns;

    private String sSearch;

    private Boolean bRegex;

    private Integer iSortingCols;

    private Integer sEcho;

    private Integer sortedColumnNumber;

    private String sortedColumnName;

    private String sortedColumnDirection;

    // ... getters and setters

    public Integer getiDisplayStart() {
        return iDisplayStart;
    }

    public void setiDisplayStart(Integer iDisplayStart) {
        this.iDisplayStart = iDisplayStart;
    }

    public Integer getiDisplayLength() {
        return iDisplayLength;
    }

    public void setiDisplayLength(Integer iDisplayLength) {
        this.iDisplayLength = iDisplayLength;
    }

    public Integer getiColumns() {
        return iColumns;
    }

    public void setiColumns(Integer iColumns) {
        this.iColumns = iColumns;
    }

    public String getsSearch() {
        return sSearch;
    }

    public void setsSearch(String sSearch) {
        this.sSearch = sSearch;
    }

    public Boolean getbRegex() {
        return bRegex;
    }

    public void setbRegex(Boolean bRegex) {
        this.bRegex = bRegex;
    }

    public Integer getiSortingCols() {
        return iSortingCols;
    }

    public void setiSortingCols(Integer iSortingCols) {
        this.iSortingCols = iSortingCols;
    }

    public Integer getsEcho() {
        return sEcho;
    }

    public void setsEcho(Integer sEcho) {
        this.sEcho = sEcho;
    }

    public Integer getSortedColumnNumber() {
        return sortedColumnNumber;
    }

    public void setSortedColumnNumber(Integer sortedColumnNumber) {
        this.sortedColumnNumber = sortedColumnNumber;
    }

    public String getSortedColumnName() {
        return sortedColumnName;
    }

    public void setSortedColumnName(String sortedColumnName) {
        this.sortedColumnName = sortedColumnName;
    }

    public String getSortedColumnDirection() {
        return sortedColumnDirection;
    }

    public void setSortedColumnDirection(String sortedColumnDirection) {
        this.sortedColumnDirection = sortedColumnDirection;
    }

    public static long getSerialversionuid() {
        return serialVersionUID;
    }

}