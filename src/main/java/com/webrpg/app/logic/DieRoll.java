package com.webrpg.app.logic;

import com.webrpg.app.model.derived.RollRequest;

import java.util.Random;
import java.security.SecureRandom;

public class DieRoll {
    String request;
    String result;
    RollRequest rollRequest;

    public DieRoll(String request) {
        this.request = request;
    }

    public DieRoll(RollRequest rollRequest) {
        this.rollRequest = rollRequest;
        this.result = rollDie(rollRequest);
    }


    private String rollDieBasic(String request) {
        String result = new String("You rolled a 20 for " + request);
        return result;
    }

    private String rollDie(RollRequest rollRequest){
        //int modifier = rollRequest.getModifier().intValue();
        int modifier = 0;
        //String splitRequest[] = request.split("\\.",3);
        SecureRandom diceRoller = new SecureRandom();
        int roll = diceRoller.nextInt(20);
        String result = new String("You rolled a " + Integer.toString(roll) + " for " + rollRequest.getAttribute());
        //String result = new String("You rolled a " + Integer.toString(roll));
        return result;
    }


    public String getRequest() {
        return request;
    }
    public void setRequest(String request) {
        this.request = request;
    }

    public String getResult() { return result; }
    public void setResult(String result) { this.result = result; }

    public RollRequest getRollRequest() { return rollRequest; }
    public void setRollRequest(RollRequest rollRequest) { this.rollRequest = rollRequest; }
}
