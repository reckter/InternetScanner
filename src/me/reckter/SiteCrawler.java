package me.reckter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: mediacenter
 * Date: 06.11.13
 * Time: 20:58
 * To change this template use File | Settings | File Templates.
 */
public class SiteCrawler {
    protected String startURL;
    protected LinkedList<String> toCrawlURLs;
    protected ArrayList<String> crawledURLs;

    public SiteCrawler(){
        startURL = "";
        toCrawlURLs = new LinkedList<String>();
        crawledURLs = new ArrayList<String>();
    }

    public void crawl(){
        Site site;
        String currentURL;
        while(toCrawlURLs.size() > 0){

            try {
                currentURL = toCrawlURLs.getFirst();
                Log.info("searching " + currentURL);

                site = new Site("http://" + currentURL);
                crawledURLs.add(currentURL.toLowerCase());
                if(crawledURLs.size() % 10 == 0){
                    Log.status("crawled " + crawledURLs.size() + " sites");
                }

                site.load();
                site.parseLinks();
                ArrayList<String> urlsFetched = site.getLinks();

                for(String urlFetched: urlsFetched){
                    if(!crawledURLs.contains(urlFetched.toLowerCase()) && !toCrawlURLs.contains(urlFetched.toLowerCase())){
                        if((toCrawlURLs.size() + crawledURLs.size()) % 10 == 0){
	                        if((toCrawlURLs.size() + crawledURLs.size()) % 100 == 0){
		                        Log.info("found " + (toCrawlURLs.size() + crawledURLs.size())+ " urls");
	                        } else {
                                Log.note("found " + (toCrawlURLs.size() + crawledURLs.size())+ " urls");
	                        }
                        }
                        toCrawlURLs.add(urlFetched.toLowerCase());
                        Log.verbose("fetched " + urlFetched + " from " + currentURL);
                    }
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            } catch (IOException e) {
                if(e instanceof FileNotFoundException){};
            }
            toCrawlURLs.removeFirst();
        }
	    Log.important("crawler endet!");
	    Log.important("with " + crawledURLs.size() + " fetched urls");
    }

    public void setStartURL(String startURL) {
        this.startURL = startURL;
        toCrawlURLs.add(startURL);
    }
}
