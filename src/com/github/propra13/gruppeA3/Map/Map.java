package com.github.propra13.gruppeA3.Map;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.github.propra13.gruppeA3.Game;
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
	
	/**
	 * Liste der Namen aller verfügbaren Maps. Wird mit updateMapList() aktualisiert.
	 */
	public static LinkedList<String> mapList = new LinkedList<String>();
	
	static public Room[] mapRooms;
	static public LinkedList<Field> spawns = new LinkedList<Field>();
	static public Link[] links;
	static public Field end;
	
	private static String mapName;
	
	public static MapHeader header;
	

	final static String roomEnding = "room";
	final static String headerEnding = "map";
	
	/** Ob man durchs Ziel gehen kann oder nicht*/
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
	 * Interpretiert alle durchnummerierten "xy.room"-Dateien als Raum-Dateien.
	 * @param dirName Verzeichnisname der Map
	 * @throws FileNotFoundException Falls eine geforderte Datei nicht vorhanden ist
	 * @throws MapFormatException Falls beim Lesen der Map ein Fehler auftrat, der auf das Format der Mapdateien zurückzuführen ist
	 * @throws IOException System-IO-Fehler
	 * @throws InvalidRoomLinkException Falls ein Fehler bei einem Link vorliegt
	 */
	public static void initialize(String mapName) 
			throws FileNotFoundException, MapFormatException, IOException, InvalidRoomLinkException {
		
		spawns.clear();
		setMapName(mapName);
		
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
	 * Erstellt eine neue Karte mit einem leeren Raum.
	 * @param mapName Name der neuen Karte
	 */
	public static void newMap(String mapName) {
		Map.mapName = mapName;
		
		//Attribute und Listen zurücksetzen
		spawns.clear();
		links = null;
		end = null;
		endIsOpen = false;
		
		mapRooms = new Room[1];
		mapRooms[0] = new Room(0);
	}
	
	/**
	 * Updatet die Liste der verfügbaren Maps.
	 */
	public static void updateMapList() {
		/*
		 * Sammle Verzeichnisse in maps/
		 */
		//Alle Dateien in maps/
		String dir = System.getProperty("user.dir");
		dir = dir + File.separator + "data" + File.separator + "maps";
		File f = new File(dir);
		if (! f.exists())
			try {
				throw new FileNotFoundException(dir);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		
		File[] mapsDirEntries = f.listFiles();
		
		//Verzeichnisse raussuchen
		LinkedList<File> dirs = new LinkedList<File>();
		for(int i=0; i < mapsDirEntries.length; i++) {
			if(mapsDirEntries[i].isDirectory())
				dirs.add(mapsDirEntries[i]);
		}
		
		//In Verzeichnissen die raussuchen, die .room- und .map-Dateien haben
		LinkedList<File> mapHeaderFiles = new LinkedList<File>(); //Sammlung aller einzulesenden Header-Dateien
		LinkedList<MapHeader> headers = new LinkedList<MapHeader>();
		
		//Iteriert über Map-Verzeichnise
		File testDir;
		for(Iterator<File> iter = dirs.iterator(); iter.hasNext();) {
			testDir = iter.next();
			boolean hasRoom = false;
			boolean hasHeader = false;
			
			File[] mapFiles = testDir.listFiles();
			File header=null;
			
			//Iteriert über Dateien in Map-Verzeichnis
			for(int i=0; i < mapFiles.length; i++) {
				String fileName = mapFiles[i].getName();
				String[] fileNameParts = fileName.split("\\.(?=[^\\.]+$)");
				
				//Auf Raum- und Headerdateien prüfen
				if(fileNameParts.length > 1) {
					if(fileNameParts[1].equals(roomEnding))
						hasRoom = true;
					else if(fileNameParts[1].equals(headerEnding)) {
						header = mapFiles[i];
						hasHeader = true;
					}
				}
			}
			
			//Falls Header und Raumdateien gefunden wurden, ist dieses Verzeichnis eine Map
			if(hasRoom && hasHeader)
				mapHeaderFiles.add(header);
		}
		
		
		//Liest Header aus
		File header;
		for(Iterator<File> iter = mapHeaderFiles.iterator(); iter.hasNext();) {
			header = iter.next();
			
			//DOM-doc mit xml-Inhalt erzeugen
			Document doc = null;
			try {
				DocumentBuilder dBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
				doc = dBuilder.parse(header);
			} catch (SAXException | IOException | ParserConfigurationException e) {
				e.printStackTrace();
			}
			doc.getDocumentElement().normalize();
			
			
			/*
			 * Header-Inhalt auslesen
			 */
			
			NodeList headerNodes = doc.getElementsByTagName("header");
			Element headerEl = (Element)headerNodes.item(0);
			
			int type = -1;
			switch(headerEl.getAttribute("typ")) {
			case "kampagne":
				type = MapHeader.STORY_MAP;
				break;
			case "coop":
				type = MapHeader.COOP_MAP;
				break;
			case "deathmatch":
				type = MapHeader.DEATHMATCH_MAP;
				break;
			case "einzelspieler":
				type = MapHeader.CUSTOM_MAP;
				break;
			}
			
			String name = headerEl.getAttribute("name");
			int maxPlayers = Integer.parseInt(headerEl.getAttribute("maxSpieler"));
			int storyID = Integer.parseInt(headerEl.getAttribute("kampagneID"));
			
			headers.add(new MapHeader(name, type, maxPlayers, storyID));
		}
		
		Game.mapHeaders = headers;
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
		 		throw new Exception("Veraltete Einlesemethode");
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
		String[] roomNames = new String[fileArray.length - 1];
		int roomFoundCounter=0;
		for (int i=0; i < fileArray.length; i++) {
			String fileName = fileArray[i].getName();
			String[] fileNameParts = fileName.split("\\.(?=[^\\.]+$)");
			
			if(fileNameParts.length > 1) { //TODO
				switch (fileNameParts[1]) {
					case Map.roomEnding:
						roomNames[roomFoundCounter] = fileNameParts[0];
						roomFoundCounter++;
						break;
					case Map.headerEnding:
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
		spawns.add(spawn);
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
	
	public static void setMapName(String mapName) {
		Map.mapName = mapName;
	}
}
