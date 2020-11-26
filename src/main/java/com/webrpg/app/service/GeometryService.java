package com.webrpg.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.webrpg.app.model.Geometry;
import com.webrpg.app.model.derived.Shape;
import org.springframework.stereotype.Service;

import java.awt.*;

@Service
public class GeometryService {

    public Shape parseShapeData(Geometry geometry) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        TypeFactory typeFactory = mapper.getTypeFactory();
        Shape shape = mapper.readValue(geometry.getShapeData(),Shape.class);
        return shape;
    }

    public boolean isInShape(Point point, Shape shape){
        //Code to test if point is inside a shape (region)
        return false;
    }


}
