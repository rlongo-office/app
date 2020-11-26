$.fn.parseThingDef = function(tdObj){
    let tdTableText = "<table border='1'>";
    /*** row 1 ***/
    //private Long thingDefId;
    tdTableText += "<tr><td id='td_thingDefId'>tdID:" + tdObj["thingDefId"] + "</td>";
    //private BigDecimal age;
    tdTableText += "<td id='td_age'>Age:" + tdObj["age"] + "</td>";
    //private BigDecimal weight;
    tdTableText += "<td id='td_weight'>Weight:" + tdObj["weight"] + "</td>";
    //private BigDecimal height;
    tdTableText += "<td id='td_height'>Height:" + tdObj["height"] + "</td>";
    //private BigDecimal armorclass;
    tdTableText += "<td id='td_armorclass'>armorclass:" + tdObj["armorclass"] + "</td>";
    //private BigDecimal speed;
    tdTableText += "<td id='td_speed'>Speed:" + tdObj["speed"] + "</td>";
    //private BigDecimal gender;
    tdTableText += "<td id='td_gender'>Height:" + tdObj["gender"] + "</td>";
    //private BigDecimal hitpoints;
    tdTableText += "<td id='td_hitpoints'>HPs:" + tdObj["hitpoints"] + "</td>";
    //private String alignment;
    tdTableText += "<td id='td_alignment'>Alignment:" + tdObj["alignment"] + "</td>";
    tdTableText += "</tr>";
    //private List<Ability> abilities = new ArrayList<>();
    tdTableText += "<tr><td id='td_STR'>STR:" + tdObj["abilities"][0]["valScore"] + "</td>";
    tdTableText += "<td id='td_CON'>CON:" + tdObj["abilities"][1]["valScore"] + "</td>";
    tdTableText += "<td id='td_DEX'>DEX:" + tdObj["abilities"][2]["valScore"] + "</td>";
    tdTableText += "<td id='td_INT'>INT:" + tdObj["abilities"][3]["valScore"] + "</td>";
    tdTableText += "<td id='td_WIS'>WIS:" + tdObj["abilities"][4]["valScore"] + "</td>";
    tdTableText += "<td id='td_CHA'>CHA:" + tdObj["abilities"][5]["valScore"] + "</td>";
    tdTableText += "</tr>";
    //private Map<String, BigDecimal> species = new LinkedHashMap<>();
    //private Map<String, BigDecimal> skills = new LinkedHashMap<>();
    //private Map<String, BigDecimal> lores = new LinkedHashMap<>();
    //private Map<String, BigDecimal> bigFives = new LinkedHashMap<>();
    //private Map<String, BigDecimal> subFives = new LinkedHashMap<>();

    return tdTableText;
}