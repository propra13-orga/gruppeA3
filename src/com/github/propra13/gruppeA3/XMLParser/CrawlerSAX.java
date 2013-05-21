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
	private Map map;
	
	/**
	 * @author Majida Dere
	 * Konstruktor erzeugt einen Handler und weist die übergebene Map seiner privaten zu.
	 */
	public CrawlerSAX(Map map) {
		super();
		this.map = map;
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
			/*System.out.println(attrs.getValue("length"));
			System.out.println(attrs.getValue("height"));*/
			//map = new Entities [Integer.parseInt(attrs.getValue("height"))][Integer.parseInt(attrs.getValue("length"))];
			
		}
		else if (qName.equals("walls")){
				int power= Integer.parseInt(attrs.getValue("power"));
				int type= Integer.parseInt(attrs.getValue("type"));
				int posx= Integer.parseInt(attrs.getValue("posx"));
				int posy= Integer.parseInt(attrs.getValue("posy"));
				
				// Es wird eine neue Wand erzeugt mit den zuvor ausgelesenen Informationen aus level.xml
				Walls wall=new Walls(map.getMapRoom(roomID),power,type,posx,posy);
				//map[posx][posy]=wall;		
				
				//map.getMapRoom(roomID).entities.add(wall);

		}
		
		else if(qName.equals("monster")){
				int size = Integer.parseInt(attrs.getValue("size"));
				double speed = Double.parseDouble(attrs.getValue("speed"));
				int power = Integer.parseInt(attrs.getValue("power"));
				int life = Integer.parseInt(attrs.getValue("life"));
				int type = Integer.parseInt(attrs.getValue("type"));
				int posx= Integer.parseInt(attrs.getValue("posx"));
				int posy= Integer.parseInt(attrs.getValue("posy"));
				
				// Es wird ein neues Monster erzeugt mit den zuvor ausgelesenen Informationen aus level.xml
				Monster monster=new Monster(map.getMapRoom(roomID), speed, power, type, size, life, posx, posy);
			
				//MAP
				
				map.getMapRoom(roomID).entities.add(monster);

		}
		
		
		
		else if(qName.equals("item")){
			int size = Integer.parseInt(attrs.getValue("size"));
			double speed = Double.parseDouble(attrs.getValue("speed"));
			int power = Integer.parseInt(attrs.getValue("power"));
			int life = Integer.parseInt(attrs.getValue("life"));
			int type = Integer.parseInt(attrs.getValue("type"));
			int posx= Integer.parseInt(attrs.getValue("posx"));
			int posy= Integer.parseInt(attrs.getValue("posy"));
			
			//Item item=new Item(map.getMapRoom(roomID), speed, power, type, size, life, posx, posy);
			
			//map.getMapRoom(roomID).entities.add(item);
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
