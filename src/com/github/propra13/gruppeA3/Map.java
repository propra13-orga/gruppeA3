package com.github.propra13.gruppeA3;

import com.github.propra13.gruppeA3.Map;
import com.github.propra13.gruppeA3.Field;

public class Map {
	public Room[] mapRooms;
	public Field[] spawns = new Field[2];
	
	public Map(String dirName) {
		readRooms(dirName);
	}

	public void setSpawn() {
		/* TODO:
		 * Spawn nur aus 00-Room akzeptieren
		 */
	}
	//Liest alle Rooms einer Map ein
	private void readRooms(String dirName) {
		/* Ablauf:
		 * Room-Dateien zählen; Array anlegen
		 * Rooms auslesen, Room-Objekte erzeugen
		 * Links zwischen Rooms aufbauen und prüfen
		 * Ziel / Ausgang setzen
		 * Fertig ist die Kuh.
		 */
	}
}
	
