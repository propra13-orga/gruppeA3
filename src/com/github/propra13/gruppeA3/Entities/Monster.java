package com.github.propra13.gruppeA3.Entities;

import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.LinkedList;

import com.github.propra13.gruppeA3.Game;
import com.github.propra13.gruppeA3.GameWindow;
import com.github.propra13.gruppeA3.Map.FieldPosition;
import com.github.propra13.gruppeA3.Map.Map;
import com.github.propra13.gruppeA3.Map.Position;

/**
 * Diese Klasse stellt die Gegner im Spiel dar
 *
 */

public class Monster extends Moveable {

	private String desc;
	private Coin coins=null;
	private int type;
	private int roomID;
	private int power;
	private Element element;
	
	private int hurtAnimationCounter = 0;
	
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
	
	public Monster (int roomID, double speed, int power, int type, int life, 
					Position pos, String desc, int coinValue, int coinType, int armour, boolean isBoss, Element element){
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
		this.pos = pos;
		setDirection(Direction.NONE);
		coins=new Coin(coinValue, coinType, this.pos);
		this.type = type;
		this.element = element;
		if(type == 5){ 
			this.hitbox.height = 48;
			this.hitbox.width  = 48;
		}
	}
	
	public Monster (int roomID, double speed, int power, int type, int life, 
			FieldPosition pos, String desc, int coinValue, int coinType, int armour,
			boolean isBoss, Element element) {
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
		this.pos = new Position(pos.x*32+hitbox.width/2, pos.y*32+hitbox.height/2);
		setDirection(Direction.NONE);
		coins=new Coin(coinValue, coinType, this.pos);
		this.type = type;
		this.element = element;
		if(type == 5){ 
			this.hitbox.height = 48;
			this.hitbox.width  = 48;
		}
	}
	
	
	/*Um Verwirrungen bzgl der Position zu vermeiden, sollte dieser Konstruktor bald gelöscht werden - x und y sind uneindeutig*/
	@Deprecated
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
		if(type == 5){ 
			this.hitbox.height = 48;
			this.hitbox.width  = 48;
		}
}
	
	
	
	// Getter Methoden
	
	/**
	 * Gibt die Hitbox des Monsters zurück
	 * @see Hitbox
	 * @return hitbox - Die Hitbox des Monsters
	 */
	public Hitbox getHitbox() {
		return this.hitbox;
	}

	/**
	 * Liefert die Coins, die das Monster beherbergt.
	 * @see Coin
	 * @return coins - Die Anzahl der Münzen
	 */
	public Coin getCoin(){
		return this.coins;
	}
	
	/**Liefert den Typ des Monsters
	 * @return type - der Typ des Monsters
	 */
	public int getType(){
		return type;
	}
	
	/**
	 * Liefert die Beschreibung des Monsters
	 * @return desc - Die Beschreibung des Monsters
	 */
	public String getDesc(){
		return desc;
	}
	
	/**
	 * Liefert die ID des Raums, in dem sich das Monster befindet
	 * @see Room
	 * @return roomID - Die ID des Raums
	 */
	public int getRoomID(){
		return this.roomID;
	}
	
	/**
	 * Liefert die Angriffsstärke des Monsters
	 * @return power - die Angriffsstärke des Monsters
	 */
	public int getPower(){
		return this.power;
	}
	
	public boolean isBoss(){
		return this.isBoss;
	}
	
	/**
	 * Tötet das Monster und verteilt das Gold.
	 * Entfernt das Monster aus der Entityliste und fügt die Münzen hinzu
	 */
	public void death() {
		coins.setPosition(getPosition());
		getRoom().removeCandidates.add(this);
		getRoom().entities.add(coins);
		Game.Menu.player.exp = Game.Menu.player.exp + 10;
		if(Game.Menu.player.exp >= 100){
			Game.Menu.player.level++;
			Game.Menu.player.exp = 0;
		}
	}
	
	//Dummies
	@Override
	public void collision(Entities entity) {}
	
	/**
	 * Wird jeden Gametick ausgeführt.
	 */
	@Override
	public void tick() {
		if(hurtAnimationCounter > 0)
			hurtAnimationCounter--;
	}
	
	
	/**
	 * Diese Methode setzt den aktuellen Health Status eines bewegbaren Objektes
	 * @param health Anzahl der zu setzenden Lebenspunkte
	 */
	@Override
	public void setHealth(int health) {
		if(health < this.health) {
			hurtAnimationCounter = 20;
		}
		
		this.health = health;
		if (this.health <= 0) {
			death();
			if (isBoss) {
				Map.endIsOpen = true;
			}
		}
	}

	/**
	 * Führt einen Angriff in Abhänigkeit von der Blickrichtung des Monsters aus, indem die Liste der Entities auf Kollisionen geprüft wird
	 *  und berechnet den Schaden mit Monster.calculateDamage()
	 * @see Monster.calculateDamage()
	 */
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
	
	/**
	 * Führt die Schadensberechnung für einen Angriff durch ein Monster auf einen Spieler durch.
	 * Sollte die Berechnung einen Wert kleiner 1 ergeben, so wird 1 zurückgegeben
	 * @see Monster.attack()
	 * @param player - Der Spieler der Angegriffen wird
	 * @return damage - Der Schaden der dem Spieler zugefügt wird
	 */
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
	 
	/**
	 * Gibt das zu zeichnende Bild dieses Monsters zurück.
	 * @return
	 */
	@Override
	public BufferedImage getImage() {
		BufferedImage img = null;
		
		switch(getType()) {
		case 1:
			switch(getFaceDirection()){
			case UP: 
				img = GameWindow.monsterimg_1up;
				break;
			case DOWN:
				img = GameWindow.monsterimg_1down;
				break;
			case LEFT:
				img = GameWindow.monsterimg_1left;
				break;
			case RIGHT:
				img = GameWindow.monsterimg_1right;
				break;
			default:
				img = GameWindow.monsterimg_1down;
				break;
			}
			break;
		case 2:
			switch(getFaceDirection()){
			case UP: 
				img = GameWindow.monsterimg_2up;
				break;
			case DOWN:
				img = GameWindow.monsterimg_2down;
				break;
			case LEFT:
				img = GameWindow.monsterimg_2left;
				break;
			case RIGHT:
				img = GameWindow.monsterimg_2right;
				break;
			default:
				img = GameWindow.monsterimg_2down;
				break;
			}
			break;
		case 3:	
			switch(getFaceDirection()){
			case UP: 
				img = GameWindow.monsterimg_3up;
				break;
			case DOWN:
				img = GameWindow.monsterimg_3down;
				break;
			case LEFT:
				img = GameWindow.monsterimg_3left;
				break;
			case RIGHT:
				img = GameWindow.monsterimg_3right;
				break;
			default:
				img = GameWindow.monsterimg_3down;
				break;
			}
			break;
		case 4:
			switch(getFaceDirection()){
			case UP: 
				img = GameWindow.monsterimg_4up;
				break;
			case DOWN:
				img = GameWindow.monsterimg_4down;
				break;
			case LEFT:
				img = GameWindow.monsterimg_4left;
				break;
			case RIGHT:
				img = GameWindow.monsterimg_4right;
				break;
			default:
				img = GameWindow.monsterimg_4down;
				break;
			}
			break;
		case 5:
			
			//Entscheiden, welches Element aus Boss-Arrays genommen werden soll nach Element
			int elementIndex = -1;
			switch(getElement()) {
			case PHYSICAL:
				elementIndex = 0;
				break;
			case FIRE:
				elementIndex = 1;
				break;
			case WATER:
				elementIndex = 2;
				break;
			case ICE:
				elementIndex = 3;
				break;
			}
          	
			switch(getFaceDirection()){
			case UP: 
				img = GameWindow.bossImgs_up[elementIndex];
				break;
			case DOWN:
				img = GameWindow.bossImgs_down[elementIndex];
				break;
			case LEFT:
				img = GameWindow.bossImgs_left[elementIndex];
				break;
			case RIGHT:
				img = GameWindow.bossImgs_right[elementIndex];
				break;
			case NONE:
				img = GameWindow.bossImgs_right[elementIndex];
				break;
			}
			break;
		}
		
		if(hurtAnimationCounter > 0) 
			img = GameWindow.turnRed(img);
		
		return img;
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
		if(type == 5){ 
			this.hitbox.height = 48;
			this.hitbox.width  = 48;
		}

		resetSpeed(monster.getSpeed());
		resetAttack(monster.getAttack());
	}
	
	/**
	 * Setzt Monster-Typ.
	 * @param type - Der Typ des Monsters
	 */
	public void setType(int type) {
		this.type = type;
	}
	
	/**
	 * Gibt eine Kopie dieses Monsters zurück.
	 * @return Monster - eine Kopie des Monsters
	 */
	public Monster clone() {
		return new Monster(roomID,
				getSpeed(),
				getAttack(),
				type,
				getHealth(),
				getPosition(),
				desc,
				coins.getValue(),
				coins.getType(),
				getArmour(),
				isBoss,
				getElement());	
	}
	
	/**
	 * Setzt das Element des Monsters für die Schadensberechnung
	 * @see com.github.propra13.gruppeA3.Moveable.Element
	 * @param element Das zu setztende Element
	 */
	public void setElement(Element element){
		this.element = element;
	}
	
	/**
	 * Gibt das Element des Monsters für die Schadensberechnung zurück
	 * @see com.github.propra13.gruppeA3.Moveable.Element
	 * @return element - Das Element des Monsters
	 */
	public Element getElement(){
		return this.element;
	}
	
	
}
	

