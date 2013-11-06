package me.reckter;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: mediacenter
 * Date: 06.11.13
 * Time: 20:01
 * To change this template use File | Settings | File Templates.
 */
public class Site {
    protected URL url;
    protected String urlString;
    private boolean isLoaded = false;
    protected String HTML;
    protected ArrayList<String> links;

    public Site(String url) throws MalformedURLException {
        this.url = new URL(url);
        this.urlString = url;
        this.links = new ArrayList<String>();
    }

    protected boolean isEndCharacter(char a){
        if(a == ' ' || a == '\"' || a == '\'' || a == '<' || a == '>' || a == ')' || a == '(' || a == ']' || a == '[' || a == '{' || a == '}' || a == '#') {
            return true;
        }
        return false;
    }

    public void load() throws IOException {

        URLConnection connection = url.openConnection();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream()));
        String inputLine;
        String response = "";

        while ((inputLine = in.readLine()) != null){
            response += inputLine;
        }

        in.close();


        HTML = response.toString();

        isLoaded = true;
    }

    public void parseLinks(){
        String[] parts = HTML.split("http://");
        for(String part: parts){
            for(int i = 0; i < part.length(); i++){
                if(isEndCharacter((part.charAt(i)))){
                    if(i >= 4){
                        String matchingUrl = part.substring(0, i);
                        if(matchingUrl.contains(".")){
                            links.add(part.substring(0, i));
                        }
                    }
                    i = part.length();
                }
            }
        }
    }

    public String getUrl() {
        return urlString;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public String getHTML() {
        return HTML;
    }

    public ArrayList<String> getLinks() {
        return links;
    }
}
