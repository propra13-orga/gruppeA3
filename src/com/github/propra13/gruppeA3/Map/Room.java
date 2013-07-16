package com.github.propra13.gruppeA3.Map;

import com.github.propra13.gruppeA3.Exceptions.MapFormatException;
import com.github.propra13.gruppeA3.Entities.Entities;
import com.github.propra13.gruppeA3.Entities.Item;
import com.github.propra13.gruppeA3.Entities.Monster;
import com.github.propra13.gruppeA3.Entities.NPC;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Room {
	
	public int ID;
	public Field[][] roomFields;	//roomFields[Spalte][Zeile]
	public LinkedList<Entities> entities = new LinkedList<Entities>();
	public LinkedList<Entities> removeCandidates = new LinkedList<Entities>();
	public Field[] spawns = null;
	final static int fieldBytes = 4;
	
	
	//Temporäre Sammellisten
	
	/** Positionen, an denen Checkpoint-Links sind.
	 * Direkte Link-Refs können nicht genutzt werden, da zur Einlesezeit
	 * unbekannt.
	 */
	private List<Link> checkpointLinks = new LinkedList<Link>();
	private List<Field> checkpointsToBuild = new LinkedList<Field>(); //für buildCheckpoints()
	
	/**
	 * Baut Room-Objekt aus Datei
	 * @param roomID ID des Raums
	 * @param filename Name der Datei, aus der der Raum gelesen werden soll
	 * @throws IOException
	 * @throws MapFormatException
	 */
	public Room(int roomID, String filename)
			throws IOException, MapFormatException {
		this.ID = roomID;
		this.roomFields = readFile(filename);
		buildCheckpoints();
	}
	
	/**
	 * Baut frischen Raum mit Wänden am Rand und Boden in der Mitte.
	 * Wird für den Editor benötigt.
	 * @param ID ID des neuen Raums
	 */
	public Room(int ID) {
		this.ID = ID;
		roomFields = new Field[Map.ROOMWIDTH][Map.ROOMHEIGHT];
		
		//Felder setzen
		//Iteriert über Spalten
		for (int i = 0; i < roomFields.length; i++) {
			//Iteriert über Zeilen
			for (int j = 0; j < roomFields[i].length; j++) {
				
				//Raumrand
				if(i == 0 || i == roomFields.length - 1 ||
						j == 0 || j == roomFields[i].length - 1)
					roomFields[i][j] = new Field(this, 2, 0, 0, 0, new FieldPosition(i, j));
				
				//kein Raumrand
				else
					roomFields[i][j] = new Field(this, 1, 0, 0, 0, new FieldPosition(i, j));
			}
		}
	}
	
	/**
	 * Liest Raum aus einer Datei des alten Raumdateiformats aus.
	 * @param filename Datei, die ausgelesen werden soll.
	 * @return Gibt zweidimensionales Array aller Felder des Raums zurück.
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws MapFormatException
	 */
	@SuppressWarnings("unused")
	private Field[][] readFile_old (String filename)
			throws FileNotFoundException, IOException, MapFormatException {
		
		
		/* Spielfelddatei -> Buffer-Array */
		File file = new File(filename);
		int[] buffer = new int[(int) file.length()];
		
		//Total hässlich, muss aber so (IO halt)
		if (! file.exists())
			throw new FileNotFoundException (filename);
		FileInputStream fis = new FileInputStream(filename);
			
		try {
			for (int i=0; i < file.length(); i++) {
				buffer[i] = fis.read(); //Liest byteweise Datei in buffer[]
			}
		} catch (IOException e) {
			throw new IOException (filename);
		} finally {
			try {
				fis.close();
			} catch (IOException e){}
		}
		
		
		/*Symmetriecheck (EOL: EndOfLine)*/
		//Durchläuft Buffer-Array und zählt Zeilenenden
		int EOL_counter=0;
		for (int i=0; i < buffer.length; i++) {
			if (buffer[i] == 255) {
				EOL_counter++;
			}
		}
		
		//Symmcheck
		//Durchläuft alle Zeilen und vergleicht Zeilenlänge
		//mit der ersten Zeile (in Bytes!)
		EOL_counter=0;		//Aktuelle Zeile
		int lineLen=0;		//Vergleichszeilenlänge
		int lineIterate=0;	//Zeilenbreitenzähler
		for (int i=0; i < buffer.length; i++) {
			
			if (buffer[i] == 255) {
				if (EOL_counter == 0) {
					//Setzt Vergleichszeilenlänge
					lineLen = lineIterate;
				}
				else if (lineLen != lineIterate) {
					//Eine Zeilenlänge war von Vergleichszeilenlänge verschieden
					throw new MapFormatException(filename + " ist nicht rechteckig.");
				}
				
				EOL_counter++; //Damit später die Zeilenzahl stimmt
				lineIterate = 0; //Setzt Zeilenbreitenzähler zurück
			}
			else {
				lineIterate++;
			}
		}
		
		
		/* Buffer -> Map-Array */
		Field room[][] = new Field[lineLen/fieldBytes][EOL_counter];
		
		//Durchläuft Zeilen
		int spawncounter = 0;
		spawns = new Field[2];
		spawns[0] = null;
		spawns[1] = null;
		
		int lineIndex = 0;   //Anfang der aktuellen Zeile
		int bufferIndex = 0; //Anfang des aktuellen Felds
		
		/* i: Zeilennummer
		 * lineIndex: Nummer des buffer-Arrayplatzes des ersten Bytes delineIndex r aktuellen Zeile
		 * 
		 * j: Feldnummer in aktueller Zeile
		 * bufferIndex: Nummer des buffer-Arrayplatzes des ersten Bytes des aktuellen Felds
		 * 
		 * k: Arrayplatz-Offset für Bestimmung der Feldattribute
		 */
		for (int i=0; i < EOL_counter; i++) {
			lineIndex = i*lineLen + i; //Zeile*Zeilenlänge + EOLs
			
			
			// Durchläuft Spalten (in Feldern)
			for (int j=0; j < lineLen/fieldBytes; j++) {
				
				//Nimmt erstes Feldbyte
				bufferIndex = lineIndex + j*fieldBytes;
				
				//Iteriert über alle sechs Feldbytes
				int texture = 255; //255: Eclipse meckert sonst
				int type = 255;
				int attr1 = 255;
				int attr2 = 255;
				
				for (int k=0; k < 4; k++) {
					switch (k) {
						case 0:
							type = buffer[bufferIndex + k];
							break;
						case 1:
							texture = buffer[bufferIndex + k];
							break;
						case 2:
							attr1 = buffer[bufferIndex + k];
							break;
						case 3:
							attr2 = buffer[bufferIndex + k];
							break;
					}
				}
				FieldPosition pos=new FieldPosition(j, i);
				room[j][i] = new Field(
						this, type, texture, attr1, attr2, pos);
				
				// Checkpoints, Trigger, Spawns und Links setzen
				switch (room[j][i].type) {
				
					//Spawn
					case 1:
						if (attr1 == 2) {
							
							// Um den Pfad vom Dateinamen einfach abhacken zu können
							File compare = new File(filename);
							
							
							if( !compare.getName().equals( "00." + Map.roomEnding) )
								throw new MapFormatException("Spawns dürfen nur in Raum 00 sein.");
							
							else if (spawncounter > 1 || attr2 > 1)
								throw new MapFormatException("Nicht mehr als zwei Spawns erlaubt");
							
							else if (spawns[spawncounter] != null)
								throw new MapFormatException("Zwei Spawns mit gleicher ID");
							
							else
								Map.addSpawn(room[j][i]);
							
							spawncounter++;
						}
						break;
						
					//Link und Ziel
					case 5:
						if (attr2 != 254) 
							new Link(attr1, attr2, room[j][i].pos, false);
						else {
							Map.setEnd(room[j][i], this);
							room[j][i].type = 1;
						}
						break;
						
					/* Checkpoint-Link
					 * Werden gesammelt für buildCheckpoints()
					 */
					case 6:
						checkpointLinks.add(new Link(attr1, attr2, room[j][i].pos, true));
						break;
						
					// Trigger
					case 7:
						// Sucht Trigger-Subobjekt raus
						switch (attr2) {
							case 0:
								checkpointBuildLater(room[j][i]);
								break;
						}
						break;
						
				}
			}
		}
		return room;
	}
	
	/**
	 * Liest Raum aus einer Datei des neuen xml-Raumdateiformats aus.
	 * @param filename Name der Datei, die ausgelesen werden soll.
	 * @return Gibt zweidimensionales Array aller Felder des Raums zurück.
	 * @throws MapFormatException 
	 */
	private Field[][] readFile (String filename) throws MapFormatException {
		//DOM-doc mit xml-Inhalt erzeugen
		File file = new File(filename);
		Document doc = null;
		try {
			DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			doc = dBuilder.parse(file);
		} catch (SAXException | IOException | ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		doc.getDocumentElement().normalize();
		
		
		/*
		 * Felder auslesen
		 */
		Field[][] fields = new Field[Map.ROOMWIDTH][Map.ROOMHEIGHT];
		NodeList fieldNodes = doc.getElementsByTagName("field");
		
		//Iteriert über Feld-Elemente
		for (int i = 0; i < fieldNodes.getLength(); i++) {
			Element fieldEl = (Element)fieldNodes.item(i);
			
			//Feld-Attribute abgrasen
			int x = Integer.parseInt(fieldEl.getAttribute("x"));
			int y = Integer.parseInt(fieldEl.getAttribute("y"));
			int type = Integer.parseInt(fieldEl.getAttribute("typ"));
			int texture = Integer.parseInt(fieldEl.getAttribute("textur"));
			FieldPosition pos = new FieldPosition(x, y);
			
			//Zusatzattribute einsammeln
			boolean isCheckpoint = true; //für Unterscheidung zwischen Checkpoint- und normalem Link
			switch(type) {
			//Boden
			case 1:
				//switch rutscht durch
			//Wand
			case 2:
				fields[x][y] = new Field(this, type, texture, 0, 0, pos);
				break;
				
			//Wasser
			case 3:
				NodeList waterNodes = fieldEl.getElementsByTagName("fluss");
				Element waterEl = (Element)waterNodes.item(0);
				int attr1 = -1;
				switch(waterEl.getAttribute("richtung")) {
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
				
				fields[x][y] = new Field(this, type, texture, attr1, 0, pos);
				break;
				
			//Link
			case 5:
				isCheckpoint = false;
				//rutscht durch
				
			//Checkpoint-Link
			case 6:
				NodeList linkNodes = null;
				if(!isCheckpoint)
					linkNodes = fieldEl.getElementsByTagName("link");
				else
					linkNodes = fieldEl.getElementsByTagName("checkpointlink");
				
				Element linkEl = (Element)linkNodes.item(0);
				int target = Integer.parseInt(linkEl.getAttribute("zielraum"));
				int ID = Integer.parseInt(linkEl.getAttribute("ID"));
				
				Link link = new Link(ID, target, pos, isCheckpoint);
				
				//Falls Checkpoint, eintragen für checkpointBuildLater()
				if(isCheckpoint)
					checkpointLinks.add(link);
				
				fields[x][y] = new Field(this, type, texture, ID, target, pos);
				break;
				
			//Checkpoint-Trigger
			case 7:
				//Trigger-Typ raussuchen
				NodeList fieldChildren = fieldEl.getChildNodes();
				Element testEl;
				
				for(int j=0; j < fieldChildren.getLength(); j++) {
					if(fieldChildren.item(j) instanceof Element) {
						
						testEl = (Element)fieldChildren.item(j);
						
						switch(testEl.getNodeName()) {
						
						//Checkpoint-Trigger
						case "checkpoint":
							int cpID = Integer.parseInt(testEl.getAttribute("ID"));
							fields[x][y] = new Field(this, type, texture, cpID, 0, pos);
							checkpointBuildLater(fields[x][y]);
							break;
						default:
							break;
						}
					}
				}
				break;
			}

		}
		
		//Check, ob alle Felder gesetzt sind
		//Iteriert über Spalten
		for (int i = 0; i < fields.length; i++) {
			//Iteriert über Zeilen
			for (int j = 0; j < fields[i].length; j++) {
				if(fields[i][j] == null)
					throw new MapFormatException("Feld "+i+":"+j+" fehlt.");
			}
		}
		
		//Spawns auslesen
		if(this.ID == 0) {
			NodeList spawnNodes = doc.getElementsByTagName("spawn");
			
			//Iteriert über Spawn-Elemente
			Element testElement;
			for (int i = 0; i < spawnNodes.getLength(); i++) {
				if(spawnNodes.item(i) instanceof Element) {
					testElement = (Element)spawnNodes.item(i);
					int x = Integer.parseInt(testElement.getAttribute("x"));
					int y = Integer.parseInt(testElement.getAttribute("y"));
					Map.addSpawn(fields[x][y]);
				}
			}
		}
		
		//Ziel auslesen
		NodeList endNodes = doc.getElementsByTagName("ziel");
		for(int i=0; i < endNodes.getLength(); i++) {
			if(endNodes.item(i) instanceof Element) {
				Element endEl = (Element)endNodes.item(i);
				int x = Integer.parseInt(endEl.getAttribute("x"));
				int y = Integer.parseInt(endEl.getAttribute("y"));
				Map.setEnd(fields[x][y], this);
				break;
			}
		}
		
		
		
		/*
		 * Entities auslesen
		 */
		
		//Monster
		NodeList monsterNodes = doc.getElementsByTagName("monster");
		Element monsterEl;
		for(int i=0; i < monsterNodes.getLength(); i++) {
			if(monsterNodes.item(i) instanceof Element) {
				monsterEl = (Element)monsterNodes.item(i);
				
				String desc = monsterEl.getAttribute("beschreibung");
				
				boolean isBoss = false;
				if(desc.equals("Bossi"))
					isBoss = true;
				else
					isBoss = false;
				
				entities.add(new Monster(
						this.ID,
						Double.parseDouble(monsterEl.getAttribute("geschwindigkeit")),
						Integer.parseInt(monsterEl.getAttribute("angriff")),
						Integer.parseInt(monsterEl.getAttribute("typ")),
						Integer.parseInt(monsterEl.getAttribute("leben")),
						Integer.parseInt(monsterEl.getAttribute("x")),
						Integer.parseInt(monsterEl.getAttribute("y")),
						desc,
						Integer.parseInt(monsterEl.getAttribute("muenzen")),
						1,
						Integer.parseInt(monsterEl.getAttribute("ruestung")),
						isBoss
						));
			}
		}
		
		//NPCs
		NodeList npcNodes = doc.getElementsByTagName("NPC");
		Element npcEl;
		for(int i=0; i < npcNodes.getLength(); i++) {
			if(npcNodes.item(i) instanceof Element) {
				npcEl = (Element)npcNodes.item(i);
				
				NPC npc = new NPC(
						Integer.parseInt(npcEl.getAttribute("typ")),
						npcEl.getAttribute("beschreibung"),
						npcEl.getAttribute("name"),
						Integer.parseInt(npcEl.getAttribute("x")),
						Integer.parseInt(npcEl.getAttribute("y"))
						);
				
				npc.setText(npcEl.getAttribute("text"));
				
				entities.add(npc);
			}
		}
		
		//Items
		NodeList itemNodes = doc.getElementsByTagName("item");
		Element itemEl;
		LinkedList<Item> itemsToDo = new LinkedList<Item>();
		
		for(int i=0; i < itemNodes.getLength(); i++) {
			if(itemNodes.item(i) instanceof Element) {
				itemEl = (Element)itemNodes.item(i);
				itemsToDo.add(new Item(
						Integer.parseInt(itemEl.getAttribute("staerke")),
						Integer.parseInt(itemEl.getAttribute("typ")),
						Integer.parseInt(itemEl.getAttribute("x")),
						Integer.parseInt(itemEl.getAttribute("y")),
						itemEl.getAttribute("beschreibung"),
						itemEl.getAttribute("name"),
						Integer.parseInt(itemEl.getAttribute("wert"))
						));
			}
		}
		
		//Shop-Items auf Raum und Shops verteilen anhand der Position von Shop und Item
		Item testItem;
		for(Iterator<Item> itemIter = itemsToDo.iterator(); itemIter.hasNext();) {
			testItem = itemIter.next();
			
			//Entities-Liste nach NPC durchsuchen, der auf der selben Position wie das Item ist
			boolean shopFound = false;
			Entities testEntity;
			for(Iterator<Entities> entIter = entities.iterator(); entIter.hasNext();) {
				testEntity = entIter.next();
				
				if(testEntity instanceof NPC) {
					NPC npc = (NPC)testEntity;
					
					if(npc.getPosition().equals(testItem.getPosition())) {
						npc.getItems().add(testItem);
						itemIter.remove();
						shopFound = true;
					}
				}
			}
			
			//Falls kein Shop gefunden, Item auf Raum legen
			if(! shopFound)
				entities.addFirst(testItem);
		}

		return fields;
	}
	
	/**
	 * Schreibt den Raum in eine xml-Datei mit Namen "ID.xml", mit ID der Raum-ID als zweistellige Dezimalzahl.
	 * @param mapDir Verzeichnis der Map.
	 * @throws TransformerException 
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 */
	public void writeFile(String mapDir) throws TransformerException, ParserConfigurationException, IOException {
		
		//Document-Setup
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		DOMImplementation impl = builder.getDOMImplementation();
		
		Document doc = impl.createDocument(null,null,null);
		Element roomEl = doc.createElement("room");
		doc.appendChild(roomEl);
		
		
		//Felder zum Document hinzufügen
		Field field;
		Element fieldToAppend;
		Element el;
		
		//Iteriert über Spalten
		for (int i = 0; i < roomFields.length; i++) {
			//Iteriert über Zeilen
			for (int j = 0; j < roomFields[i].length; j++) {

				//Element erstellen und einsetzen
				field = roomFields[i][j];
				fieldToAppend = doc.createElement("field");
				roomEl.appendChild(fieldToAppend);

				//Attribute setzen
				fieldToAppend.setAttribute("x", field.pos.x+"");
				fieldToAppend.setAttribute("y", field.pos.y+"");
				fieldToAppend.setAttribute("typ", field.type+"");
				fieldToAppend.setAttribute("textur", field.texture+"");
				
				//Link
				if(field.link != null) {
					el = null;
					//Normaler Link
					if(field.link.isActivated())
						el = doc.createElement("link");
					//Checkpoint-Link
					else
						el = doc.createElement("checkpointlink");
					fieldToAppend.appendChild(el);
					
					//Link-Target-Array-Index suchen, der die andere Seite darstellt
					int target;
					if(field.link.targetRooms[0] == this)
						target = field.link.targetRooms[1].ID;
					else
						target = field.link.targetRooms[0].ID;
					
					//anwenden
					el.setAttribute("zielraum", target+"");
					el.setAttribute("ID", field.link.ID+"");
				}
				
				//Trigger
				if(field.trigger instanceof Checkpoint) {
					Checkpoint cp = (Checkpoint)field.trigger;
					
					el = doc.createElement("checkpoint");
					el.setAttribute("ID", cp.getToActivate().ID+"");
					
					fieldToAppend.appendChild(el);
				}
				
				//Fluss
				if(field.type == 3) {
					el = doc.createElement("fluss");
					fieldToAppend.appendChild(el);
					String richtung = null;
					switch(field.attribute1) {
					case 0:
						richtung = "hoch";
						break;
					case 1:
						richtung = "rechts";
						break;
					case 2:
						richtung = "runter";
						break;
					case 3:
						richtung = "links";
						break;
					default:
						break;
					}
					el.setAttribute("richtung", richtung);
				}
        	}
        }
		
		//Ziel
		if(Map.end.getRoom() == this) {
			el = doc.createElement("ziel");
			roomEl.appendChild(el);
			el.setAttribute("x", Map.end.pos.x+"");
			el.setAttribute("y", Map.end.pos.y+"");
		}
		
		//Spawns hinzufügen
		if(ID == 0) {
			Element spawnToAppend;
			for(Iterator<Field> iter = Map.spawns.iterator(); iter.hasNext();) {
				Field spawn = iter.next();
				spawnToAppend = doc.createElement("spawn");
				roomEl.appendChild(spawnToAppend);
				spawnToAppend.setAttribute("x", spawn.pos.x+"");
				spawnToAppend.setAttribute("y", spawn.pos.y+"");
			}
		}
		
		
		
		/*
		 * Entities
		 */
		
		
		Entities testEntity;
		for(Iterator<Entities> iter = entities.iterator(); iter.hasNext();) {
			testEntity = iter.next();
			
			//Monster
			if(testEntity instanceof Monster) {
				Monster monster = (Monster)testEntity;
				el = doc.createElement("monster");
				roomEl.appendChild(el);
				el.setAttribute("typ", monster.getType()+"");
				el.setAttribute("geschwindigkeit", Double.toString(monster.getSpeed()));
				el.setAttribute("angriff", monster.getAttack()+"");
				el.setAttribute("leben", monster.getHealth()+"");
				el.setAttribute("beschreibung", monster.getDesc());
				el.setAttribute("muenzen", monster.getCoin().getValue()+"");
				el.setAttribute("ruestung", monster.getArmour()+"");
				el.setAttribute("x", monster.getPosition().toFieldPos().x+"");
				el.setAttribute("y", monster.getPosition().toFieldPos().y+"");
			}
			
			//Items
			else if(testEntity instanceof Item) {
				Item item = (Item)testEntity;
				el = doc.createElement("item");
				roomEl.appendChild(el);
				el.setAttribute("typ", item.getType()+"");
				el.setAttribute("staerke", item.getDamage()+"");
				el.setAttribute("beschreibung", item.getDesc());
				el.setAttribute("name", item.getName());
				el.setAttribute("wert", item.getValue()+"");
				el.setAttribute("x", item.getPosition().toFieldPos().x+"");
				el.setAttribute("y", item.getPosition().toFieldPos().y+"");
			}
			
			//NPC
			else if(testEntity instanceof NPC) {
				NPC npc = (NPC)testEntity;
				el = doc.createElement("NPC");
				roomEl.appendChild(el);
				el.setAttribute("typ", npc.getType()+"");
				el.setAttribute("x", npc.getPosition().toFieldPos().x+"");
				el.setAttribute("y", npc.getPosition().toFieldPos().y+"");
				el.setAttribute("text", npc.getText());
				el.setAttribute("beschreibung", npc.getDesc());
				el.setAttribute("name", npc.getName());
				
				if(npc.getType() == 2) {
					Item testItem;
					Element item;
					for(Iterator<Item> it = npc.getItems().iterator(); it.hasNext();) {
						testItem = it.next();
						item = doc.createElement("item");
						el.appendChild(item);
						item.setAttribute("typ", testItem.getType()+"");
						item.setAttribute("staerke", testItem.getDamage()+"");
						item.setAttribute("beschreibung", testItem.getDesc());
						item.setAttribute("name", testItem.getName());
						item.setAttribute("wert", testItem.getValue()+"");
						item.setAttribute("x", npc.getPosition().toFieldPos().x+"");
						item.setAttribute("y", npc.getPosition().toFieldPos().y+"");
					}
				}
			}
		}
		
		
		//Transformiert Document in einen String
		
		//Transformer-Setup
		DOMSource domSource = new DOMSource(doc);
		TransformerFactory tf = TransformerFactory.newInstance();
		Transformer transformer = tf.newTransformer();
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.METHOD, "xml");
		transformer.setOutputProperty(OutputKeys.ENCODING,"UTF-8");
		transformer.setOutputProperty
			("{http://xml.apache.org/xslt}indent-amount", "4");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		
		//transformieren
		java.io.StringWriter sw = new java.io.StringWriter();
		StreamResult sr = new StreamResult(sw);
		transformer.transform(domSource, sr);
		
		
		//Schreibt xml-String in Datei
		String filename = null;
		if(ID < 10)
			filename = "0"+ID;
		else
			filename = Integer.toString(ID);
		BufferedWriter writer = null;
		writer = new BufferedWriter(new FileWriter(mapDir +File.separator+ filename+".room"));
		writer.write(sw.toString());
		writer.close();
	}
	
	private void checkpointBuildLater(Field location) {
		checkpointsToBuild.add(location);
	}
	
	/** 
	 * Gibt Feld zurück, auf dem sich eine gegebene Position in Pixelkoords befindet.
	 * @param pos Position, zu der das Feld bestimmt werden soll
	 * @return Feld an Position pos
	 */
	public Field getField(Position pos) {
		return getField(new FieldPosition(pos.toFieldPos().x, pos.toFieldPos().y));
	}
	
	/**
	 *  Baut alle Checkpoint-Trigger aus checkpointsToBuild
	 */
	private void buildCheckpoints() {
		Iterator<Field> iter = checkpointsToBuild.iterator();
		Field toBuild;

		//Iteriert über alle gefundenen Checkpointtrigger
		while (iter.hasNext()) {
			toBuild = iter.next();
	
			Iterator<Link> iter2 = checkpointLinks.iterator();
			Link linkToCheck;
			//Iteriert über alle gefundenen Checkpoint-Link-Positionen
			while (iter2.hasNext()) {
				linkToCheck = iter2.next();
				
				/* Falls die ID des Checkpoint-Links (aus checkpointLinks) mit der ID
				 * des Triggers (aus checkpointsToBuild) übereinstimmt, wird der
				 * Checkpoint-Trigger erzeugt.
				 */
				if (linkToCheck.ID == toBuild.attribute1) {
					Field linkField = roomFields[linkToCheck.pos.x][linkToCheck.pos.y];
					Map.addTrigger(toBuild, linkField);
				}
			}
        }
	}
	
	/**
	 * Entfernt alle Entities aus der removeCandidates-Liste aus dem Raum
	 */
	public void removeEntities() {
		Entities toRemove = null;
		Iterator<Entities> itprj = removeCandidates.iterator();
		while (itprj.hasNext()) {
			toRemove = itprj.next();
			Entities testent = null;	//durch alle Entitys der Liste iterieren
		    Iterator<Entities> itent = entities.iterator();
			while(itent.hasNext()){
				testent = itent.next();
				if(testent == toRemove)
					itent.remove();
			}
		}
	}

	/**
	 * Gibt Feld an Feldposition pos zurück
	 * @param pos Position, deren Feld bestimmt werden soll
	 * @return Feld an Feldposition pos zurück
	 */
	public Field getField(FieldPosition pos) {
		return roomFields[pos.x][pos.y];
	}
	
	public Field getField(int x, int y) {
		return roomFields[x][y];
	}
	
	public int getWidth() {
		return roomFields.length;
	}
	
	public int getHeight() {
		return roomFields[0].length;
	}
}
	
