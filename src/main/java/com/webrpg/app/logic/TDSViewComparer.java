package com.webrpg.app.logic;

import com.webrpg.app.model.Thingdefstatsview;

import java.util.Comparator;

public class TDSViewComparer implements Comparator<Thingdefstatsview> {

    public TDSViewComparer(){};

    @Override
    public int compare(Thingdefstatsview x, Thingdefstatsview y){
        return x.getAttrid() < y.getAttrid() ? -1
                : x.getAttrid() > y.getAttrid() ? 1
                :0;
    }
}
