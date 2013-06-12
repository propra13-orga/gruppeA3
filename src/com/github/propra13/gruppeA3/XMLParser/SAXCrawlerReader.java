package com.github.propra13.gruppeA3.XMLParser;

import java.io.File;

import com.github.propa13.gruppeA3.Map.Map;

/** 
 * @author Majida Dere
 * Mit einen so genannten Handler kann spezifiziert werden, was beim Auftreten von bestimmten 
 * XML-Konstrukten während des Durcharbeiten des Dokuments zu unternehmen ist.
 */

public class SAXCrawlerReader {
	
	/**
	 * @author Majida Dere
	 * Konstruktor erzeugt einen Reader
	 */
	public SAXCrawlerReader(){
		super();
	}
	
	public String read(String fileName)
							throws Exception{
		// übergibt die Map an den handler
		CrawlerSAX handler=new CrawlerSAX();
		SAX.parse(fileName,handler);
		return handler.toString();
	}
	
}
