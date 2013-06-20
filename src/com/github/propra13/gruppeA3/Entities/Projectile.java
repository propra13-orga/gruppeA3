package com.github.propra13.gruppeA3.Entities;

import com.github.propra13.gruppeA3.Map.Room;

public abstract class Projectile extends Moveable {
	
	Moveable.Direction direct1;
	Moveable.Direction direct2;

	/**
	 * Schießt Projektil durch die Gegend
	 * @param room_bind Raum
	 * @param damage	Schaden, den das Projektil verursacht
	 * @param direct1	Richtung 1
	 * @param direct2	Richtung 2 (R1 != R2 für diagonale Schüsse; R1 = R2 für orthogonale Schüsse)
	 */
	public Projectile(Room room_bind, Moveable.Direction direct1, Moveable.Direction direct2) {
		super(room_bind);
		this.direct1 = direct1;
		this.direct2 = direct2;
		super.direct = direct1;
	}
	
	public abstract int getDamage();
	
	public void collision(Entities entity) {
		if(entity instanceof Monster) {
			entity = (Monster)entity;
			entity.setHealth(entity.getHealth() - getDamage());
			terminate();
		}
	}
	
	public void terminate() {
		getRoom().removeCandidates.add(this);
	}
	
	public void tick() {
		
		// Richtung wechseln zwischen direct1 und direct2
		if (super.direct == direct1)
			super.direct = direct2;
		else
			super.direct = direct1;
		
		move();
		// falls am Raumrand, Projektil auflösen
		if (	pos.getCornerTopLeft(hitbox).x <= 0 ||
				pos.getCornerTopLeft(hitbox).y <= 0 || 
				pos.getCornerTopRight(hitbox).x >= getRoom().getWidth()*32 ||
				pos.getCornerBottomLeft(hitbox).y >= getRoom().getHeight()*32)
		{
			terminate();
		}
	}
}
