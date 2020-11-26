package com.webrpg.app.model;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Geometry {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "shape_data", nullable = true, length = -1)
    private String shapeData;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getShapeData() { return shapeData; }
    public void setShapeData(String shapeData) { this.shapeData = shapeData; }
}