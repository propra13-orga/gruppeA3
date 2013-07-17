package com.github.propra13.gruppeA3.Entities;

import java.util.Iterator;
import java.util.LinkedList;
import java.lang.Math;

import com.github.propra13.gruppeA3.Map.Field;
import com.github.propra13.gruppeA3.Map.FieldPosition;
import com.github.propra13.gruppeA3.Map.Map;
import com.github.propra13.gruppeA3.Map.Position;
import com.github.propra13.gruppeA3.Map.Room;
import com.github.propra13.gruppeA3.Menu.MenuStart;

/**
 * Diese Klasse dient als Vorlage für alle bewegbaren Objekte im Spiel.
 *
 */
public abstract class Moveable extends Entities {
	

	public static enum Direction{LEFT,RIGHT,UP,DOWN,NONE}
	protected Direction direct;
	private Direction facedirect;
	protected Position pos;
	protected int roomID;
	public static enum Element {PHYSICAL,FIRE,WATER,ICE}

	protected Hitbox hitbox;
	protected int health;
	private int attack;
	private int armour;
	private double speed;
	private int attackcounter;
	private int castcounter;
	private boolean isAttacking;
	private String cast;
	
	// falls Bossmonster, öffnet sich das Ziel, wenn besiegt
	public boolean isBoss;
	
	private LinkedList<Double> speedFactors = new LinkedList<Double>();
	private LinkedList<Double> attackFactors = new LinkedList<Double>();
	private LinkedList<Integer> attackSummands = new LinkedList<Integer>();
	
	protected Field actualField; //Feld, wo das Ding derzeit ist
	protected Field lastField; //Feld, wo das Ding vor dem aktuellen Movement war
	
	/* Anzahl der Pixel, die bei einer Bewegung
	 * mit Speed 1 zurückgelegt werden sollen */
	final public static int movePx = 2;
	
	
	/**
	 * Der Konstruktor wird von den Unterklassen aufgerufen und erstellt zuerst ein Dummy-Moveable, 
	 * bevor die Werte im Konstruktor der Unterklasse sinnvoll belegt werden
	 * @param roomID Die ID des Raums in dem das Moveable erstellt werden soll
	 */
	public Moveable(int roomID){
		this.pos = new Position(0,0);
		this.roomID = roomID;
		this.direct = Direction.NONE;
		this.attack = 1;
		this.speed = 1; 
		this.armour = 0;
		this.health = 1;
		this.facedirect = Direction.NONE;
		this.attackcounter = 0;
		this.castcounter = 0;
		this.isAttacking = false;
		this.cast = "";
	}

