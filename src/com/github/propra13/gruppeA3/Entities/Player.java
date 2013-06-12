package com.github.propra13.gruppeA3.Entities;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.github.propra13.gruppeA3.Field;
import com.github.propra13.gruppeA3.FieldPosition;
import com.github.propra13.gruppeA3.Link;
import com.github.propra13.gruppeA3.Map;
import com.github.propra13.gruppeA3.Position;
import com.github.propra13.gruppeA3.Room;
import com.github.propra13.gruppeA3.Trigger;
import com.github.propra13.gruppeA3.Menu.GameEndWindow;
import com.github.propra13.gruppeA3.Menu.MenuStart;

/**
 * @author Majida Dere
 *         Diese Klasse definiert einen Spieler und seine Eigenschaften und Methoden.
 */

public class Player extends Moveable implements KeyListener {
   // Attribute
	@SuppressWarnings("unused")
	private int health = 100;
	private int lives = 7;
	private int money = 0;

	final public static int movePx = Moveable.movePx;
	final public static Hitbox hitbox = new Hitbox(32, 32);

    // Konstruktoren
    public Player(Room room_bind) {
        super(room_bind);
        //+16: damit Player in der Mitte des Felds landet
        setPosition(Map.spawns[0].pos.toPosition().x+16, Map.spawns[0].pos.toPosition().y+16);
    }

