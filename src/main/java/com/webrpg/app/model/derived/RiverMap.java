package com.webrpg.app.model.derived;

import java.util.ArrayList;
import java.util.List;

public class RiverMap {
    List<RiverMap> parents;     //holds the parents, which hold the inflow edges
    List<RiverEdge> outflows;      //holds all edge objects related to river outflows from this tactical map
    int type;                   // 0 = not an endpoint, 1 = origin point, 2 = destination point
    Point point;                //the point on the region(big) map where this river submap resides

    public RiverMap(){
        parents = new ArrayList<RiverMap>();
        outflows = new ArrayList<RiverEdge>();
        type = 0;
        point = new Point(0,0);
    }

    public RiverMap(Point p){
        parents = new ArrayList<RiverMap>();
        outflows = new ArrayList<RiverEdge>();
        type = 0;
        point = p;
    }

    public Point getPoint() {
        return point;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    class RiverEdge{
        Point first;        //first boundary point on submap for this river edge
        Point second;       //second boundary point on submap for this river edge
    }
}
