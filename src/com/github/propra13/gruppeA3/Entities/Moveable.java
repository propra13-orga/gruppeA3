package com.github.propra13.gruppeA3.Entities;

import java.util.Iterator;
import java.util.List;
import java.lang.Math;

import com.github.propra13.gruppeA3.Position;
import com.github.propra13.gruppeA3.Room;
import com.github.propra13.gruppeA3.FieldPosition;

/**
 * @author Majida Dere 
 * Diese Klasse dient als Vorlage für alle bewegbaren Objekte im Spiel.
 *
 */
public class Moveable extends Entities {
	

	public enum direction{LEFT,RIGHT,UP,DOWN,NONE}
	public direction direct; 
	private Position pos;
	private Room currentroom;
	private int health;
	private int power;
	private int armour;
	protected double speed; 
	
	/* Anzahl der Pixel, die bei einer Bewegung
	 * mit Speed 1 zurückgelegt werden sollen */
	final public static int movePx = 3;
	
	
	//Konstruktor
	public Moveable(Room room_bind){
		System.out.println("Moveable"); //nicht erreichbar - wtf
		this.pos = new Position(0,0);
		this.currentroom = room_bind;
		this.direct = direction.NONE;
		this.power = 0;
		this.speed = 1; 
		this.armour = 0;
	}

	/** 
	 * Diese Methode bewegt ein bewegbares Objekt im Raum
	 * Begehbarkeit des Feldes prüfen
	 * Wenn begehbar, setposition anwenden
	 **/
	public void move(){
		switch(this.direct){
		case LEFT:
			if(currentroom.roomFields[this.pos.x - 1][this.pos.y].walkable) {
				setPosition(this.pos.x - (int)(movePx*speed), this.pos.y);
			}
					break;
					
		case UP:
			if(currentroom.roomFields[this.pos.x][this.pos.y + 1].walkable){
				setPosition(this.pos.x, this.pos.y + (int)(movePx*speed));
			}
					break;
					
		case RIGHT:
			if(currentroom.roomFields[this.pos.x + 1][this.pos.y].walkable){
				setPosition(this.pos.x + (int)(movePx*speed), this.pos.y);
			}
					break;
					
		case DOWN:
			if(currentroom.roomFields[this.pos.x][this.pos.y + 1].walkable){
				setPosition(this.pos.x, this.pos.y + (int)(movePx*speed));
			}
					break;
					
		default: //nichts tun
		}
				
	}
	
	public boolean rangeCheck(){
		int xdelta;
		int ydelta;
		boolean flag = false;
		Entities test = null;
		 List<Entities> tempEntities = getRoom().entities;
	        Iterator<Entities> iter = tempEntities.iterator();
		while(iter.hasNext()){
			if(test != this){
				test = iter.next();
				xdelta = this.getPosition().x - test.getPosition().x;
				if(xdelta < 0)
					xdelta = xdelta * (-1);
				ydelta = this.getPosition().y - test.getPosition().y;
				if(ydelta < 0)
					ydelta = ydelta * (-1);
				
				if(Math.sqrt(xdelta*xdelta + ydelta*ydelta) < 42){
					flag = true;
				}
			}
		}
		
		return flag;
	}
	
	/**
	 * Diese Methode liefert die aktuelle Position im Raum
	 * @return liefert die Position im Raum
	 */
	public Position getPosition(){
		return pos;
	}

	/**
	 * Diese Methode ändert die aktuelle Position im Raum
	 * @param x X-Achse
	 * @param y Y-Achse
	 */
	public void setPosition(int x, int y){ 
		pos.setPosition(x, y);
	}
	
	public FieldPosition getFieldPos() {
		return new FieldPosition(pos.x/32, pos.y/(32));
	}
	
	/**
	 * Diese Methode setzt den aktuellen Health Status eines bewegbaren Objektes
	 * @param life leben
	 */
	public void setHealth(int health){
		this.health = health;
	}

	/**
	 * Diese Methode liefert den aktuellen Health Status eines bewegbaren Objektes
	 * @return liefert ein int Leben
	 */
	public int getHealth(){	
		return this.health;
	}
	
	public int getPower(){
		return power;
	}
	
	public void setPower(int power){
		this.power = power;
	}
	
	public int getArmour(){
		return armour;
	}
	
	public void setArmour(int armour){
		this.armour = armour;
	}
	
	public double getSpeed(){
		return speed;
	}
	
	public void setSpeed(double speed){
		this.speed = speed;
	}
	
	public Room getRoom(){
		return currentroom;
	}
	
	public void setRoom(Room room){
		this.currentroom = room;
	}
	
}