package com.github.propra13.gruppeA3.XMLParser;
import org.xml.sax.helpers.DefaultHandler;
import java.util.*;
import org.xml.sax.*;
import com.github.propra13.gruppeA3.Entities.*;
import com.github.propra13.gruppeA3.Map;

/**
 * 
 * @author Majida Dere
 *
 */
public class CrawlerSAX extends DefaultHandler{
//	public static Entities[][] map;
	public static String title;
	
	private int roomID;
	
	@Override
	/**
	 * Der SAX Crawler erzeugt f�r zwei Elementknoten zwei Ereignisse,
	 * eines wenn das �ffnende Tag des Elements gefunden ist,
	 * und eines beim SChlie�en des tags.
	 * Die Paramter
	 * @param uri,
	 * @param localName,
	 * @param qName,
	 * @param attrs,
	 * geben genaue Informationen �ber den Tagnamen.
	 */
	public void startElement (String uri, String localName, 
								String qName,Attributes attrs)
										throws SAXException {
		if(qName.equals("level")){
			//System.out.println(attrs.getValue("id"));
			title = new String(attrs.getValue("desc"));
			//System.out.println(attrs.getValue("player"));
		}
		else if (qName.equals("rooms")){
			roomID = Integer.parseInt(attrs.getValue("id"));
			/*System.out.println(attrs.getValue("length"));
			System.out.println(attrs.getValue("height"));*/
			//map = new Entities [Integer.parseInt(attrs.getValue("height"))][Integer.parseInt(attrs.getValue("length"))];
			
		}
		else if (qName.equals("walls")){
				int power= Integer.parseInt(attrs.getValue("power"));
				int type= Integer.parseInt(attrs.getValue("type"));
				int posx= Integer.parseInt(attrs.getValue("posx"));
				int posy= Integer.parseInt(attrs.getValue("posy"));
				
				Walls wall=new Walls(power,type,posx,posy);
				//map[posx][posy]=wall;		
		}
		else if(qName.equals("monster")){
				int size = Integer.parseInt(attrs.getValue("size"));
				double speed = Double.parseDouble(attrs.getValue("speed"));
				int power = Integer.parseInt(attrs.getValue("power"));
				int life = Integer.parseInt(attrs.getValue("life"));
				int type = Integer.parseInt(attrs.getValue("type"));
				int posx= Integer.parseInt(attrs.getValue("posx"));
				int posy= Integer.parseInt(attrs.getValue("posy"));
				
				Monster monster=new Monster(Map.mapRooms[roomID], speed, power, type, size, life, posx, posy);
		}
		
		else if(qName.equals("item")){
			int size = Integer.parseInt(attrs.getValue("size"));
			double speed = Double.parseDouble(attrs.getValue("speed"));
			int power = Integer.parseInt(attrs.getValue("power"));
			int life = Integer.parseInt(attrs.getValue("life"));
			int type = Integer.parseInt(attrs.getValue("type"));
			int posx= Integer.parseInt(attrs.getValue("posx"));
			int posy= Integer.parseInt(attrs.getValue("posy"));
			
			Monster monster=new Monster(Map.mapRooms[roomID], speed, power, type, size, life, posx, posy);
	}
	}
	
/**	@Override
	public void endElement(String uri,String localN,String qName)
										throws SAXException {
		
		
		if(qName.equals("rooms"))
		System.out.println("rooms: "+qName.toString());
			else if(qName.equals("entry"))
		System.out.println("entry: "+qName.toString());
			else if (qName.equals("exit"))
		System.out.println("exit: "+qName.toString());
			else if (qName.equals("walls"))
		System.out.println("walls: "+qName.toString());

	}
**/	
	
	
}	
