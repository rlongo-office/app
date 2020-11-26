package com.webrpg.app.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.webrpg.app.dao.LoadConstants;
import com.webrpg.app.model.Attribute;
import com.webrpg.app.model.derived.RollRequest;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class QuantGenerator{
    private String SEMICOLON_DELIMITER = ";";
    public QuantGenerator(){}

    /*
    We need to accept different roll requests. For now, Ability, Skill, Damage and General.  This method will take a
    json string, parse it, make the necessary dice rolls, and return the result
     */
    public String rollDice(String request) throws JsonProcessingException, NoSuchProviderException, NoSuchAlgorithmException {
        ArrayList<DiceSeq> arrDieSeq = new ArrayList<>();
        int count;
        int modifier;

        //Get the RollRequest object from the passed json string
        SecureRandom diceRoller = new SecureRandom();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = mapper.readTree(request);
        String category = mapper.convertValue(node.get("category"), String.class);
        String sequence = mapper.convertValue(node.get("sequence"), String.class);
        Integer value = mapper.convertValue(node.get("value"), Integer.class);
        //parse the sequence for the dice roll
        String token;
        //Split the sequence at semicolon
        String[] tokens = sequence.split(SEMICOLON_DELIMITER);		//effectively tokenizes the string
        for (int x = 0; x<tokens.length; x++){
            count = 0;
            token = tokens[x];
            arrDieSeq.add(new DiceSeq());
            Pattern numPattern = Pattern.compile("\\d+");
            //A die roll with follow the model of nDx+c where n=number of dice, x=size of dice, c = any constant
            //For example 3D6+3 rolls 3 six sided dice and adds 3
            Matcher matcher = numPattern.matcher(token);
            while(matcher.find()){
                count++;
                //Convert the parsed string to integer values for the DicSeq class members
                switch(count){
                    case 1: arrDieSeq.get(x).setQuantity(Integer.parseInt(matcher.group())); break;          //number of dice
                    case 2: arrDieSeq.get(x).setDie(Integer.parseInt(matcher.group())); break;               //dice size
                    case 3:  arrDieSeq.get(x).setConstant(Integer.parseInt(matcher.group())); break;            //constant
                }
            }
        }
        //Some
        switch(category){
            case "Ability": modifier = LoadConstants.abilityMods[value]; break;
            default: modifier = 0;
        }
        return rollDiceSeq(arrDieSeq, modifier);
    }

    public String rollDiceSeq(List<DiceSeq> arrDieSeq, int modifier) throws NoSuchProviderException, NoSuchAlgorithmException {
        //SecureRandom secRand = new SecureRandom();
        SecureRandom secRand = SecureRandom.getInstance("SHA1PRNG", "SUN");
        Date date = new Date(System.currentTimeMillis());
        byte[] b = date.toString().getBytes();
        secRand.nextBytes(b);
        int diceSum = 0;
        for (DiceSeq diceSeq: arrDieSeq){
            for (int n=1; n<=diceSeq.getQuantity(); n++){
                diceSum += secRand.nextInt(diceSeq.getDie())+1;
            }
            diceSum += diceSeq.getConstant();
        }
        diceSum += modifier;
        return String.valueOf(diceSum);
    }


    class DiceSeq{
        private int quantity;
        private int die;
        private int constant;

        public DiceSeq() {
        }
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        public int getDie() { return die; }
        public void setDie(int die) { this.die = die; }

        public int getConstant() { return constant; }
        public void setConstant(int constant) { this.constant = constant; }

        @Override
        public String toString() {
            return "DiceSeq{" +
                    "quantity=" + quantity +
                    ", die=" + die +
                    ", constant=" + constant +
                    '}';
        }
    }
}
