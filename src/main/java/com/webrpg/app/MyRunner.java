package com.webrpg.app;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.webrpg.app.dao.AttributeRepository;
import com.webrpg.app.dao.LoadConstants;
import com.webrpg.app.model.Attribute;
import com.webrpg.app.model.derived.RollRequest;
import com.webrpg.app.model.derived.ThingDefRecord;
import com.webrpg.app.service.QuantGenerator;
import com.webrpg.app.service.TerrainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import javax.transaction.Transactional;
import java.util.Arrays;

@Component
public class MyRunner implements CommandLineRunner {
    @Autowired
    private AttributeRepository attributeRepository;
    @Autowired
    private LoadConstants loadConstants;
    @Autowired
    ThingDefRecord thingDefRecord;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        //Load Constants from Database
        loadConstants.loadAllConstants();
        loadConstants.loadAbilityModifiers();
        TerrainService terrainService = new TerrainService();
        //terrainService.testPseudoRandSeq(204097005l);
        //terrainService.testAlphaValues("C:\\development\\maps\\AlphaTest.png");
        //terrainService.generateTerrain("C:\\development\\maps\\faerun.6.10.gif",6,10,1000,1000);
        terrainService.createSeaGradientPalette();
    }
}
