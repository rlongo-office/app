package com.webrpg.app.model;

import javax.persistence.*;

@Entity
public class Condition {
    @Id
    @Column(name = "id", nullable = false)
    private Integer id;
    @Basic
    @Column(name = "name", nullable = true, length = 45)
    private String name;
    @Basic
    @Column(name = "type", nullable = true)
    private Integer type;
    @Basic
    @Column(name = "description", nullable = true, length = -1)
    private String description;
    @ManyToOne
    @JoinColumn(name = "id_storyline", referencedColumnName = "id")
    private Storyline storylineByIdStoryline;


    public Integer getId() {
        return id;
    }
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() { return name; }
    public void setName(String name) {
        this.name = name;
    }

    public Integer getType() {
        return type;
    }
    public void setType(Integer type) {
        this.type = type;
    }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Storyline getStorylineByIdStoryline() { return storylineByIdStoryline; }
    public void setStorylineByIdStoryline(Storyline storylineByIdStoryline) { this.storylineByIdStoryline = storylineByIdStoryline; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Condition condition = (Condition) o;

        if (id != null ? !id.equals(condition.id) : condition.id != null) return false;
        if (name != null ? !name.equals(condition.name) : condition.name != null) return false;
        if (type != null ? !type.equals(condition.type) : condition.type != null) return false;

        return true;
    }
    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }
}
