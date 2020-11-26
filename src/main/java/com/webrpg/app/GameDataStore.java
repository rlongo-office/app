package com.webrpg.app;

import com.webrpg.app.dao.ThingdefRepository;
import com.webrpg.app.dao.ThingdefstatsviewRepository;
import com.webrpg.app.model.Thingdef;
import com.webrpg.app.model.Thingdefstatsview;
import com.webrpg.app.model.derived.ThingDefRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class GameDataStore {

    static List<ThingDefRecord> listCharacter;
    @Autowired
    ThingdefRepository thingdefRepo;
    @Autowired
    ThingdefstatsviewRepository defstatviewRepo;

    public GameDataStore(){
        listCharacter = new ArrayList<>();
    }

    public static List<ThingDefRecord> getListCharacter() {
        return listCharacter;
    }

    public static void setListCharacter(List<ThingDefRecord> listCharacter) {
        GameDataStore.listCharacter = listCharacter;
    }

    public GameDataStore(List<ThingDefRecord> listCharacter){
         this.listCharacter = listCharacter;
    }

    /* In the future, we'll need a more sophisticated way to store 'things' we pull into the resident game, and then
    either sort them or use a map to easily retrieve when we need their stats.  For now, we are pulling the records
    sequentially by id, so access will be simply by the ArrayList index for now.
     */

    public ThingDefRecord getThingDefRecord(int id){
        //Check if record already in the current maintained list of records
        int arrIndex = searchThingDefList(id);
        if (arrIndex>=0) return listCharacter.get(arrIndex);
        //If not load the record from the database
            //Add the new record to the list of records
            //Sort the list
        //Return the requested record
        return insertNewThingDef(id);
    }
    public void loadAllDefRecords(){
        //For test purposes load all the thing def records in the database.  Find the count, iterate, an pull in the stats view the viewRepo
        for (int i = 1; i<(thingdefRepo.count())+1; i++){
            listCharacter.add(new ThingDefRecord((long) i, defstatviewRepo.findByDefid(i)));
        }
    }

    private ThingDefRecord insertNewThingDef(int id){
        ThingDefRecord newRecord = new ThingDefRecord((long) id, defstatviewRepo.findByDefid(id));
        listCharacter.add(newRecord);
        Collections.sort(listCharacter);
        return newRecord;
    }

    //Using simply Binary Search to find an id in the list of Things
    //List has to be sorted ascending first (on ThingDefID) for this to work
    private int searchThingDefList(int id){
        int firstIndex = 0;
        int lastIndex = listCharacter.size()-1;

        // termination condition (element isn't present)
        while(firstIndex <= lastIndex) {
            int middleIndex = (firstIndex + lastIndex) / 2;
            // if the middle element is our goal element, return its index
            if (listCharacter.get(middleIndex).getThingDefId() == id) {
                return middleIndex;
            }
            // if the middle element is smaller
            // point our index to the middle+1, taking the first half out of consideration
            else if (listCharacter.get(middleIndex).getThingDefId() < id)
                firstIndex = middleIndex + 1;
                // if the middle element is bigger
                // point our index to the middle-1, taking the second half out of consideration
            else if (listCharacter.get(middleIndex).getThingDefId() > id)
                lastIndex = middleIndex - 1;
        }
        return -1;
    }
}
