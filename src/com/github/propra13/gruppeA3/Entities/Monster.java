package com.github.propra13.gruppeA3.Entities;

import java.util.Iterator;
import java.util.LinkedList;

import com.github.propra13.gruppeA3.Map.Map;
import com.github.propra13.gruppeA3.Map.Position;

/**
 * @author Majida Dere
 * Die Klasse Monster liefert hauptsächlich die zugehörigen getter und setter Methoden zur Entity Monster
 */

public class Monster extends Moveable {

	/**
	 * Attribute:
	 * 			desc: Beschreibung des Monsters
	 * 			coins: Die Münze, die das Monster droppt, wenn es stirbt.
	 * 			type: Typ des Monsters
	 */
	private String desc;
	private Coin coins=null;
	private int type;
	private int roomID;
	private int power;
	private Element element;
	
	/**
	 * Der Konstruktor erzeugt ein Monster mit folgenden Parametern
	 * @param room_bind	Der Raum, in dem das Monster sich befindet
	 * @param speed Geschwindigkeit des Monsters
	 * @param power Kampfkraft des Monsters
	 * @param type Typ des Monsters
	 * @param life Lebenseinheit des Monsters
	 * @param x Position X Achse
	 * @param y Position Y Achse
	 * @param desc Beschreibung des Monsters
	 * @param coinValue Wert der Münze
	 * @param coinType Typ der Münze
	 * @param armour Rüstung des Monsters
	 * @param element Element des Monsters
	 */
	
	/*Hinweis: Der erste Konstruktor sollte, sobald in der XML-Datei alle Monster mit einem Element ausgestattet wurden, 
	 * nicht mehr verwendet und gelöscht werden.
	 */
	
	public Monster (int roomID, double speed, int power, int type, int life, 
					int x, int y, String desc, int coinValue, int coinType, int armour, boolean isBoss){
		super(roomID);
		this.roomID = roomID;
		addSpeedFactor(speed);
		this.power = power;
		addAttackFactor(power);
		setHealth(life);
		setArmour(armour);
		this.desc = desc;
		this.hitbox = new Hitbox();
		this.isBoss = isBoss;
		pos = new Position(x*32+(hitbox.width/2), y*32+(hitbox.height/2));;
		setDirection(Direction.NONE);
		coins=new Coin(coinValue, coinType, this.pos);
		this.type = type;
		this.element = Element.PHYSICAL;
		/**
		 * Diese Abfrage ist für Bossmonster, die größer sind als gewöhnliche Monster
		 */
		if(type == 5){ 
			this.hitbox.height = 48;
			this.hitbox.width  = 48;
		}
	}

	public Monster (int roomID, double speed, int power, int type, int life, 
			int x, int y, String desc, int coinValue, int coinType, int armour, boolean isBoss,Element element){
		super(roomID);
		this.roomID = roomID;
		addSpeedFactor(speed);
		this.power = power;
		addAttackFactor(power);
		setHealth(life);
		setArmour(armour);
		this.desc = desc;
		this.hitbox = new Hitbox();
		this.isBoss = isBoss;
		pos = new Position(x+(hitbox.width/2), y+(hitbox.height/2)); //kein setPos()!
		setDirection(Direction.NONE);
		coins=new Coin(coinValue, coinType, this.pos);
		this.type = type;
		this.element = element;
		/**
		 * Diese Abfrage ist für Bossmonster, die größer sind als gewöhnliche Monster
		 */
		if(type == 5){ 
			this.hitbox.height = 48;
			this.hitbox.width  = 48;
		}
}
	
	
	
	// Getter Methoden
	
	public Hitbox getHitbox() {
		return this.hitbox;
	}

	/**
	 * Liefert die Coins, die das Monster beherbergt.
	 * @return Coin
	 */
	public Coin getCoin(){
		return this.coins;
	}
	
	public int getType(){
		return type;
	}
	
	public String getDesc(){
		return desc;
	}
	
	public int getRoomID(){
		return this.roomID;
	}
	
	public int getPower(){
		return this.power;
	}
	
	public boolean isBoss(){
		return this.isBoss;
	}
	
	/**
	 * Tötet das Monster und verteilt das Gold.
	 */
	public void death() {
		coins.setPosition(getPosition());
		getRoom().removeCandidates.add(this);
		getRoom().entities.add(coins);
	}
	
	//Dummies
	@Override
	public void tick() {}
	@Override
	public void collision(Entities entity) {}
	
	
	/**
	 * Diese Methode setzt den aktuellen Health Status eines bewegbaren Objektes
	 * @param life leben
	 */
	@Override
	public void setHealth(int health){
		this.health = health;
		if (this.health <= 0) {
			death();
			if (isBoss) {
				Map.endIsOpen = true;
			}
		}
	}

