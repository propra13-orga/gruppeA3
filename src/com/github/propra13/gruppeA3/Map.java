package com.github.propra13.gruppeA3;

import com.github.propra13.gruppeA3.Map;

public class Map {
	int maxPlayers;
	Room[] mapRooms;
	
	public Map(int e) {
		readRooms();
	}

	//Liest alle Rooms einer Map ein
	private void readRooms() {
		/* Ablauf:
		 * Room-Dateien zählen; Array anlegen
		 * Rooms auslesen, Room-Objekte erzeugen
		 * Links zwischen Rooms aufbauen und prüfen
		 * (Links sind zwingend bidirektional)
		 * Absolute Spawns und maxPlayers setzen
		 * Ziel / Ausgang setzen
		 * Fertig ist die Kuh.
		 */
	}
}
	
