package com.github.propra13.gruppeA3.Entities;

import java.util.Iterator;
import java.util.LinkedList;

import com.github.propra13.gruppeA3.Exceptions.MapFormatException;
import com.github.propra13.gruppeA3.Map.Field;
import com.github.propra13.gruppeA3.Map.FieldPosition;
import com.github.propra13.gruppeA3.Map.Link;
import com.github.propra13.gruppeA3.Map.Map;
import com.github.propra13.gruppeA3.Map.Position;
import com.github.propra13.gruppeA3.Map.Room;
import com.github.propra13.gruppeA3.Map.Trigger;
import com.github.propra13.gruppeA3.Menu.MenuStart;

/**
 * @author Majida Dere
 *         Diese Klasse definiert einen Spieler und seine Eigenschaften und Methoden.
 */

public class Player extends Moveable {
   // Attribute
	private int lives = 7;
	private int money = 0;
	private int mana = 100;
	private LinkedList<Item> items = null;
	
	private Buff buff;
	private Elements atkelement;
	private Elements defelement;

	final public static int movePx = Moveable.movePx;

	private int playerID=0;

    // Konstruktoren
    public Player(int roomID) {
        super(roomID);
        this.atkelement = Elements.PHYSICAL;
        this.defelement = Elements.PHYSICAL;
        hitbox = new Hitbox(28, 28);
        setFaceDirection(Direction.DOWN);
        setHealth(100);
        this.items = new LinkedList<Item>();
        initialize();
    }

    public Player(int roomID, int playerID, int x, int y){
    	this(roomID);
    	setPlayerID(playerID);
    	setPosition(x,y);
    }
    
    /**
     * Wird für das Protokoll verwendet
     * @param roomID
     * @param playerID
     * @param lives
     * @param health
     * @param speed
     * @param mana
     * @param dir
     * @param face
     * @param pos
     * @param armour
     * @param attack
     * @param attackCount
     */
    public Player(int roomID, int playerID, int lives, int health, double speed, int mana,
    			  int dir, int face, Position pos, int armour, int attack, int attackCount){
    	this(roomID);
    	setPlayerID(playerID);
    	setLives(lives);
    	setHealth(health);
    	setSpeed(speed);
    	setMana(mana);
    	if (dir == 1)
    		setDirection(Direction.LEFT);
    	else if(dir == 2)
    		setDirection(Direction.RIGHT);
    	else if (dir == 3)	
    		setDirection(Direction.UP);
    	else if(dir == 4)
    		setDirection(Direction.DOWN);
    	else
    		setDirection(Direction.NONE);
    		
    	if (face == 1)
    		setFaceDirection(Direction.LEFT);
    	else if(face == 2)
    		setFaceDirection(Direction.RIGHT);
    	else if (face == 3)	
    		setFaceDirection(Direction.UP);
    	else if(face == 4)
    		setFaceDirection(Direction.DOWN);
    	else
    		setFaceDirection(Direction.NONE);
    	
    	setPosition(pos);
    	setArmour(armour);
    	setAttack(attack);
    	setAttackCount(attackCount);
    }
    
    /**
     * Initialisiert den Spieler für den Start einer neuen Karte
     */
    public void initialize() {
    	MenuStart.activeRoom = Map.getRoom(0);
    	direct = Direction.NONE;
    	resetAttack();
    	setRoomID(0);
    	getRoom().entities.add(this);
    	setPosition(Map.spawns[0].pos.toPosition().x+16, Map.spawns[0].pos.toPosition().y+16);
    	
    }

