package com.webrpg.app.model.derived;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webrpg.app.dao.LoadConstants;
import com.webrpg.app.dao.ThingdefstatsviewRepository;
import com.webrpg.app.logic.Utility;
import com.webrpg.app.model.Attribute;
import com.webrpg.app.model.Thingdefstatsview;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

@Service
public class ThingDefRecord implements Comparable<ThingDefRecord> {

    private Long thingDefId;
    private BigDecimal age;
    private BigDecimal weight;
    private BigDecimal height;
    private Map<String, BigDecimal> species = new LinkedHashMap<>();
    private List<Ability> abilities = new ArrayList<>();
    private Map<String, BigDecimal> skills = new LinkedHashMap<>();
    private Map<String, BigDecimal> lores = new LinkedHashMap<>();
    private Map<String, BigDecimal> bigFives = new LinkedHashMap<>();
    private Map<String, BigDecimal> subFives = new LinkedHashMap<>();
    private BigDecimal armorclass;
    private BigDecimal speed;
    private BigDecimal gender;
    private BigDecimal hitpoints;
    private String alignment;

    @Autowired
    ThingdefstatsviewRepository thingdefstatsviewRepository;

    public ThingDefRecord(){ }
    public ThingDefRecord(Long id, List<Thingdefstatsview> statsList){ loadAllStats(id, statsList); }
    public ThingDefRecord(Long id){
        loadAllStats(id);
    }
    public ThingDefRecord(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(json);
        Long tdID = mapper.convertValue(node.get("tdID"),Long.class);
        loadAllStats(tdID);
    }

    public void loadAllStats(Long id, List<Thingdefstatsview> statsList){
        this.thingDefId = id;

        loadAbilities(statsList,LoadConstants.abilityType,0);

        loadStats(statsList,LoadConstants.skillType,skills,0);
        //printMap(skills);
        loadStats(statsList,LoadConstants.loreType,lores,0);
        //printMap(lores);
        loadStats(statsList,LoadConstants.speciesType,species,2);
        //printMap(races);
        loadStats(statsList,LoadConstants.big5Type,bigFives,2);
        //printMap(bigFives);
        loadStats(statsList,LoadConstants.sub5Type,subFives,2);
        //printMap(subFives);
        loadAlignment(statsList);
        loadSingles(statsList);
    }

    public void loadAllStats(long id){
        this.thingDefId = id;
        List<Thingdefstatsview> statsList = thingdefstatsviewRepository.findByDefid(thingDefId.intValue());
        //System.out.println(statsList);
        loadAbilities(statsList,LoadConstants.abilityType,0);
        loadStats(statsList,LoadConstants.skillType,skills,0);
        //printMap(skills);
        loadStats(statsList,LoadConstants.loreType,lores,0);
        //printMap(lores);
        loadStats(statsList,LoadConstants.speciesType,species,2);
        //printMap(races);
        loadStats(statsList,LoadConstants.big5Type,bigFives,2);
        //printMap(bigFives);
        loadStats(statsList,LoadConstants.sub5Type,subFives,2);
        //printMap(subFives);
        loadAlignment(statsList);
        loadSingles(statsList);
    }

    private void loadSingles(List<Thingdefstatsview> statsList){
        for (Thingdefstatsview r: statsList){
            switch(r.getAttrname()){
                case "Age":
                    this.age =  r.getNumericStat(); break;
                case "Weight":
                    this.weight = r.getNumericStat(); break;
                case "Armor Class":
                    this.armorclass = r.getNumericStat(); break;
                case "Hit Points":
                    this.hitpoints = r.getNumericStat(); break;
                case "Gender":
                    this.gender = r.getNumericStat(); break;
                case "Height":
                    this.height = r.getNumericStat(); break;
                default:
            }
        }
    }

    private void loadAlignment(List<Thingdefstatsview> statsList){
        //find alignment from list
        for (String s: LoadConstants.alignmentType){
            for (Thingdefstatsview r: statsList){
                if (s.equals(r.getAttrname())) {
                    this.alignment = s;
                }
            }
        }
    }
    private void loadAbilities(List<Thingdefstatsview> statsList,List<String> keys, int scale){
        int modifier =0;
        for(String s: keys){
            for (Thingdefstatsview r: statsList){
                if (s.equals(r.getAttrname())) {
                    if (r.getStat()!=null){
                        modifier = LoadConstants.abilityMods[r.getNumericStat().intValue()];
                        abilities.add(new Ability(s,r.getNumericStat().setScale(scale, RoundingMode.HALF_UP),modifier));
                    } else abilities.add(new Ability(s,r.getNumericStat(),modifier));;
                }
            }
        }
    }

