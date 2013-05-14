package com.github.propra13.gruppeA3.XMLParser;

import java.util.List;
import java.io.File;

/** Mit einen so genannten Handler kann spezifiziert werden, was beim Auftreten von bestimmten 
 * XML-Konstrukten während des Durcharbeiten des Dokuments zu unternehmen ist.
 */

public class SAXCrawlerReader {
	public String read(String fileName)
							throws Exception{
		CrawlerSAX handler=new CrawlerSAX();
		File pfad = new File("");
        System.out.println(pfad.getAbsolutePath());
		SAX.parse(fileName,handler);
		return handler.toString();
	}
	
}
