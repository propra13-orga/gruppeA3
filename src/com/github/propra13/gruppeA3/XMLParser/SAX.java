package com.github.propra13.gruppeA3.XMLParser;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.*;
import org.xml.sax.*;
import java.io.File;

/**
 * @author Majida Dere
 * Die Klasse SAX hat nur eine einzige Funktion, den SAX Parser zu erzeugen.
 */

public class SAX {
	
	/**
	 * Die Methode parse bekommt das zu bearbeitende XML File und einen Handler übergeben, 
	 * der bestimmt, was beim Auftreten von bestimmten XML-Konstrukten während des Durcharbeitens des Dokuments zu unternehmen ist.
	 * 
	 * @param file das einzulesende XML File
	 * @param handler Handler reagiert auf einzelne Ereignisse im XML File
	 * @throws Exception es wird ein neuer Parser erzeugt, nicht wie üblich mit "new Instanz", sondern mit einer Fabrik
	 */
	
	public static void parse (String file,DefaultHandler handler)
		throws Exception{
			SAXParserFactory.newInstance().newSAXParser().parse (new File (file), handler);
	}
}
