package com.webrpg.app.model;

import javax.persistence.*;

@Entity
public class Inventory {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "name", nullable = true, length = 45)
    private String name;
    @ManyToOne
    @JoinColumn(name = "id_condition", referencedColumnName = "id")
    private Condition conditionByIdCondition;

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

    public Condition getConditionByIdCondition() {
        return conditionByIdCondition;
    }
    public void setConditionByIdCondition(Condition conditionByIdCondition) { this.conditionByIdCondition = conditionByIdCondition; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Inventory inventory = (Inventory) o;

        if (id != null ? !id.equals(inventory.id) : inventory.id != null) return false;
        if (name != null ? !name.equals(inventory.name) : inventory.name != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

}
