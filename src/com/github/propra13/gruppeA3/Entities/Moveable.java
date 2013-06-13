package com.github.propra13.gruppeA3.Entities;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.lang.Math;

import com.github.propra13.gruppeA3.Map.FieldPosition;
import com.github.propra13.gruppeA3.Map.Position;
import com.github.propra13.gruppeA3.Map.Room;

/**
 * @author Majida Dere 
 * Diese Klasse dient als Vorlage für alle bewegbaren Objekte im Spiel.
 *
 */
public abstract class Moveable extends Entities {
	

	public enum direction{LEFT,RIGHT,UP,DOWN,NONE}
	private direction direct;
	private direction facedirect;
	private Position pos;
	private Room currentroom;
	protected Hitbox hitbox;
	private int health;
	private int power;
	private int armour;
	private double speed; 
	
	/* Anzahl der Pixel, die bei einer Bewegung
	 * mit Speed 1 zurückgelegt werden sollen */
	final public static int movePx = 2;
	
	
	//Konstruktor
	public Moveable(Room room_bind){
		//System.out.println("Moveable"); //nicht erreichbar - wtf
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
	public abstract void move();
	
	
	//Kollisionsabfragen
	public boolean rangeCheck(){
		System.out.println("rangeCheck!");
		int xdelta;
		int ydelta;
		boolean flag = true;
		Entities testent = null;	//durch alle Entitys der Liste iterieren
		 LinkedList<Entities> tempEntities = getRoom().entities;
	        Iterator<Entities> iter = tempEntities.iterator();
		while(iter.hasNext()){
			if(testent != this){
				testent = iter.next();
				//System.out.println(testent.getClass());
				xdelta = this.getPosition().x - testent.getPosition().x; //x-Abstand der Mittelpunkte bestimmen
				if(xdelta < 0)
					xdelta = xdelta * (-1);
				ydelta = this.getPosition().y - testent.getPosition().y; //y-Abstand der Mittelpunkte bestimmen
				if(ydelta < 0)
					ydelta = ydelta * (-1);
				if(Math.sqrt(xdelta*xdelta + ydelta*ydelta) < 50){	//Wenn wurzel(x^2 + y^2) < 50 ist, auf hitboxkollision prüfen
					if(hitboxCheck(testent) == false){
						flag = false;
						return flag;
					}
				}
			}
		}
		return flag;
	}
	
	/**
	 * Kollisionsabfrage für this mit gegebenem Kollisionsgegner
	 * @param test Kollisionsgegner
	 * @return Kollisionswahrheitswert
	 */
	protected boolean hitboxCheck(Entities testent) {
		Entities test = testent;
		switch(direct){
		case LEFT:
			System.out.println("Spieler posx: "+ getPosition().x + " Monster posx: " + test.getPosition().x + " Abstand der Hitboxen: " + ((this.getPosition().x - (this.getHitbox().width/2) - (test.getPosition().x + (test.getHitbox().width/2)))));
			if(((this.getPosition().x - (this.getHitbox().width/2) - (speed*movePx)) - (test.getPosition().x + (test.getHitbox().width/2))) < 0){ //wenn der x-Abstand der Mittelpunkte - der weite des Schrittes - der halben Breite der getHitbox()en kleiner als 0 ist
				if(this.getPosition().y - test.getPosition().y > 0){ //überprüfe den y-Abstand
					if(((this.getPosition().y - (this.getHitbox().height/2)) - (test.getPosition().y + (test.getHitbox().height/2)) >= 0))
						return true;
					else
						return false;
				}
				else{
					if(((test.getPosition().y - (test.getHitbox().height/2)) - (this.getPosition().y + (this.getHitbox().height/2)) >= 0))
						return true;
					else
						return false;
				}
			}
			else
				return true;
			
		case RIGHT:
			if(((test.getPosition().x - (test.getHitbox().width/2) - (speed*movePx)) - (this.getPosition().x + (this.getHitbox().width/2))) < 0){
				if(this.getPosition().y - test.getPosition().y > 0){
					if(((this.getPosition().y - (this.getHitbox().height/2)) - (test.getPosition().y + (test.getHitbox().height/2)) >= 0))
						return true;
					else
						return false;
				}
				else{
					if(((test.getPosition().y - (test.getHitbox().height/2)) - (this.getPosition().y + (this.getHitbox().height/2)) >= 0))
						return true;
					else
						return false;
				}
			}
			else
				return true;
			
		case UP:
			System.out.println("Spieler posy: "+ getPosition().y + " Monster posy: " + test.getPosition().y + " Abstand der Hitboxen: " + ((this.getPosition().y - (this.getHitbox().height/2)) - (test.getPosition().y + (test.getHitbox().height/2))));
			if(((this.getPosition().y - (this.getHitbox().height/2) - (speed*movePx)) - (test.getPosition().y + (test.getHitbox().height/2))) < 0){
				if(this.getPosition().x - test.getPosition().x > 0){
					if(((this.getPosition().x - (this.getHitbox().width/2)) - (test.getPosition().x + (test.getHitbox().width/2)) >= 0))
						return true;
					else
						return false;
				}
				else{
					if(((test.getPosition().x - (test.getHitbox().width/2)) - (this.getPosition().x + (this.getHitbox().width/2)) >= 0))
						return true;
					else
						return false;
				}
			}
			else
				return true;
			
		case DOWN:
			if(((test.getPosition().y - (test.getHitbox().height/2) - (speed*movePx)) - (this.getPosition().y + (this.getHitbox().height/2))) < 0){
				if(this.getPosition().x - test.getPosition().x > 0){
					if(((this.getPosition().x - (this.getHitbox().width/2)) - (test.getPosition().x + (test.getHitbox().width/2)) >= 0))
						return true;
					else
						return false;
				}
				else{
					if(((test.getPosition().x - (test.getHitbox().width/2)) - (this.getPosition().x + (this.getHitbox().width/2)) >= 0))
						return true;
					else
						return false;
				}
			}
			else
				return true;
			
		default:
			return false;
		}
	}
	
	
	/**
	 * Diese Methode liefert die aktuelle Position im Raum
	 * @return liefert die Position im Raum
	 */	public Position getPosition(){
		return this.pos;
	}

	/**
	 * Diese Methode ändert die aktuelle Position im Raum
	 * @param x X-Achse
	 * @param y Y-Achse
	 */
	public void setPosition(int x, int y) { 
		pos.setPosition(x, y);
	}
	
	public void setPosition(Position pos) {
		this.pos = pos;
	}
	
	public FieldPosition getFieldPos() {
		return pos.toFieldPos();
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
	
	public direction getDirection(){
		return this.direct;
	}
	
	public void setDirection(direction direct){
		this.direct = direct;
	}
	@Override
	public Hitbox getHitbox(){
		return hitbox;
	}
	
	public direction getFaceDirection(){
		return facedirect;
	}
	
	public void setFaceDirection(direction direct){
		facedirect = direct;
	}
}