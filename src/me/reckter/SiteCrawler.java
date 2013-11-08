package me.reckter;

import javax.print.DocFlavor;
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
    protected RulesContainer rules;

	protected long time;
	public static long TIME_TO_PUSH = 20 * 1000;

	protected String host = "http://brony.me/crawler/index.php";

    public SiteCrawler(){
        startURL = "";
        toCrawlURLs = new LinkedList<String>();
        crawledURLs = new ArrayList<String>();
	    parser = new LinkParser(this);
        rules = new RulesContainer();
    }


	public void pushState() throws IOException {
		Log.status("pushing state");
		Log.info("preparing parsed links");
		String links = "";
		for(String link: toCrawlURLs){
			links += link + "|";
		}

		Site parseSite = new Site(host);
		parseSite.setMethod("POST");
		parseSite.setUrlParameters("type=parsed&links=" + links);
		Log.debug(links);
		Log.info("pushing parsed links");
		parseSite.load();


		Log.info("preparing crawled links");
		links = "";
		for(String link: crawledURLs){
			links += link + "|";
		}

		parseSite = new Site(host);
		parseSite.setMethod("POST");
		parseSite.setUrlParameters("type=crawled&links=" + links);

		Log.info("pushing crawled links");
		parseSite.load();

		Log.info("done.");
	}

	public void pullState() throws IOException {

		Log.status("pulling state");

		Log.info("pulling parsed links");

		Site pullSite = new Site(host);
		pullSite.setMethod("GET");
		pullSite.setUrlParameters("get=1&type=parsed");

		pullSite.load();

        Log.debug(pullSite.getHTML());

		Log.info("parsing pulled parsed links");
		parser.addSites(pullSite);

		Log.info("pulling crawled links");

		Site pullSite2 = new Site(host);
		pullSite2.setMethod("GET");
		pullSite2.setUrlParameters("get=1&type=crawled");

		pullSite2.load();
		pullSite2.parseLinks();
		Log.info("parsing pulled crawled links");

		for(String url: pullSite2.getLinks()){
            crawledURLs.add(url);
        }

        Log.debug(String.valueOf(crawledURLs.size()));
        for(String url:crawledURLs){
            Log.debug(url);
        }

		Log.info("done.");



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
			Log.verbose("fetched " + url);
		}
	}

    public void crawl(){
        Site site;
        String currentURL;
	    time = System.currentTimeMillis();
	    do {
		    if(System.currentTimeMillis() - time >= TIME_TO_PUSH){
			    time = System.currentTimeMillis();
			    while(parser.hasSitesToParse()){
				    try {
					    Thread.sleep(100);
				    } catch (InterruptedException e) {
					    e.printStackTrace();
				    }
			    }
			    try {
				    pushState();
			    } catch (IOException e) {
				    e.printStackTrace();
			    }
		    }
	        if(toCrawlURLs.size() > 0){
	            try {

	                currentURL = toCrawlURLs.getFirst();
                    rules.addRule(currentURL);

                    if(rules.isAllowed(currentURL)){
                        Log.info("searching " + currentURL);

                        site = new Site("http://" + currentURL);
                        crawledURLs.add(currentURL.toLowerCase());
                        if(crawledURLs.size() % 10 == 0){
                            Log.status("crawled " + crawledURLs.size() + " sites");
                        }

                        site.load();
                        parser.addSites(site);
                    } else {
                        Log.verbose("robots.txt denied the crawling of " + currentURL);
                    }
	            } catch (MalformedURLException e) {
	                e.printStackTrace();
	            } catch (IOException e) {
	                if(e instanceof FileNotFoundException){};
	            }
	            toCrawlURLs.removeFirst();
	        }
		    try {
			    Thread.sleep(1000);
		    } catch (InterruptedException e) {
			    e.printStackTrace();
		    }
	    } while (parser.hasSitesToParse() || toCrawlURLs.size() != 0);
	    Log.important("crawler endet!");
	    Log.important("with " + crawledURLs.size() + " fetched urls");
	    try {
		    pushState();
	    } catch (IOException e) {
		    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
	    }
    }

    public void setStartURL(String startURL) {
        this.startURL = startURL;
        toCrawlURLs.add(startURL);
    }
}
