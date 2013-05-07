package com.github.propra13.gruppeA3;

public class Field {
	int x;
	int y;
	boolean hasLink;
	
	/* TODO:
	 * Links
	 * begehbar (statisch gemappt)
	 */
	int type;
	int texture; //für Abwechslung in Feldtexturen
	int attribute1;
	int attribute2;
	int item;
	
	public void setLink (int linkID, int room, boolean bidirectional) {
		/* TODO:
		 * Map vom Link erzählen
		 * Link-Vars setzen
		 */
		
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
		default:
			return "unknown";
		}
	}
	
	public char charMap () {
		switch (this.fieldType()) {
		
		case "unset":
			return 'X';
			
		case "Boden":
			return 'O';
			
		case "Wand":
			return '#';
			
		case "Wasser":
			if (this.attribute1 == 1) return 'H';
			else return '≈';
			
		case "Tuer":
			return '+';
			
		case "Spawn":
			return '%';
			
		default:
			return 'X';
		}
	}
}