    //Methode überschrieben, prüft für Spieler zusätzlich Trigger und ob bereits ein anderer Spieler auf dem Feld steht
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
            				setPosition(getPosition().x - distance, nextPos.y);
            			}
            		}
            	// ansonsten Annäherung an Raumrand 
        		} 
            	else {
        			setPosition(hitbox.width/2, nextPos.y);
        		}
            	
                break;

            case UP:
        		nextPos.setPosition(getPosition().x, getPosition().y - step);
        		// Checke, ob Spieler aus der Map rauslatscht anhand Hitbox
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
            				setPosition(getPosition().x, getPosition().y - distance);
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
            			}
            		}
            	}
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
            				setPosition(getPosition().x, getPosition().y + distance);
            			}
            		}
            	}
            	else {
            		setPosition(nextPos.x, getRoom().getHeight()*32 - hitbox.height/2);	
            	}
            	

                break;
                
            // Wasser-Check
            case NONE:
            	Field field = getRoom().getField(getPosition());
            	int swimStep = (int)((double)movePx*1.5);
            	if(field.type == 3 /*|| (lastField.type == 3 && field.link != null && field.link.isActivated()) */) { //Wasser oder Link nach Wasser
            		int moveDirection = -1; // Wird je nach Fließrichtung 1 oder -1
            		int distance;
            		switch(field.attribute1) {
            			//Fließrichtung horizontal
            			case 1: // Rechts; moveD umdrehen
            				moveDirection = moveDirection * (-1);
            			case 3: // Links
            				/* Falls Abstand zur Mitte des Flusses != 0, treibe zur Flussmitte
            				 * Distance < 0: Spieler über Fluss
            				 * Distance > 0: Spieler unter Fluss */
            				distance = getPosition().y - (field.pos.toPosition().y + 16);
            				if(distance != 0) {
            					// Falls relativ nah an Flussmitte, setze auf Flussmitte
            					if(Math.abs(distance) <= swimStep)
            						setPosition(getPosition().x, getPosition().y - distance);
            					// Ansonsten normale Schwimmbewegung zur Flussmitte
            					else {
            						int negFactor = 1;
            						if (distance < 0)
            							negFactor = -1;
            						setPosition(getPosition().x, getPosition().y - swimStep*negFactor);
            					}
            				}
            				// Ansonsten normale Schwimmbewegung mit Flussrichtung
            				else
            					setPosition(getPosition().x + swimStep * moveDirection, getPosition().y);
            				break;
            				
            				
            				//Fließrichtung vertikal
            			case 2: // Rechts; moveD umdrehen
            				moveDirection = moveDirection * (-1);
            			case 0: // Links
            				/* Falls Abstand zur Mitte des Flusses != 0, treibe zur Flussmitte
            				 * Distance < 0: Spieler links vom Fluss
            				 * Distance > 0: Spieler rechts vom Fluss */
            				distance = getPosition().x - (field.pos.toPosition().x + 16);
            				if(distance != 0)
            					// Falls relativ nah an Flussmitte, setze auf Flussmitte
            					if(Math.abs(distance) <= swimStep)
            						setPosition(getPosition().x - distance, getPosition().y);
            					// Ansonsten normale Schwimmbewegung zur Flussmitte
            					else {
            						int negFactor = 1;
            						if (distance < 0)
            							negFactor = -1;
            						setPosition(getPosition().x - swimStep*negFactor, getPosition().y);
            					}
            				// Ansonsten normale Schwimmbewegung mit Flussrichtung
            				else
            					setPosition(getPosition().x, getPosition().y + swimStep * moveDirection);
            				break;
            		}
            	}
            	else
            		setPosition(getPosition());
            	
            default:
                //nichts tun
        }
        
        // Links
        if(getRoom().getField(getFieldPos()).link != null){
        	if (getRoom().getField(getFieldPos()).link.isActivated() && lastField.link == null) {
        		try {
					followLink(getRoom().getField(getFieldPos()).link);
				} catch (MapFormatException e) {
					e.printStackTrace();
				}
        	}
        }
        
        // Win
        if(getRoom().roomFields[getPosition().toFieldPos().x][getPosition().toFieldPos().y]== Map.end && Map.endIsOpen)
        	this.win();
        
        // Trigger
        Trigger trigger = getRoom().roomFields[getPosition().toFieldPos().x][getPosition().toFieldPos().y].trigger;
        if (trigger != null) {
        	trigger.trigger();
    	}
        pickupItems();
    }
    
    public void attack(){
		Position temp = new Position(this.getPosition().x,this.getPosition().y);
		switch(this.getFaceDirection()){
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
		@SuppressWarnings("unchecked")
		LinkedList<Entities> tempEntities = (LinkedList<Entities>) getRoom().entities.clone();
	    Iterator<Entities> iter = tempEntities.iterator();
	    Monster monster = null;
		while(iter.hasNext()){
			testent = iter.next();
			if(testent != this){	
				xdelta = temp.x - testent.getPosition().x; //x-Abstand der Mittelpunkte bestimmen
				if(xdelta < 0)
					xdelta = xdelta * (-1);
				ydelta = temp.y - testent.getPosition().y; //y-Abstand der Mittelpunkte bestimmen
				if(ydelta < 0)
					ydelta = ydelta * (-1);
				if(Math.sqrt(xdelta*xdelta + ydelta*ydelta) < 50){	//Wenn wurzel(x^2 + y^2) < 50 ist, auf hitboxkollision prüfen
					if(hitboxCheck(temp, testent) == false){
						if(testent instanceof Monster){
							monster = (Monster)testent;
							monster.setHealth(monster.getHealth() - calculateDamage(monster));
							if(monster.getHealth() <= 0){
								monster.death();
							}
						}
					}
				}
			}
		}
	}
    
    private int calculateDamage(Monster monster){
		int damage=0;
		switch(this.getAttackElement()){
			case PHYSICAL:{
				switch(monster.getElement()){
					case PHYSICAL:{
						damage = this.getAttack() - monster.getArmour();
						break;
					}
					case FIRE:{
						damage = (int)((this.getAttack() - monster.getArmour())*0.7);
						break;
					}
					case WATER:{
						damage = (int)((this.getAttack() - monster.getArmour())*0.7);
						break;
					}
					case ICE:{
						damage = (int)((this.getAttack() - monster.getArmour())*0.7);
						break;
					}
				}
				break;
			}
			case FIRE:{
				switch(monster.getElement()){
					case PHYSICAL:{
						damage = this.getAttack() - monster.getArmour();
						break;
					}
					case FIRE:{
						damage = (int)((damage = this.getAttack() - monster.getArmour())*0.7);
						break;
					}
					case WATER:{
						damage = (int)((this.getAttack() - monster.getArmour())*0.5);
						break;
					}
					case ICE:{
						damage = (int)((this.getAttack() - monster.getArmour())*2.0);
						break;
					}
				}
				break;
			}
			case WATER:{
				switch(monster.getElement()){
					case PHYSICAL:{
						damage = this.getAttack() - monster.getArmour();
						break;
					}
					case FIRE:{
						damage = (int)((this.getAttack() - monster.getArmour())*2.0);
						break;
					}
					case WATER:{
						damage = (int)((this.getAttack() - monster.getArmour())*0.7);
						break;
					}
					case ICE:{
						damage = (int)((this.getAttack() - monster.getArmour())*0.5);
						break;
					}
				}
				break;
			}
			case ICE:{
				switch(monster.getElement()){
					case PHYSICAL:{
						damage = this.getAttack() - monster.getArmour();
						break;
					}
					case FIRE:{
						damage = (int)((this.getAttack() - monster.getArmour())*0.5);
						break;
					}
					case WATER:{
						damage = (int)((this.getAttack() - monster.getArmour())*2.0);
						break;
					}
					case ICE:{
						damage = (int)((this.getAttack() - monster.getArmour())*0.7);
						break;
					}
				}
				break;
			}
		}
		
		if(damage <= 0){
			damage = 1;
		}
		return damage;
	}
    
    /**
     * Beendet die Karte. Wird aufgerufen, wenn der Spieler die Karte gewonnen hat.
     */
    public void win() {
    	setPosition(Map.spawns[0].pos.toPosition().x+16, Map.spawns[0].pos.toPosition().y+16);
    	MenuStart.winMap();
    }
    
    /**
     * Setzt je nach Lebensanzahl den Spieler zurück auf den Spawn oder beendet das Spiel.
     * Wird aufgerufen, wenn der Spieler stirbt.
     */
    public void death() {
    	setLives(getLives()-1);
    	setPosition(Map.spawns[0].pos.toPosition().x+16, Map.spawns[0].pos.toPosition().y+16);
    	setRoom(0);
    	MenuStart.activeRoom = Map.getRoom(0);
    	setHealth(100);
    	if(getLives() == 0){
    		MenuStart.death();
    	}
    } 
    
    /** 
     * Benutzt einen gegebenen Link; geht in den targetRoom, der nicht der aktuelle ist.
     * @param link Zu nutzender Link
     * @throws MapFormatException falls Link nicht am Raumrand liegt.
     */
    private void followLink(Link link) throws MapFormatException {
    	Room targetRoom;
    	Field targetField/* = new Field()*/;
    	if(getRoom() == link.targetRooms[0]) {
    		targetRoom = link.targetRooms[1];
    		targetField = link.targetFields[1];
    	}
    	else {
    		targetRoom = link.targetRooms[0];
    		targetField = link.targetFields[0];
    	}
    	
    	//Raumrand finden, wo der Link liegt
    	FieldPosition field = getPosition().toFieldPos();
    	boolean followLink = false;
    	//oben
    	if(field.y == 0) {
    		if(getPosition().getCornerTopLeft(hitbox).y <= 1) //1: Gnadensspielraum für Raumwechsel
    			followLink = true;
    	//links
    	} else if(field.x == 0) {
    		if(getPosition().getCornerTopLeft(hitbox).x <= 1)
    			followLink = true;
    	//unten
    	} else if(field.y == MenuStart.activeRoom.getHeight() - 1) {
    		if(getPosition().getCornerBottomRight(hitbox).y >= MenuStart.activeRoom.getHeight()*32 - 1)
    			followLink = true;
    	//rechts
    	} else if(field.x == MenuStart.activeRoom.getWidth() - 1) {
    		if(getPosition().getCornerBottomRight(hitbox).x >= MenuStart.activeRoom.getHeight()*32 - 1)
    			followLink = true;
    	} else {
    		//TODO: Links mitten im Raum zulassen
    		throw new MapFormatException("Link "+link.ID+" in Raum "+MenuStart.activeRoom.ID+" ist nicht am Rand.");
    	}
    	
    	// Wechsle Raum, falls Spieler direkt am Raumrand
    	// WICHTIG! Erst Position wechseln, dann Raum, ansonsten stimmt lastField nicht
    	if (followLink) {
    		this.setPosition(targetField.pos.toPosition().x+16, targetField.pos.toPosition().y+16);
    		setRoom(targetRoom.ID);
    		MenuStart.activeRoom = targetRoom;
    		getRoom().entities.add(this);
    	}
    }
    
    private void pickupItems(){
    	 /** Items aufsammeln, wenn man sie ber�hrt **/
        @SuppressWarnings("unchecked")
		LinkedList<Entities> list = (LinkedList<Entities>) getRoom().entities.clone();
        Iterator<Entities> iter = list.iterator();
        Entities testEntity = null;
        while(iter.hasNext()) {
        	testEntity = iter.next();
        	if (testEntity instanceof Item){
        		if(this.getPosition().toFieldPos(). equals(testEntity.getPosition().toFieldPos())){
        			useItem((Item) testEntity);
        			getRoom().removeCandidates.add(testEntity);
        			
        	}
        		/** Coins aufsammeln, wenn Monster tot **/
        	} else if(testEntity instanceof Coin){
        		if(this.getPosition().toFieldPos(). equals(testEntity.getPosition().toFieldPos())){
        			this.setMoney(getMoney() + ((Coin)testEntity).getValue());
        			getRoom().removeCandidates.add(testEntity);
        		}
        	}
        }
    }
    
    /**
     * Feuert einen Plasmaball in die Richtung, in die der Spieler schaut
     */
    public void firePlasma() {
    	if (mana - PlasmaBall.manaCost >= 0) {
    		mana = mana - PlasmaBall.manaCost;
    		new PlasmaBall(this.roomID, getPosition(), getFaceDirection(), getFaceDirection());
    	}
    }
    
    /**
     * Feuert acht Plasmakugeln in alle Richtungen
     */
    public void fireAOEPlasma() {
    	if (mana - 4*PlasmaBall.manaCost >= 0) {
    		mana = mana - 4*PlasmaBall.manaCost;
    		
    		//acht Richtungen
    		new PlasmaBall(this.roomID, getPosition(), Moveable.Direction.LEFT, Moveable.Direction.LEFT);
    		new PlasmaBall(this.roomID, getPosition(), Moveable.Direction.DOWN, Moveable.Direction.LEFT);
    		new PlasmaBall(this.roomID, getPosition(), Moveable.Direction.DOWN, Moveable.Direction.DOWN);
    		new PlasmaBall(this.roomID, getPosition(), Moveable.Direction.RIGHT, Moveable.Direction.DOWN);
    		new PlasmaBall(this.roomID, getPosition(), Moveable.Direction.RIGHT, Moveable.Direction.RIGHT);
    		new PlasmaBall(this.roomID, getPosition(), Moveable.Direction.UP, Moveable.Direction.RIGHT);
    		new PlasmaBall(this.roomID, getPosition(), Moveable.Direction.UP, Moveable.Direction.UP);
    		new PlasmaBall(this.roomID, getPosition(), Moveable.Direction.LEFT, Moveable.Direction.UP);
    	}
    }
    
    /** 
     * Löscht den Buff des Spielers.
     */
    public void delBuff() {
    	buff = null;
    }
    
    /**
     * Getter-Methode für den Buff-Slot
     * @return Gibt den Buff zurück, den der Spieler derzeit hat
     */
    public Buff getBuff(){
    	return buff;
    }
    
    /**
     * Setzt einen Geschwindigkeitsbuff; doppelte Geschwindigkeit für fünf Sekunden
     */
    public void setSpeedBuff() {
    	if (buff == null && getMana() -  SpeedBuff.manaCost >= 0)
    		buff = new SpeedBuff(this, 2.0, 5);
    }
    
    /**
     * Setzt einen Angriffsbuff, 1.5-fache Angriffsstärke für fünf Sekunden
     */
    public void setAttackBuff() {
    	if (buff == null && getMana() -  SpeedBuff.manaCost >= 0)
    		buff = new AttackBuff(this, 1.5, 5);
    }
    
    public int getLives() {
    	return lives;
    }
    
    public void setLives(int lives){
    	this.lives = lives;
    }

    public int getMoney(){
    	return this.money;
    }
    
    public void setMoney(int money){
    	this.money = money;
    }

    public int getMana(){
    	return mana;
    }
    
    public void setMana(int mana){
    	this.mana = mana;
    	if (mana < 0)
			try {
				throw new Exception("Negative Mana");
			} catch (Exception e) {
				e.printStackTrace();
			}
    }
    
    /**
     * Items werden nach dem Aufsammeln direkt benutzt
     * @param item
     */
    public void useItem(Item item){
    	switch(item.getType()){
    	case 1:
			if(this.getHealth()<100){
				this.setHealth(this.getHealth() + item.getDamage());
				if(this.getHealth() > 100){
					this.setHealth(100);
				}
			}
			break;
			
		case 2:
			this.setHealth(this.getHealth() + item.getDamage());
			if(this.getHealth() < 1){
				this.death();
			}
			break;
			
		case 3:
			if(this.getMana() < 100){
				this.setMana(this.getMana() + item.getDamage());
				if(this.getMana() > 100){
					this.setMana(100);
				}
			}
			break;
			
		case 4:
			this.addAttackSummand(item.getDamage());
			this.items.add(item);
			this.setAttackElement(item.getElement());
			break;
			
		case 5:
			this.setArmour(this.getArmour() + item.getDamage());
			this.items.add(item);
			this.setDefenseElement(item.getElement());
			break;
			
		default:
			break;
    	}
    }
    
    /*
	@Override
	public Hitbox getHitbox() {
		// TODO Auto-generated method stub
		return hitbox;
	}
	*/

    //Dummies
  	@Override
  	public void tick() {}
  	@Override
  	public void collision(Entities entity) {}

	/**
	 * @return the playerID
	 */
	public int getPlayerID() {
		return playerID;
	}

	/**
	 * @param playerID the playerID to set
	 */
	public void setPlayerID(int playerID) {
		this.playerID = playerID;
	}
	public void setAttackElement(Elements element){
		this.atkelement = element;
	}
	
	public Elements getAttackElement(){
		return this.atkelement;
	}
	
	public void setDefenseElement(Elements element){
		this.defelement = element;
	}
	
	public Elements getDefenseElement(){
		return this.defelement;
	}
}

