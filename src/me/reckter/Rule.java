package me.reckter;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: mediacenter
 * Date: 08.11.13
 * Time: 16:20
 * To change this template use File | Settings | File Templates.
 */
public class Rule {
    protected ArrayList<String> rules;
    protected String host;

    public Rule(ArrayList<String> rules){
        this.rules = rules;
    }

    public Rule(String host, ArrayList<String> rules){
        this.host = host;
        this.rules = rules;
    }

    public Rule(String host, String robotsTxt){
        this.host = host;
        rules = new ArrayList<String>();

        String[] lines = robotsTxt.split("\n");
        for(String line: lines){
            if(line.startsWith("Disallow:")){
                if(line.contains("/")){
                    rules.add(line.substring(10, line.length()).split(" ")[0]);
                }
            }
        }
    }


    public boolean isBlocked(String url){
        for(String rule: rules){
            if(url.contains(rule)){
                return true;
            }
        }
        return false;
    }

    public boolean isAllowed(String url){
        return !isBlocked(url);
    }

}
