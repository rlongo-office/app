package com.webrpg.app.model;

import javax.persistence.*;

@Entity
public class Thinglink {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Basic
    @Column(name = "type", nullable = true, length = 60)
    private String type;
    @ManyToOne
    @JoinColumn(name = "id_thing_subject", referencedColumnName = "id")
    private Thing thingByIdThingSubject;
    @ManyToOne
    @JoinColumn(name = "id_thing_target", referencedColumnName = "id")
    private Thing thingByIdThingTarget;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Thing getThingByIdThingSubject() {
        return thingByIdThingSubject;
    }
    public void setThingByIdThingSubject(Thing thingByIdThingSubject) {
        this.thingByIdThingSubject = thingByIdThingSubject;
    }

    public Thing getThingByIdThingTarget() {
        return thingByIdThingTarget;
    }
    public void setThingByIdThingTarget(Thing thingByIdThingTarget) {
        this.thingByIdThingTarget = thingByIdThingTarget;
    }
}
