package com.webrpg.app.model.derived;

import java.util.ArrayList;
import java.util.List;

public class RiverMap {
    List<RiverMap> parents;         //holds the parent 'nodes', which hold the inflow edges
    List<RiverMap> children;        //holds the child river 'nodes', each outflow edge corresponds to a child node
    List<RiverEdge> outflows;       //holds all edge objects related to river outflows from this tactical map
    int type;                       // 0 = not an endpoint, 1 = origin point, 2 = destination point
    Point point;                    //the point on the region(big) map where this river submap resides
    int elevation;                  //ground elevation for this river point
    public static final int NORTH = 0;
    public static final int EAST = 1;
    public static final int SOUTH = 2;
    public static final int WEST = 3;

    public RiverMap(){
        parents = new ArrayList<RiverMap>();
        children = new ArrayList<RiverMap>();
        outflows = new ArrayList<RiverEdge>();
        type = 0;
        point = new Point(0,0);
        elevation = 0;
    }

    public RiverMap(Point p){
        parents = new ArrayList<RiverMap>();
        children = new ArrayList<RiverMap>();
        outflows = new ArrayList<RiverEdge>();
        type = 0;
        point = p;
        elevation = 0;
    }

    public void addEdge(Point p,int width, int direction){
        outflows.add(new RiverEdge(p,width,direction));
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

    public List<RiverMap> getParents() {
        return parents;
    }

    public void setParents(List<RiverMap> parents) {
        this.parents = parents;
    }

    public List<RiverMap> getChildren() {
        return children;
    }

    public void setChildren(List<RiverMap> children) {
        this.children = children;
    }

    public List<RiverEdge> getOutflows() {
        return outflows;
    }

    public void setOutflows(List<RiverEdge> outflows) {
        this.outflows = outflows;
    }

    public int getElevation() {
        return elevation;
    }

    public void setElevation(int elevation) {
        this.elevation = elevation;
    }


    class RiverEdge{

        RiverEdge(Point p,int width, int direction){
            edgePoint = p;
            this.width = width;
            this.direction = direction;
        }

        Point edgePoint;    //first boundary point on submap for this river edge
        int width;          //width of flow from edge point
        int direction;      //North = 0, East = 1, South = 2, West = 3

    }
}
