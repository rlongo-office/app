package com.webrpg.app.model.derived;

import java.util.List;

public class Shape {
    private String type;
    private List<ShapePoint> shapePoints;

    public Shape(){}
    public Shape(String shapeData){
        //parse shapeData as json and populate members
    }
}
