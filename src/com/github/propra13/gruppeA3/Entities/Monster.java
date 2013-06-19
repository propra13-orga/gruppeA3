package com.github.propra13.gruppeA3.Entities;

import java.util.Iterator;
import java.util.LinkedList;

import com.github.propra13.gruppeA3.Map.Position;
import com.github.propra13.gruppeA3.Map.Room;

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
	 * 
	 */
	public Monster (Room room_bind, double speed, int power, int type, int life, 
					int x, int y, String desc, int coinValue, int coinType, int armour, boolean isBoss){
		super(room_bind);
		addSpeedFactor(speed);
		addAttackFactor(power);
		setHealth(life);
		setArmour(armour);
		this.desc = desc;
		this.hitbox = new Hitbox();
		this.isBoss = isBoss;
		setPosition(x+(hitbox.width/2),y+(hitbox.height/2));
		setDirection(Direction.NONE);
		coins=new Coin(coinValue, coinType, this.pos);
		this.type = type;
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
	
	//Dummies
	@Override
	public void tick() {}
	@Override
	public void collision(Entities entity) {}

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
								if((this.getPower() - player.getArmour()) > 0){
									player.setHealth(player.getHealth() - (this.getPower() - player.getArmour()));
								}
								else{
									player.setHealth(player.getHealth() -1);
								}
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
}
	

