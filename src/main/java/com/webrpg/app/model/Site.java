package com.webrpg.app.model;

import javax.persistence.*;

@Entity
public class Site {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "name", nullable = true, length = 60)
    private String name;
    @Basic
    @Column(name = "type", nullable = true, length = 60)
    private String type;
    @ManyToOne
    @JoinColumn(name = "id_region", referencedColumnName = "id")
    private Region regionByIdRegion;
    @ManyToOne
    @JoinColumn(name = "id_state", referencedColumnName = "id")
    private State stateByIdState;
    @ManyToOne
    @JoinColumn(name = "id_geometry", referencedColumnName = "id")
    private Geometry geometryByIdGeometry;

    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public Region getRegionByIdRegion() {
        return regionByIdRegion;
    }
    public void setRegionByIdRegion(Region regionByIdRegion) {
        this.regionByIdRegion = regionByIdRegion;
    }

    public State getStateByIdState() { return stateByIdState; }
    public void setStateByIdState(State stateByIdState) { this.stateByIdState = stateByIdState; }

    public Geometry getGeometryByIdGeometry() { return geometryByIdGeometry; }
    public void setGeometryByIdGeometry(Geometry geometryByIdGeometry) { this.geometryByIdGeometry = geometryByIdGeometry; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Site site = (Site) o;

        if (id != null ? !id.equals(site.id) : site.id != null) return false;
        if (name != null ? !name.equals(site.name) : site.name != null) return false;
        if (type != null ? !type.equals(site.type) : site.type != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

}
