package com.github.propra13.gruppeA3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import com.github.propra13.gruppeA3.Map;
import com.github.propra13.gruppeA3.Field;
import com.github.propra13.gruppeA3.Exceptions.*;

public class Map {
	/**
	 * @author Christian Kr�ger
	 * TODO:
	 * Links bauen.
	 * Vorgehensweise:
	 * Links z�hlen, Array mit passender Gr��e anlegen.
	 */
	
	public Room[] mapRooms;
	public Field[] spawns = new Field[2];
	public Field end = null;
	
	final static String roomEnding = "room";
	final static String metaEnding = "xml";
	
	/* Baut Map aus gegebenem Verzeichnisnamen.
	 * Interpretiert alle durchnummerierten "xy.room"-Dateien als Rooms.
	 */
	public Map(String dirName) 
			throws FileNotFoundException, MapFormatException, IOException, InvalidRoomLinkException {
		
		this.spawns[0] = this.spawns[1] = null;
		this.mapRooms = readRooms(dirName);
		checkLinks();
		
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
	
	//�berpr�ft die Links zwischen Rooms
	public void checkLinks() throws InvalidRoomLinkException {
		
	}
	
	
	//Liest alle Rooms einer Map ein
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
					 * Datei mit Metainformationen
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
}
