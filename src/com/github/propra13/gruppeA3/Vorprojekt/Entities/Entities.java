package com.github.propra13.gruppeA3.Entities;
import com.github.propra13.gruppeA3.Position;
/** 
 * 
 * @author Majida Dere
 * Diese Klasse liefert alle Methoden, Attribute für alle Objekte im Dungeon
 */

public abstract class Entities {

	// Getter Methoden für alle Entites
	
	abstract Position getPosition();

	// Setter Methoden für alle Entites

	abstract void setPosition(int x, int y);
	
}
