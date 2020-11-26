package com.webrpg.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webrpg.app.dao.*;
import com.webrpg.app.model.*;
import com.webrpg.app.model.derived.ThingDefRecord;
import org.apache.catalina.mapper.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

/*
For now maintaining a single Service Class for interaction with all DAO entities.  However in the future
It may make sense or better design to break the services into small service classes as needed.  For now, we'll
utilize this service class to abstract any db access required by front line controllers
 */
@Service("appserv")
public class ApplicationService {

    @Autowired
    private ThingdefRepository thingdefRepo;
    @Autowired
    ThingdefstatsRepository defstatsRepo;
    @Autowired
    private ThingRepository thingRepo;
    @Autowired
    private AttributeRepository attributeRepo;
    @Autowired
    ThingdefstatsviewRepository defstatviewRepo;
    @Autowired
    PowerdefRepository powerdefRepo;
    @Autowired
    ThingDefRecord thingDefRecord;


    public ApplicationService(){

    }

    //In the future we will need Overloaded versions of each for Pageable Lists in case of server side processing

    public List<Thingstats> getThingStatsRecords(String repoAction) {
        return new ArrayList<Thingstats>();
    }

    public List<Thinglink> getThingLinkRecords(String repoAction) {
        return new ArrayList<Thinglink>();
    }

    public List<Thingdefstatsview> getThingDefStatsViewRecords(String repoAction) {
        return new ArrayList<Thingdefstatsview>();
    }

    public List<Thingdefstats> getThingDefStatsRecords(String repoAction, String[] args) {
        switch(repoAction){
            case "findAll": System.out.println("getThingDefStatsRecords FindAll called");
                            return defstatsRepo.findAll();
            case "findByDefId": return defstatsRepo.findByThingdefByIdThingdef_Id(Long.parseLong(args[0]));
            case "findAllById": List<Long> listIDs = new ArrayList<>();
                for(int i=0; i<args.length;i++){
                    listIDs.add(Long.parseLong(args[i]));
                }
                return defstatsRepo.findAllById(listIDs);
        }
        return null;
    }

    public  List<Thing> getThingRecords(String repoAction) {
        return new ArrayList<Thing>();
    }

    public List<Thingdef> getThingDefRecords(String repoAction) {
        return new ArrayList<Thingdef>();
    }

    /*
    My attempt below to eliminate Autowiring Repositories across the controllers, and centralize access to the dao layer in this
    general service class
     */
    public List<Thingdefstatsview> getThingDefStatsViewRecords(String repoAction, String[] args) {
        switch(repoAction){
            case "findAll": return defstatviewRepo.findAll();
            case "findByName": return defstatviewRepo.findByDefname(args[0]);
            case "findByDefId": return defstatviewRepo.findByDefid(Integer.parseInt(args[0]));
            case "findAllById": List<Long> listIDs = new ArrayList<>();
                for(int i=0; i<args.length;i++){
                    listIDs.add(Long.parseLong(args[i]));
                }
                return defstatviewRepo.findAllById(listIDs);
        }
        return null;
    }

    public List<Thing> getThingRecords(String repoAction, String[] args) {
        switch(repoAction){
            case "findAll": return thingRepo.findAll();
            case "findByName": return thingRepo.findByName(args[0]);
            case "findById": return thingRepo.findById(Long.parseLong(args[0]));
            case "findAllById": List<Long> listIDs = new ArrayList<>();
                for(int i=0; i<args.length;i++){
                    listIDs.add(Long.parseLong(args[i]));
                }
                return thingRepo.findAllById(listIDs);
        }
        return null;
    }

    public long getThingCount(){
        return thingRepo.count();
    }

