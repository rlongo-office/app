package com.webrpg.app.model;

import com.fasterxml.jackson.annotation.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "attribute")
@JsonIgnoreProperties({"attributeList"})
//Without the @JsonIdentityInfo annotation, attempts to grab multiple attribute records results in recursive loop
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@NamedQuery(name="Attribute.findAll", query="SELECT a FROM Attribute a order by a.id")
public class Attribute {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Basic
    @Column(name = "name", nullable = true, length = 60)
    private String name;
    @Basic
    @Column(name = "hasvalue", nullable = true)
    private Boolean hasvalue;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_attribute")
    private Attribute attributeByIdAttribute;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "attributeByIdAttribute")
    @OrderBy("id ASC")
    @JsonIgnore
    List<Attribute > attributeList = new ArrayList<Attribute>();

    public Attribute(){}

    public Attribute(String name, boolean hasValue, Attribute parent) {
        this.name = name;
        this.hasvalue = hasValue;
        this.attributeByIdAttribute = parent;
        System.out.println("Attempting to create Attribute Entity object");
    }

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


    public Boolean getHasvalue() {
        return hasvalue;
    }

    public void setHasvalue(Boolean hasvalue) {
        this.hasvalue = hasvalue;
    }

    public Attribute getAttributeByIdAttribute() {
        return attributeByIdAttribute;
    }

    public void setAttributeByIdAttribute(Attribute attributeByIdAttribute) {
        this.attributeByIdAttribute = attributeByIdAttribute;
    }
    public List<Attribute> getAttributeList() {
        return attributeList;
    }

    public void setAttributeList(List<Attribute> attributeList) {
        this.attributeList = attributeList;
    }

    //For datatable use, we just need the parent attribute's ID for Json serialization so overriding the get method.
    // Passing another Attribute object was causing recursion issues

    @JsonGetter("attributeByIdAttribute")
    public long getParentID(){
        if(attributeByIdAttribute!=null) {
            return attributeByIdAttribute.id;}
        else return -1l;
    }

    @Override
    public String toString() {
        return "Attribute {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", hasvalue=" + hasvalue +
                '}';
    }
}
