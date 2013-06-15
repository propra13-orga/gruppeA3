package com.github.propra13.gruppeA3.Entities;

import com.github.propra13.gruppeA3.Map.Field;
import com.github.propra13.gruppeA3.Map.Room;

public abstract class Projectile extends Moveable {

	public Projectile(Room room_bind, int damage) {
		super(room_bind);
	}
	
	public abstract int getDamage();
	
	public void collision(Entities entity) {
		if(entity instanceof Monster) {
			entity = (Monster)entity;
			entity.setHealth(entity.getHealth() - getDamage());
			getRoom().entities.remove(this);
		}
	}
	
	public void terminate() {
		getRoom().entities.remove(this);
	}
	
	public void tick() {
		move();
		// falls am Raumrand, Projektil auflösen
		if (	pos.getCornerTopLeft(hitbox).x <= 0 ||
				pos.getCornerTopLeft(hitbox).y <= 0 || 
				pos.getCornerTopRight(hitbox).x >= getRoom().getWidth()*32 ||
				pos.getCornerBottomLeft(hitbox).y >= getRoom().getHeight()*32)
		{
			System.out.println("Ich bin ein Projektil und löse mich auf.");
			getRoom().entities.remove(this);
		}
		System.out.println("Ich bin ein Projektil! Ich bewege mich nach "+this.pos.x+":"+this.pos.y);
	}
}
