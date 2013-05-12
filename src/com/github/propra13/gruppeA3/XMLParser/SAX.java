package com.github.propra13.gruppeA3.XMLParser;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.*;
import org.xml.sax.*;
import java.io.File;

public class SAX {
	public static void parse (String file,DefaultHandler handler)
		throws Exception{
			SAXParserFactory.newInstance().newSAXParser().parse (new File (file), handler);
	}
}
