package com.github.propra13.gruppeA3;

public class Field {
	
	/* TODO:
	 * Links
	 */
	final static int[] walkables
	= {1, 3, 4, 5}; //TODO: Wasserverhalten klären

	
	//Feldeigenschaften
	public int type;
	public int texture; //für Abwechslung in Feldtexturen
	public int attribute1;
	public int attribute2;
	public int entityType;
	public int entityAttr;
	
	public boolean walkable = false;
	public Link link = null;
	public Position pos;
	
	
	
	public Field(
			int type, int texture, int attr1, int attr2, Position pos) {
		
		this.type = type;
		this.texture = texture;
		this.attribute1 = attr1;
		this.attribute2 = attr2;
		this.pos = pos;
		
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
	
	public String fieldType () {
		switch (this.type) {
		case 0:
			return "unset";
		case 1:
			return "Boden";
		case 2:
			return "Wand";
		case 3:
			return "Wasser";
		case 4:
			return "Tuer";
		case 5:
			return "Link";
		default:
			return "unknown";
		}
	}
	
	public char charMap () {
		switch (this.fieldType()) {
		
		case "unset":
			return 'X';
			
		case "Boden":
			if (this.attribute1 == 2)
				return '$';
			else
				return 'O';
			
		case "Wand":
			return '#';
			
		case "Wasser":
			if (this.attribute1 == 1) return 'H';
			else return '�';
			
		case "Tuer":
			return '+';
			
		case "Link":
			return '%';
			
		default:
			return 'X';
		}
	}
}