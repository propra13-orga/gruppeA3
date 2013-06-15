package com.github.propra13.gruppeA3.Entities;

import java.util.Iterator;
import java.util.LinkedList;

import com.github.propra13.gruppeA3.Map.Room;

public abstract class Projectile extends Moveable {
	
	Moveable.Direction direct1;
	Moveable.Direction direct2;
	
	// Aus Synchrogründen werden hier Projektile, die zu entfernen sind, gespeichert
	private static LinkedList<Projectile> removeCandidates = new LinkedList<Projectile>();

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
			getRoom().entities.remove(this);
		}
	}
	
	public void terminate() {
		removeCandidates.add(this);
	}
	
	public static void removeProjectiles(Room room) {
		Projectile toRemove = null;
		Iterator<Projectile> itprj = removeCandidates.iterator();
		while (itprj.hasNext()) {
			toRemove = itprj.next();
			Entities testent = null;	//durch alle Entitys der Liste iterieren
		    Iterator<Entities> itent = room.entities.iterator();
			while(itent.hasNext()){
				testent = itent.next();
				if(testent == toRemove)
					itent.remove();
			}
		}
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
			System.out.println("Ich bin ein Projektil und löse mich auf.");
			getRoom().entities.remove(this);
		}
		System.out.println("Ich bin ein Projektil! Ich bewege mich nach "+this.pos.x+":"+this.pos.y);
	}
}
