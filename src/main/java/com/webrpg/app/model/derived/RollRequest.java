package com.webrpg.app.model.derived;

import java.math.BigDecimal;

public class RollRequest {
    int id;
    /*
    Category and value are related.  So for some die roll request, the category and value, like an Ability score roll in
    D&D (Strength of 17 roll provides a +3 increase to roll)
     */
    String category;
    String attribute;      //Not currently used, but possible future granularity to the use of category
    Integer value;
    /*Sequence form is nDx+c where n is quantity of dice, x is dice value, and c is any added constant, with each dice roll to be
    included in roll separated by a ";".  Example, 3D6+2;1D20+4 indicates a roll of 3 six-sided dice plus 2 added to sum, and then add
    that to a roll of 1 twenty-sided die plus 4 added.
     */
    String sequence;

    public RollRequest(){ }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getAttribute() { return attribute; }
    public void setAttribute(String attribute) { this.attribute = attribute; }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getSequence() { return sequence;}
    public void setSequence(String sequence) { this.sequence = sequence; }

    public Integer getValue() { return value; }
    public void setValue(Integer value) { this.value = value; }

    @Override
    public String toString() {
        return "RollRequest{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", attribute='" + attribute + '\'' +
                ", value=" + value +
                ", sequence='" + sequence + '\'' +
                '}';
    }
}
