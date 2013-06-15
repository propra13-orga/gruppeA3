package com.github.propra13.gruppeA3.Entities;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.lang.Math;

import com.github.propra13.gruppeA3.Map.Field;
import com.github.propra13.gruppeA3.Map.FieldPosition;
import com.github.propra13.gruppeA3.Map.Position;
import com.github.propra13.gruppeA3.Map.Room;
import com.github.propra13.gruppeA3.Menu.MenuStart;

/**
 * @author Majida Dere 
 * Diese Klasse dient als Vorlage für alle bewegbaren Objekte im Spiel.
 *
 */
public abstract class Moveable extends Entities {
	

	public static enum Direction{LEFT,RIGHT,UP,DOWN,NONE}
	protected Direction direct;
	private Direction facedirect;
	protected Position pos;
	private Room currentroom;
	protected Hitbox hitbox;
	private int health;
	private int power;
	private int armour;
	private double speed;
	private int attackcounter;
	private int castcounter;
	private boolean attack;
	private String cast;
	
	protected Field actualField; //Feld, wo das Ding derzeit ist
	protected Field lastField; //Feld, wo das Ding vor dem aktuellen Movement war
	
	/* Anzahl der Pixel, die bei einer Bewegung
	 * mit Speed 1 zurückgelegt werden sollen */
	final public static int movePx = 2;
	
	
	//Konstruktor
	public Moveable(Room room_bind){
		this.pos = new Position(0,0);
		this.currentroom = room_bind;
		this.direct = Direction.NONE;
		this.power = 0;
		this.speed = 1; 
		this.armour = 0;
		this.facedirect = Direction.NONE;
		this.attackcounter = 0;
		this.castcounter = 0;
		this.attack = false;
		this.cast = "";
	}

