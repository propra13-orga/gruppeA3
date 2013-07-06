package com.github.propra13.gruppeA3.Entities;
import com.github.propra13.gruppeA3.Map.Field;
import com.github.propra13.gruppeA3.Map.Position;
import com.github.propra13.gruppeA3.Map.Room;
/** 
 * 
 * @author Majida Dere
 * Diese Klasse liefert alle Methoden, Attribute für alle Objekte im Dungeon
 */


public abstract class Entities {
	
	protected Room room;

	// Getter Methoden für alle Entites
	
	public abstract Position getPosition();
	public abstract Hitbox getHitbox();
	abstract void setHealth(int health);
	abstract int getHealth();
	abstract void tick();
	abstract void collision(Entities entity);
	
	public Entities(Room room_bind) {
		this.room = room_bind;
	}
	
	public Room getRoom() {
		return room;
	}
	
	public Field getField() {
		return room.getField(getPosition());
	}
}
