package com.github.propra13.gruppeA3.Map;

import com.github.propra13.gruppeA3.Exceptions.MapFormatException;
import com.github.propra13.gruppeA3.Entities.Coin;
import com.github.propra13.gruppeA3.Entities.Entities;
import com.github.propra13.gruppeA3.Entities.Item;
import com.github.propra13.gruppeA3.Entities.Monster;
import com.github.propra13.gruppeA3.Entities.NPC;
import com.github.propra13.gruppeA3.XMLParser.DOM;

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

public class Room {
	
	public int ID;
	public Field[][] roomFields;	//roomFields[Spalte][Zeile]
	public LinkedList<Entities> entities = new LinkedList<Entities>();
	public LinkedList<Entities> removeCandidates = new LinkedList<Entities>();
	public Field[] spawns = null;
	final static int fieldBytes = 4;
	
	
	//Temporäre Sammellisten
	
	/* Positionen, an denen Checkpoint-Links sind.
	 * Direkte Link-Refs können nicht genutzt werden, da zur Einlesezeit
	 */
	private List<Link> checkpointLinks = new LinkedList<Link>();
	private List<Field> checkpointsToBuild = new LinkedList<Field>(); //für buildCheckpoints()
	
	/* TODO:
	 * Metadatenzeile
	 * Links
	 * Room-ID (für Links, Listen und was nicht all)
	 * vernünftiges Dateiformat
	 * ( 6 Byte pro Field (neues Item-Format) )
	 */
	
	//Baut Map-Objekt aus Datei
	public Room(int roomID, String filename)
			throws IOException, MapFormatException {
		this.ID = roomID;
		this.roomFields = readFile_old(filename);
		buildCheckpoints();
	}
	
	/**
	 * Liest Raum aus einer Datei des alten Raumdateiformats aus.
	 * @param filename Datei, die ausgelesen werden soll.
	 * @return Gibt zweidimensionales Array aller Felder des Raums zurück.
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws MapFormatException
	 */
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
						else
						Map.setEnd(room[j][i], this);
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
	 */
	private Field[][] readFile (String filename) {
		Document doc = DOM.readFile(filename);
		NodeList fields = doc.getElementsByTagName("field");
		return null;
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
				
				//Link
				if(field.link != null) {
					el = doc.createElement("link");
					fieldToAppend.appendChild(el);
					
					//Link-Target-Array-Index suchen, der die andere Seite darstellt
					int index;
					if(field.link.targetRooms[0] == this)
						index = 1;
					else
						index = 0;
					
					//anwenden
					el.setAttribute("targetX", field.link.targetFields[index].pos.x+"");
					el.setAttribute("targetY", field.link.targetFields[index].pos.y+"");
				}
				
				//Trigger
				if(field.trigger instanceof Checkpoint) {
					Checkpoint cp = (Checkpoint)field.trigger;
					el = doc.createElement("checkpoint");
					fieldToAppend.appendChild(el);
					el.setAttribute("targetX", cp.getToActivate().pos.x+"");
					el.setAttribute("targetY", cp.getToActivate().pos.y+"");
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
			for(int i=0; i < Map.spawns.length; i++) {
				spawnToAppend = doc.createElement("spawn");
				roomEl.appendChild(spawnToAppend);
				spawnToAppend.setAttribute("x", Map.spawns[i].pos.x+"");
				spawnToAppend.setAttribute("y", Map.spawns[i].pos.y+"");
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
				el.setAttribute("x", monster.getPosition().x+"");
				el.setAttribute("y", monster.getPosition().y+"");
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
				el.setAttribute("x", item.getPosition().x+"");
				el.setAttribute("y", item.getPosition().y+"");
			}
			
			//NPC
			else if(testEntity instanceof NPC) {
				NPC npc = (NPC)testEntity;
				el = doc.createElement("NPC");
				roomEl.appendChild(el);
				el.setAttribute("typ", npc.getType()+"");
				el.setAttribute("x", npc.getPosition().x+"");
				el.setAttribute("y", npc.getPosition().y+"");
				el.setAttribute("text", npc.getText());
				
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
						item.setAttribute("x", testItem.getPosition().x+"");
						item.setAttribute("y", testItem.getPosition().y+"");
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
		writer = new BufferedWriter(new FileWriter(mapDir +File.separator+ filename+".xml"));
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
	
