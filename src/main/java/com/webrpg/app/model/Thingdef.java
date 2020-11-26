package com.webrpg.app.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "thingdef")
public class Thingdef {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Basic
    @Column(name = "name", nullable = true, length = 60)
    private String name;
    @Basic
    @Column(name = "description", nullable = true, length = 1000)
    private String description;
    //Thingdef owns the M2M relationship back to powerdef
    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "many_powerdef_has_many_thingdef",
            joinColumns = @JoinColumn(name = "id_thingdef"),
            inverseJoinColumns = @JoinColumn(name = "id_powerdef"))
    //The prevailing wisdom is to NOT use a List with Many to Many, and instead use a HashSet
    //I may have to change this in the future if this proves to be the correct course
    private List<Powerdef> lstPower = new ArrayList<>();

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

    public List<Powerdef> getLstPower() {return lstPower; }
    public void setLstPower(List<Powerdef> lstPower) { this.lstPower = lstPower; }

    @Override
    public String toString() {
        return "Thingdef{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", lstPower=" + lstPower +
                '}';
    }
}
