package com.webrpg.app.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Thingdefstats {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Basic
    @Column(name = "value", nullable = true, precision = 4)
    private String value;
    @ManyToOne
    @JoinColumn(name = "id_thingdef", referencedColumnName = "id", nullable = false)
    private Thingdef thingdefByIdThingdef;
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


    public Thingdef getThingdefByIdThingdef() {
        return thingdefByIdThingdef;
    }

    public void setThingdefByIdThingdef(Thingdef thingdefByIdThingdef) {
        this.thingdefByIdThingdef = thingdefByIdThingdef;
    }

    public Attribute getAttributeByIdAttribute() {
        return attributeByIdAttribute;
    }

    public void setAttributeByIdAttribute(Attribute attributeByIdAttribute) {
        this.attributeByIdAttribute = attributeByIdAttribute;
    }

    @Override
    public String toString() {
        return "Thingdefstats{" +
                "id=" + id +
                ", value=" + value +
                ", thingdefByIdThingdef=" + thingdefByIdThingdef.getId() +
                ", attributeByIdAttribute=" + attributeByIdAttribute.getId() +
                '}';
    }
}
