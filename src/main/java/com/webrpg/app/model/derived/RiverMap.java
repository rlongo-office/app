package com.webrpg.app.model.derived;

import java.util.ArrayList;
import java.util.List;

public class RiverMap {
    List<RiverEdge> entries;   //holds all edge objects related to river inputs into the map
    List<RiverEdge> exits;     //holds all edge objects related to river inputs into the map
    int type;                   // 0= not an endpoint, 1=origin point, 2=destination point
    Point point;                //the point on the region(big) map where this river submap resides

    public RiverMap(){
        entries = new ArrayList<RiverEdge>();
        exits = new ArrayList<RiverEdge>();
        type = 0;
        point = new Point(0,0);
    }

    class RiverEdge{
        Point first;        //first boundary point on submap for this river edge
        Point second;       //second boundary point on submap for this river edge
    }
}
