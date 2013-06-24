package com.github.propra13.gruppeA3.Map;

import com.github.propra13.gruppeA3.Exceptions.MapFormatException;


public class Link {
	public int ID;
	public Room[] targetRooms = new Room[2];
	public Field[] targetFields = new Field[2];
	boolean bidirectional;
	private boolean isActivated = true;
	
	// Temporäre Variablen
	public int targetRoomID;
	public FieldPosition pos;
	
	
	/** Konstruktor für einen "halben" Link
	 * Situation:
	 * 	Ein Raum wird ausgelesen und ein Link gefunden. Ganze Links kennen beide Seiten,
	 * 	beim Auslesen sind jedoch noch nicht alle Räume bekannt. Daher werden provisorisch
	 * 	halbe Links gebaut, aus denen später ganze Links werden.
	 * 
	 * Parameter:
	 * Link-ID; Zielraum; Zielfeld im Zielraum; Position des halben Links; ist Checkpointlink?
	 */
	public Link(int ID, int targetRoom, FieldPosition position, boolean isCheckpoint) {
		if (ID < 0)
			try {
				throw new MapFormatException("Negative Link-ID: "+ID+" auf "+targetRoom+":"+position);
			} catch (MapFormatException e) {
				e.printStackTrace();
			}
		else
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
	
	 /**
	  * Konstruktor für einen ganzen Link
	  * Link ist beiden Zielräumen bekannt
	  */
	public Link(int ID, Room[] targets, Field[] target_Fields, boolean bidirectional, boolean isCheckpoint) {
		if (ID < 0)
			try {
				throw new MapFormatException("Negative Link-ID: "+ID+" auf "+targets[0]+":"+target_Fields[0].pos);
			} catch (MapFormatException e) {
				e.printStackTrace();
			}
		else
			this.ID = ID;
		this.targetRooms = targets;
		this.targetFields = target_Fields;
		this.bidirectional = bidirectional;
		isActivated = ! isCheckpoint;
	}
	
	/**
	 * @return Gibt zurück, ob der Link passierbar ist
	 */
	public boolean isActivated() {
		return isActivated;
	}
	
	/** 
	 * Schaltet ggf. Checkpointlink frei
	 */
	public void activate() {
		isActivated = true;
	}
	
	
	/*
	 * Map-Editor-Methoden
	 */
	
	public void setCheckpoint(boolean isCheckpoint) {
		isActivated = ! isCheckpoint;
	}
	
	/**
	 * Ändert die Link-Attribute
	 * @param link Link, dessen Attribute übernommen werden sollen
	 */
	public void edit(Link link) {
		this.ID = link.ID;
		this.targetRooms = link.targetRooms;
		this.targetFields = link.targetFields;
		this.bidirectional = link.bidirectional;
		isActivated = link.isActivated();
	}
	
	/**
	 * Klont den Link
	 * @return Gibt eine Kopie des Links zurück
	 */
	public Link clone() {
		//Bei halben Links sind die target-Arrays nicht gefüllt
		if (targetRooms[0] == null)
			try {
				throw new Exception("clone() ist für halbe Links nicht definiert");
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		//Konstruktor vorbereiten
		Room[] targetRooms = new Room[2];
		targetRooms[0] = this.targetRooms[0];
		targetRooms[1] = this.targetRooms[1];
		
		Field[] targetFields = new Field[2];
		targetFields[0] = this.targetFields[0];
		targetFields[1] = this.targetFields[1];
		
		return new Link(ID, targetRooms, targetFields, true, ! isActivated);
		
	}
	
}
