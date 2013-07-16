package com.github.propra13.gruppeA3.Entities;

import com.github.propra13.gruppeA3.Entities.Moveable.Element;
import com.github.propra13.gruppeA3.Map.FieldPosition;
import com.github.propra13.gruppeA3.Map.Position;

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
	 * 			element: Bei Waffen und Rüstungen das Element des Items
	 */
	private Position pos;
	private int damage = 0;
	private int type=1;
	private String desc=null;
	private String name=null;
	private Hitbox hitbox=null;
	private int value=0;
	private Element element;
	
	//Standard-Stärken von Standard-Items
	final public static int standardManaPower = 30;
	final public static int standardHealthPower = 20;
	final public static int standardPoisonPower = 15;
	final public static int standardSwordPower = 2;
	final public static int standardShieldPower = 2;
	
	//Standard-Münzwerte von Standard-Items
	final public static int standardManaValue = 4;
	final public static int standardHealthValue = 4;
	final public static int standardPoisonValue = 4;
	final public static int standardSwordValue = 10;
	final public static int standardShieldValue = 10;
	
	
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
	 * @param element	Das Element des Items
	 **/
	
	public Item(int damage, int type, Position pos, String desc, String name, int value, Element element) {
		this.damage = damage;
		this.type = type;
		this.desc = desc;
		this.name = name;
		this.hitbox = Hitbox.standard;
		this.pos = pos;
		this.value = value;
		this.element = element;
	}
	
	public Item(int damage, int type, FieldPosition pos, String desc, String name, int value, Element element) {
		this.damage = damage;
		this.type = type;
		this.desc = desc;
		this.name = name;
		this.hitbox = Hitbox.standard;
		this.pos = new Position(pos.x*32+hitbox.width/2, pos.y*32+hitbox.height/2);
		this.value = value;
		this.element = element;
	}
	
	/*Um Verwirrungen bzgl der Position zu vermeiden, sollte dieser Konstruktor bald gelöscht werden - x und y sind uneindeutig*/
	@Deprecated
	public Item(int damage, int type, int x, int y, String desc, String name, int value, Element element) {
		this.damage = damage;
		this.type = type;
		this.desc = desc;
		this.name = name;
		this.hitbox = Hitbox.standard;
		this.pos = new Position(x+(hitbox.width/2),y+(hitbox.height/2));
		this.value = value;
		this.element = element;
	}

	/**
	 * @return Liefert die Position des Items 
	 */
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
	/**
	 * @return Liefert die Hitbox des Items zurück
	 */
	public Hitbox getHitbox(){
		return this.hitbox;
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
	
	/*
	 * Map-Editor-Methoden
	 */
	
	/**
	 * Gibt eine Kopie dieses Items zurück.
	 */
	public Item clone() {
		return new Item(
						damage,
						type,
						getPosition(),
						getDesc(),
						getName(),
						getValue(),
						getElement());
	}
	
	/**
	 * Ändert die Item-Attribute.
	 * @param item Item, dessen Attribute übernommen werden sollen
	 */
	public void edit(Item item) {
		setDamage(item.getDamage());
		setType(item.getType());
		setPosition(item.getPosition());
		setDesc(item.getDesc());
		setName(item.getName());
		setValue(item.getValue());
	}
	
	public void setDamage(int damage) {
		this.damage = damage;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public void setPosition(Position pos) {
		this.pos = pos;
	}
	
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	public void setElement(Element element){
		this.element = element;
	}
	
	public Element getElement(){
		return this.element;
	}
}

