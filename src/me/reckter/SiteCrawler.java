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
    protected volatile LinkedList<String> toCrawlURLs;
    protected ArrayList<String> crawledURLs;
	protected LinkParser parser;

    public SiteCrawler(){
        startURL = "";
        toCrawlURLs = new LinkedList<String>();
        crawledURLs = new ArrayList<String>();
	    parser = new LinkParser(this);
    }

	public void addUrl(String url){
		if(!crawledURLs.contains(url.toLowerCase()) && !toCrawlURLs.contains(url.toLowerCase())){
			if((toCrawlURLs.size() + crawledURLs.size()) % 10 == 0){
				if((toCrawlURLs.size() + crawledURLs.size()) % 100 == 0){
					Log.info("found " + (toCrawlURLs.size() + crawledURLs.size())+ " urls");
				} else {
					Log.note("found " + (toCrawlURLs.size() + crawledURLs.size())+ " urls");
				}
			}
			toCrawlURLs.add(url.toLowerCase());
			Log.verbose("fetched " + url + " from " + url);
		}
	}

    public void crawl(){
        Site site;
        String currentURL;
	    do {
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
	                parser.addSites(site);

	            } catch (MalformedURLException e) {
	                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
	            } catch (IOException e) {
	                if(e instanceof FileNotFoundException){};
	            }
	            toCrawlURLs.removeFirst();
	        }
		    try {
			    Thread.sleep(1000);
		    } catch (InterruptedException e) {
			    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		    }
	    } while (parser.hasSitesToParse() || toCrawlURLs.size() != 0);
	    Log.important("crawler endet!");
	    Log.important("with " + crawledURLs.size() + " fetched urls");
    }

    public void setStartURL(String startURL) {
        this.startURL = startURL;
        toCrawlURLs.add(startURL);
    }
}
