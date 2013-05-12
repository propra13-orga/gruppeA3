package com.github.propra13.gruppeA3.XMLParser;
import org.xml.sax.helpers.DefaultHandler;
import java.util.*;
import org.xml.sax.*;
import com.github.propra13.gruppeA3.Entities.*;


public class CrawlerSAX extends DefaultHandler{
	public static Entities[][] map;
	
	@Override
	public void startElement (String uri, String localName, 
								String qName,Attributes attrs)
										throws SAXException {
		if(qName.equals("level")){
			System.out.println(attrs.getValue("id"));
			System.out.println(attrs.getValue("desc"));
			System.out.println(attrs.getValue("player"));
		}
		else if (qName.equals("rooms")){
			System.out.println(attrs.getValue("id"));
			System.out.println(attrs.getValue("length"));
			System.out.println(attrs.getValue("height"));
			map = new Entities [Integer.parseInt(attrs.getValue("length"))][Integer.parseInt(attrs.getValue("height"))];

		}
		else if (qName.equals("walls")){
				int power= Integer.parseInt(attrs.getValue("power"));
				int type= Integer.parseInt(attrs.getValue("type"));
				int posx= Integer.parseInt(attrs.getValue("posx"));
				int posy= Integer.parseInt(attrs.getValue("posy"));
				
				Walls wall=new Walls(power,type,posx,posy);
				map[posx][posy]=wall;
		
		}

	}
	
	@Override
	public void endElement(String uri,String localN,String qName)
										throws SAXException {
		
		/**
		if(qName.equals("rooms"))
		System.out.println("rooms: "+qName.toString());
			else if(qName.equals("entry"))
		System.out.println("entry: "+qName.toString());
			else if (qName.equals("exit"))
		System.out.println("exit: "+qName.toString());
			else if (qName.equals("walls"))
		System.out.println("walls: "+qName.toString());
**/
	}
	
	
	
}	
