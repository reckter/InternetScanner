package me.reckter;

import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created with IntelliJ IDEA.
 * User: mediacenter
 * Date: 06.11.13
 * Time: 19:58
 * To change this template use File | Settings | File Templates.
 */
public class Main {

    static public void main(String[] args){
        Log.setConsoleLevel(Log.DEBUG);
	    Log.setShowTimeSinceStart(true);
	    Log.setShowModule(false);
	    Log.setShowVerboseLevel(false);

        SiteCrawler crawler = new SiteCrawler();
	    try {
		    crawler.pullState();
	    } catch (IOException e) {
		    e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
	    }
	    crawler.crawl();


    }
}
