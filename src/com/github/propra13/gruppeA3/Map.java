package com.github.propra13.gruppeA3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;
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
	static public Field end = null;
	
	final static String roomEnding = "room";
	final static String metaEnding = "xml";
	
	private static LinkedList linkBuffer = new LinkedList();
	
	
	
	/* Baut Map aus gegebenem Verzeichnisnamen.
	 * Interpretiert alle durchnummerierten "xy.room"-Dateien als Rooms.
	 */
	public Map(String dirName) 
			throws FileNotFoundException, MapFormatException, IOException, InvalidRoomLinkException {
		
		spawns[0] = spawns[1] = null;
		mapRooms = readRooms(dirName);
		checkLinks();
		
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
	
	//Überprüft die Links zwischen Rooms
	public void checkLinks() throws InvalidRoomLinkException {
		
	}
	
	/* Fügt einen Link zum Link-Buffer hinzu
	 * Link-Buffer wird später ausgelesen und die Links ordentlich zusammengestellt
	 */
	public static void setLink(Link link) {
		linkBuffer.add(link);
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
}
