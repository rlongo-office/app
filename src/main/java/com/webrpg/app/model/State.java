package com.webrpg.app.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class State {
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
    @JoinColumn(name = "id_state", referencedColumnName = "id")
    private State stateByIdState;
    @ManyToOne
    @JoinColumn(name = "id_geometry", referencedColumnName = "id")
    private Geometry geometryByIdGeometry;
    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "many_region_has_many_state",
            joinColumns = @JoinColumn(name = "id_state"),
            inverseJoinColumns = @JoinColumn(name = "id_region"))
    //The prevailing wisdom is to NOT use a List with Many to Many, and instead use a HashSet
    //I may have to change this in the future if this proves to be the correct course
    private List<Region> lstRegion = new ArrayList<>();

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

    public State getStateByIdState() {
        return stateByIdState;
    }
    public void setStateByIdState(State stateByIdState) {
        this.stateByIdState = stateByIdState;
    }

    public List<Region> getLstRegion() { return lstRegion; }
    public void setLstRegion(List<Region> lstRegion) { this.lstRegion = lstRegion; }

    public Geometry getGeometryByIdGeometry() { return geometryByIdGeometry; }
    public void setGeometryByIdGeometry(Geometry geometryByIdGeometry) { this.geometryByIdGeometry = geometryByIdGeometry; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        State state = (State) o;

        if (id != null ? !id.equals(state.id) : state.id != null) return false;
        if (name != null ? !name.equals(state.name) : state.name != null) return false;
        if (type != null ? !type.equals(state.type) : state.type != null) return false;

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
