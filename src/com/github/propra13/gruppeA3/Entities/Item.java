/**
 * 
 */
package com.github.propra13.gruppeA3.Entities;

import com.github.propra13.gruppeA3.Map.FieldPosition;
import com.github.propra13.gruppeA3.Map.Position;
import com.github.propra13.gruppeA3.Map.Room;

/**
 * @author Majida Dere
 *
 */
public class Item extends Entities {

	private Room currentroom;
	private Position pos;
	// bei Waffen die Schlagkraft, bei Tränken die Wirkungsmenge
	private int damage = 0;
	// muss noch spezifiziert werden: bei Tränken (Mana, Gift, Leben), Schild und Schwert
	// Vorerst: 1 Leben, 2 Gift, 3 Mana, 4 Schwert, 5 Schild, weitere folgen.
	private int type;
	private String desc;
	private Hitbox hitbox;
	
	/**
	 * @author Majida Dere
	 * @param room_bind
	 * @param pos
	 * @param damage
	 * @param affect
	 */
	public Item(Room room_bind, int damage, int type, int x, int y, String desc) {
		this.currentroom = room_bind;
		this.damage = damage;
		this.type = type;
		this.desc = desc;
		this.hitbox = Hitbox.standard;
		this.pos = new Position(x+(hitbox.width/2),y+(hitbox.height/2));
	}

	@Override
	public Position getPosition() {
		return pos;
	}

	/**
	 * Liefert die Wirkungseinheit des Items
	 */
	public int getDamage() {
		return this.damage;
	}
	
	/**
	 * Liefert den Typen zurück
	 */
	public int getType(){
		return this.type;
	}
	
	public Hitbox getHitbox(){
		return this.hitbox;
	}

	public void setHealth(int health){
	}
	
	public int getHealth(){
		return 0;
	}
}
