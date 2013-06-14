package com.github.propra13.gruppeA3.Entities;

import com.github.propra13.gruppeA3.Map.Position;
import com.github.propra13.gruppeA3.Map.Room;

public abstract class PlasmaBall extends Projectile {
	
	final static int damage = 5;
	Position pos;
	public PlasmaBall(Room room_bind, Position pos, Moveable.Direction direct) {
		super(room_bind, damage);
		this.pos = pos;
		this.direct = direct;
		room_bind.entities.add(this);
	}
}
