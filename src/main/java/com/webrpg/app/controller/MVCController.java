package com.webrpg.app.controller;

import com.webrpg.app.GameDataStore;
import com.webrpg.app.dao.AttributeRepository;
import com.webrpg.app.dao.ThingdefRepository;
import com.webrpg.app.dao.ThingdefstatsviewRepository;
import com.webrpg.app.model.derived.ThingDefRecord;
import com.webrpg.app.service.ApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MVCController {

    @Autowired
    ThingdefstatsviewRepository defstatviewRepository;
    @Autowired
    ThingDefRecord thingDefRecord;
    @Autowired
    GameDataStore gameDataStore;
    @Autowired
    private AttributeRepository attributeRepository;
    @Autowired
    ApplicationService applicationService;

    @GetMapping("/mainapp")
    public ModelAndView getMainAppPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("MainPageTemplate.html");
        modelAndView.addObject("listAttributes", applicationService.getAttributeRecords("findAll",null));
        return modelAndView;
    }

    @GetMapping("/datathingdef")
    public ModelAndView getDataThingAndThingdefPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("DataThingDefThing.html");
        modelAndView.addObject("listThingdef", applicationService.getThingDefRecords("findAll",null));
        modelAndView.addObject("listThing", applicationService.getThingRecords("findAll",null));
        modelAndView.addObject("listAttributes", applicationService.getAttributeRecords("findAll",null));
        modelAndView.addObject("listPowerdef", applicationService.getPowerdefRecords("findAll",null));
        return modelAndView;
    }

    @GetMapping(path="/characterdef/{defid}")
    public ModelAndView thymeLeafCharacterDefByID(@PathVariable String defid) {
        Long id = Long.parseLong(defid);
        thingDefRecord.loadAllStats(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("CharacterPage.html");
        //Adds an attribute to the model as a name/value(object) pair
        modelAndView.addObject("thingstats", thingDefRecord);
        return modelAndView;
    }

    @GetMapping(path="/mobiletest/{defid}")
    public ModelAndView thymeLeafMobileTest(@PathVariable String defid) {
        int id = Integer.parseInt(defid);
        //thingDefRecord.loadAllStats(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("CollapseMobileTemplate.html");
        //Adds an attribute to the model as a name/value(object) pair
        modelAndView.addObject("thingstats", gameDataStore.getThingDefRecord(id));
        System.out.println(GameDataStore.getListCharacter());
        return modelAndView;
    }
    @GetMapping(path="/testpage/{defid}")
    public ModelAndView testPageByID(@PathVariable String defid) {
        Long id = Long.parseLong(defid);
        thingDefRecord.loadAllStats(id);
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("TestPage.html");
        //Adds an attribute to the model as a name/value(object) pair
        modelAndView.addObject("thingstats", thingDefRecord);
        return modelAndView;
    }

    @GetMapping("/fixednavbar")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("FixedNavbarExample.html");
        //Adds an attribute to the model as a name/value(object) pair
        //modelAndView.addObject("thingstats", thingDefRecord);
        return modelAndView;
    }
}
