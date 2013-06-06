package com.github.propra13.gruppeA3.Entities;

import com.github.propra13.gruppeA3.Position;

public class Item extends Entities {
	// Vorbereitung für Item-Klasse
	
	public int power;
	public int type;
	Position pos = new Position(0, 0);
	
	public Item (int type, int power, Position pos) {
		this.type = type;
		this.power = power;
		this.pos = pos;
	}

	@Override
	Position getPosition() {
		return pos;
	}

	public void setPosition(int x, int y) {
		this.pos.changePosition(x, y);
	}

}
