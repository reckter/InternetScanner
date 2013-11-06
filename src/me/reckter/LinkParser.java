package me.reckter;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: reckter
 * Date: 11/6/13
 * Time: 11:21 PM
 * To change this template use File | Settings | File Templates.
 */
public class LinkParser {
	protected LinkedList<Site> sites;
	protected SiteCrawler crawler;

	public LinkParser(SiteCrawler crawler){
		this.crawler = crawler;
		sites = new LinkedList<Site>();
		ParseThread parser = new ParseThread();
		parser.start();
	}

	public void addSites(Site site){
		sites.add(site);
	}

	public boolean hasSitesToParse(){
		return sites.size() > 0;
	}

	protected class ParseThread extends Thread {
		@Override
		public void run(){
			while(true){
				if(sites.size() == 0)
				{
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
					}
				}
				while(sites.size() > 0){
					Site site = sites.getFirst();
					site.parseLinks();
					ArrayList<String> urlsFetched = site.getLinks();

					for(String urlFetched: urlsFetched){
						crawler.addUrl(urlFetched);
					}
					sites.removeFirst();
				}

			}
		}
	}
}
