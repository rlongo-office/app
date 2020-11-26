package com.webrpg.app.controller;

import com.webrpg.app.dao.AttributeRepository;
import com.webrpg.app.model.Attribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @Autowired
    AttributeRepository attributeRepository;

    @GetMapping("/")
    public String index() {
        return "index";
    }

    private void insertRecord() {
        Attribute attribute;
        System.out.println("Attempting to insert record");
        attribute = attributeRepository.save(new Attribute("material",false,null));
        System.out.println("Attribute: "+ attribute.getName()+ ":"+ attribute.getHasvalue());
    }
}