package me.reckter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: mediacenter
 * Date: 08.11.13
 * Time: 16:27
 * To change this template use File | Settings | File Templates.
 */
public class RulesContainer {
    protected HashMap<String, Rule> rules;

    public RulesContainer(){
        rules = new HashMap<String, Rule>();
    }

    public void addRule(String url) throws IOException {
        url = getHost(url);
        Log.debug(url);
        if(!rules.containsKey(url)){
            Site site = new Site("http://" + url + "/robots.txt");
            addRule(url, site);
        }
    }

    public void addRule(String name, Site site) throws IOException {
        Log.verbose("fetching robot.txt for " + name);

        site.load();
        String html = site.getHTML();
        rules.put(name, new Rule(name, html));
    }

    protected String getHost(String url){
        return url.split("/")[0];
    }

    public boolean isBlocked(String url){
        if(rules.containsKey(getHost(url))){
           return rules.get(getHost(url)).isBlocked(url);
        }
        return false;
    }

    public boolean isAllowed(String url){
        return !isBlocked(url);
    }
}
