package com.github.propra13.gruppeA3.XMLParser;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class DOM {

	/**
	 * Liest eine xml-Datei aus.
	 * @param xmlName Name der xml-Datei, die ausgelesen werden soll.
	 * @return Gibt ein DOM-Document zurück.
	 */
	public static Document readFile(String xmlName) {
		DocumentBuilder builder;
		Document document = null;
		try {
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			document = builder.parse(new File(xmlName));
		} catch (ParserConfigurationException | SAXException | IOException e) {
			e.printStackTrace();
		}
		return document;
	}
	
	/**
	 * Schreibt eine xml-Datei.
	 * @param doc DOM-Document, das in die Datei abgebildet werden soll.
	 * @param xmlName Name der xml-Datei, die erzeugt werden soll.
	 */
	public static void writeFile(Document doc, String xmlName) {
		
	}
}
