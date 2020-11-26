package com.webrpg.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "powerdef")
public class Powerdef {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Basic
    @Column(name = "name", nullable = false, length = 60)
    private String name;
    @Basic
    @Column(name = "description", nullable = false, length = 1000)
    private String description;
    //By mapping back to lstPower, we have set Thingdef as owner of this M2M relationship
    @JsonIgnore     //Don't need powerdef to list the thingdef's that link to it
    @ManyToMany(mappedBy = "lstPower")
    private List<Thingdef> lstThingDef = new ArrayList<>();

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

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public List<Thingdef> getLstThingDef() { return lstThingDef; }

    public void setLstThingDef(List<Thingdef> lstThingDef) { this.lstThingDef = lstThingDef; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Powerdef powerdef = (Powerdef) o;

        if (id != null ? !id.equals(powerdef.id) : powerdef.id != null) return false;
        if (name != null ? !name.equals(powerdef.name) : powerdef.name != null) return false;
        if (description != null ? !description.equals(powerdef.description) : powerdef.description != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Powerdef{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", lstThingDef=" + lstThingDef +
                '}';
    }
}
