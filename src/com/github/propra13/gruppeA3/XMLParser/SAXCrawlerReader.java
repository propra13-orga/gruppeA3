package com.github.propra13.gruppeA3.XMLParser;

import java.io.File;
import com.github.propra13.gruppeA3.Map;

/** 
 * @author Majida Dere
 * Mit einen so genannten Handler kann spezifiziert werden, was beim Auftreten von bestimmten 
 * XML-Konstrukten während des Durcharbeiten des Dokuments zu unternehmen ist.
 */

public class SAXCrawlerReader {
	
	/**
	 * @author Majida Dere
	 * Konstruktor erzeugt einen Reader und weist die übergebene Map seiner privaten Map zu, um sie dann an den Händler zu übergeben.
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
