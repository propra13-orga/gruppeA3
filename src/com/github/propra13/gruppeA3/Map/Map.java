package com.github.propra13.gruppeA3.Map;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.github.propra13.gruppeA3.Editor.Editor;
import com.github.propra13.gruppeA3.Exceptions.*;
import com.github.propra13.gruppeA3.Map.Map;
import com.github.propra13.gruppeA3.Menu.MenuStart;
import com.github.propra13.gruppeA3.XMLParser.SAXCrawlerReader;

public class Map {
	/**
	 * @author Christian Krüger
	 */
	
	final public static int ROOMHEIGHT = 17;
	final public static int ROOMWIDTH = 25;
	
	static public Room[] mapRooms;
	static public Field[] spawns = new Field[2];
	static public Link[] links;
	static public Field end;
	
	private static String mapName;
	
	final static String roomEnding = "room";
	final static String metaEnding = "xml";
	
	// Ob man durchs Ziel gehen kann oder nicht
	public static boolean endIsOpen = false;
	
	// Temporäre Sammellisten
	private static LinkedList<Link> linkBuffer = new LinkedList<Link>();
	private static LinkedList<Field> checkpointFieldsToBuild = new LinkedList<Field>();
	private static LinkedList<Field> checkpointLinksToBuild = new LinkedList<Field>();
	
	/** 
	 * Privater Konstruktor, damit Map nicht instanziert wird
	 */
	private Map() {}
	
	/**
	 * Baut Map aus gegebenem Verzeichnisnamen.
	 * Interpretiert alle durchnummerierten "xy.room"-Dateien als Rooms.
	 * @param dirName Verzeichnisname der Map
	 * @throws FileNotFoundException Falls eine geforderte Datei nicht vorhanden ist
	 * @throws MapFormatException Falls beim Lesen der Map ein Fehler auftrat, der auf das Format der Mapdateien zurückzuführen ist
	 * @throws IOException System-IO-Fehler
	 * @throws InvalidRoomLinkException Falls ein Fehler bei einem Link vorliegt
	 */
	public static void initialize(String dirName) 
			throws FileNotFoundException, MapFormatException, IOException, InvalidRoomLinkException {
		
		spawns[0] = spawns[1] = null;
		mapName = dirName;
		
		//Map einlesen
		mapRooms = readRooms();
		
		

		buildLinks();
		buildCheckpoints();
		
		// Damit sich Links bei Mehrfach-Initialisierungen nicht stacken
		linkBuffer.clear();
		checkpointFieldsToBuild.clear();
		checkpointLinksToBuild.clear();
		
	}
	
	/**
	 * Lädt eine XML-Datei des alten Formats
	 * @param xmlName
	 */
	public static void loadXML(String xmlName) {
		if(xmlName != null && !xmlName.equals("")) {
			SAXCrawlerReader reader=new SAXCrawlerReader();
		 	try {
		 		reader.read("data/levels/"+xmlName+".xml");
		 		
		 	} catch (Exception e) {
		 			e.printStackTrace();
		 	}
		}
	}
	
	/**
	 * Liest alle Rooms einer Map ein
	 * Vorgehensweise:
	 * Sammelt aus gegebenem Map-Verzeichnis alle .room-Dateien
	 * Erzeugt Room-Objekte aus .room-Dateien
	 * @return Gibt alle Räume in einem Array zurück
	 * @throws FileNotFoundException Eine Raumdatei oder das Mapverzeichnis lag nicht vor
	 * @throws MapFormatException Raumdateiinterpretationsfehler
	 * @throws IOException System-IO-Fehler
	 */
	private static Room[] readRooms()
			throws FileNotFoundException, MapFormatException, IOException {
		/* TODO:
		 * Map-Zip
		 */
		
		//Sammle Map-Dateien
		String dir = System.getProperty("user.dir");
		dir = dir + File.separator + "data" + File.separator + "maps" + File.separator + mapName;
		File f = new File(dir);
		if (! f.exists())
			throw new FileNotFoundException(dir);
		File[] fileArray = f.listFiles();
		
		
		//Sortiere Dateien (Rooms-Array (sortiert), sonstige)
		String[] roomNames = new String[fileArray.length];
		int roomFoundCounter=0;
		for (int i=0; i < fileArray.length; i++) {
			String fileName = fileArray[i].getName();
			String[] fileNameParts = fileName.split("\\.(?=[^\\.]+$)");
			
			if(fileNameParts.length > 1) {
				switch (fileNameParts[1]) {
					case Map.roomEnding:
						roomNames[roomFoundCounter] = fileNameParts[0];
						roomFoundCounter++;
						break;
					case Map.metaEnding:
						/* TODO:
						 * Datei mit Mapinformationen (xml)
						 */
						break;
					default:
						break;
				}
			}
			else
				throw new MapFormatException("Inkompatible Datei gefunden: "+roomNames[i]);
		}
		
		
		//Room-Namen-Check
		Arrays.sort(roomNames);
		for (int i=0; i < roomNames.length; i++) {
			int compare = Integer.parseInt(roomNames[i]);
			if (compare != i) {
				throw new MapFormatException("Inkompatible Datei gefunden: "+roomNames[i]);
			}
		}
		
		//Baue Rooms auf
		Room[] mapRooms = new Room[roomNames.length];
		for (int i=0; i < roomNames.length; i++) {
			mapRooms[i] = new Room(i, dir + File.separator + roomNames[i] + "." + roomEnding);
		}
		
		return mapRooms;
	}
	
