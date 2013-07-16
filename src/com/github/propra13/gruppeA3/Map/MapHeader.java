package com.github.propra13.gruppeA3.Map;

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
	
	public MapHeader(String name, int type, int maxPlayers, int storyID) {
		mapName = name;
		this.maxPlayers = maxPlayers;
		this.type = type;
		
		if (type != STORY_MAP)
			storyID = -1;
		this.storyID = storyID;
	}
}