	/** 
	 * Diese Methode bewegt ein bewegbares Objekt im Raum
	 * Begehbarkeit des Feldes prüfen
	 * Wenn begehbar, setposition anwenden
	 **/
	public void move() {

    	int step = (int)(movePx * getSpeed());
    	Position nextPos = new Position(0,0); //Position, auf die gelaufen werden soll
    	Field[] fieldsToWalk = new Field[2];  // Felder, die betreten werden sollen
        switch (this.getDirection()) {
            case LEFT:
            	nextPos.setPosition(getPosition().x - step, getPosition().y);
            	// Checke, ob Spieler aus der Map rauslatscht anhand Hitbox
            	if(nextPos.getCornerTopLeft(hitbox).x > 0) {
            		
            		// Kollision mit Wänden und setPosition
            		Position p = new Position(nextPos.getCornerTopLeft(hitbox).x, nextPos.getCornerTopLeft(hitbox).y +1);
            		fieldsToWalk[0] = MenuStart.activeRoom.getField(p); //+1: damit Spieler durch Gänge passt
            		
            		p.setPosition(nextPos.getCornerBottomLeft(hitbox).x, nextPos.getCornerBottomLeft(hitbox).y -1);
            		fieldsToWalk[1] = MenuStart.activeRoom.getField(p);
            		
            		// Falls fieldsToWalk[0] und [1] begehbar, beweg dich einfach
            		if(rangeCheck()){
            			if (fieldsToWalk[0].walkable && fieldsToWalk[1].walkable) {
            				setPosition(nextPos);
            				break;
            				// Ansonsten liegt Kollision vor, daher Annäherung an Feldgrenze
            			} else {
            				int distance = getPosition().getCornerTopLeft(hitbox).x - (fieldsToWalk[0].pos.toPosition().x + 32);
            				if (distance != 0)
            					setPosition(getPosition().x - distance, nextPos.y);
            				//Projektile verpuffen an Wand
            				else if(this instanceof Projectile){
            					Projectile proj = (Projectile)this;
            					proj.terminate();
            				}
            			}
            		}
            	}
            	// ansonsten Annäherung an Raumrand
            	else {
        			System.out.println("bin am Rand");
        			setPosition(hitbox.width/2, nextPos.y);
        		}
            	
                break;

            case UP:
        		nextPos.setPosition(getPosition().x, getPosition().y - step);
        		if (this instanceof Projectile)
        			System.out.println("Versuche, nach "+nextPos+" zu fliegen");
        		// Checke, ob Moveable aus der Map rauslatscht anhand Hitbox
            	if(nextPos.getCornerTopLeft(hitbox).y > 0) {
            		
            		// Kollision mit Wänden und setPosition
            		Position p = new Position(nextPos.getCornerTopLeft(hitbox).x +1, nextPos.getCornerTopLeft(hitbox).y);
            		fieldsToWalk[0] = MenuStart.activeRoom.getField(p); //+1: damit Spieler durch Gänge passt
            		
            		p.setPosition(nextPos.getCornerTopRight(hitbox).x -1, nextPos.getCornerTopRight(hitbox).y);
            		fieldsToWalk[1] = MenuStart.activeRoom.getField(p);

            		// Falls fieldsToWalk[0] und [1] begehbar, beweg dich einfach
            		if(rangeCheck()){
            			if (fieldsToWalk[0].walkable && fieldsToWalk[1].walkable) {
            				setPosition(nextPos);
            				break;
            				
            			// Ansonsten liegt Kollision vor, daher Annäherung an Feldgrenze
            			} 
            			else {
            				int distance = getPosition().getCornerTopLeft(hitbox).y - (fieldsToWalk[0].pos.toPosition().y + 32);
            				if (distance != 0)
            					setPosition(getPosition().x, getPosition().y - distance);
            				// Projektile verpuffen an Wand
            				else if(this instanceof Projectile) {
            					Projectile proj = (Projectile)this;
            					proj.terminate();
            				}
            			}
            		}
					//ansonsten Annäherung an Raumrand
            		else {
            			setPosition(nextPos.x, hitbox.height/2);
            		}
            	}
            	
            	break;

            case RIGHT:
        		nextPos.setPosition(getPosition().x + step, getPosition().y);
        		// Checke, ob Spieler aus der Map rauslatscht anhand Hitbox
            	if(nextPos.getCornerTopRight(hitbox).x < getRoom().getWidth()*32) {
            		
            		// Kollision mit Wänden und setPosition
            		Position p = new Position(nextPos.getCornerTopRight(hitbox).x, nextPos.getCornerTopRight(hitbox).y +1);
            		fieldsToWalk[0] = MenuStart.activeRoom.getField(p); //+1: damit Spieler durch Gänge passt
            		
            		p.setPosition(nextPos.getCornerBottomRight(hitbox).x, nextPos.getCornerBottomRight(hitbox).y -1);
            		fieldsToWalk[1] = MenuStart.activeRoom.getField(p);
            		
            		// Falls fieldsToWalk[0] und [1] begehbar, beweg dich einfach
            		if(rangeCheck()){
            			if (fieldsToWalk[0].walkable && fieldsToWalk[1].walkable){
            				setPosition(nextPos);
            				break;
            				// Ansonsten liegt Kollision vor, daher Annäherung an Feldgrenze
            			} else {
            				int distance = fieldsToWalk[0].pos.toPosition().x - getPosition().getCornerTopRight(hitbox).x;
            				if (distance != 0)
            					setPosition(getPosition().x + distance, nextPos.y);
            				//Projektile verpuffen an Wand
            				else if(this instanceof Projectile){
            					Projectile proj = (Projectile)this;
            					proj.terminate();
            				}
            			}
            		}
            	}
            	//ansonsten Annäherung an Raumrand
            	else {
            		setPosition(getRoom().getWidth()*32 - hitbox.width/2, nextPos.y);
            	}
            	
                break;

            case DOWN:
        		nextPos.setPosition(getPosition().x, getPosition().y + step);
        		// Checke, ob Spieler aus der Map rauslatscht anhand Hitbox
            	if(nextPos.getCornerBottomLeft(hitbox).y < getRoom().getHeight()*32) {
            		
            		// Kollision mit Wänden und setPosition
            		Position p = new Position(nextPos.getCornerBottomLeft(hitbox).x +1, nextPos.getCornerBottomLeft(hitbox).y);
            		fieldsToWalk[0] = MenuStart.activeRoom.getField(p); //+1: damit Spieler durch Gänge passt
            		
            		p.setPosition(nextPos.getCornerBottomRight(hitbox).x -1, nextPos.getCornerBottomRight(hitbox).y);
            		fieldsToWalk[1] = MenuStart.activeRoom.getField(p);
            		
            		// Falls fieldsToWalk[0] und [1] begehbar, beweg dich einfach
            		if(rangeCheck()){
            			if (fieldsToWalk[0].walkable && fieldsToWalk[1].walkable) {
            				setPosition(nextPos);
            				break;
            				// Ansonsten liegt Kollision vor, daher Annäherung an Feldgrenze
            			} else {
            				int distance = fieldsToWalk[0].pos.toPosition().y - getPosition().getCornerBottomLeft(hitbox).y;
            				if (distance != 0)
            					setPosition(getPosition().x, getPosition().y + distance);
            				//Projektile verpuffen an Wand
            				else if(this instanceof Projectile){
            					Projectile proj = (Projectile)this;
            					proj.terminate();
            				}
            			}
            		}
            	}
            	//ansonsten Annäherung an Raumrand
            	else {
            		setPosition(nextPos.x, getRoom().getHeight()*32 - hitbox.height/2);	
            	}
            	
            	break;
        
            default:
            	break;
	
        }
	}
	
