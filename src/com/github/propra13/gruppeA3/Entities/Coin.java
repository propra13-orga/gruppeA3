package com.github.propra13.gruppeA3.Entities;

import com.github.propra13.gruppeA3.Map.Position;

public class Coin extends Entities {

	private int value=0;
	private int type=1;
	private Position pos = null;
	private Hitbox hitbox = null;
	
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
		// TODO Auto-generated method stub
		return this.hitbox;
	}

	@Override
	void setHealth(int health) {
		// TODO Auto-generated method stub
		
	}

	@Override
	int getHealth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	void tick() {
		// TODO Auto-generated method stub
		
	}

	@Override
	void collision(Entities entity) {
		// TODO Auto-generated method stub
		
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
