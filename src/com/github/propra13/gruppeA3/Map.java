package com.github.propra13.gruppeA3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;

import com.github.propra13.gruppeA3.Map;
import com.github.propra13.gruppeA3.Field;
import com.github.propra13.gruppeA3.Exceptions.*;

public class Map {
	/**
	 * @author Christian Krüger
	 * TODO:
	 * Links bauen.
	 * Vorgehensweise:
	 * Links zählen, Array mit passender Größe anlegen.
	 */
	
	static public Room[] mapRooms;
	static public Field[] spawns = new Field[2];
	static public Link[] links;
	
	final static String roomEnding = "room";
	final static String metaEnding = "xml";
	
	private static LinkedList<Link> linkBuffer = null;
	
	
	
	/* Baut Map aus gegebenem Verzeichnisnamen.
	 * Interpretiert alle durchnummerierten "xy.room"-Dateien als Rooms.
	 */
	public Map(String dirName) 
			throws FileNotFoundException, MapFormatException, IOException, InvalidRoomLinkException {
		
		spawns[0] = spawns[1] = null;
		mapRooms = readRooms(dirName);
		buildLinks();
		checkLinks();
		linkBuffer = new LinkedList<Link>();
	}
	
	
	/* Liest alle Rooms einer Map ein
	 * Vorgehensweise:
	 * Sammelt aus gegebenem Map-Verzeichnis alle .room-Dateien
	 * Erzeugt Room-Objekte aus .room-Dateien
	 */
	private Room[] readRooms(String mapName)
			throws FileNotFoundException, MapFormatException, IOException {
		/* TODO:
		 * Map-Zip
		 */
		
		//Sammle Map-Dateien
		String dir = System.getProperty("user.dir");
		dir = dir/* + "maps"*/ + File.separator + mapName;
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
		
		
		//Room-Namen-Check
		Arrays.sort(roomNames);
		for (int i=0; i < roomNames.length; i++) {
			int compare = Integer.parseInt(roomNames[i]);
			if (compare != i) {
				throw new MapFormatException("Non-standard room name found: \"" + roomNames[i] + "\"");
			}
		}
		
		//Baue Rooms auf
		Room[] mapRooms = new Room[roomNames.length];
		for (int i=0; i < roomNames.length; i++) {
			mapRooms[i] = new Room(i, dir + File.separator + roomNames[i] + "." + roomEnding);
		}
		
		return mapRooms;
	}
	
	
	
	/* Baut Links aus Link-Buffer auf
	 * Vorgehensweise:
	 * 	Sucht Link mit der höchsten ID und baut Array mit passender Größe auf
	 * 	Befüllt Array zweidimensional (Jeder Arrayplatz enthält zwei zusammengehörende halbe Links)
	 * 	Baut aus zwei zusammengehörenden halben Links einen ganzen Link
	 * 	Teilt beiden Zielräumen den Link mit und fügt den Link in die Linkliste der Map ein
	 * 	Überprüft alle Links auf Konsistenz (evtl. auslagern auf Link-Klasse)
	 */
	public void buildLinks() throws InvalidRoomLinkException {
/**
		// Sucht Link mit der höchsten ID
		int highID = 0;
		for (Iterator<Link> i = linkBuffer.iterator(); i.hasNext();) {
			//TODO: LinkedList.next() checken (erstes Element abgedeckt?)
			if (i.next().ID > highID)
				highID = i.next().ID;
		}
		
		System.out.println(highID);
		// Lädt halbe Links in zweidim. Array
		Link[][] halfLinks;
		halfLinks = new Link[highID + 1][2];
		
		//TODO: pop()
		// Funktioniert im Augenblick nicht, wird überarbeitet.
		for (Iterator<Link> i = linkBuffer.iterator(); i.hasNext();) {
			Link link = i.next();
			
			if (halfLinks[link.ID][0] == null)
				halfLinks[link.ID][0] = link;
			else if (halfLinks[link.ID][1] == null)
				halfLinks[link.ID][1] = link;
			else
				throw new InvalidRoomLinkException("dritten halben Link gefunden; Link-ID: " + link.ID);
		}
		linkBuffer.clear();
		
		// Fügt halbe Links zu ganzen zusammen
		for (int i=0; i < halfLinks.length; i++) {
			
			Room[] targetRooms = new Room[2];
			Field[] targetFields = new Field[2];
			
			//Setzt bidirectional
			boolean bidirectional = true;
			if (halfLinks[i][0].bidirectional == false || halfLinks[i][0].bidirectional == false )
				bidirectional = false;
			
			//Setzt targetRooms
			targetRooms[0] = mapRooms[halfLinks[i][0].ID];
			targetRooms[1] = mapRooms[halfLinks[i][1].ID];
			
			//Setzt targetFields
			
			
			Link link = new Link(i, targetRooms, targetFields, bidirectional);
			
		}
		
	**/	
	}
	
	//Überprüft die Links zwischen Rooms auf Konsistenz
	public void checkLinks() throws InvalidRoomLinkException {
		
	}
	
	/* Fügt einen Link zum Link-Buffer hinzu
	 * Link-Buffer wird später ausgelesen und die Links ordentlich zusammengestellt
	 */
	public static void setLink(Link link) {
		linkBuffer.push(link);
	}

	//Setzt einen Spawn auf der Map
	public void setSpawn(Field spawn) {
		/* TODO:
		 * Spawn nur aus 00-Room akzeptieren
		 */
	}
	
	//Setzt Ziel der Map
	public void setEnd(Field end) {
		
	}
	
	/**
	 * @author Majida Dere
	 * @param roomID - gibt die ID des aktuellen Raumes an
	 * @return liefert den aktuellen Raum
	 */
	public Room getMapRoom(int roomID){
		return this.mapRooms[roomID];
	}
}
