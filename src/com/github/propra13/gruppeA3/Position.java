package com.github.propra13.gruppeA3;

public class Position {
	/**
	 * 
	 * @author Majida Dere
	 *
	 */
		// Hat nur zwei Attribute: int x und int y
		public int x;
		public int y;
		
		//Konstruktor
		
		public Position(int x, int y){
			this.x=x;
			this.y=y;
		}
		
		// vergleicht zwei Positionen auf Gleichheit
		public boolean equals(Position pos) {
			if (this.x == pos.x && this.y == pos.y)
				return true;
			else
				return false;
		}
}
