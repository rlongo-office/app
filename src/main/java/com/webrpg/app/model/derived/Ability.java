package com.webrpg.app.model.derived;

import com.fasterxml.jackson.annotation.JsonValue;

import java.math.BigDecimal;

public class Ability{
    String ability;
    BigDecimal valScore;
    int modifier;

    public Ability(String ability, BigDecimal value, int modifier) {
        this.ability = ability;
        this.valScore = value;
        this.modifier = modifier;
    }

    public String getAbility() {
        return ability;
    }

    public void setAbility(String ability) {
        this.ability = ability;
    }

    public BigDecimal getValScore() {
        return valScore;
    }

    public void setValScore(BigDecimal valScore) {
        this.valScore = valScore;
    }

    public int getModifier() {
        return modifier;
    }

    public void setModifier(int modifier) {
        this.modifier = modifier;
    }

    @Override
    public String toString() {
        return "{" +
                "ability='" + ability + '\'' +
                ", valScore=" + valScore +
                ", modifier=" + modifier +
                '}';
    }
}