	/** 
	 * Diese Methode bewegt ein bewegbares Objekt im Raum, prüft die Begehbarkeit und wendet setposition() an,
	 *  wenn die neue Position begehbar ist
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
        			setPosition(hitbox.width/2, nextPos.y);
        		}
            	
                break;

            case UP:
        		nextPos.setPosition(getPosition().x, getPosition().y - step);
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
            			}
            			// Ansonsten liegt Kollision vor, daher Annäherung an Feldgrenze
            			else {
            				int distance = getPosition().getCornerTopLeft(hitbox).y - (fieldsToWalk[0].pos.toPosition().y + 32);
            				if (distance != 0) {
            					setPosition(getPosition().x, getPosition().y - distance);
            				}
            				// Projektile verpuffen an Wand
            				else if(this instanceof Projectile) {
            					Projectile proj = (Projectile)this;
            					proj.terminate();
            				}
            			}
            		}
            	}
            	//ansonsten Annäherung an Raumrand
        		else {
        			setPosition(nextPos.x, hitbox.height/2);
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
            				setPosition(getPosition().x + distance, nextPos.y);
            				//Projektile verpuffen an Wand
            				if(distance == 0 && this instanceof Projectile){
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
	/**
	 * Prüft ob es ein Objekt gibt, dessen Abstand Mittelpunkt zu Mittelpunkt unter 60Pixeln liegt, falls ja wird hitboxCheck() aufgerufen
	 * @see Moveable.hitboxCheck()
	 * @return flag - false, wenn eine Kollision vorliegt, true, wenn keine Kollision vorliegt
	 */
	//Kollisionsabfragen
	public boolean rangeCheck(){
		int xdelta;
		int ydelta;
		boolean flag = true;
		Entities testent = null;	//durch alle Entitys der Liste iterieren
		@SuppressWarnings("unchecked")
		LinkedList<Entities> tempEntities = (LinkedList<Entities>) getRoom().entities.clone();
	    Iterator<Entities> iter = tempEntities.iterator();
		while(iter.hasNext()){
			testent = iter.next();
			if(testent != this && !(testent instanceof Item) && !(testent instanceof Coin) &&
				//einem Projektil ist die Kollision mit dem Player egal
				!(this instanceof Projectile && testent instanceof Player)){

				//System.out.println(testent.getClass());
				xdelta = this.getPosition().x - testent.getPosition().x; //x-Abstand der Mittelpunkte bestimmen
				if(xdelta < 0)
					xdelta = xdelta * (-1);
				ydelta = this.getPosition().y - testent.getPosition().y; //y-Abstand der Mittelpunkte bestimmen
				if(ydelta < 0)
					ydelta = ydelta * (-1);
				if(Math.sqrt(xdelta*xdelta + ydelta*ydelta) < 60){	//Wenn wurzel(x^2 + y^2) < 60 ist, auf hitboxkollision prüfen
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
	 * @param testent Kollisionsgegner
	 * @param pos Die Position, auf die gelaufen werden soll
	 * @see Moveable.rangeCheck()
	 * @return Kollisionswahrheitswert
	 */
	public boolean hitboxCheck(Position pos, Entities testent) {
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
						//collision(test); <-- mutterseelenallein vorgefunden
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
	
	
	
	/**
	 * Diese Methode liefert die aktuelle Position im Raum
	 * @return pos - liefert die Position im Raum
	 */
	public Position getPosition(){
		return this.pos;
	}

	/**
	 * Ändert die aktuelle Position im Raum.
	 * Prüft auf Verlassen des Raums und nähert ggf. an Raumrand an.
	 * @param x X-Koordinate
	 * @param y Y-Koordinate
	 */
	public void setPosition(int x, int y) {
		Direction uebertritt = Direction.NONE;
		//Check, ob der Raum verlassen würde und ggf. intervenieren
		if((x + hitbox.width/2) /32 > Map.ROOMWIDTH - 1)
			uebertritt = Direction.RIGHT;
		else if((x - hitbox.width/2) /32 < 0)
			uebertritt = Direction.LEFT;
		else if((y + hitbox.height/2) /32 > Map.ROOMHEIGHT - 1)
			uebertritt = Direction.DOWN;
		else if((y - hitbox.height/2) /32 < 0)
			uebertritt = Direction.UP;
		//Falls kein Übertritt vorliegt, normale Bewegung
		else
		{
	    	// lastField, actualField setzten
	    	actualField = getRoom().getField(getPosition());
	    	Field nextField = getRoom().getField(x/32, y/32);
	    	if (lastField == null || nextField != actualField) { // initial oder falls Feldwechsel vorliegt
	    		lastField = actualField;
	    	}
	    	getPosition().setPosition(x, y);
		}
		//ggf. Annäherung an Raumrand
		if(uebertritt != Direction.NONE) {
			switch(uebertritt) {
			case UP:
				getPosition().setPosition(x, hitbox.height / 2);
				break;
			case LEFT:
				getPosition().setPosition(hitbox.width/2, y);
				break;
			case DOWN:
				getPosition().setPosition(x, Map.ROOMHEIGHT*32 - hitbox.height/2);
				break;
			case RIGHT:
				getPosition().setPosition(Map.ROOMWIDTH*32 - hitbox.width/2, y);
				break;
			default:
				break;
			}
		}
    }
	
	public void setPosition(Position pos) {
		setPosition(pos.x, pos.y);
	}
	
	public FieldPosition getFieldPos() {
		return pos.toFieldPos();
	}
	
	/**
	 * Diese Methode setzt den aktuellen Health Status eines bewegbaren Objektes
	 * @param health zu setzende Lebenspunkte
	 */
	public void setHealth(int health){
		this.health = health;
		if (this.health <= 0 && ! (this instanceof Player)) {
			getRoom().removeCandidates.add(this);
			if (isBoss) {
				Map.endIsOpen = true;
			}
		}
	}

	/**
	 * Diese Methode liefert den aktuellen Health Status eines bewegbaren Objektes
	 * @return liefert ein int Leben
	 */
	public int getHealth(){	
		return this.health;
	}
	
	/**
	 * Liefert die Angriiffsstärke eines Objekts
	 * @return attack - die Angriffsstärke
	 */
	public int getAttack(){
		return attack;
	}
	/**
	 * Liefert die Stärke der Rüstung
	 * @return armour - die Stärke der Rüstung
	 */
	public int getArmour(){
		return armour;
	}
	
	/**
	 * Setzt die Stärke der Rüstung
	 * @param armour zu setzende Stärke
	 */
	public void setArmour(int armour) {
		this.armour = armour;
	}
	
	/**
	 * Liefert die Geschwindigkeit eines beweglichen Objekts
	 * @return speed - die Geschwindigkeit
	 */
	public double getSpeed(){
		return speed;
	}
	
	/**
	 * Setzt die Geschwindigkeit eines beweglichen Objekts
	 * @param speed die zu setztende Geschwindigkeit
	 */
	public void setSpeed(double speed){
		this.speed = speed;
	}
	
	
	/* Faktor-Methoden: Fügen zu Speed, Angriff und Rüstung
	 * Faktoren hinzu, sodass Faktoren ordnungsgemäß hinzugefügt
	 * und wieder entfernt werden können.
	 * reset() rechnet jeweils die Faktoren (und ggf. Summanden) zusammen.
	 */
	
	/**
	 * Fügt einen Speed-Faktor hinzu.
	 * @param factor Faktor, der hinzugefügt werden soll.
	 */
	public void addSpeedFactor(double factor) {
		speedFactors.add(factor);
		resetSpeed();
	}
	
	/**
	 * Löscht einen Speed-Faktor.
	 * @param factor Faktor, der gelöscht werden soll.
	 */
	public void delSpeedFactor(double factor) {
		if (! speedFactors.remove(factor)) {
			try {
				throw new Exception("Speedfaktorliste: Sollte "+factor+" entfernen, habs aber nicht gefunden.");
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		resetSpeed();
	}
	
	/**
	 * Berechnet die Geschwindigkeit neu.
	 */
	public void resetSpeed() {
		speed = 1.0;
		for(Iterator<Double> iter = speedFactors.iterator(); iter.hasNext();)
			speed = speed*iter.next();
	}
	
	/**
	 * Fügt einen Angriffsfaktor hinzu.
	 * @param factor Faktor, der hinzugefügt werden soll.
	 */
	public void addAttackFactor(double factor) {
		attackFactors.add(factor);
		resetAttack();
	}
	
	/**
	 * Fügt einen Angriffssummanden hinzu.
	 * @param summand Summand, der hinzugefügt werden soll.
	 */
	public void addAttackSummand(int summand) {
		attackSummands.add(summand);
		resetAttack();
	}
	
	/**
	 * Löscht einen Angriffsfaktor.
	 * @param factor Faktor, der gelöscht werden soll.
	 */
	public void delAttackFactor(double factor) {
		attackFactors.remove(factor);
		resetAttack();
	}
	
	/**
	 * Löscht einen Angriffssummanden.
	 * @param summand Summand, der gelöscht werden soll.
	 */
	public void delAttackSummand(int summand) {
		attackSummands.remove(summand);
		resetAttack();
	}
	
	/**
	 * Berechnet die Angriffsstärke neu.
	 */
	public void resetAttack() {
		attack = 0;
		for(Iterator<Integer> iter = attackSummands.iterator(); iter.hasNext();)
			attack = attack + iter.next();
		if(attack == 0) //Keine Summanden vorrätig
			attack = 1; //wg Multiplikation
		for(Iterator<Double> iter = attackFactors.iterator(); iter.hasNext();)
			attack = (int)((double)attack*iter.next());
	}
	
	/**
	 * Setzt die Grundangriffsstärke eines beweglichen Objekts
	 * @param attack die zu setzende Angriffsstärke
	 */
	public void setAttack(int attack){
		this.attack = attack;
	}
	
	/**
	 * Liefert den Raum, in dem sich das bewegliche Objekt aufhält
	 * @return room - der Raum in dem sich das Objekt befindet
	 */
	public Room getRoom(){
		return Map.getRoom(roomID);
	}
	
	/**
	 * Setzt den Raum, in dem sich ein Objekt befindet
	 * @param roomID Die ID des neuen Raums
	 */
	public void setRoom(int roomID){
		//lastField richtig setzen
		lastField = getRoom().getField(lastField.pos.x, lastField.pos.y);
		actualField = Map.getRoom(roomID).getField(actualField.pos.x, actualField.pos.y);
		this.roomID = roomID;
	}
	
	/**
	 * Liefert die Richtung in die sich das Objekt bewegen soll
	 * @return direct - Die Richtung in die sich Obhjekt bewegen soll
	 */
	public Direction getDirection(){
		return this.direct;
	}
	
	/**
	 * Setzt die Richtung, in die sich das Objekt bewegen soll
	 * @param direct Die Richtung in die sich das Objekt bewegen soll
	 */
	public void setDirection(Direction direct){
		this.direct = direct;
	}
	/**
	 * @see Entities.getHitbox()
	 */
	@Override
	public Hitbox getHitbox(){
		return hitbox;
	}
	
	/**
	 * Liefert die Blickrichtung des Objekts
	 * @return facedirect - Die Blickrichtung des Objekts
	 */
	public Direction getFaceDirection(){
		return facedirect;
	}
	
	/**Setzt die Blickrichtung eines Objekts
	 * @param direct Die zu setzende Richtung
	 */
	public void setFaceDirection(Direction direct){
		facedirect = direct;
	}
	
	/**
	 * Liefert den Zähler, wann wieder ein Angriff ausgeführt werden kann
	 * @return attackcounter - Der Angriffszähler
	 */
	public int getAttackCount(){
		return attackcounter;
	}
	
	/**
	 * Setzt den Zähler, wann wieder ein Angriff ausgeführt werden kann
	 * @param count Anzahl der Spielticks, bis wieder ein Angriff ausgeführt werden kann
	 */
	public void setAttackCount(int count){
		attackcounter = count;
	}
	
	/**
	 * Liefert den Zähler, wann wieder gezaubert werden kann
	 * @return castcounter -  Den Zauberzähler
	 */
	public int getCastCount(){
		return castcounter;
	}
	
	/**
	 * Setzt den Zähler, wann wieder gezaubert werden kann
	 * @param count Anzahl der Spielticks, bis wieder gezaubert werden kann
	 */
	public void setCastCount(int count){
		castcounter = count;
	}
	
	/**
	 * Setzt den Wert, ob ein Objekt im aktuellen Tick angreifen will
	 * @param isAttacking Wert, ob das Objekt angreifen will
	 */
	public void setAttack(boolean isAttacking){
		this.isAttacking = isAttacking;
	}
	
	/**
	 * Liefert den Wert, ob ein Objekt im aktuellen Tick angreifen will
	 * @return isAttacking - Wert, ob das Objekt angreifen will
	 */
	public boolean getIsAttacking(){
		return isAttacking;
	}
	/**
	 * Setzt den nächsten auszuführenden Zauber
	 * @param cast Zauber der ausgeführt werden soll
	 */
	public void setCast(String cast){
		this.cast = cast;
	}
	/**
	 * Gibt den nächsten auszuführenden Zauber zurück
	 * @return cast - Der nächste auszuführende Zauber
	 */
	public String getCast(){
		return cast;
	}
	
	/**
	 * Gibt den Namen eines Elementes als String zurück
	 * @param element Das Element
	 * @return elName - Den Namen des Elements
	 */
	public static String getElementName(Element element) {
		String elName = null;
		switch(element) {
		case FIRE:
			elName = "Feuer";
			break;
		case ICE:
			elName = "Eis";
			break;
		case PHYSICAL:
			elName = "Physisch";
			break;
		case WATER:
			elName = "Wasser";
			break;
		}
		
		return elName;
	}
	
	/*
	 * Map-Editor-Methoden
	 */
	
	/**
	 * Setzt die Angriffsstärke zurück. Wird im Map-Editor genutzt.
	 * @param newAttack Neuer Angriffswert.
	 */
	public void resetAttack(int newAttack) {
		attackFactors.clear();
		attackSummands.clear();
		addAttackFactor(newAttack);
	}
	
	/**
	 * Setzt die Geschwindigkeit zurück. Wird im Map-Editor genutzt.
	 * @param newSpeed Neuer Geschwindigkeitswert.
	 */
	public void resetSpeed(double newSpeed) {
		speedFactors.clear();
		addSpeedFactor(newSpeed);
	}
	
	/**
	 * Liefert die RaumID des Objekts
	 * @return roomID - Die RaumID
	 */
	public int getRoomID() {
		return roomID;
	}
	
	/**
	 * Setzt die RaumID eines Objekts
	 * @param roomID die zu setzende ID
	 */
	public void setRoomID(int roomID) {
		this.roomID = roomID;
	}
	

}