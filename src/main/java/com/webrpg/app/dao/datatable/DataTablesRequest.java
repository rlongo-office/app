package com.webrpg.app.dao.datatable;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class DataTablesRequest {
    /**
     * Draw counter. This is used by DataTables to ensure that the Ajax returns from server-side processing requests are drawn in sequence by DataTables
     * (Ajax requests are asynchronous and thus can return out of sequence). This is used as part of the draw return parameter (see below).
     */
    private int draw;

    /**
     * Paging first record indicator. This is the start point in the current data set (0 index based - i.e. 0 is the first record).
     */
    private int start;

    /**
     * Number of records that the table can display in the current draw. It is expected that the number of records returned will be equal to this number, unless
     * the server has fewer records to return. Note that this can be -1 to indicate that all records should be returned (although that negates any benefits of
     * server-side processing!)
     */
    private int length;

    /**
     * @see Search
     */
    private Search search;

    /**
     * @see Order
     * Note, we're using the JsonProperty to ensure proper mapping back to JSon that DataTable
     * requires (which is 'order' not 'orders')
     */
    @JsonProperty("order")
    private List<Order> orders;

    /**
     * @see Column
     */
    private List<Column> columns;


    public DataTablesRequest() {}

    public DataTablesRequest(int draw, int start, int length, Search search, List<Order> orders, List<Column> columns) {
        super();
        this.draw = draw;
        this.start = start;
        this.length = length;
        this.search = search;
        this.orders = orders;
        this.columns = columns;
    }

    //Setters and getters...
    public int getDraw() {
        return draw;
    }

    public void setDraw(int draw) {
        this.draw = draw;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
    }

    public Search getSearch() {
        return search;
    }

    public void setSearch(Search search) {
        this.search = search;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public List<Column> getColumns() {
        return columns;
    }

    public void setColumns(List<Column> columns) {
        this.columns = columns;
    }

    @Override
    public String toString() {
        return "DataTablesRequest{" +
                "draw=" + draw +
                ", start=" + start +
                ", length=" + length +
                ", search=" + search +
                ", orders=" + orders +
                ", columns=" + columns +
                '}';
    }
}
