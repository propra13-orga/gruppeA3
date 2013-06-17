package com.github.propra13.gruppeA3.Map;


public class Field {
	
	/* TODO:
	 * Links
	 */
	final static int[] walkables
	= {1, 3, 4, 5, 6, 7}; //TODO: Wasserverhalten klären

	
	//Feldeigenschaften
	public int type;
	public int texture; //für Abwechslung in Feldtexturen
	public int attribute1;
	public int attribute2;
	public int entityType;
	public int entityAttr;
	
	public boolean walkable = false;
	public Link link = null;
	public FieldPosition pos;
	
	private Room room;
	public Trigger trigger;
	
	
	
	public Field(Room room, int type, int texture, int attr1, int attr2, FieldPosition pos) {
		
		this.type = type;
		this.texture = texture;
		this.attribute1 = attr1;
		this.attribute2 = attr2;
		this.pos = pos;
		this.room = room;
		
		//Walkable setzen
		for (int i=0; i < walkables.length; i++) {
			if (walkables[i] == type) {
				walkable = true;
				break;
			}
		}
	}
	
	public void setLink (Link link) {
		this.link = link;
	}
	
	public Room getRoom() {
		return this.room;
	}
}