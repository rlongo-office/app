package com.webrpg.app.model;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "powerdefstats")
public class Powerdefstats {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Basic
    @Column(name = "value", nullable = true, precision = 4)
    private String value;
    @ManyToOne
    @JoinColumn(name = "id_powerdef", referencedColumnName = "id")
    private Powerdef powerdefByIdPowerdef;
    @ManyToOne
    @JoinColumn(name = "id_attribute", referencedColumnName = "id", nullable = false)
    private Attribute attributeByIdAttribute;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Powerdefstats that = (Powerdefstats) o;

        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (value != null ? !value.equals(that.value) : that.value != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        return result;
    }


    public Powerdef getPowerdefByIdPowerdef() {
        return powerdefByIdPowerdef;
    }
    public void setPowerdefByIdPowerdef(Powerdef powerdefByIdPowerdef) {
        this.powerdefByIdPowerdef = powerdefByIdPowerdef;
    }

    public Attribute getAttributeByIdAttribute() { return attributeByIdAttribute; }

    public void setAttributeByIdAttribute(Attribute attributeByIdAttribute) { this.attributeByIdAttribute = attributeByIdAttribute; }
}
