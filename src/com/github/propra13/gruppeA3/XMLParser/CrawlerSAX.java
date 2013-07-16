package com.github.propra13.gruppeA3.XMLParser;
import org.xml.sax.helpers.DefaultHandler;
import org.xml.sax.*;

import com.github.propra13.gruppeA3.Entities.*;
import com.github.propra13.gruppeA3.Map.Field;
import com.github.propra13.gruppeA3.Map.FieldPosition;
import com.github.propra13.gruppeA3.Map.Link;
import com.github.propra13.gruppeA3.Map.Map;
import com.github.propra13.gruppeA3.Map.Room;

/**
 * @author Majida Dere
 * Die Klasse CrawlerSAX liest die XML Dateien ein und wirft der Reihe nach bestimmte Ereignisse aus. 
 * SAX bietet eine Standardimplementierung für Handler an, die hier genutzt wird.
 */

public class CrawlerSAX extends DefaultHandler{
	
	/**
	 * Attribute:
	 * 		title: Titelbeschreibung des Levels
	 * 		levelID: Die ID des levels
	 * 		playerCount: Anzahl der Spieler im Level
	 * 		roomID: Die ID des Raumes
	 * 		checkNPC: Wird dazu verwendet um festzustellen, ob ein NPC Element aufgemacht wurde
	 * 		npc: Ein NPC
	 * 		text: Der Text des NPCs
	 */
	
