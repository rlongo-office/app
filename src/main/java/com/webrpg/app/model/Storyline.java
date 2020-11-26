package com.webrpg.app.model;

import javax.persistence.*;

@Entity
public class Storyline {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    @Basic
    @Column(name = "name", nullable = true, length = 60)
    private String name;
    @Basic
    @Column(name = "description", nullable = true, length = 1000)
    private String description;
    @Basic
    @Column(name = "type", nullable = true, length = 60)
    private String type;
    @Basic
    @Column(name = "begin_time", nullable = true)
    private Long beginTime;
    @Basic
    @Column(name = "end_time", nullable = true)
    private Long endTime;
    @ManyToOne
    @JoinColumn(name = "id_storyline", referencedColumnName = "id")
    private Storyline storylineByIdStoryline;

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

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }

    public Long getBeginTime() {
        return beginTime;
    }
    public void setBeginTime(Long beginTime) {
        this.beginTime = beginTime;
    }

    public Long getEndTime() {
        return endTime;
    }
    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Storyline getStorylineByIdStoryline() {
        return storylineByIdStoryline;
    }
    public void setStorylineByIdStoryline(Storyline storylineByIdStoryline) {
        this.storylineByIdStoryline = storylineByIdStoryline;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Storyline storyline = (Storyline) o;

        if (id != null ? !id.equals(storyline.id) : storyline.id != null) return false;
        if (name != null ? !name.equals(storyline.name) : storyline.name != null) return false;
        if (description != null ? !description.equals(storyline.description) : storyline.description != null)
            return false;
        if (type != null ? !type.equals(storyline.type) : storyline.type != null) return false;
        if (beginTime != null ? !beginTime.equals(storyline.beginTime) : storyline.beginTime != null) return false;
        if (endTime != null ? !endTime.equals(storyline.endTime) : storyline.endTime != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (beginTime != null ? beginTime.hashCode() : 0);
        result = 31 * result + (endTime != null ? endTime.hashCode() : 0);
        return result;
    }

}
