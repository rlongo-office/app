package com.webrpg.app.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webrpg.app.GameDataStore;
import com.webrpg.app.logic.TDSViewComparer;
import com.webrpg.app.model.Thingdef;
import com.webrpg.app.model.Thingdefstats;
import com.webrpg.app.service.ApplicationService;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import com.webrpg.app.dao.AttributeRepository;
import com.webrpg.app.dao.ThingdefstatsviewRepository;
import com.webrpg.app.dao.datatable.DataTablesRequest;
import com.webrpg.app.dao.datatable.DataTablesResponse;
import com.webrpg.app.logic.DieRoll;
import com.webrpg.app.model.Attribute;
import com.webrpg.app.model.Thingdefstatsview;
import com.webrpg.app.model.derived.RollRequest;
import com.webrpg.app.model.derived.ThingDefRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value = "rest-api", consumes= MediaType.ALL_VALUE, produces = { MediaType.APPLICATION_JSON_VALUE })
public class GameController {

    @Autowired
    GameDataStore gameDataStore;
    @Autowired
    private AttributeRepository attributeRepository;
    @Autowired
    ThingdefstatsviewRepository defstatviewRepository;
    @Autowired
    ThingDefRecord thingDefRecord;
    @Autowired
    ApplicationService appService;

    @PostMapping(path="/loadAttributes")
    public @ResponseBody List<Attribute> getAllAttributes(){
        return appService.getAttributeRecords("findAll",null);
    }

    @PostMapping(path="/addAttribute")
    public  @ResponseBody Attribute  addNewAttribute(@RequestBody String attrString) throws JsonProcessingException {
        Attribute attribute = appService.saveNewAttribute(attrString);
        System.out.println((attribute!=null)?attribute:"The attribute record is null!");
        return attribute;
    }
    @PostMapping(path="/addThingDef")
    public  @ResponseBody Thingdef  addNewThingDef(@RequestBody String tdString) throws JsonProcessingException {
        Thingdef thingdef = appService.saveNewThingdef(tdString);
        System.out.println((thingdef!=null)?thingdef:"The attribute record is null!");
        return thingdef;
    }
    @PostMapping(path="/updateThingDef")
    public  @ResponseBody int  updateThingDefStats(@RequestBody String tdUpdateString) throws JsonProcessingException {
        System.out.println("updateThingDefStats Rest Controller Called");
        return appService.updateThingdef(tdUpdateString);
    }

    @PostMapping(path="/cloneThingDef")
    public  @ResponseBody int  cloneThingDefStats(@RequestBody String tdCopyString) throws JsonProcessingException {
        System.out.println("cloneThingDefStats Rest Controller Called");
        return appService.cloneThingdef(tdCopyString);
    }


    @PostMapping(path="/getThingDefStats")
    public  @ResponseBody List<Thingdefstatsview>  getThingDefStats(@RequestBody Map<String,Object> body) {
        String[] tdID = new String[1];
        tdID[0] = body.get("tdID").toString();
        List<Thingdefstatsview> attrList = appService.getThingDefStatsViewRecords("findByDefId",tdID);
        Collections.sort(attrList,new TDSViewComparer());
        return attrList;
        //return gameDataStore.getThingDefRecord(tdID);
    }

    //******** For Server Side Loading of a JQuery DataTable
    //Notice the response body object, which is a POJO created to hold the object expected from a DataTable Request
    //Likewise, We send a specific Response Object back, which includes a List of the correct data objects for the table
    @PostMapping(path="/loadAttrDT")
    //@ResponseBody  -- this annotation not needed as it's implied in a spring @RestController class
    public DataTablesResponse<Attribute> dtAjaxCall(@RequestBody final DataTablesRequest dataTablesRequest) {
        long recordsTotal;
        long recordsFiltered;
        int curPage;
        List<Attribute> attributes;
        if ((attributeRepository).count() == 0) {
            //Return some error message
        }
        recordsTotal = (attributeRepository).count();
        curPage = (dataTablesRequest.getStart()<1) ? 0 : (int)dataTablesRequest.getStart()/(int)dataTablesRequest.getLength();
        Pageable pageable = PageRequest.of(curPage, dataTablesRequest.getLength());				//Creates a Pageable object to pass for our query
        Page<Attribute> pAttributes = attributeRepository.findAll(pageable);	//Once I added @Repository to the Repository class error went away
        attributes = pAttributes.getContent();
        recordsFiltered = recordsTotal;
        DataTablesResponse<Attribute> dataTableResponse =  new DataTablesResponse<Attribute>(dataTablesRequest.getDraw(),recordsTotal, recordsFiltered,"",attributes);
        return dataTableResponse;
    }
    //**************************************


    @GetMapping("/attributes")
    public List<Attribute> allAttributes() {

        return appService.getAttributeRecords("findAll",null);
    }

    @GetMapping("/defstats")
    public List<Thingdefstatsview> allDefStats() {

        return defstatviewRepository.findAll();
    }


    @GetMapping("/attributes/count")
    public Long count() {

        return appService.getAttributeCount();
    }

    @GetMapping("/attributes/{id}")
    public String findRecordById(@PathVariable String id) {
        //Long userId = Long.parseLong(id);
        return appService.getAttributeRecords("findById", new String[] { id}).toString();
    }

    @GetMapping("/defstats/{defid}")
    public List<Thingdefstatsview> findDefStatsByDefid(@PathVariable String defid) {

        Integer searchId = Integer.parseInt(defid);
        return defstatviewRepository.findByDefid(searchId);
    }
    @GetMapping("/mycharacter/{defid}")
    public ThingDefRecord findCharacterById(@PathVariable String defid) {
        Long id = Long.parseLong(defid);
        thingDefRecord.loadAllStats(id);
        return thingDefRecord;
    }

    @PostMapping("/dieroll")
    @ResponseBody
    public String rollDieForStat(@RequestBody RollRequest rollRequest){
        System.out.println(rollRequest);
        DieRoll dieRoll = new DieRoll(rollRequest);
        return dieRoll.getResult();
    }


}
