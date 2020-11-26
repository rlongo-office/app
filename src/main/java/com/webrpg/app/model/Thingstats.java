package com.webrpg.app.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class Thingstats {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic
    @Column(name = "value", nullable = true, precision = 4)
    private String value;

    @ManyToOne
    @JoinColumn(name = "id_thing", referencedColumnName = "id", nullable = false)
    private Thing thingByIdThing;

    @ManyToOne
    @JoinColumn(name = "id_attribute", referencedColumnName = "id", nullable = false)
    private Attribute attributeByIdAttribute;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Thing getThingByIdThing() {
        return thingByIdThing;
    }

    public void setThingByIdThing(Thing thingByIdThing) {
        this.thingByIdThing = thingByIdThing;
    }

    public Attribute getAttributeByIdAttribute() {
        return attributeByIdAttribute;
    }

    public void setAttributeByIdAttribute(Attribute attributeByIdAttribute) {
        this.attributeByIdAttribute = attributeByIdAttribute;
    }
}
