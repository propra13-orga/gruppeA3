package com.github.propra13.gruppeA3.XMLParser;

import java.io.File;
import com.github.propra13.gruppeA3.Map;

/** 
 * @author Majida Dere
 * Mit einen so genannten Handler kann spezifiziert werden, was beim Auftreten von bestimmten 
 * XML-Konstrukten während des Durcharbeiten des Dokuments zu unternehmen ist.
 */

public class SAXCrawlerReader {
	
	private Map map;
	
	/**
	 * @author Majida Dere
	 * Konstruktor erzeugt einen Reader und weist die übergebene Map seiner privaten zu.
	 */
	public SAXCrawlerReader(Map map){
		super();
		this.map = map;
	}
	
	public String read(String fileName)
							throws Exception{
		// übergibt die Map an den handler
		CrawlerSAX handler=new CrawlerSAX(map);
		File pfad = new File("");
        System.out.println(pfad.getAbsolutePath());
		SAX.parse(fileName,handler);
		return handler.toString();
	}
	
}