	/**
	 * Schreibt die Map in Dateien. Die Dateien werden nach data/maps/mapName geschrieben.
	 * @param mapName Name des Verzeichnisses, in dem die Map gespeichert werden soll.
	 * @throws TransformerException
	 * @throws ParserConfigurationException
	 * @throws IOException
	 */
	public static void writeRooms(String mapName) throws TransformerException, ParserConfigurationException, IOException {
		//Sammle Map-Dateien
		String dir = System.getProperty("user.dir");
		dir = dir + File.separator + "data" + File.separator + "maps" + File.separator + mapName;
		
		for(int i=0; i < mapRooms.length; i++) {
			mapRooms[i].writeFile(dir);
		}
	}
	
	
	
	/** Baut Links aus Link-Buffer auf
	 * Vorgehensweise:
	 * 	Sucht Link mit der höchsten ID und baut Array mit passender Größe auf
	 * 	Befüllt Array zweidimensional (Jeder Arrayplatz enthält zwei zusammengehörende halbe Links)
	 * 	Baut aus zwei zusammengehörenden halben Links einen ganzen Link
	 * 	Teilt beiden Zielräumen den Link mit und fügt den Link in die Linkliste der Map ein
	 * 	Überprüft alle Links auf Konsistenz (evtl. auslagern auf Link-Klasse)
	 */
	private static void buildLinks() throws InvalidRoomLinkException {
		
		// Sucht Link mit der höchsten ID
		int highID = 0;
		Link testLink;
		
		for (Iterator<Link> i = linkBuffer.iterator(); i.hasNext();) {
			testLink = i.next();
			//TODO: LinkedList.next() checken (erstes Element abgedeckt?)
			if (testLink.ID > highID) {
				highID = testLink.ID;
			}
		}
		
		//System.out.println(highID);
		// Lädt halbe Links in zweidim. Array
		Link[][] halfLinks;
		halfLinks = new Link[highID + 1][2];
		
		for (Iterator<Link> i = linkBuffer.iterator(); i.hasNext();) {
			Link link = i.next();
			
			if (halfLinks[link.ID][0] == null)
				halfLinks[link.ID][0] = link;
			else if (halfLinks[link.ID][1] == null)
				halfLinks[link.ID][1] = link;
			else
				throw new InvalidRoomLinkException("dritten halben Link gefunden; Link-ID: "+link.ID+", Position: "+link.pos.x+":"+link.pos.y+", Ziel: Room "+link.targetRoomID);
		}
		
		// Fügt halbe Links zu ganzen zusammen
		for (int i=0; i < halfLinks.length; i++) {
			
			Room[] targetRooms = new Room[2];
			Field[] targetFields = new Field[2];
			
			//Setzt bidirectional
			boolean bidirectional = true;
			if (halfLinks[i][0].bidirectional == false || halfLinks[i][0].bidirectional == false )
				bidirectional = false;
			
			//Setzt targetRooms
			targetRooms[0] = mapRooms[halfLinks[i][0].targetRoomID];
			targetRooms[1] = mapRooms[halfLinks[i][1].targetRoomID];
			
			//Setzt targetFields: Position des gegenüberliegenden halben Links
			targetFields[0] = targetRooms[0].roomFields[halfLinks[i][1].pos.x][halfLinks[i][1].pos.y];
			targetFields[1] = targetRooms[1].roomFields[halfLinks[i][0].pos.x][halfLinks[i][0].pos.y];
			
			Link link = new Link(i, targetRooms, targetFields, bidirectional, ! halfLinks[i][0].isActivated());
			
			//Mitteilung an Map-Editor
			if (MenuStart.getGameStatus() == MenuStart.GameStatus.EDITOR)
				Editor.editor.notify(link);
			
			//Setzt ganze Links auf Felder
			targetFields[0].setLink(link);
			targetFields[1].setLink(link);
			
		}
	}
	