    public List<Thingdef> getThingDefRecords(String repoAction, String[] args) {
        switch(repoAction){
            case "findAll": return thingdefRepo.findAll();
            case "findByName": return thingdefRepo.findByName(args[0]);
            case "findById": return thingdefRepo.findById(Long.parseLong(args[0]));
            case "findAllById": List<Long> listIDs = new ArrayList<>();
                for(int i=0; i<args.length;i++){
                    listIDs.add(Long.parseLong(args[i]));
                }
                return thingdefRepo.findAllById(listIDs);
        }
        return null;
    }

    public long getThingDefCount(){
        return thingdefRepo.count();
    }

    public int cloneThingdef(String json) throws JsonProcessingException {
        System.out.println("cloneThingdef Service called");
        Thingdefstats newThingDefStats;

        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(json);
        Long tdID = mapper.convertValue(node.get("tdID"),Long.class);
        String[] strIdArray = new String[]{Long.toString(tdID)};
        //Unless we use TypeReference, the covertValue will default to a linkedHashMap, which won't reference our POJOs
        //and make iterating the list of POJO objects far less readable and understandable
        List<Thingdefstatsview> tdStatsList = mapper.convertValue(node.get("tdStatsList"), new TypeReference<>() {});
        List<Attribute> attrList;
        //Find the ThingDef related to this ID. Must be used for all the ThingDefStat inserts below
        List<Thingdef> tdList = getThingDefRecords("findById", strIdArray);
        //Check to see if there already exists entries for this ThingDef ID. Only add records if none exists
        if(getThingDefStatsRecords("findByDefId",strIdArray).isEmpty()){
            //We can iterate as list of TDSV because we used the TypeReference above when getting the values
            for (Thingdefstatsview row : tdStatsList) {
                newThingDefStats = new Thingdefstats();                         //Need new instance or we'll simply be writing over same DB record!
                newThingDefStats.setThingdefByIdThingdef(tdList.get(0));        //We'll keep the ThingDef the same across all the inserts
                newThingDefStats.setValue(row.getStat());
                //Convert Attribute ID to string, send over to search databas for correct Attribute record
                attrList = getAttributeRecords("findById", new String[]{String.valueOf(row.getAttrid())});
                newThingDefStats.setAttributeByIdAttribute(attrList.get(0));
                defstatsRepo.save(newThingDefStats);
            }
            return 1;
        }
        return 0;
    }

    public int updateThingdef(String json) throws JsonProcessingException {
        System.out.println("updateThingdef Service called");
        int recCount = 0;
        Thingdefstats newThingDefStats;
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(json);
        Long tdID = mapper.convertValue(node.get("tdID"),Long.class);
        String[] strIdArray = new String[]{Long.toString(tdID)};
        List<Thingdefstatsview> tdStatsList = mapper.convertValue(node.get("tdStatsList"), new TypeReference<>() {});
        List<Attribute> attrList;
        List<Thingdef> tdList = getThingDefRecords("findById", strIdArray);
        //converting the list back from the database to a hashmap will allow lookups in our loop
        //Using Lambda expression to load the list into a map
        Map<Long, Thingdefstats> mapTDSDBList = getThingDefStatsRecords("findByDefId",strIdArray)
                .stream().collect(toMap(s->s.getAttributeByIdAttribute().getId() ,s->s));
        //Iterate through ThingDefStats records; if value differs, change to new value and update record...
        for (Thingdefstatsview row : tdStatsList) {
                       //Need new instance in case we have new record entry
            if(mapTDSDBList.containsKey( Long.valueOf(row.getAttrid()))){
                newThingDefStats = mapTDSDBList.get(Long.valueOf(row.getAttrid()));
                //Test to see if value is different and change is true.  Because these are both BigDecimals, we need
                //to use 'compareTo', comparison operators only work on primitive data types
                if(row.getStat() !=null && row.getStat().compareTo(newThingDefStats.getValue())!=0){
                    System.out.println("values are NOT equal");
                    newThingDefStats.setValue(row.getStat());
                    defstatsRepo.save(newThingDefStats);
                    recCount++;
                } else System.out.println("values ARE equal");
            } else {  //...otherwise create and add new Thingdefstats object for new values
                newThingDefStats = new Thingdefstats();
                newThingDefStats.setThingdefByIdThingdef(tdList.get(0));        //We'll keep the ThingDef the same across all the inserts
                newThingDefStats.setValue(row.getStat());
                //Convert Attribute ID to string, send over to search database for correct Attribute record
                attrList = getAttributeRecords("findById", new String[]{String.valueOf(row.getAttrid())});
                newThingDefStats.setAttributeByIdAttribute(attrList.get(0));
                defstatsRepo.save(newThingDefStats);
                recCount++;
                System.out.println("A row is updated");
            }
        }

        return recCount;
    }


