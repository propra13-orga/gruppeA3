/**
 * 
 */
package com.github.propra13.gruppeA3.Entities;

import com.github.propra13.gruppeA3.Position;
import com.github.propra13.gruppeA3.Room;

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
	
	/**
	 * @author Majida Dere
	 * @param room_bind
	 * @param pos
	 * @param damage
	 * @param affect
	 */
	public Item(Room room_bind, int damage, int type, int x, int y, String desc) {
		this.currentroom = room_bind;
		this.pos = new Position(x,y);
		this.damage = damage;
		this.type = type;
		this.desc = desc;
	}

	@Override
	public Position getPosition() {
		return pos;
	}

	@Override
	public void setPosition(int x, int y) {
		this.pos.x = x;
		this.pos.y = y;
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

}