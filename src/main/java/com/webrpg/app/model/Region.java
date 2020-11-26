package com.webrpg.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Region {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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
    @JoinColumn(name = "id_geometry", referencedColumnName = "id")
    private Geometry geometryByIdGeometry;
    //By mapping back to lstState, we have set State as owner of this M2M relationship
    @ManyToMany(mappedBy = "lstRegion")
    private List<State> lstState = new ArrayList<>();

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

    public List<State> getLstState() { return lstState; }
    public void setLstState(List<State> lstState) { this.lstState = lstState; }

    public Geometry getGeometryByIdGeometry() { return geometryByIdGeometry; }
    public void setGeometryByIdGeometry(Geometry geometryByIdGeometry) { this.geometryByIdGeometry = geometryByIdGeometry; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Region region = (Region) o;

        if (id != null ? !id.equals(region.id) : region.id != null) return false;
        if (name != null ? !name.equals(region.name) : region.name != null) return false;
        if (type != null ? !type.equals(region.type) : region.type != null) return false;

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
