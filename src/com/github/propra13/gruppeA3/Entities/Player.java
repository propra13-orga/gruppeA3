package com.github.propra13.gruppeA3.Entities;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.github.propra13.gruppeA3.Menu.GameEndWindow;
import com.github.propra13.gruppeA3.Menu.Menu;
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
              if (currentroom.roomFields[pos.x - 1][pos.y].walkable 
            		  && currentroom.roomFields[pos.x - 1][pos.y].entityType != 1) {
                    setPosition(this.pos.x - 1, this.pos.y);
              }
                break;

            case UP:
               if (currentroom.roomFields[pos.x][pos.y - 1].walkable
                       && currentroom.roomFields[pos.x][pos.y - 1].entityType != 1) {
                    setPosition(this.pos.x, this.pos.y - 1);
                    }
                break;

            case RIGHT:
                if (currentroom.roomFields[this.pos.x + 1][this.pos.y].walkable
                        && currentroom.roomFields[pos.x + 1][pos.y].entityType != 1) {
                    setPosition(this.pos.x + 1, this.pos.y);
                }
                break;

            case DOWN:
                if (currentroom.roomFields[this.pos.x][this.pos.y + 1].walkable
                        && currentroom.roomFields[pos.x][pos.y + 1].entityType != 1) {
                    setPosition(this.pos.x, this.pos.y + 1);
                }
                break;
            default:
                //nichts tun
        }

        /**
         * Die Entitites Liste soll durchlaufen werden, um zu überprüfen, ob an der Position xy des Spielers ein Monster ist.
         */
        List<Entities> tempEntities = this.currentroom.entities;
        Iterator<Entities> iter = tempEntities.iterator();

        Entities testEntity;
        while (iter.hasNext()) {
            testEntity = iter.next();
            System.out.println("Checking: Entity " + testEntity.getPosition().x + ":" + testEntity.getPosition().y);
            if ((testEntity instanceof Monster) && (this.pos.equals(testEntity.getPosition()))){
               //funktioniert nicht mehr JPanel
			  this.death();
            }
        }

        if(currentroom.roomFields[pos.x][pos.y].link != null){
        	System.out.println("Hier ist ein Link!");
        	System.out.println("isActivated: "+currentroom.roomFields[pos.x][pos.y].link.isActivated());
        	
        	if (currentroom.roomFields[pos.x][pos.y].link.isActivated()) {
        		System.out.println("Ich darf durch den Link!");
        		changeRooms(currentroom.roomFields[pos.x][pos.y].link);
        	}
        }
        
        if(currentroom.roomFields[pos.x][pos.y]== Map.end)
        	this.win();
        
        Trigger trigger = currentroom.roomFields[this.pos.x][this.pos.y].trigger;
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
    	if(this.currentroom == link.targetRooms[0]) {
    		this.currentroom = link.targetRooms[1];	//currentroom auf neuen Raum setzten
    		this.setPosition(link.targetFields[1].pos.x, link.targetFields[1].pos.y);
    		MenuStart.activeRoom = currentroom.ID;
    	}
    	else {
    		this.currentroom = link.targetRooms[0];
    		this.setPosition(link.targetFields[0].pos.x, link.targetFields[0].pos.y);
    		MenuStart.activeRoom = currentroom.ID;
    	}
    }
    
    public int getHealth() {
    	return health;
    }
    
    public void setHealth(int health){
    	this.health = health;
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

