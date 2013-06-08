package com.github.propra13.gruppeA3.Entities;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;
import java.util.LinkedList;
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

public class Player extends Moveable implements KeyListener {
   // Attribute
	@SuppressWarnings("unused")
	private int health = 100;
	private int lives = 7;
	private int money = 0;
	
	private LinkedList<Item> items = null;
	private int mana;
	
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

        System.out.println("Setze Richtung auf "+this.direct);

        switch (this.direct) {
            // TODO: Überprüfung ob Spieler aus der Map läuft nicht nötig, wenn Wände richtig gesetzt sind

            case LEFT:
              if (getRoom().roomFields[getFieldPos().x - 1][getFieldPos().y].walkable 
            		  && getRoom().roomFields[getFieldPos().x - 1][getFieldPos().y].entityType != 1) {
                    setPosition(getPosition().x - (int)(movePx*speed), getPosition().y);
              }
                break;

            case UP:
            	if (getRoom().roomFields[getFieldPos().x][getFieldPos().y - 1].walkable 
              		  && getRoom().roomFields[getFieldPos().x][getFieldPos().y - 1].entityType != 1) {
                      setPosition(getPosition().x, getPosition().y - (int)(movePx*speed));
                    }
                break;

            case RIGHT:
            	if (getRoom().roomFields[getFieldPos().x + 1][getFieldPos().y].walkable 
              		  && getRoom().roomFields[getFieldPos().x + 1][getFieldPos().y].entityType != 1) {
                      setPosition(getPosition().x + (int)(movePx*speed), getPosition().y);
                }
                break;

            case DOWN:
            	if (getRoom().roomFields[getFieldPos().x][getFieldPos().y + 1].walkable 
                		  && getRoom().roomFields[getFieldPos().x][getFieldPos().y + 1].entityType != 1) {
                        setPosition(getPosition().x, getPosition().y + (int)(movePx*speed));
                }
                break;
            default:
                //nichts tun
        }

        /**
         * Die Entitites Liste soll durchlaufen werden, um zu überprüfen, ob an der Position xy des Spielers ein Monster ist.
         * TODO: Pixelkoordinatensystemkollisionsabfrage
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

        // Links
        if(getRoom().roomFields[getFieldPos().x][getFieldPos().y].link != null){
        	System.out.println("Hier ist ein Link!");
        	System.out.println("isActivated: "+getRoom().roomFields[getFieldPos().x][getFieldPos().y].link.isActivated());
        	
        	if (getRoom().roomFields[getFieldPos().x][getFieldPos().y].link.isActivated()) {
        		System.out.println("Ich darf durch den Link!");
        		followLink(getRoom().roomFields[getPosition().x][getPosition().y].link);
        	}
        }
        
        // Win
        if(getRoom().roomFields[getPosition().toFieldPos().x][getPosition().toFieldPos().y]== Map.end)
        	this.win();
        
        // Trigger
        Trigger trigger = getRoom().roomFields[getPosition().toFieldPos().x][getPosition().toFieldPos().y].trigger;
        if (trigger != null) {
        	System.out.println("Ich triggere");
        	trigger.trigger();
    	}
    }
   
    private void win() {
    	new GameEndWindow("Gewonnen! Fuck Yeah!");
    }
    
    private void death() {
    	new GameEndWindow("Game Over!");
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
	
	public void keyTyped(KeyEvent e){}
	
	public void keyPressed(KeyEvent e){
        System.out.println("Key pressed: "+e.getKeyCode());

		int pressed = e.getKeyCode();
		switch(pressed) {
			case 37: this.direct = direction.LEFT;
					 break;
			case 38: this.direct = direction.UP;
					 break;
			case 39: this.direct = direction.RIGHT;
					 break;
			case 40: this.direct = direction.DOWN;
					 break;		 
		}

	}
	
	public void keyReleased(KeyEvent e){
		this.direct = direction.NONE;
    }
}

