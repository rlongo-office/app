package com.webrpg.app.dao;

import com.webrpg.app.model.Attribute;
import com.webrpg.app.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class LoadConstants {
    //Many of the objects tracked in the game reference attributes organized under specific lists
    @Autowired
    AttributeRepository attributeRepository;
    @Autowired
    ApplicationService appService;
    //Making these Lists static may not be the best way to make them Globally available.  It's a patch for now
    public static List<String> abilityType;
    public static List<String> featType;
    public static List<String> speciesType;
    public static List<String> skillType;
    public static List<String> loreType;
    public static List<String> big5Type;
    public static List<String> sub5Type;
    public static List<String> alignmentType;
    public static int[] abilityMods = new int[31];      //The corresponding Mod based on ability score

    public LoadConstants(){
        abilityType = new ArrayList<>();
        featType= new ArrayList<>();
        speciesType= new ArrayList<>();
        skillType= new ArrayList<>();
        big5Type= new ArrayList<>();
        sub5Type= new ArrayList<>();
        loreType= new ArrayList<>();
        alignmentType = new ArrayList<>();
    }
    public void loadAllConstants(){
        //Get Abilities list
        var attribute = appService.getAttributeRecords("findByName",new String[] { "Ability"});
            for (Attribute a: attribute.get(0).getAttributeList()){
                abilityType.add(a.getName());
        }

        //Get Skills list
        attribute = appService.getAttributeRecords("findByName",new String[] { "Skill"});
            for (Attribute a: attribute.get(0).getAttributeList()){
                skillType.add(a.getName());
        }
        attribute = appService.getAttributeRecords("findByName",new String[] { "Lore"});
        for (Attribute a: attribute.get(0).getAttributeList()){
            loreType.add(a.getName());
        }
        attribute = appService.getAttributeRecords("findByName",new String[] { "Species"});
        for (Attribute a: attribute.get(0).getAttributeList()){
            speciesType.add(a.getName());
        }
        attribute = appService.getAttributeRecords("findByName",new String[] { "Feat"});
        for (Attribute a: attribute.get(0).getAttributeList()){
           featType.add(a.getName());
        }
        attribute = appService.getAttributeRecords("findByName",new String[] { "Alignment"});
        for (Attribute a: attribute.get(0).getAttributeList()){
            alignmentType.add(a.getName());
        }

        //Get Big 5 Personality Traits List
        List<String> big5Ids = new ArrayList<>(); ;
        attribute = appService.getAttributeRecords("findByName",new String[] { "Personality Trait"});
        for (Attribute a: attribute.get(0).getAttributeList()){
            big5Type.add(a.getName());
            big5Ids.add(String.valueOf(a.getId()));
        }
        //Get sub-traits List under Big 5
        var attributeList = appService.getAttributeRecords("findAllById",big5Ids.toArray( new String [0]));
        for (Attribute bigFive: attributeList){
            for (Attribute subFive: bigFive.getAttributeList()) {
                sub5Type.add(subFive.getName());
            }
        }
        //Get alignment List
        attribute = appService.getAttributeRecords("findByName",new String[] { "Alignment"});
        for (Attribute a: attribute.get(0).getAttributeList()){
            alignmentType.add(a.getName());
        }
    }

    public void loadAbilityModifiers(){
        for(int x=0; x<31; x++){
            switch(x){
                case 0:
                    abilityMods[x] = -10; break;
                case 1:
                    abilityMods[x] = -5; break;
                    case 2: case 3:
                    abilityMods[x] = -4; break;
                case 4: case 5:
                    abilityMods[x] = -3; break;
                case 6: case 7:
                    abilityMods[x] = -2; break;
                case 8: case 9:
                    abilityMods[x] = -1; break;
                case 10: case 11:
                    abilityMods[x] = 0; break;
                case 12: case 13:
                    abilityMods[x] = 1; break;
                case 14: case 15:
                    abilityMods[x] = 2; break;
                case 16: case 17:
                    abilityMods[x] = 3; break;
                case 18: case 19:
                    abilityMods[x] = 4; break;
                case 20: case 21:
                    abilityMods[x] = 5; break;
                case 22: case 23:
                    abilityMods[x] = 6; break;
                case 24: case 25:
                    abilityMods[x] = 7; break;
                case 26: case 27:
                    abilityMods[x] = 8; break;
                case 28: case 29:
                    abilityMods[x] = 9; break;
                case 30:
                    abilityMods[x] = 10; break;
                default:
            }
        }

    }
}
