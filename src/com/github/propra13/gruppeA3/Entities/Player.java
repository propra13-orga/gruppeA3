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
	@SuppressWarnings("unused")
	private int health = 100;
	private int lives = 7;
	private int money = 0;
	private int mana = 100;
	
	public Buff buff;

	final public static int movePx = Moveable.movePx;
	

    // Konstruktoren
    public Player(Room room_bind) {
        super(room_bind);
        //+16: damit Player in der Mitte des Felds landet
        setPosition(Map.spawns[0].pos.toPosition().x+16, Map.spawns[0].pos.toPosition().y+16);
        hitbox = new Hitbox(28, 28);
        setFaceDirection(direction.DOWN);
    }

    //Methode überschrieben, prüft für Spieler zusätzlich Trigger und ob bereits ein anderer Spieler auf dem Feld steht
    public void move() {
    	if (this.getDirection() != direction.NONE)
    		System.out.println("Setze Richtung auf "+this.getDirection());

    	int step = (int)(movePx * getSpeed());
    	Position nextPos = new Position(0,0); //Position, auf die gelaufen werden soll
    	Field[] fieldsToWalk = new Field[2];  // Felder, die betreten werden sollen

    	boolean wannaPrint = true;
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
        			System.out.println("bin am Rand");
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
            	if(field.type == 3) { //Wasser
            		int moveNegFactor = -1; // Wird je nach Fließrichtung 1 oder -1
            		switch(field.attribute1) {
            			//Fließrichtung horizontal
            			case 1: // Rechts
            				moveNegFactor = moveNegFactor * (-1);
            			case 3: // Links
            				/* Falls Abstand zur Mitte des Flusses != 0, treibe zur Flussmitte
            				 * Distance < 0: Spieler über Fluss
            				 * Distance > 0: Spieler unter Fluss */
            				int distance = getPosition().y - field.pos.toPosition().y + 16;
            				if(getPosition().y - distance != 0)
            					// Falls relativ nah an Flussmitte, setze auf Flussmitte
            					if(Math.abs(distance) <= swimStep)
            						setPosition(getPosition().x, getPosition().y - distance);
            					// Ansonsten normale Schwimmbewegung zur Flussmitte
            					else {
            						System.out.println("Schwimme richtung Mitte");
            						int negFactor = 1;
            						if (distance < 0)
            							negFactor = -1;
            						setPosition(getPosition().x, getPosition().y + swimStep*negFactor);
            					}
            				// Ansonsten normale Schwimmbewegung mit Flussrichtung
            				else
            					setPosition(getPosition().x + swimStep * moveNegFactor, getPosition().y);
            				break;
            		}
            	}
            	else
            		wannaPrint = false;
            	
            default:
                //nichts tun
        }
        if (wannaPrint)
        	System.out.println("Spielerpos: "+getPosition().x+":"+getPosition().y);

        /**
         * Die Entitites Liste soll durchlaufen werden, um zu überprüfen, ob an der Position xy des Spielers ein Monster ist.
         * TODO: Pixelkoordinatensystemkollisionsabfrage
         */
        LinkedList<Entities> tempEntities = getRoom().entities;
        Iterator<Entities> iter = tempEntities.iterator();

        
        Entities testEntity;
        Monster monster = null;
        @SuppressWarnings("unused")
		Item item = null;
        while (iter.hasNext()) {
            testEntity = iter.next();
            if (testEntity instanceof Monster)
            	if ((getFieldPos().x == (testEntity.getPosition().x)/32) && (getFieldPos().y == (testEntity.getPosition().y)/32)){
            		//funktioniert nicht mehr JPanel
            		monster = (Monster)testEntity;
            		System.out.println("Monster: "+monster.getPosition().x+", "+monster.getPosition().y);
            		System.out.println("Monster: "+getPosition().x+", "+getPosition().y);
            		this.death();
            	}
        }
		
        // Links
        if(getRoom().getField(getFieldPos()).link != null){
        	System.out.println("Ich bin auf nen Link gelatscht!");
    	
        	if (getRoom().roomFields[getFieldPos().x][getFieldPos().y].link.isActivated()) {
        		try {
					followLink(getRoom().roomFields[getFieldPos().x][getFieldPos().y].link);
				} catch (MapFormatException e) {
					e.printStackTrace();
				}
        	}
        }
        
        // Win
        if(getRoom().roomFields[getPosition().toFieldPos().x][getPosition().toFieldPos().y]== Map.end)
        	this.win();
        
        // Trigger
        Trigger trigger = getRoom().roomFields[getPosition().toFieldPos().x][getPosition().toFieldPos().y].trigger;
        if (trigger != null) {
        	trigger.trigger();
    	}
    }
    private void win() {
    	MenuStart.win=true;
    	MenuStart.ingame=false;
    	MenuStart.menu=false;
    }
    
    private void death() {
    	int lives = getLives();
    	lives --;
    	setLives(lives);
    	setPosition(Map.spawns[0].pos.toPosition().x+16, Map.spawns[0].pos.toPosition().y+16);
    	if(lives == 0){
    	MenuStart.win=false;
    	MenuStart.ingame=false;
    	}
    } 
    // Benutzt einen gegebenen Link; geht in den targetRoom, der nicht der aktuelle ist
    private void followLink(Link link) throws MapFormatException {
    	System.out.println("Call: followLink()");
    	Room targetRoom;
    	if(getRoom() == link.targetRooms[0]) {
    		targetRoom = link.targetRooms[1];
    	}
    	else {
    		targetRoom = link.targetRooms[0];
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
    		System.out.println("Fehler");
    		throw new MapFormatException("Link "+link.ID+" in Raum "+MenuStart.activeRoom.ID+" ist nicht am Rand.");
    	}
    	
    	// Wechsle Raum, falls Spieler direkt am Raumrand
    	if (followLink) {
    		System.out.println("Würd gern Raum wechseln");
    		setRoom(targetRoom);
    		MenuStart.activeRoom = targetRoom;
    		this.setPosition(link.targetFields[1].pos.toPosition().x+16, link.targetFields[1].pos.toPosition().y+16);
    	}
    		
    }
    
    public void setSpeedBuff() {
    	if (getMana() -  SpeedBuff.manaCost >= 0)
    		buff = new SpeedBuff(this, 3, 5);
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
    }
    /*
	@Override
	public Hitbox getHitbox() {
		// TODO Auto-generated method stub
		return hitbox;
	}
	*/
}