	//Kollisionsabfragen
	public boolean rangeCheck(){
		int xdelta;
		int ydelta;
		boolean flag = true;
		Entities testent = null;	//durch alle Entitys der Liste iterieren
		 LinkedList<Entities> tempEntities = getRoom().entities;
	        Iterator<Entities> iter = tempEntities.iterator();
		while(iter.hasNext()){
			testent = iter.next();
			if(testent != this && !(testent instanceof Item) &&
				//einem Projektil ist die Kollision mit dem Player egal
				!(this instanceof Projectile && testent instanceof Player)){

				//System.out.println(testent.getClass());
				xdelta = this.getPosition().x - testent.getPosition().x; //x-Abstand der Mittelpunkte bestimmen
				if(xdelta < 0)
					xdelta = xdelta * (-1);
				ydelta = this.getPosition().y - testent.getPosition().y; //y-Abstand der Mittelpunkte bestimmen
				if(ydelta < 0)
					ydelta = ydelta * (-1);
				if(Math.sqrt(xdelta*xdelta + ydelta*ydelta) < 50){	//Wenn wurzel(x^2 + y^2) < 50 ist, auf hitboxkollision prüfen
					if(hitboxCheck(this.getPosition(), testent) == false){
						
						if(this instanceof Projectile) {
							Projectile proj = (Projectile)this;
							proj.collision(testent);
						}
						return false;
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
	protected boolean hitboxCheck(Position pos, Entities testent) {
		if (this instanceof Projectile) {
			System.out.println("Ich bin ein Projektil und mache Hitboxcheck mit "+testent);
		}
		Entities test = testent;
		switch(direct){
		case LEFT:
			if(this.getPosition().x > test.getPosition().x){
			if(((pos.x - (this.getHitbox().width/2) - (speed*movePx)) - (test.getPosition().x + (test.getHitbox().width/2))) < 0){ //wenn der x-Abstand der Mittelpunkte - der weite des Schrittes - der halben Breite der getHitbox()en kleiner als 0 ist
				if(pos.y - test.getPosition().y > 0){ //überprüfe den y-Abstand
					if(((pos.y - (this.getHitbox().height/2)) - (test.getPosition().y + (test.getHitbox().height/2)) >= 0))
						return true;
					else
						return false;
				}
				else{
					if(((test.getPosition().y - (test.getHitbox().height/2)) - (pos.y + (this.getHitbox().height/2)) >= 0))
						return true;
					else
						return false;
				}
			}
			else
				return true;
			}
			return true;
		case RIGHT:
			if(this.getPosition().x < test.getPosition().x){
			if(((test.getPosition().x - (test.getHitbox().width/2) - (speed*movePx)) - (pos.x + (this.getHitbox().width/2))) < 0){
				if(pos.y - test.getPosition().y > 0){
					if(((pos.y - (this.getHitbox().height/2)) - (test.getPosition().y + (test.getHitbox().height/2)) >= 0))
						return true;
					else
						return false;
				}
				else{
					if(((test.getPosition().y - (test.getHitbox().height/2)) - (pos.y + (this.getHitbox().height/2)) >= 0))
						return true;
					else {
						collision(test);
						return false;
					}
				}
			}
			else
				return true;
		}
			return true;
		case UP:
			if(this.getPosition().y > test.getPosition().y){
			if(((pos.y - (this.getHitbox().height/2) - (speed*movePx)) - (test.getPosition().y + (test.getHitbox().height/2))) < 0){
				if(pos.x - test.getPosition().x > 0){
					if(((pos.x - (this.getHitbox().width/2)) - (test.getPosition().x + (test.getHitbox().width/2)) >= 0))
						return true;
					else
						return false;
				}
				else{
					if(((test.getPosition().x - (test.getHitbox().width/2)) - (pos.x + (this.getHitbox().width/2)) >= 0))
						return true;
					else
						return false;
				}
			}
			else
				return true;
			}
			return true;
		case DOWN:
			if(this.getPosition().y < test.getPosition().y){
			if(((test.getPosition().y - (test.getHitbox().height/2) - (speed*movePx)) - (pos.y + (this.getHitbox().height/2))) < 0){
				if(pos.x - test.getPosition().x > 0){
					if(((pos.x - (this.getHitbox().width/2)) - (test.getPosition().x + (test.getHitbox().width/2)) >= 0))
						return true;
					else
						return false;
				}
				else{
					if(((test.getPosition().x - (test.getHitbox().width/2)) - (pos.x + (this.getHitbox().width/2)) >= 0))
						return true;
					else
						return false;
				}
			}
			else
				return true;
			}
			return true;
		case NONE:
				if(test.getPosition().y < pos.y){
					if(((pos.y - (this.getHitbox().height/2) - (test.getPosition().y + (test.getHitbox().height/2))) < 0)){
						if(pos.x - test.getPosition().x > 0){
							if(((pos.x - (this.getHitbox().width/2)) - (test.getPosition().x + (test.getHitbox().width/2)) >= 0))
								return true;
							else
								return false;
						}
						else{
							if(((test.getPosition().x - (test.getHitbox().width/2)) - (pos.x + (this.getHitbox().width/2)) >= 0))
								return true;
							else
								return false;
						}
					}	
				}
				else{
					if(((test.getPosition().y - (test.getHitbox().height/2) - (speed*movePx)) - (pos.y + (this.getHitbox().height/2))) < 0){
						if(pos.x - test.getPosition().x > 0){
							if(((pos.x - (this.getHitbox().width/2)) - (test.getPosition().x + (test.getHitbox().width/2)) >= 0))
								return true;
							else
								return false;
						}
						else{
							if(((test.getPosition().x - (test.getHitbox().width/2)) - (pos.x + (this.getHitbox().width/2)) >= 0))
								return true;
							else
								return false;
						}
					}
					else
						return true;
				}
		default:
			return false;
		}
	}
	
	
	public void attack(){
			Position temp = new Position(this.getPosition().x,this.getPosition().y);
			switch(this.facedirect){
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
			LinkedList<Entities> tempEntities = getRoom().entities;
		    Iterator<Entities> iter = tempEntities.iterator();
			while(iter.hasNext()){
				if(testent != this){
					testent = iter.next();
					xdelta = temp.x - testent.getPosition().x; //x-Abstand der Mittelpunkte bestimmen
					if(xdelta < 0)
						xdelta = xdelta * (-1);
					ydelta = temp.y - testent.getPosition().y; //y-Abstand der Mittelpunkte bestimmen
					if(ydelta < 0)
						ydelta = ydelta * (-1);
					if(Math.sqrt(xdelta*xdelta + ydelta*ydelta) < 50){	//Wenn wurzel(x^2 + y^2) < 50 ist, auf hitboxkollision prüfen
						if(hitboxCheck(temp, testent) == false){
							testent.setHealth(testent.getHealth() - this.power);
							if(testent.getHealth() <= 0){
								getRoom().removeCandidates.add(testent);
							}
						}
					}
				}
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
    	// lastField, actualField setzten
    	actualField = getRoom().getField(getPosition());
    	Field nextField = getRoom().getField(x/32, y/32);
    	if (lastField == null || nextField != actualField) { // initial oder falls Feldwechsel vorliegt
    		lastField = actualField;
    	}
    	getPosition().setPosition(x, y);
    }
	
	public void setPosition(Position pos) {
		setPosition(pos.x, pos.y);;
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
		if (this.health <= 0 && ! (this instanceof Player))
			getRoom().removeCandidates.add(this);
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
		//lastField richtig setzen
		lastField = currentroom.getField(lastField.pos.x, lastField.pos.y);
		actualField = room.getField(actualField.pos.x, actualField.pos.y);
		currentroom = room;
	}
	
	public Direction getDirection(){
		return this.direct;
	}
	
	public void setDirection(Direction direct){
		this.direct = direct;
	}
	@Override
	public Hitbox getHitbox(){
		return hitbox;
	}
	
	public Direction getFaceDirection(){
		return facedirect;
	}
	
	public void setFaceDirection(Direction direct){
		facedirect = direct;
	}
	
	public int getAttackCount(){
		return attackcounter;
	}
	
	public void setAttackCount(int count){
		attackcounter = count;
	}
	
	public int getCastCount(){
		return castcounter;
	}
	
	public void setCastCount(int count){
		castcounter = count;
	}
	
	public void setAttack(boolean attack){
		this.attack = attack;
	}
	
	public boolean getAttack(){
		return attack;
	}
	
	public void setCast(String cast){
		this.cast = cast;
	}
	
	public String getCast(){
		return cast;
	}
}