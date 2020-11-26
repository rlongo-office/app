package com.webrpg.app.model;

import com.fasterxml.jackson.annotation.JsonGetter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Thing {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Basic
    @Column(name = "name", nullable = true, length = 60)
    private String name;
    @Basic
    @Column(name = "count", nullable = true)
    private Long count;
    @Basic
    @Column(name = "description", nullable = true)
    private String description;
    @ManyToOne
    @JoinColumn(name = "id_location", referencedColumnName = "id")
    private Location locationByIdLocation;
    @ManyToOne
    @JoinColumn(name = "id_thingdef", referencedColumnName = "id")
    private Thingdef thingdefByIdThingdef;
    @ManyToMany(mappedBy = "lstThing")
    private List<Schedule> lstSchedule = new ArrayList<>();

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public Long getCount() {
        return count;
    }
    public void setCount(Long count) {
        this.count = count;
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public Location getLocationByIdLocation() { return locationByIdLocation; }
    public void setLocationByIdLocation(Location locationByIdLocation) { this.locationByIdLocation = locationByIdLocation; }

    public Thingdef getThingdefByIdThingdef() {
        return thingdefByIdThingdef;
    }
    public void setThingdefByIdThingdef(Thingdef thingdefByIdThingdef) {
        this.thingdefByIdThingdef = thingdefByIdThingdef;
    }

    public List<Schedule> getLstSchedule() { return lstSchedule; }
    public void setLstSchedule(List<Schedule> lstSchedule) { this.lstSchedule = lstSchedule; }

    @JsonGetter("thingdefByIdThingdef")
    public long getParentID(){
        if(thingdefByIdThingdef!=null) {
            return thingdefByIdThingdef.getId();}
        else return -1l;
    }


}
