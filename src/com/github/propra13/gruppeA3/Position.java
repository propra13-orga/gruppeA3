package com.github.propra13.gruppeA3;

import com.github.propra13.gruppeA3.Entities.Hitbox;

/** @author Christian Krüger
 * Definiert eine Position auf dem Pixelkoordinatensystem
 */
public class Position {
	// Position im Pixelkoordsys
	public int x;
	public int y;
	
	public Position(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	/** 
	 * Berechnet Pos im Feldkoordsys aus Pos im Pixelkoordsys
	 * @param pos Umzurechnende Position
	 * @return gibt Position im Feldkoordsys zurück
	 */
	public FieldPosition toFieldPos() {
		FieldPosition fieldpos = new FieldPosition(x/32, y/32);
		return fieldpos;
	}
	
	/**
	 * Berechnet Position von Ecken aus gegebener Hitbox
	 * aus gegebener Hitbox
	 */
	public Position getDrawPosition(Hitbox hitbox) {
		return getCornerTopLeft(hitbox);
	}
	
	public Position getCornerTopLeft(Hitbox hitbox) {
		int x, y;
		x = this.x - hitbox.width/2;
		y = this.y - hitbox.height/2;
		return new Position(x, y);
	}

	public Position getCornerTopRight(Hitbox hitbox) {
		int x, y;
		x = this.x + hitbox.width/2;
		y = this.y - hitbox.height/2;
		return new Position(x, y);
	}

	public Position getCornerBottomRight(Hitbox hitbox) {
		int x, y;
		x = this.x + hitbox.width/2;
		y = this.y + hitbox.height/2;
		return new Position(x, y);
	}
	
	public Position getCornerBottomLeft(Hitbox hitbox) {
		int x, y;
		x = this.x - hitbox.width/2;
		y = this.y + hitbox.height/2;
		return new Position(x, y);
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
}
