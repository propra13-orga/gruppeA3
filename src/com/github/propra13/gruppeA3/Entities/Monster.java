package com.github.propra13.gruppeA3.Entities;

import com.github.propra13.gruppeA3.Position;
import com.github.propra13.gruppeA3.Room;

public class Monster extends Moveable {

	// Attribute
	private double speed;
	private Position pos;
	private int size;
	private int power;
	private int life;
	
	//Konstruktor
	
	public Monster (Room room_bind, double speed, int power, int type, int size, int life, int x, int y){
		super(room_bind);
		this.speed=speed;
		this.size=size;
		this.power=power;
		this.life=life;
		this.pos=new Position(x,y);
	}
	
	// Getter Methoden
	
	public double getSpeed(){
		return this.speed;
	}
	
	public double getSize(){
		return this.size;
	}
	
	public int getPower() {
		return this.power;
	}
	public int getLife() {
		return this.life;
	}
	
	
	@Override
	public Position getPosition() {
		// TODO Auto-generated method stub
		return pos;
	}

	
	// Setter Methoden
	@Override
	public void setPosition(int x, int y) {
		// TODO Auto-generated method stub
		this.pos.x = x;
		this.pos.y = y;
	}
	
	public void setSpeed(double speed){
		this.speed = speed;
	}
	
	public void setPower(int power){
		this.power = power;
	}
	
	public void setSize(int size){
		this.size = size;
	}
	
	public void setLife(int life){
		this.life = life;
	}
}