	public void attack(){
		Position temp = new Position(this.getPosition().x,this.getPosition().y);
		switch(this.getFaceDirection()){
		case UP:
			temp.setPosition(temp.x , temp.y -6);
			break;
		case DOWN:
			temp.setPosition(temp.x , temp.y +6);
			break;
		case LEFT:
			temp.setPosition(temp.x - 6 , temp.y);
			break;
		case RIGHT:
			temp.setPosition(temp.x + 6 , temp.y);
			break;
		default:
			break;
		}
		
		int xdelta;
		int ydelta;
		Entities testent = null;	//durch alle Entitys der Liste iterieren
		@SuppressWarnings("unchecked")
		LinkedList<Entities> tempEntities = (LinkedList<Entities>) getRoom().entities.clone();
	    Iterator<Entities> iter = tempEntities.iterator();
	    Player player = null;
		while(iter.hasNext()){
			testent = iter.next();
			if(testent != this){	
				xdelta = temp.x - testent.getPosition().x; //x-Abstand der Mittelpunkte bestimmen
				if(xdelta < 0)
					xdelta = xdelta * (-1);
				ydelta = temp.y - testent.getPosition().y; //y-Abstand der Mittelpunkte bestimmen
				if(ydelta < 0)
					ydelta = ydelta * (-1);
				if(Math.sqrt(xdelta*xdelta + ydelta*ydelta) < 50){	//Wenn wurzel(x^2 + y^2) < 50 ist, auf hitboxkollision prüfen
					if(hitboxCheck(temp, testent) == false){
						if(testent instanceof Player){
							if(this.getAttackCount() == 0){
								player = (Player) testent;
								player.setHealth(player.getHealth() - calculateDamage(player));
								if(player.getHealth() <= 0){
									player.death();
								}
								this.setAttackCount(30);
							}
						}
					}
				}
			}
		}
	}
	
	 private int calculateDamage(Player player){
			int damage=0;
			switch(this.getElement()){
				case PHYSICAL:{
					switch(player.getDefenseElement()){
						case PHYSICAL:{
							damage = this.getAttack() - player.getArmour();
							break;
						}
						case FIRE:{
							damage = (int)((this.getAttack() - player.getArmour())*0.7);
							break;
						}
						case WATER:{
							damage = (int)((this.getAttack() - player.getArmour())*0.7);
							break;
						}
						case ICE:{
							damage = (int)((this.getAttack() - player.getArmour())*0.7);
							break;
						}
					}
					break;
				}
				case FIRE:{
					switch(player.getDefenseElement()){
						case PHYSICAL:{
							damage = this.getAttack() - player.getArmour();
							break;
						}
						case FIRE:{
							damage = (int)((damage = this.getAttack() - player.getArmour())*0.7);
							break;
						}
						case WATER:{
							damage = (int)((this.getAttack() - player.getArmour())*0.5);
							break;
						}
						case ICE:{
							damage = (int)((this.getAttack() - player.getArmour())*2.0);
							break;
						}
					}
					break;
				}
				case WATER:{
					switch(player.getDefenseElement()){
						case PHYSICAL:{
							damage = this.getAttack() - player.getArmour();
							break;
						}
						case FIRE:{
							damage = (int)((this.getAttack() - player.getArmour())*2.0);
							break;
						}
						case WATER:{
							damage = (int)((this.getAttack() - player.getArmour())*0.7);
							break;
						}
						case ICE:{
							damage = (int)((this.getAttack() - player.getArmour())*0.5);
							break;
						}
					}
					break;
				}
				case ICE:{
					switch(player.getDefenseElement()){
						case PHYSICAL:{
							damage = this.getAttack() - player.getArmour();
							break;
						}
						case FIRE:{
							damage = (int)((this.getAttack() - player.getArmour())*0.5);
							break;
						}
						case WATER:{
							damage = (int)((this.getAttack() - player.getArmour())*2.0);
							break;
						}
						case ICE:{
							damage = (int)((this.getAttack() - player.getArmour())*0.7);
							break;
						}
					}
					break;
				}
			}
			
			if(damage <= 0){
				damage = 1;
			}
			return damage;
		}
	
	
	/*
	 * Map-Editor-Methoden
	 */
	
	/**
	 * Ändert die Monster-Attribute.
	 * @param monster Monster, dessen Attribute übernommen werden sollen
	 */
	public void edit(Monster monster) {
		setHealth(monster.getHealth());
		setArmour(monster.getArmour());
		desc = monster.desc;
		this.isBoss = monster.isBoss;
		coins=new Coin(monster.coins.getValue(), monster.coins.getType(), this.pos);
		this.type = monster.type;
		/**
		 * Diese Abfrage ist für Bossmonster, die größer sind als gewöhnliche Monster
		 */
		if(type == 5){ 
			this.hitbox.height = 48;
			this.hitbox.width  = 48;
		}

		resetSpeed(monster.getSpeed());
		resetAttack(monster.getAttack());
	}
	
	/**
	 * Setzt Monster-Typ.
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	/**
	 * Gibt eine Kopie dieses Monsters zurück.
	 */
	public Monster clone() {
		return new Monster(roomID,
				getSpeed(),
				getAttack(),
				type,
				getHealth(),
				getFieldPos().x,
				getFieldPos().y,
				desc,
				coins.getValue(),
				coins.getType(),
				getArmour(),
				isBoss);	
	}
	
	public void setElement(Element element){
		this.element = element;
	}
	
	public Element getElement(){
		return this.element;
	}
}
	

