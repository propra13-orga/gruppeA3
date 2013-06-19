package com.github.propra13.gruppeA3.Entities;

import com.github.propra13.gruppeA3.Map.Position;
import com.github.propra13.gruppeA3.Map.Room;

/**
 * @author Majida Dere
 * Die Klasse Item erzeugt ein Item, welches entweder auf der Map liegt 
 * oder der Spieler mit sich trägt 
 * oder im Shop gekauft werden kann.
 *
 */
public class Item extends Entities {

	/**
	 * Attribute:
	 * 			currentRoom: Der Raum in dem sich das Item befindet
	 * 			pos: Die Position des Items im Raum
	 * 			damage: Bei Waffen: die Schlagkraft
	 * 					Bei Schild und Rüstng: die Schutzkraft
	 * 					Bei Tränken: die Wirkungsmenge
	 * 			type: 	Wert 1: Lebenstrank
	 * 					Wert 2: Gifttrank
	 * 					Wert 3: Manatrank
	 * 					Wert 4: Schild
	 * 					Wert 5: Schwert
	 * 			desc: Die Beschreibung des Items aus der XML
	 * 			name: Der Name des Items aus der XML
	 * 			hitbox: Die Hitbox des Items
	 * 			value: Der Wert, den jedes Item haben kann, wichtig beim Kauf/Verkauf
	 */
	private Room currentRoom;
	private Position pos;
	private int damage = 0;
	private int type=1;
	private String desc=null;
	private String name=null;
	private Hitbox hitbox=null;
	private int value=0;
	
	
	/**
	 * Der Konstruktor erzeugt ein neues Item mit den übergebenen Parametern
	 * @param room_bind Der Raum indem sich das Item befindet
	 * @param damage	Die Wirkungseinheit des Items
	 * @param type		Der Typ des Items
	 * @param x			Der Wert der X-Aches
	 * @param y			Der Wert der Y-Achse
	 * @param desc		Die Beschreibung des Items
	 * @param name		Der Name des Items
	 * @param value		Der Wert des Items
	 * 
	 **/
	public Item(Room room_bind, int damage, int type, int x, int y, String desc, String name, int value) {
		this.currentRoom = room_bind;
		this.damage = damage;
		this.type = type;
		this.desc = desc;
		this.name = name;
		this.hitbox = Hitbox.standard;
		this.pos = new Position(x+(hitbox.width/2),y+(hitbox.height/2));
		this.value = value;
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
	 * @return Liefert den Typen zurück
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
	
	/**
	 * 
	 * @return liefert die Beschreibung des Items
	 */
	public String getDesc(){
		return this.desc;
	}
	
	/**
	 * 
	 * @return liefert den Namen des Items
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 *
	 * @return liefert den Wert des Items zurück
	 */

	public int getValue(){
		return this.value;
	}
	
	/**
	 * Ändert den Wert des Items, meist durch Shopverkäufe
	 * @param value neuer Wert
	 */
	public void setValue(int value){
		this.value = value;
	}
	
	/**
	 * Diese Methode aus Object wird nur für den Shop benutzt
	 * @return Text für den Shop
	 */
	@Override
	public String toString() {
		return "Coins "+getValue()+": "+getName()+" - "+getDesc();
	}
	
	//Dummies
	@Override
	public void tick() {}
	@Override
	public void collision(Entities entity) {}
}
