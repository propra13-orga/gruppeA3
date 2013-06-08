package com.github.propra13.gruppeA3.XMLParser;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.*;
import com.github.propra13.gruppeA3.Entities.*;
import com.github.propra13.gruppeA3.Map;

/**
 * @author Majida Dere
 * Die Klasse CrawlerSAX liest die XML Dateien ein und wirft der Reihe nach bestimmte Ereignisse aus. 
 * SAX bietet eine Standardimplementierung für Handler an, die hier genutzt wird.
 */

public class CrawlerSAX extends DefaultHandler{
	
	// Attribute
	
	//	public static Entities[][] map;
	public static String title;
	private int roomID;
	
	/**
	 * @author Majida Dere
	 * Konstruktor erzeugt einen Handler und weist die übergebene Map seiner privaten Map zu.
	 */
	public CrawlerSAX() {
		super();
	}
	
	// Methoden
	
	/**
	 * Der SAX Crawler erzeugt für zwei Elementknoten zwei Ereignisse,
	 * eines wenn das öffnende Tag des Elements gefunden ist, und eines beim SChließen des tags.
	 * Die Paramter
	 * @param uri,
	 * @param localName,
	 * @param qName,
	 * @param attrs,
	 * geben genaue Informationen über den Tagnamen.
	 */
	@Override
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
			//map = new Entities [Integer.parseInt(attrs.getValue("height"))][Integer.parseInt(attrs.getValue("length"))];
		}
		else if(qName.equals("monster")){
			//int size = Integer.parseInt(attrs.getValue("size"));
			double speed = Double.parseDouble(attrs.getValue("speed"));
			int power = Integer.parseInt(attrs.getValue("power"));
			int life = Integer.parseInt(attrs.getValue("life"));
			int type = Integer.parseInt(attrs.getValue("type"));
			int posx = Integer.parseInt(attrs.getValue("posx"));
			int posy = Integer.parseInt(attrs.getValue("posy"));
			String desc = new String(attrs.getValue("name"));

			// Es wird ein neues Monster erzeugt mit den zuvor ausgelesenen Informationen aus level.xml
			Monster monster=new Monster(Map.getMapRoom(roomID), speed, power, type, life, posx, posy, desc);
			//MAP
			//System.out.println("Monster");
			Map.getMapRoom(roomID).entities.add(monster);
			System.out.println(desc);
		}
		else if(qName.equals("item")){
			int damage = Integer.parseInt(attrs.getValue("damage"));
			int type = Integer.parseInt(attrs.getValue("type"));
			int posx = Integer.parseInt(attrs.getValue("posx"));
			int posy = Integer.parseInt(attrs.getValue("posy"));
			String desc = new String(attrs.getValue("desc"));

			Item item=new Item(Map.getMapRoom(roomID), damage, type, posx, posy, desc);

			Map.getMapRoom(roomID).entities.add(item);
			System.out.println(desc);
		}
	}
	
	@Override
	public void endElement(String uri,String localN,String qName)
										throws SAXException {
		
		
		/*if(qName.equals("rooms"))
		System.out.println("rooms: "+qName.toString());
		
		else if(qName.equals("level"))
		System.out.println("level: "+qName.toString());*/
		
		
		
		//	else if(qName.equals("entry"))
		//System.out.println("entry: "+qName.toString());
		//	else if (qName.equals("exit"))
		//System.out.println("exit: "+qName.toString());
		//	else if (qName.equals("walls"))
		//System.out.println("walls: "+qName.toString());

	}	
}	
