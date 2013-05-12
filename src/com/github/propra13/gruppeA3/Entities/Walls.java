package com.github.propra13.gruppeA3.Entities;

import com.github.propra13.gruppeA3.Position;


public class Walls extends Entities {

	//Attribute
	
	private int power;
	private Position pos;
	private int type;
	
	//Konstruktor
	
	public Walls(int power,int type,int x,int y){
		this.power=power;
		this.type=type;
		this.pos=new Position(x,y);
	}
	
	// Getter Methoden
	
	@Override
	public Position getPosition() {
		// TODO Auto-generated method stub
		return pos;
	}
	
	public int getPower() {
		return this.power;
	}
	
	public int getType(){
		return this.type;
	}

	
	// Setter Methoden
	@Override
	public void setPosition(int x, int y) {
		// TODO Auto-generated method stub
		this.pos.x = x;
		this.pos.y = y;
	}
	
	public void setPower(int power){
		this.power = power;
	}
	
	public void setType(int type){
		this.type = type;
	}

}