	//	public static Entities[][] map;
	private String title;
	private int levelID;
	private int playerCount;
	private int playerID;
	private int roomCount;
	private int roomID=0;
	private boolean checkNPC=false;
	private NPC npc=null;
	private String text=null;
	private Player[] player=null;
	private Field[][] fields;
	private Field field;
	private FieldPosition fieldPos;
	private boolean isFluss;
	private String flussrichtung;
	private boolean isCheckpointLink;
	private int linkID, linkZiel;
	private boolean isCheckpoint;
	
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
			setLevelID(Integer.parseInt(attrs.getValue("id")));
			setTitle(new String(attrs.getValue("desc")));
			setPlayerCount(Integer.parseInt(attrs.getValue("player")));
			roomCount = Integer.parseInt(attrs.getValue("rooms"));
			Map.mapRooms = new Room[roomCount];
			this.player = new Player[playerCount];
			playerID = 0;
		}
		else if (qName.equals("rooms")){
			roomID = Integer.parseInt(attrs.getValue("id"));
			fields = new Field[Map.ROOMWIDTH][Map.ROOMHEIGHT];
			Map.mapRooms[roomID] = new Room(roomID);
			Map.getRoom(roomID).roomFields = fields;
		}
		else if(qName.equals("monster")){
			//int size = Integer.parseInt(attrs.getValue("size"));
			double speed = Double.parseDouble(attrs.getValue("speed"));
			int power = Integer.parseInt(attrs.getValue("power"));
			int life = Integer.parseInt(attrs.getValue("life"));
			int type = Integer.parseInt(attrs.getValue("type"));
			int x = Integer.parseInt(attrs.getValue("x"))*32;
			int y = Integer.parseInt(attrs.getValue("y"))*32;
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
			Monster monster=new Monster(roomID, speed, power, type, life, x, y, desc, 
										coinValue, coinType, armour, isBoss);
			//MAP
			//System.out.println("Monster");
			Map.getRoom(roomID).entities.add(monster);

		}
		else if(qName.equals("item")){
			int damage = Integer.parseInt(attrs.getValue("damage"));
			int type = Integer.parseInt(attrs.getValue("type"));
			int x = Integer.parseInt(attrs.getValue("x"))*32;
			int y = Integer.parseInt(attrs.getValue("y"))*32;
			String desc = new String(attrs.getValue("desc"));
			String name = new String(attrs.getValue("name"));
			int value = Integer.parseInt(attrs.getValue("value"));

			Item item=new Item(damage, type, x, y, desc, name, value);
			
			if(checkNPC){
				npc.getItems().add(item);
			}else
				Map.getRoom(roomID).entities.addFirst(item);
		}
		else if(qName.equals("npc")){
			checkNPC = true;
			int type = Integer.parseInt(attrs.getValue("type"));
			int x = Integer.parseInt(attrs.getValue("x"))*32;
			int y = Integer.parseInt(attrs.getValue("y"))*32;
			String desc = new String(attrs.getValue("desc"));
			String name = new String(attrs.getValue("name"));
			
			npc = new NPC(type, desc, name, x, y);
		}
		else if(qName.equals("field")){
			isCheckpointLink = false;
			isCheckpoint = false;
			isFluss = false;
			int x = Integer.parseInt(attrs.getValue("x"));
			int y = Integer.parseInt(attrs.getValue("y"));
			int type = Integer.parseInt(attrs.getValue("typ"));
			int texture = Integer.parseInt(attrs.getValue("textur"));
			fieldPos = new FieldPosition(x, y);
			field = new Field(Map.getRoom(roomID), type, texture, 0, 0, fieldPos);
			fields[x][y] = field;
		}
		else if(qName.equals("fluss")){
			isFluss = true;
			flussrichtung = new String(attrs.getValue("richtung"));
		}
		else if(qName.equals("link")){
			linkID = Integer.parseInt(attrs.getValue("ID"));
			linkZiel = Integer.parseInt(attrs.getValue("zielraum"));
		}
		else if(qName.equals("checkpointlink")){
			isCheckpointLink = true;
			linkID = Integer.parseInt(attrs.getValue("ID"));
			linkZiel = Integer.parseInt(attrs.getValue("zielraum"));
		}
		else if(qName.equals("checkpoint")){
			isCheckpoint = true;
			linkID = Integer.parseInt(attrs.getValue("ID"));
		}
		else if(qName.equals("ziel")){
			int x = Integer.parseInt(attrs.getValue("x"));
			int y = Integer.parseInt(attrs.getValue("y"));
			Map.setEnd(fields[x][y], Map.getRoom(roomID));
		}
		else if(qName.equals("spawn")){
			int x = Integer.parseInt(attrs.getValue("x"));
			int y = Integer.parseInt(attrs.getValue("y"));
			System.out.println("roomID "+roomID);
			System.out.println("playerID "+playerID);
			System.out.println("playerCount "+playerCount);
			Map.addSpawn(fields[x][y]);
			player[playerID] = new Player(roomID, playerID, x, y);
			playerID++;
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
			Map.getRoom(roomID).entities.add(npc);
		} else if(qName.equals("field")){
			switch(field.type) {
			case 3:
				int attr1=-1;
				if(isFluss){
					switch(flussrichtung) {
					case "rauf":
						attr1 = 0;
						break;
					case "links":
						attr1 = 3;
						break;
					case "runter":
						attr1 = 2;
						break;
					case "rechts":
						attr1 = 1;
						break;
					default:
						break;
					}
					field.attribute1=attr1;
				}
				break;
			case 5:
			case 6:
				field.attribute1 = linkID;
				field.attribute2 = linkZiel;
				if(isCheckpointLink){
					Link link = new Link(linkID, linkZiel, fieldPos, true);
					Map.getRoom(roomID).getCheckpointLinks().add(link);
				}
				break;
			case 7:
				if(isCheckpoint){
					field.attribute1 = linkID;
					Map.getRoom(roomID).checkpointBuildLater(field);
				}
				break;
			default:
				break;
			}
		}else if(qName.equals("room")){
			Map.getRoom(roomID).roomFields = fields;
		}

	}	
	
    @Override
    public void characters(char ch[], int start, int length){
    	this.text = new String(ch, start, length);
    	this.text = this.text.replace("\\n", "\n");
     }

	/**
	 * @return the playerCount
	 */
	public int getPlayerCount() {
		return playerCount;
	}

	/**
	 * @param playerCount the playerCount to set
	 */
	public void setPlayerCount(int playerCount) {
		this.playerCount = playerCount;
	}

	/**
	 * @return the levelID
	 */
	public int getLevelID() {
		return levelID;
	}

	/**
	 * @param levelID the levelID to set
	 */
	public void setLevelID(int levelID) {
		this.levelID = levelID;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the player
	 */
	public Player[] getPlayer() {
		return player;
	}
}	
