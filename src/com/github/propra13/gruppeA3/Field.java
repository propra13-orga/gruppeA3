package com.github.propra13.gruppeA3;

public class Field {
	int x;
	int y;
	int type;
	int attribute;
	int item;
	
	public String mapType () {
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
			return "Spawn";
		default:
			return "unknown";
		}
	}
	
	public char charMap () {
		switch (this.mapType()) {
		
		case "unset":
			return 'X';
			
		case "Boden":
			return 'O';
			
		case "Wand":
			return '#';
			
		case "Wasser":
			if (this.item == 1) return 'H';
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
