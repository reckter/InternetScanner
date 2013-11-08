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
	protected String urlParameters = "";
	protected String method = "GET";

    public Site(String url) throws MalformedURLException {
        this.url = new URL(url);
        this.urlString = url;
        this.links = new ArrayList<String>();
    }


    protected boolean isEndCharacter(char a){
        if(a == ' ' || a == '|' || a == '\"' || a == '\'' || a == '<' || a == '>' || a == ')' || a == '(' || a == ']' || a == '[' || a == '{' || a == '}' || a == '#') {
            return true;
        }
        return false;
    }

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getUrlParameters() {
		return urlParameters;
	}

	public void setUrlParameters(String urlParameters) {
		this.urlParameters = urlParameters;
	}

	public void load() throws IOException {

		if(method == "GET"){
			url = new URL(urlString + "?" + urlParameters);
		}

	    HttpURLConnection con = (HttpURLConnection) url.openConnection();

	    //add reuqest header
	    con.setRequestMethod(method);
	    //con.setRequestProperty("User-Agent", USER_AGENT);

	    // Send post request
		if(method == "POST"){
		    con.setDoOutput(true);
		    DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		    wr.writeBytes(urlParameters);
		    wr.flush();
		    wr.close();
		}

	    BufferedReader in = new BufferedReader(
			    new InputStreamReader(con.getInputStream()));
	    String inputLine;
	    StringBuffer response = new StringBuffer();

	    while ((inputLine = in.readLine()) != null) {
		    response.append(inputLine + "\n");
	    }
	    in.close();

		HTML = response.toString();

	        isLoaded = true;
    }

    public void parseLinks(){
	    if(isLoaded){
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
    }

    public String getUrl() {
        return urlString;
    }

    public boolean isLoaded() {
        return isLoaded;
    }

    public String getHTML() {
	    if(isLoaded)
	        return HTML;
	    return "";
    }

    public ArrayList<String> getLinks() {
        return links;
    }
}
