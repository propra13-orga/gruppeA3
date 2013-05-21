package com.github.propra13.gruppeA3;

public class Link {
	int ID;
	Room[] targetRooms = new Room[2];
	Field[] targetFields = new Field[2];
	boolean bidirectional;
	
	// Temporäre Variablen
	int targetRoomID;
	Position pos;
	
	
	/* Konstruktor für einen "halben" Link
	 * Situation:
	 * 	Ein Raum wird ausgelesen und ein Link gefunden. Ganze Links kennen beide Seiten,
	 * 	beim Auslesen sind jedoch noch nicht alle Räume bekannt. Daher werden provisorisch
	 * 	halbe Links gebaut, aus denen später ganze Links werden.
	 * 
	 * Parameter:
	 * Link-ID; Zielraum; Zielfeld im Zielraum
	 */
	public Link(int ID, int targetRoom, Position position) {
		this.ID = ID;
		this.targetRoomID = targetRoom;
		this.pos = position;
		
		// Setzt Möglichkeit, ob dieser Link genutzt werden kann
		if (targetRoom == 254)
			this.bidirectional = false;
		else
			this.bidirectional = true;
	}
	
	 /* Konstruktor für einen ganzen Link
	  * Ist beiden Zielräumen bekannt
	  */
	public Link(int ID, Room[] targetRooms, Field[] targetFields, boolean bidirectional) {
		this.ID = ID;
		this.targetRooms = targetRooms;
		this.targetFields = targetFields;
		this.bidirectional = bidirectional;
	}
}
