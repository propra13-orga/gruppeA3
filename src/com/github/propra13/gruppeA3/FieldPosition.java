package com.github.propra13.gruppeA3;

/* Definiert eine Position im Feldkoordinatensystem
 * Felder haben nur eine Feldposition; alle Entities
 * haben außerdem eine Position im Pixelkoordinatensystem
 * 
 * Feldkoords berechnen sich aus Pixelkoords mittels x/32
 */
public class FieldPosition {
		// Mittelpunkt
		public int x;
		public int y;
		
		//Konstruktor
		
		public FieldPosition(int x, int y){
			this.x=x;
			this.y=y;
		}
		
		// vergleicht zwei Positionen auf Gleichheit
		public boolean equals(FieldPosition pos) {
			if (this.x == pos.x && this.y == pos.y)
				return true;
			else
				return false;
		}
		
		public void setPosition(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		// Gibt obere linke Ecke des Felds in Pixelkoords zurück
		public Position toPosition() {
			return new Position(x*32, y*32);
		}
}
