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
	 * @author Christian Krüger
	 */
	
	public Room[] mapRooms;
	public Field[] spawns = new Field[2];
	public Field end = null;
	
	final static String roomEnding = "room";
	final static String metaEnding = "xml";
	
	public Map(String dirName)
			throws MapFormatException, IOException, InvalidRoomLinkException {
		
		readRooms(dirName);
		try {
			checkLinks();
		} catch (InvalidRoomLinkException e) {
			throw e;
		}
		
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
	
	//Überprüft die Links zwischen Rooms
	public void checkLinks() throws InvalidRoomLinkException {
		
	}
	
	//Liest alle Rooms einer Map ein
	private Room[] readRooms(String mapName) throws MapFormatException, IOException {
		/* TODO:
		 * Map-Zip
		 */
		
		//Sammle Map-Dateien
		String dir = System.getProperty("user.dir");
		dir = dir + File.separator + "maps";
		File f = new File(dir);
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
		Room mapRooms[] = new Room[roomNames.length];
		for (int i=0; i < roomNames.length; i++) {
			try {
				mapRooms[i] = new Room(i, roomNames[i] + "." + roomEnding);
			} catch (FileNotFoundException e) {
				throw e;
			} catch (IOException e) {
				throw e;
			}
		}
		
		return mapRooms;
	}
}
	
