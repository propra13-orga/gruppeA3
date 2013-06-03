package com.github.propra13.gruppeA3;

public class Link {
	int ID;
	public Room[] targetRooms = new Room[2];
	public Field[] targetFields = new Field[2];
	boolean bidirectional;
	private boolean isActivated = true;
	
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
	 * Link-ID; Zielraum; Zielfeld im Zielraum; Position des halben Links; ist Checkpointlink?
	 */
	public Link(int ID, int targetRoom, Position position, boolean isCheckpoint) {
		this.ID = ID;
		this.targetRoomID = targetRoom;
		this.pos = position;
		
		// Setzt Möglichkeit, ob dieser Link genutzt werden kann
		if (targetRoom == 254)
			this.bidirectional = false;
		else
			this.bidirectional = true;
		
		//Setzt isActivated auf false, falls Link Checkpointlink ist 
		isActivated = ! isCheckpoint;
		
		Map.addLink(this);
	}
	
	 /* Konstruktor für einen ganzen Link
	  * Ist beiden Zielräumen bekannt
	  */
	public Link(int ID, Room[] targets, Field[] target_Fields, boolean bidirectional, boolean isCheckpoint) {
		this.ID = ID;
		this.targetRooms = targets;
		this.targetFields = target_Fields;
		this.bidirectional = bidirectional;
		isActivated = ! isCheckpoint;
	}
	
	public boolean isActivated() {
		return isActivated;
	}
	
	// Schaltet ggf. Checkpointlink frei
	public void activate() {
		System.out.println(this.targetRooms[0].ID);
		System.out.println(this.targetRooms[1].ID);
		isActivated = true;
	}
}