    //Methode überschrieben, prüft für Spieler zusätzlich Trigger und ob bereits ein anderer Spieler auf dem Feld steht
    public void move() {
    	System.out.println("Setze Richtung auf "+this.getDirection());

    	int step = (int)(movePx * getSpeed());
    	Position nextPos = new Position(0,0); //Position, auf die gelaufen werden soll
    	Field[] fieldsToWalk = new Field[2];  // Felder, die betreten werden sollen
    	Position fieldPos = new Position(0,0);
    	boolean collision;

        switch (this.getDirection()) {
            case LEFT:
            	if(getFieldPos().x > 0) {
            		nextPos.setPosition(getPosition().x - step, getPosition().y);
            		
            		// Kollision mit Wänden und setPosition
            		Position p = new Position(nextPos.getCornerTopLeft(hitbox).x, nextPos.getCornerTopLeft(hitbox).y +1);
            		fieldsToWalk[0] = Map.mapRooms[MenuStart.activeRoom].getField(p); //+1: damit Spieler durch Gänge passt
            		
            		p.setPosition(nextPos.getCornerBottomLeft(hitbox).x, nextPos.getCornerBottomLeft(hitbox).y -1);
            		fieldsToWalk[1] = Map.mapRooms[MenuStart.activeRoom].getField(p);
            		
            		// Falls fieldsToWalk[0] und [1] begehbar, beweg dich einfach
            		if (fieldsToWalk[0].walkable && fieldsToWalk[1].walkable) {
            			setPosition(nextPos);
            			break;
            		// Ansonsten liegt Kollision vor, daher Annäherung an Feldgrenze
            		} else {
            			int distance = getPosition().getCornerTopLeft(hitbox).x - (fieldsToWalk[0].pos.toPosition().x + 32);
	        			setPosition(getPosition().x - distance, nextPos.y);
            		}
        		}
                break;

            case UP:
            	if(getFieldPos().y > 0) {
            		nextPos.setPosition(getPosition().x, getPosition().y - step);
            		
            		// Kollision mit Wänden und setPosition
            		Position p = new Position(nextPos.getCornerTopLeft(hitbox).x +1, nextPos.getCornerTopLeft(hitbox).y);
            		fieldsToWalk[0] = Map.mapRooms[MenuStart.activeRoom].getField(p); //+1: damit Spieler durch Gänge passt
            		
            		p.setPosition(nextPos.getCornerTopRight(hitbox).x -1, nextPos.getCornerTopRight(hitbox).y);
            		fieldsToWalk[1] = Map.mapRooms[MenuStart.activeRoom].getField(p);

            		// Falls fieldsToWalk[0] und [1] begehbar, beweg dich einfach
            		if (fieldsToWalk[0].walkable && fieldsToWalk[1].walkable) {
            			setPosition(nextPos);
            			break;
            		// Ansonsten liegt Kollision vor, daher Annäherung an Feldgrenze
            		} else {
            			int distance = getPosition().getCornerTopLeft(hitbox).y - (fieldsToWalk[0].pos.toPosition().y + 32);
	        			setPosition(getPosition().x, getPosition().y - distance);
	        		}
            	}
                break;

            case RIGHT:
            	if(getFieldPos().x < getRoom().roomFields.length - 1) {
            		nextPos.setPosition(getPosition().x + step, getPosition().y);
            		
            		// Kollision mit Wänden und setPosition
            		Position p = new Position(nextPos.getCornerTopRight(hitbox).x, nextPos.getCornerTopRight(hitbox).y +1);
            		fieldsToWalk[0] = Map.mapRooms[MenuStart.activeRoom].getField(p); //+1: damit Spieler durch Gänge passt
            		
            		p.setPosition(nextPos.getCornerBottomRight(hitbox).x, nextPos.getCornerBottomRight(hitbox).y -1);
            		fieldsToWalk[1] = Map.mapRooms[MenuStart.activeRoom].getField(p);
            		
            		// Falls fieldsToWalk[0] und [1] begehbar, beweg dich einfach
            		if (fieldsToWalk[0].walkable && fieldsToWalk[1].walkable) {
            			setPosition(nextPos);
            			break;
            		// Ansonsten liegt Kollision vor, daher Annäherung an Feldgrenze
            		} else {
	        			int distance = fieldsToWalk[0].pos.toPosition().x - getPosition().getCornerTopRight(hitbox).x;
	        			setPosition(getPosition().x + distance, nextPos.y);
	        		}
            	}
                break;

            case DOWN:
            	if(getFieldPos().y < getRoom().roomFields[0].length - 1) {
            		nextPos.setPosition(getPosition().x, getPosition().y + step);
            		
            		// Kollision mit Wänden und setPosition
            		Position p = new Position(nextPos.getCornerBottomLeft(hitbox).x +1, nextPos.getCornerBottomLeft(hitbox).y);
            		fieldsToWalk[0] = Map.mapRooms[MenuStart.activeRoom].getField(p); //+1: damit Spieler durch Gänge passt
            		
            		p.setPosition(nextPos.getCornerBottomRight(hitbox).x -1, nextPos.getCornerBottomRight(hitbox).y);
            		fieldsToWalk[1] = Map.mapRooms[MenuStart.activeRoom].getField(p);
            		
            		// Falls fieldsToWalk[0] und [1] begehbar, beweg dich einfach
            		if (fieldsToWalk[0].walkable && fieldsToWalk[1].walkable) {
            			setPosition(nextPos);
            			break;
            		// Ansonsten liegt Kollision vor, daher Annäherung an Feldgrenze
            		} else {
	        			int distance = fieldsToWalk[0].pos.toPosition().y - getPosition().getCornerBottomLeft(hitbox).y;
	        			setPosition(getPosition().x, getPosition().y + distance);
	        		}
            	}	
                break;
            default:
                //nichts tun
        }
        System.out.println("Spielerpos: "+getPosition().x+":"+getPosition().y);

        /**
         * Die Entitites Liste soll durchlaufen werden, um zu überprüfen, ob an der Position xy des Spielers ein Monster ist.
         * TODO: Pixelkoordinatensystemkollisionsabfrage
         */
        LinkedList<Entities> tempEntities = getRoom().entities;
        Iterator<Entities> iter = tempEntities.iterator();

        Entities testEntity;
        Monster monster = null;
        Item item = null;
        while (iter.hasNext()) {
            testEntity = iter.next();
            if (testEntity instanceof Monster)
            	if ((getFieldPos().x == testEntity.getPosition().x) && (getFieldPos().y == testEntity.getPosition().y)){
            		//funktioniert nicht mehr JPanel
            		monster = (Monster)testEntity;
            		System.out.println("Monster: "+monster.getPosition().x+", "+monster.getPosition().y);
            		System.out.println("Monster: "+getPosition().x+", "+getPosition().y);
            		this.death();
            	}
        }

        // Links
        if(getRoom().roomFields[getFieldPos().x][getFieldPos().y].link != null){
    	
        	if (getRoom().roomFields[getFieldPos().x][getFieldPos().y].link.isActivated()) {
        		followLink(getRoom().roomFields[getFieldPos().x][getFieldPos().y].link);
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
    }
    
    private void death() {
    	MenuStart.win=false;
    	MenuStart.ingame=false;
    }
    
    // Benutzt einen gegebenen Link; geht in den targetRoom, der nicht der aktuelle ist
    private void followLink(Link link){
    	if(getRoom() == link.targetRooms[0]) {
    		setRoom(link.targetRooms[1]);	//currentroom auf neuen Raum setzten
    		this.setPosition(link.targetFields[1].pos.toPosition().x+16, link.targetFields[1].pos.toPosition().y+16);
    		MenuStart.activeRoom = getRoom().ID;
    	}
    	else {
    		setRoom(link.targetRooms[0]);
    		this.setPosition(link.targetFields[0].pos.toPosition().x+16, link.targetFields[0].pos.toPosition().y+16);
    		MenuStart.activeRoom = getRoom().ID;
    	}
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
    
    // KeyListening findet nun hier statt
	
	public void keyTyped(KeyEvent e){
	}
	
	public void keyPressed(KeyEvent e){
        System.out.println("Key pressed: "+e.getKeyCode());

		int pressed = e.getKeyCode();
		switch(pressed) {
			case 37: this.setDirection(direction.LEFT);
					 break;
			case 38: this.setDirection(direction.UP);
					 break;
			case 39: this.setDirection(direction.RIGHT);
					 break;
			case 40: this.setDirection(direction.DOWN);
					 break;		 
		}

	}
	
	public void keyReleased(KeyEvent e){
		this.setDirection(direction.NONE);
    }
}

