package com.github.propra13.gruppeA3.Entities;

import com.github.propra13.gruppeA3.Position;

public class Monster extends Entities {

	// Attribute
	private double speed;
	private Position pos;
	private int size;
	private int power;
	private int life;
	
	//Konstruktor
	
	public Monster (int power,int type,int x,int y){
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
	Position getPosition() {
		// TODO Auto-generated method stub
		return null;
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
