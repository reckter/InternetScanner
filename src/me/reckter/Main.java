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
        Log.setConsoleLevel(Log.INFO);
        SiteCrawler crawler = new SiteCrawler();
        crawler.setStartURL("wlp-systems.de");
        crawler.crawl();

    }
}
