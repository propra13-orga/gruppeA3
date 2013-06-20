package com.github.propra13.gruppeA3.XMLParser;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.*;

import com.github.propra13.gruppeA3.Entities.*;
import com.github.propra13.gruppeA3.Map.Map;

/**
 * @author Majida Dere
 * Die Klasse CrawlerSAX liest die XML Dateien ein und wirft der Reihe nach bestimmte Ereignisse aus. 
 * SAX bietet eine Standardimplementierung für Handler an, die hier genutzt wird.
 */

public class CrawlerSAX extends DefaultHandler{
	
	// Attribute
	
	//	public static Entities[][] map;
	//public static String title;
	private int roomID=0;
	private boolean checkNPC=false;
	private NPC npc=null;
	private String text=null;
	
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
	 * eines wenn das öffnende Tag des Elements gefunden ist, und eines beim Schließen des tags.
	 * Die Paramter
	 * @param uri, ist null
	 * @param localName, ist null
	 * @param qName, beinhaltet den String des Tags
	 * @param attrs, beinhaltet die Attribute eines Tags
	 * 
	 **/
	@Override
	public void startElement (String uri, String localName, 
								String qName,Attributes attrs)
										throws SAXException {
		if(qName.equals("level")){
			//System.out.println(attrs.getValue("id"));
			//title = new String(attrs.getValue("desc"));
			//System.out.println(attrs.getValue("player"));
		}
		else if (qName.equals("rooms")){
			roomID = Integer.parseInt(attrs.getValue("id"));

		}
		else if(qName.equals("monster")){
			//int size = Integer.parseInt(attrs.getValue("size"));
			double speed = Double.parseDouble(attrs.getValue("speed"));
			int power = Integer.parseInt(attrs.getValue("power"));
			int life = Integer.parseInt(attrs.getValue("life"));
			int type = Integer.parseInt(attrs.getValue("type"));
			int posx = Integer.parseInt(attrs.getValue("posx"))*32;
			int posy = Integer.parseInt(attrs.getValue("posy"))*32;
			String desc = new String(attrs.getValue("name"));
			int coinValue = Integer.parseInt(attrs.getValue("coinValue"));
			int coinType = Integer.parseInt(attrs.getValue("coinType"));
			int armour = Integer.parseInt(attrs.getValue("armour"));

			
			//Boss-Monster regeln
			boolean isBoss = false;
			if(desc.equals("Bossi"))
				isBoss = true;
			else
				isBoss = false;

			// Es wird ein neues Monster erzeugt mit den zuvor ausgelesenen Informationen aus level.xml
			Monster monster=new Monster(Map.getMapRoom(roomID), speed, power, type, life, posx, posy, desc, coinValue, coinType, armour, isBoss);
			//MAP
			//System.out.println("Monster");
			Map.getMapRoom(roomID).entities.add(monster);

		}
		else if(qName.equals("item")){
			int damage = Integer.parseInt(attrs.getValue("damage"));
			int type = Integer.parseInt(attrs.getValue("type"));
			int posx = Integer.parseInt(attrs.getValue("posx"))*32;
			int posy = Integer.parseInt(attrs.getValue("posy"))*32;
			String desc = new String(attrs.getValue("desc"));
			String name = new String(attrs.getValue("name"));
			int value = Integer.parseInt(attrs.getValue("value"));

			Item item=new Item(Map.getMapRoom(roomID), damage, type, posx, posy, desc, name, value);
			
			if(checkNPC){
				npc.getItems().add(item);
			}else
				Map.getMapRoom(roomID).entities.add(item);
		}
		else if(qName.equals("npc")){
			checkNPC = true;
			int type = Integer.parseInt(attrs.getValue("type"));
			int posx = Integer.parseInt(attrs.getValue("posx"))*32;
			int posy = Integer.parseInt(attrs.getValue("posy"))*32;
			String desc = new String(attrs.getValue("desc"));
			String name = new String(attrs.getValue("name"));
			
			npc = new NPC(Map.getMapRoom(roomID), type, desc, name, posx, posy);
		}
	}
	
	
	
	@Override
	public void endElement(String uri,String localN,String qName)
										throws SAXException {
		
		if(qName.equals("text")){
	    	if(true==checkNPC && !text.equals("")){
	    		npc.setText(text);
	    	}
		} else if(qName.equals("npc")){
			checkNPC=false;
			Map.getMapRoom(roomID).entities.add(npc);
		}

	}	
	
    @Override
    public void characters(char ch[], int start, int length){
    	this.text = new String(ch, start, length);
    	this.text = this.text.replace("\\n", "\n");
     }
}	