    private void loadStats(List<Thingdefstatsview> statsList, List<String> keys, Map map, int scale){
        for(String s: keys){
            for (Thingdefstatsview r: statsList){
                if (s.equals(r.getAttrname())) {
                    if (r.getStat()!=null){
                        if (Utility.isNumeric(r.getStat())) {
                            //WE had an issue where a strange value that appeared numeric was not converting from string
                            //to numeric, appearing actually null when a conversion to BigDecimal was attempted.  For now
                            //I'll keep the try-catch here in case we see this again
                            try{map.put(s, r.getNumericStat().setScale(scale, RoundingMode.HALF_UP));}
                            catch (Exception e){
                                System.out.println(e);
                                System.out.println("String is " + s);
                                System.out.println("r.getNumericStat is " + r.getNumericStat().setScale(scale, RoundingMode.HALF_UP));
                                map.put(s,new BigDecimal("999999.00"));
                            }
                        }
                        else map.put(s,r.getStat());
                    } else map.put(s,r.getStat());
                }
            }
        }
    }


    public static void printMap(Map mp) {
        Iterator it = mp.entrySet().iterator();
        System.out.println();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
            it.remove(); // avoids a ConcurrentModificationException
        }
    }
    /*Setters and Getters*/

    public Long getThingDefId() {
        return thingDefId;
    }

    public void setThingDefId(Long thingDefId) {
        this.thingDefId = thingDefId;
    }

    public BigDecimal getAge() {
        return age;
    }

    public void setAge(BigDecimal age) {
        this.age = age;
    }

    public BigDecimal getWeight() {
        return weight;
    }

    public void setWeight(BigDecimal weight) {
        this.weight = weight;
    }

    public BigDecimal getHeight() {
        return height;
    }

    public void setHeight(BigDecimal height) {
        this.height = height;
    }

    public Map<String, BigDecimal> getSpecies() {
        return species;
    }

    public void setSpecies(Map<String, BigDecimal> species) {
        this.species = species;
    }

    public List<Ability> getAbilities() {
        return abilities;
    }

    public void setAbilities(List<Ability> abilities) {
        this.abilities = abilities;
    }

    public Map<String, BigDecimal> getSkills() {
        return skills;
    }

    public void setSkills(Map<String, BigDecimal> skills) {
        this.skills = skills;
    }

    public Map<String, BigDecimal> getLores() {
        return lores;
    }

    public void setLores(Map<String, BigDecimal> lores) {
        this.lores = lores;
    }

    public Map<String, BigDecimal> getBigFives() {
        return bigFives;
    }

    public void setBigFives(Map<String, BigDecimal> bigFives) {
        this.bigFives = bigFives;
    }

    public Map<String, BigDecimal> getSubFives() {
        return subFives;
    }

    public void setSubFives(Map<String, BigDecimal> subFives) {
        this.subFives = subFives;
    }

    public BigDecimal getArmorclass() {
        return armorclass;
    }

    public void setArmorclass(BigDecimal armorclass) {
        this.armorclass = armorclass;
    }

    public BigDecimal getSpeed() {
        return speed;
    }

    public void setSpeed(BigDecimal speed) {
        this.speed = speed;
    }

    public BigDecimal getGender() {
        return gender;
    }

    public void setGender(BigDecimal gender) {
        this.gender = gender;
    }

    public BigDecimal getHitpoints() {
        return hitpoints;
    }

    public void setHitpoints(BigDecimal hitpoints) {
        this.hitpoints = hitpoints;
    }

    public String getAlignment() {
        return alignment;
    }

    public void setAlignment(String alignment) {
        this.alignment = alignment;
    }


    @Override
    public String toString() {
        return "ThingDefRecord{" +
                "thingDefId=" + thingDefId +
                ", age=" + age +
                ", weight=" + weight +
                ", height=" + height +
                ", species=" + species +
                ", abilities=" + abilities +
                ", skills=" + skills +
                ", lores=" + lores +
                ", bigFives=" + bigFives +
                ", subFives=" + subFives +
                ", armorclass=" + armorclass +
                ", speed=" + speed +
                ", gender=" + gender +
                ", hitpoints=" + hitpoints +
                ", alignment='" + alignment + '\'' +
                ", thingdefstatsviewRepository=" + thingdefstatsviewRepository +
                '}';
    }


    @Override
    public int compareTo(ThingDefRecord otherRecord) {
        return (int)(this.getThingDefId() - otherRecord.getThingDefId());
    }
}