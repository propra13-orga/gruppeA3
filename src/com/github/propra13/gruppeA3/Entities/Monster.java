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
}
