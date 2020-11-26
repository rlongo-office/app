package com.webrpg.app.model;

import javax.persistence.*;

@Entity
public class Area {
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
    @JoinColumn(name = "id_area", referencedColumnName = "id")
    private Area areaByIdArea;
    @ManyToOne
    @JoinColumn(name = "id_site", referencedColumnName = "id")
    private Site siteByIdSite;
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

    public Area getAreaByIdArea() {
        return areaByIdArea;
    }
    public void setAreaByIdArea(Area areaByIdArea) {
        this.areaByIdArea = areaByIdArea;
    }

    public Site getSiteByIdSite() { return siteByIdSite; }
    public void setSiteByIdSite(Site siteByIdSite) { this.siteByIdSite = siteByIdSite; }

    public Geometry getGeometryByIdGeometry() { return geometryByIdGeometry; }
    public void setGeometryByIdGeometry(Geometry geometryByIdGeometry) { this.geometryByIdGeometry = geometryByIdGeometry; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Area area = (Area) o;

        if (id != null ? !id.equals(area.id) : area.id != null) return false;
        if (name != null ? !name.equals(area.name) : area.name != null) return false;
        if (type != null ? !type.equals(area.type) : area.type != null) return false;

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
