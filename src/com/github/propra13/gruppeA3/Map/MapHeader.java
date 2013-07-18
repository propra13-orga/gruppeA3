package com.github.propra13.gruppeA3.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class MapHeader {

	public String mapName;
	public int maxPlayers;
	
	/**Map-Typ*/
	public int type;
	
	/**Map-Typ-Konstanten*/
	public final static int STORY_MAP = 1;
	public final static int CUSTOM_MAP = 2;
	public final static int DEATHMATCH_MAP = 3;
	public final static int COOP_MAP = 4;
	
	public int storyID;
	
	/**
	 * Erstellt einen MapHeader.
	 * @param name Name der Map
	 * @param type Typ der Map (siehe Konstanten)
	 * @param maxPlayers Maximale Anzahl an Spielern, die auf der Karte Platz finden (Anzahl der Spawns)
	 * @param storyID Nummer der Karte in der Kampagnenkartenreihenfolge. Falls die Karte keine Kampagnenkarte ist, ist dieses Feld irrelevant.
	 */
	public MapHeader(String name, int type, int maxPlayers, int storyID) {
		mapName = name;
		this.maxPlayers = maxPlayers;
		this.type = type;
		
		if (type != STORY_MAP)
			storyID = -1;
		this.storyID = storyID;
	}
	
	/**
	 * Erstellt einen MapHeader aus einem gegebenen DOM-Doc-Element.
	 * @param element Header-DOM-Doc-Element
	 */
	public MapHeader(Element element) {
		switch(element.getAttribute("typ")) {
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
		default:
			type = -1;
			break;
		}
		
		mapName = element.getAttribute("name");
		maxPlayers = Integer.parseInt(element.getAttribute("maxSpieler"));
		storyID = Integer.parseInt(element.getAttribute("kampagneID"));
	}
	
	/**
	 * Gibt zurück, ob ein gegebener Header diesem gleicht.
	 * @param header auf Gleichheit zu prüfender Header.
	 */
	public boolean equals(MapHeader header) {
		if(header.mapName.equals(mapName) &&
				header.maxPlayers == maxPlayers &&
				header.type == type &&
				header.storyID == storyID)
			return true;
		else
			return false;
		
	}
	
	/**
	 * Fügt dem gegebenen DOM-Element ein Child-Element hinzu, das diesen Header repräsentiert.
	 * @param element Element, dem das Header-Element hinzugefügt werden soll.
	 */
	public void appendToDoc(Element element) {

		Document doc = element.getOwnerDocument();
		Element headerEl = doc.createElement("header");
		element.appendChild(headerEl);
		
		String typeString = null;
		switch(type) {
		case MapHeader.STORY_MAP:
			typeString = "kampagne";
			break;
		case MapHeader.CUSTOM_MAP:
			typeString = "einzelspieler";
			break;
		case MapHeader.DEATHMATCH_MAP:
			typeString = "deathmatch";
			break;
		case MapHeader.COOP_MAP:
			typeString = "coop";
			break;
		}
		
		headerEl.setAttribute("typ", typeString);
		headerEl.setAttribute("name", mapName);
		headerEl.setAttribute("maxSpieler", Integer.toString(maxPlayers));
		headerEl.setAttribute("kampagneID", Integer.toString(storyID));
	}
}
