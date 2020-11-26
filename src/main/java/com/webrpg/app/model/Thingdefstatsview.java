package com.webrpg.app.model;

import com.webrpg.app.logic.Utility;
import org.hibernate.annotations.Immutable;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Immutable
@Table(name = "thingdefstatsview")
public class Thingdefstatsview {
    @Id
    @Column(name = "rowid", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "defid", nullable = false)
    private Integer defid;
    @Basic
    @Column(name = "defname", nullable = true, length = 60)
    private String defname;
    @Basic
    @Column(name = "stat", nullable = true, precision = 4)
    private String stat;
    @Column(name = "attrname", nullable = false, length = 60)
    private String attrname;
    @Column(name = "attrid", nullable = false, length = 60)
    private Integer attrid;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Integer getDefid() {
        return defid;
    }
    public void setDefid(Integer defid) {
        this.defid = defid;
    }

    public String getDefname() {
        return defname;
    }
    public void setDefname(String defname) {
        this.defname = defname;
    }
    //While we store the 'stats' as string, many will be of numeric value...see 'getNumericStat method to return the numeric value
    public String getStat() {
        return stat;
    }
    public void setStat(String stat) {
        this.stat = stat;
    }
    // Used below if we know the "stat" will be a numeric value, which will be frequently
    public BigDecimal getNumericStat() {
        if (this.stat != null && Utility.isNumeric(this.stat) ) {
            try {
                return new BigDecimal(this.stat);
            } catch (Exception e){
                System.out.println("Attribute " + this.attrname + " is" + this.stat);
                return null;
            }
        } else {
            return null;
        }
    }
    //
    public String getAttrname() {
        return attrname;
    }
    public void setAttrname(String attrname) {
        this.attrname = attrname;
    }

    public Integer getAttrid() { return attrid; }
    public void setAttrid(Integer attrid) { this.attrid = attrid; }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Thingdefstatsview that = (Thingdefstatsview) o;
        return id.equals(that.id) &&
                defid.equals(that.defid) &&
                defname.equals(that.defname) &&
                Objects.equals(stat, that.stat) &&
                attrname.equals(that.attrname);
    }

    @Override
    public String toString() {
        return "Thingdefstatsview{" +
                "id=" + id +
                ", defid=" + defid +
                ", defname='" + defname + '\'' +
                ", stat=" + stat +
                ", attrname='" + attrname + '\'' +
                ", attrid=" + attrid +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, defid, defname, stat, attrname);
    }
}
