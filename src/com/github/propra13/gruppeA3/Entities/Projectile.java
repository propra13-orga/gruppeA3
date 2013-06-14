package com.github.propra13.gruppeA3.Entities;

import com.github.propra13.gruppeA3.Map.Room;

public abstract class Projectile extends Moveable {

	public Projectile(Room room_bind, int damage) {
		super(room_bind);
	}
	
	public abstract int getDamage();
}
