package com.github.propra13.gruppeA3.Entities;

import com.github.propra13.gruppeA3.Position;
import com.github.propra13.gruppeA3.Room;

/**
 * @author Majida Dere
 * Die Klasse Monster liefert hauptsächlich die zugehörigen getter und setter Methoden zur Entity Monster
 */

public class Monster extends Moveable {

	// Attribute
	
	private double speed;
	private Position pos;
	private int power;
	private int life;
	private String desc;
	
	private Hitbox hitbox;
	
	//Konstruktor
	
	public Monster (Room room_bind, double speed, int power, int type, int life, Position pos, Hitbox hitbox, String desc){
		super(room_bind);
		System.out.println("Ich bin ein Monster!"); //Offenbar nicht erreichbar
		this.speed=speed;
		this.power=power;
		this.life=life;
		this.pos=pos;
		this.desc = desc;
		this.hitbox = hitbox;
	}
	
	
	// Getter Methoden
	
	public Hitbox getHitbox() {
		return hitbox;
	}
	
	/** liefert die Schnelligkeit eines Monsters
	 * @return gibt die Schnelligkeit zurück
	 */
	public double getSpeed(){
		return this.speed;
	}
	
	/**
	 * liefert die Kampfkraft eines Monsters
	 * @return gibt die Kampfkraft zurück
	 */
	public int getPower() {
		return this.power;
	}
	
	/**
	 * liefert die Anzahl der Monsterleben, e.g. wie oft ein Monster sterben darf, bis es endgültig tot ist.
	 * @return gibt die Anzahl der Monsterleben zurück
	 */
	public int getLife() {
		return this.life;
	}
	
	/**
	 * liefert die Position des Monsters im Raum
	 * @return die Raumposition
	 */
	@Override
	public Position getPosition() {
		return pos;
	}

	
	
	// Setter-Methoden
	
	/**
	 * legt die Position des Monsters fest
	 * @ param x Position auf der x Achse
	 * @ param y Position auf der y Achse
	 */
	@Override
	public void setPosition(int x, int y) {
		this.pos.x = x;
		this.pos.y = y;
	}
	
	/**
	 * legt die Monstergeschwindigkeit fest
	 * @param speed Monstergeschwindigkeit
	 */
	
	public void setSpeed(double speed){
		this.speed = speed;
	}
	
	/**
	 * legt die Kampfkraft des Monsters fest
	 * @param power Kampfkraft
	 */
	
	public void setPower(int power){
		this.power = power;
	}
	
	/**
	 * legt die Anzahl der Monsterleben fest
	 * @ param life Monsterleben
	 */
	public void setLife(int life){
		this.life = life;
	}
}
