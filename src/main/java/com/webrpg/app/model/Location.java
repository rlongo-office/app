package com.webrpg.app.model;

import javax.persistence.*;

@Entity
public class Location {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "name", nullable = true, length = 45)
    private String name;
    /*
    Lesson learned - had to rename coordinate fields in database to be all lower case, but more importantly I could not name these coordinate
    properties with a lower case followed by Upper Case, or have upper case as first character.  Either naming would cause
    exception in the related JPARepository class
     */
    @Basic
    @Column(name = "xcoordinate", nullable = true)
    private Long xcoordinate;
    @Basic
    @Column(name = "ycoordinate", nullable = true)
    private Long ycoordinate;
    @Basic
    @Column(name = "zcoordinate", nullable = true)
    private Long zcoordinate;
    @ManyToOne
    @JoinColumn(name = "id_area", referencedColumnName = "id")
    private Area areaByIdArea;
    @ManyToOne
    @JoinColumn(name = "id_site", referencedColumnName = "id")
    private Site siteByIdSite;
    @ManyToOne
    @JoinColumn(name = "id_region", referencedColumnName = "id")
    private Region regionByIdRegion;
    @ManyToOne
    @JoinColumn(name = "id_state", referencedColumnName = "id")
    private State stateByIdState;
    @ManyToOne
    @JoinColumn(name = "id_world", referencedColumnName = "id")
    private World worldByIdWorld;
    @ManyToOne
    @JoinColumn(name = "id_storyline", referencedColumnName = "id")
    private Storyline storylineByIdStoryline;
    @ManyToOne
    @JoinColumn(name = "id_inventory", referencedColumnName = "id")
    private Inventory inventoryByIdInventory;

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

    public Long getXcoordinate() {
        return xcoordinate;
    }

    public void setXcoordinate(Long xcoordinate) {
        this.xcoordinate = xcoordinate;
    }

    public Long getYcoordinate() {
        return ycoordinate;
    }

    public void setYcoordinate(Long ycoordinate) {
        this.ycoordinate = ycoordinate;
    }

    public Long getZcoordinate() {
        return zcoordinate;
    }

    public void setZcoordinate(Long zcoordinate) {
        this.zcoordinate = zcoordinate;
    }



    public Area getAreaByIdArea() {
        return areaByIdArea;
    }
    public void setAreaByIdArea(Area areaByIdArea) {
        this.areaByIdArea = areaByIdArea;
    }

    public Site getSiteByIdSite() { return siteByIdSite; }
    public void setSiteByIdSite(Site siteByIdSite) { this.siteByIdSite = siteByIdSite; }

    public Region getRegionByIdRegion() { return regionByIdRegion; }
    public void setRegionByIdRegion(Region regionByIdRegion) { this.regionByIdRegion = regionByIdRegion; }

    public State getStateByIdState() { return stateByIdState; }
    public void setStateByIdState(State stateByIdState) { this.stateByIdState = stateByIdState; }

    public World getWorldByIdWorld() { return worldByIdWorld; }
    public void setWorldByIdWorld(World worldByIdWorld) { this.worldByIdWorld = worldByIdWorld; }

    public Storyline getStorylineByIdStoryline() { return storylineByIdStoryline; }
    public void setStorylineByIdStoryline(Storyline storylineByIdStoryline) { this.storylineByIdStoryline = storylineByIdStoryline; }

    public Inventory getInventoryByIdInventory() {
        return inventoryByIdInventory;
    }
    public void setInventoryByIdInventory(Inventory inventoryByIdInventory) { this.inventoryByIdInventory = inventoryByIdInventory; }
}
