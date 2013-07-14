package com.github.propra13.gruppeA3.Entities;
import com.github.propra13.gruppeA3.Map.Position;
/** 
 * 
 * @author Majida Dere
 * Dies ist eine abstrakte Klasse für alle Objekte im Spiel
 */


public abstract class Entities {
	
	// Getter Methoden für alle Entites
	
	public abstract Position getPosition();
	public abstract Hitbox getHitbox();
	abstract void tick();
	abstract void collision(Entities entity);

}
