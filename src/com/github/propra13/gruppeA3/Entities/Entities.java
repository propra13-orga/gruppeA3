package com.github.propra13.gruppeA3.Entities;
import com.github.propra13.gruppeA3.Map.Position;
/** 
 * 
 * @author Majida Dere
 * Diese Klasse liefert alle Methoden, Attribute für alle Objekte im Dungeon
 */


public abstract class Entities {
	

	// Getter Methoden für alle Entites
	
	public abstract Position getPosition();
	public abstract Hitbox getHitbox();
	abstract void setHealth(int health);
	abstract int getHealth();
	abstract void tick();
	abstract void collision(Entities entity);
}
