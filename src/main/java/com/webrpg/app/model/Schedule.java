package com.webrpg.app.model;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Schedule {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "name", nullable = true, length = 45)
    private String name;
    @ManyToMany(cascade = {CascadeType.PERSIST})
    @JoinTable(
            name = "many_thing_has_many_schedule",
            joinColumns = @JoinColumn(name = "id_schedule"),
            inverseJoinColumns = @JoinColumn(name = "id_thing"))
    private List<Thing> lstThing = new ArrayList<>();

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

    public List<Thing> getLstThing() { return lstThing; }
    public void setLstThing(List<Thing> lstThing) { this.lstThing = lstThing; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Schedule schedule = (Schedule) o;

        if (id != null ? !id.equals(schedule.id) : schedule.id != null) return false;
        if (name != null ? !name.equals(schedule.name) : schedule.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }
}
