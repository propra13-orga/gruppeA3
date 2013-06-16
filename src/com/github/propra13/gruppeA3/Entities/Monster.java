package com.github.propra13.gruppeA3.Entities;

import java.util.Iterator;
import java.util.LinkedList;

import com.github.propra13.gruppeA3.Map.Map;
import com.github.propra13.gruppeA3.Map.Position;
import com.github.propra13.gruppeA3.Map.Room;
import com.github.propra13.gruppeA3.Map.Trigger;

/**
 * @author Majida Dere
 * Die Klasse Monster liefert hauptsächlich die zugehörigen getter und setter Methoden zur Entity Monster
 */

public class Monster extends Moveable {

	// Attribute
	private String desc;
	private Coin coins=null;
	
	//Konstruktor
	
	public Monster (Room room_bind, double speed, int power, int type, int life, 
					int x, int y, String desc, int coinValue, int coinType){
		super(room_bind);
		setSpeed(speed);
		setPower(power);
		setHealth(life);
		this.desc = desc;
		this.hitbox = new Hitbox();
		setPosition(x+(hitbox.width/2),y+(hitbox.height/2));
		setDirection(Direction.NONE);
		coins=new Coin(coinValue, coinType, this.pos);
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
								player.setHealth(player.getHealth() - this.getPower());
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
	

