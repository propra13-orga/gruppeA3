package com.github.propra13.gruppeA3.Entities;

import java.util.Iterator;
import java.util.List;

import com.github.propra13.gruppeA3.Menu.GameEndWindow;
import com.github.propra13.gruppeA3.Menu.MenuStart;
import com.github.propra13.gruppeA3.Link;
import com.github.propra13.gruppeA3.Map;
import com.github.propra13.gruppeA3.Trigger;

import com.github.propra13.gruppeA3.Room;

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

    // Konstruktoren
    public Player(Room room_bind) {
        super(room_bind);
        setPosition(Map.spawns[0].pos.x, Map.spawns[0].pos.y);
    }

    //Methode überschrieben, prüft für Spieler zusätzlich Trigger und ob bereits ein anderer Spieler auf dem Feld steht
    public void move() {

        System.out.println("Setze Richtung auf "+this.direct);

        switch (this.direct) {
            // TODO: Überprüfung ob Spieler aus der Map läuft nicht nötig, wenn Wände richtig gesetzt sind

            case LEFT:
              if (getRoom().roomFields[getPosition().x - 1][getPosition().y].walkable 
            		  && getRoom().roomFields[getPosition().x - 1][getPosition().y].entityType != 1) {
                    setPosition(getPosition().x - 1, getPosition().y);
              }
                break;

            case UP:
            	if (getRoom().roomFields[getPosition().x][getPosition().y - 1].walkable 
              		  && getRoom().roomFields[getPosition().x][getPosition().y - 1].entityType != 1) {
                      setPosition(getPosition().x, getPosition().y - 1);
                    }
                break;

            case RIGHT:
            	if (getRoom().roomFields[getPosition().x + 1][getPosition().y].walkable 
              		  && getRoom().roomFields[getPosition().x + 1][getPosition().y].entityType != 1) {
                      setPosition(getPosition().x + 1, getPosition().y);
                }
                break;

            case DOWN:
            	if (getRoom().roomFields[getPosition().x][getPosition().y + 1].walkable 
                		  && getRoom().roomFields[getPosition().x][getPosition().y + 1].entityType != 1) {
                        setPosition(getPosition().x, getPosition().y + 1);
                }
                break;
            default:
                //nichts tun
        }

        /**
         * Die Entitites Liste soll durchlaufen werden, um zu überprüfen, ob an der Position xy des Spielers ein Monster ist.
         */
        List<Entities> tempEntities = getRoom().entities;
        Iterator<Entities> iter = tempEntities.iterator();

        Entities testEntity;
        while (iter.hasNext()) {
            testEntity = iter.next();
            System.out.println("Checking: Entity " + testEntity.getPosition().x + ":" + testEntity.getPosition().y);
            if ((testEntity instanceof Monster) && (getPosition().equals(testEntity.getPosition()))){
               //funktioniert nicht mehr JPanel
			  this.death();
            }
        }

        if(getRoom().roomFields[getPosition().x][getPosition().y].link != null){
        	System.out.println("Hier ist ein Link!");
        	System.out.println("isActivated: "+getRoom().roomFields[getPosition().x][getPosition().y].link.isActivated());
        	
        	if (getRoom().roomFields[getPosition().x][getPosition().y].link.isActivated()) {
        		System.out.println("Ich darf durch den Link!");
        		changeRooms(getRoom().roomFields[getPosition().x][getPosition().y].link);
        	}
        }
        
        if(getRoom().roomFields[getPosition().x][getPosition().y]== Map.end)
        	this.win();
        
        Trigger trigger = getRoom().roomFields[getPosition().x][getPosition().y].trigger;
        if (trigger != null) {
        	System.out.println("Ich triggere");
        	//TODO: Testen
        	trigger.trigger();
    	}
    }

    private void win() {
    	new GameEndWindow("Gewonnen! Fuck Yeah!");
    }
    
    private void death() {
    	new GameEndWindow("Game Over!");
    }
    
    private void changeRooms(Link link){
    	if(getRoom() == link.targetRooms[0]) {
    		setRoom(link.targetRooms[1]);	//currentroom auf neuen Raum setzten
    		this.setPosition(link.targetFields[1].pos.x, link.targetFields[1].pos.y);
    		MenuStart.activeRoom = getRoom().ID;
    	}
    	else {
    		setRoom(link.targetRooms[0]);
    		this.setPosition(link.targetFields[0].pos.x, link.targetFields[0].pos.y);
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
}

