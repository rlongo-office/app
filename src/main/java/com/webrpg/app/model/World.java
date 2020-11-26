package com.webrpg.app.model;

import javax.persistence.*;

@Entity
public class World {
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
    @Basic
    @Column(name = "latsize", nullable = true)
    private Long latsize;
    @Basic
    @Column(name = "longsize", nullable = true)
    private Long longsize;
    @ManyToOne
    @JoinColumn(name = "id_world", referencedColumnName = "id")
    private World worldByIdWorld;
    @ManyToOne
    @JoinColumn(name = "id_geometry", referencedColumnName = "id")
    private Geometry geometryByIdGeometry;

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

    public Long getLatsize() {
        return latsize;
    }
    public void setLatsize(Long latsize) {
        this.latsize = latsize;
    }

    public Long getLongsize() {
        return longsize;
    }
    public void setLongsize(Long longsize) {
        this.longsize = longsize;
    }

    public World getWorldByIdWorld() {
        return worldByIdWorld;
    }
    public void setWorldByIdWorld(World worldByIdWorld) {
        this.worldByIdWorld = worldByIdWorld;
    }

    public Geometry getGeometryByIdGeometry() { return geometryByIdGeometry; }
    public void setGeometryByIdGeometry(Geometry geometryByIdGeometry) { this.geometryByIdGeometry = geometryByIdGeometry; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        World world = (World) o;

        if (id != null ? !id.equals(world.id) : world.id != null) return false;
        if (name != null ? !name.equals(world.name) : world.name != null) return false;
        if (type != null ? !type.equals(world.type) : world.type != null) return false;
        if (latsize != null ? !latsize.equals(world.latsize) : world.latsize != null) return false;
        if (longsize != null ? !longsize.equals(world.longsize) : world.longsize != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (latsize != null ? latsize.hashCode() : 0);
        result = 31 * result + (longsize != null ? longsize.hashCode() : 0);
        return result;
    }

}