	/* TODO: Überprüft die Links zwischen Rooms auf Konsistenz
	 * Derzeit: Testweise Ausgabe aller Links
	 * TODO: Kriterien für vernünftige Links:
	 * 			Ein Feld hinter Link ist frei
	 * 			Neben Links sind Wände (Links sind einzelne Einbuchtungen in der Randwand)
	 * 
	 */
	@SuppressWarnings("unused")
	private void checkLinks() throws InvalidRoomLinkException {
		//Iteriert über Räume
		for (int k=0; k < Map.mapRooms.length; k++) {
			
			//Iteriert über Zeilen
			for (int i=0; i < Map.mapRooms[k].roomFields[0].length; i++) {
				
				//Iteriert über Spalten
				for (int j=0; j < Map.mapRooms[k].roomFields.length; j++) {
					
					if(Map.mapRooms[k].roomFields[j][i].link != null) {
						FieldPosition pos1 = Map.mapRooms[k].roomFields[j][i].link.targetFields[0].pos;
						FieldPosition pos2 = Map.mapRooms[k].roomFields[j][i].link.targetFields[1].pos;
						Room room1 = Map.mapRooms[k].roomFields[j][i].link.targetRooms[0];
						Room room2 = Map.mapRooms[k].roomFields[j][i].link.targetRooms[1];
						Room room = Map.mapRooms[k];
						FieldPosition pos = Map.mapRooms[k].roomFields[j][i].pos;
					}
				}
			}
		}
		
	}
	
	/**
	 * Verknüpft Checkpoint-Trigger mit Checkpoint-Links
	 */
	private static void buildCheckpoints() {
		Iterator<Field> i = checkpointFieldsToBuild.iterator();
		Iterator<Field> j = checkpointLinksToBuild.iterator();
		while (i.hasNext()) {
			Field triggerField = i.next();
			Field linkField = j.next();
			Link checkpointLink = linkField.link;
			triggerField.trigger = new Checkpoint(triggerField, checkpointLink);
			
			//Mitteilung an Map-Editor
			if (MenuStart.getGameStatus() == MenuStart.GameStatus.EDITOR)
				Editor.editor.notify(triggerField.trigger);
		}
	}
	
	/** Fügt einen Link zum Link-Buffer hinzu
	 * Link-Buffer wird später ausgelesen und die Links ordentlich zusammengestellt
	 */
	public static void addLink(Link link) {
		linkBuffer.push(link);
	}
	
	/** Fügt einen Checkpoint zum Checkpoint-Buffer hinzu
	 * Checkpoint-Buffer wird später ausgelesen und die Trigger mit den Links verknüpft
	 */
	public static void addTrigger(Field trigger, Field link) {
		checkpointFieldsToBuild.push(trigger);
		checkpointLinksToBuild.push(link);
	}

	/** Setzt einen Spawn auf der Map
	 * @param spawn Spawn, der hinzugefügt werden soll
	 */
	public static void addSpawn(Field spawn) {
		if (spawns[0] == null)
			spawns[0] = spawn;
		else if (spawns[1] == null)
			spawns[1] = spawn;
	}
	
	/** Setzt Ziel der Map
	 * @param endField Zielfeld
	 * @param room Raum, in dem sich das Zielfeld befindet
	 */
	public static void setEnd(Field endField, Room room) {
		end = endField;
	}
	
	/**
	 * @author Majida Dere
	 * @param roomID - gibt die ID des aktuellen Raumes an
	 * @return liefert den aktuellen Raum
	 */
	public static Room getRoom(int roomID){
		return mapRooms[roomID];
	}
	
	public static String getName() {
		return mapName;
	}
}
