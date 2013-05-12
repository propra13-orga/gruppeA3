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
	
	
	
	@Override
	Position getPosition() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	void setPosition(int x, int y) {
		// TODO Auto-generated method stub
		
	}

}
