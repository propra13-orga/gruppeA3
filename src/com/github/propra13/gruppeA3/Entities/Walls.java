package com.github.propra13.gruppeA3.Entities;

import com.github.propra13.gruppeA3.Position;
import com.github.propra13.gruppeA3.Room;

/**
 * @author Majida Dere
 * Die Klasse Walls dient dazu, Wände als nicht bewegliche Items zu implementieren. 
 **/

public class Walls extends Entities {

	//Attribute
	
	private int power;
	private Position pos;
	private int type;
	private Room currentroom;

	
	//Konstruktor
	
	public Walls(Room room_bind,int power,int type,int x,int y){
		this.power=power;
		this.type=type;
		this.pos=new Position(x,y);
		this.currentroom = room_bind;
	}
	
	// Getter Methoden
	/**
	 * @ return liefert die Position der Wand 
	 * */
	@Override
	public Position getPosition() {
		// TODO Auto-generated method stub
		return pos;
	}
	
	/**
	 * 
	 * @return liefert die Stärke der Wand, d.h. wie oft der Spieler dagegen schlagen muss, um durch zu kommen
	 **/
	public int getPower() {
		return this.power;
	}
	
	/**
	 * 
	 * @return liefert den Wandtypen
	 **/
	
	public int getType(){
		return this.type;
	}

	
	// Setter Methoden
	@Override
	/**  Erwartet zwei int Paramter für die Position im Raum
	 * @ param x Position an der Stelle x
	 * @ param y Position an der Stelle y
	 **/
	public void setPosition(int x, int y) {
		// TODO Auto-generated method stub
		this.pos.x = x;
		this.pos.y = y;
	}
	
	/**  setPower() Erwartet zwei int Paramter für die Position im Raum
	 * @ param x Position an der Stelle x
	 * @ param y Position an der Stelle y
	 **/
	public void setPower(int power){
		this.power = power;
	}
	
	/**  setPosition erwartet einen int Paramter für den Wandtypen.
	 * @ param type Wandtyp
	 **/
	
	public void setType(int type){
		this.type = type;
	}

}
