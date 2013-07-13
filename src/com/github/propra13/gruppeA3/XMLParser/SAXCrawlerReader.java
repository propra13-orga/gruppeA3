package com.github.propra13.gruppeA3.XMLParser;

/** 
 * @author Majida Dere
 * Mit einen so genannten Handler kann spezifiziert werden, was beim Auftreten von bestimmten 
 * XML-Konstrukten während des Durcharbeiten des Dokuments zu unternehmen ist.
 */

public class SAXCrawlerReader {
	
	private CrawlerSAX handler;
	
	/**
	 * @author Majida Dere
	 * Konstruktor erzeugt einen Reader
	 */
	public SAXCrawlerReader(){
		super();
	}
	
	public String read(String fileName)
							throws Exception{
		handler=new CrawlerSAX();
		SAX.parse(fileName,handler);
		return handler.toString();
	}

	/**
	 * @return the handler
	 */
	public CrawlerSAX getHandler() {
		return handler;
	}
}