    public Thingdef saveNewThingdef(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(json);
        Thingdef thingdef = mapper.convertValue(node.get("thingdef"),Thingdef.class);
        if (!thingdefRepo.existsByName(thingdef.getName())){
            thingdefRepo.save(thingdef);
            return thingdef;
        }
        return null;
    }

    public Thingdef saveNewThingdef(Thingdef t){
        //An attribute is unique by it's name and parent id, if it has one
        if (!thingdefRepo.existsByName(t.getName())){
            thingdefRepo.save(t);
            return t;
        }
        return null;
    }


    public List<Attribute> getAttributeRecords(String repoAction, String[] args) {
        switch(repoAction){
            case "findAll": return attributeRepo.findAll();
            //For future review - may have multiple Attributes with same name, so below should be recoded to accommodate
            case "findByName": return attributeRepo.findByName(args[0]);
            case "findById": return attributeRepo.findById(Long.parseLong(args[0]));
            case "findAllById": List<Long> listIDs = new ArrayList<>();
                                for(int i=0; i<args.length;i++){
                                 listIDs.add(Long.parseLong(args[i]));
                                }
                                return attributeRepo.findAllById(listIDs);
        }
        return null;
    }

    public long getAttributeCount(){
        return attributeRepo.count();
    }

    /*
    The json string is coming from the controller, passed by the View(page) code
     */
    public Attribute saveNewAttribute(String json) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(json);
        Attribute attr = mapper.convertValue(node.get("attribute"),Attribute.class);
        Long parentId = mapper.convertValue(node.get("parentId"),Long.class);
        if(parentId!=null){
            List<Attribute> parentAttr = getAttributeRecords("findById", new String[]{String.valueOf(parentId)});
            attr.setAttributeByIdAttribute(parentAttr.get(0));
        } else {
            attr.setAttributeByIdAttribute(null);
        }
        //Uniqueness defined as combination of name and parentId for an Attribute. A null value for parentId still defines
        //uniqueness for any given named attribute
        if (!attributeRepo.existByNameAndParentID(attr.getName(), parentId)){
            System.out.println((parentId!=null)?"parentId has value":"parentId is null!");
            attributeRepo.save(attr);
            return attr;
        }
        return null;
    }

    public boolean saveNewAttribute(Attribute a){
        //An attribute is unique by it's name and parent id, if it has one
        if (!attributeRepo.existByNameAndParentID(a.getName(), a.getParentID())){
            attributeRepo.save(a);
         return true;
        }
        return false;
    }

    public String getTDStats(String tdID){
        return "success";
    }

    public List<Powerdef> getPowerdefRecords(String repoAction, String[] args) {
        switch(repoAction){
            case "findAll": return powerdefRepo.findAll();
            //For future review - may have multiple Attributes with same name, so below should be recoded to accommodate
            case "findByName": return powerdefRepo.findByName(args[0]);
            case "findById": return powerdefRepo.findById(Long.parseLong(args[0]));
            case "findAllById": List<Long> listIDs = new ArrayList<>();
                for(int i=0; i<args.length;i++){
                    listIDs.add(Long.parseLong(args[i]));
                }
                return powerdefRepo.findAllById(listIDs);
        }
        return null;
    }

}
