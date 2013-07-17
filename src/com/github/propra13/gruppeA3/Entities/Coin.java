package com.github.propra13.gruppeA3.Entities;

import com.github.propra13.gruppeA3.Map.Position;

/**
 * 
 * @author Majida Dere
 * Diese Klasse erzeugt einen Coin, welcher von einem Monster gedroppt wird.
 * Beim Aufsammeln wird der Wert auf die Geldeinheit vom Spieler hinzuaddiert.
 *
 */
public class Coin extends Entities {

	/**
	 * Attribute:
	 * 			value: Werteinheit der Münze
	 * 			type: Typ der Münze (es kann sein, dass es später verschiedene Münztypen gibt.)
	 * 			pos: Position der Münze im Raum
	 * 			hitbox: Die Hitbox der Münze
	 */
	private int value=0;
	private int type=1;
	private Position pos = null;
	private Hitbox hitbox = null;
	
	/**
	 * Der Konstruktor erzeugt eine neue Münze
	 * @param value Wert der Münze
	 * @param type Typ der Münze
	 * @param pos Position der Münze
	 */
	public Coin(int value, int type, Position pos){
		this.value = value;
		this.type = type;
		this.pos = pos;
		this.hitbox = new Hitbox();
	}

	@Override
	public Position getPosition() {
		return pos;
	}
	
	public void setPosition(Position pos){
		this.pos = pos;
	}
	
	public void setPosition(int x, int y){
		this.pos.x = x;
		this.pos.y = y;
	}

	@Override
	public Hitbox getHitbox() {
		return this.hitbox;
	}
	
	public void setHitbox(Hitbox hitbox){
		this.hitbox = hitbox;
	}

	@Override
	public void tick() {		
	}

	@Override
	void collision(Entities entity) {
	}

	/**
	 * Wird benutzt um diesen Wert dem Spieler zuzuweisen
	 * @return Wert
	 */
	public int getValue(){
		return value;
	}
	
	/**
	 * Wird verwendet, falls später zwischen Cointypen unterschieden werden soll.
	 * @return
	 */
	public int getType(){
		return type;
	}

}
