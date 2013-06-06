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
	// Falls das Item ein Trank ist, so kann es auch einen negativen Effekt haben.
	private boolean posionNegativeEffect = false;
	
	/**
	 * @author Majida Dere
	 * @param room_bind
	 * @param pos
	 * @param damage
	 * @param affect
	 */
	public Item(Room room_bind, Position pos, int damage, boolean effect) {
		this.currentroom = room_bind;
		this.pos = pos;
		this.damage = damage;
		this.posionNegativeEffect = effect;
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
	 * Liefert den negativ/positiv Effekt eines Trankes.
	 */
	public boolean getEffect(){
		return this.posionNegativeEffect;
	}

}
