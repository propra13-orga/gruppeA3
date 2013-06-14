package com.github.propra13.gruppeA3.Entities;

import java.util.Iterator;
import java.util.LinkedList;

import com.github.propra13.gruppeA3.Map.Map;
import com.github.propra13.gruppeA3.Map.Position;
import com.github.propra13.gruppeA3.Map.Room;
import com.github.propra13.gruppeA3.Map.Trigger;

/**
 * @author Majida Dere
 * Die Klasse Monster liefert hauptsächlich die zugehörigen getter und setter Methoden zur Entity Monster
 */

public class Monster extends Moveable {

	// Attribute
	private String desc;
	
	private Hitbox hitbox;
	
	//Konstruktor
	
	public Monster (Room room_bind, double speed, int power, int type, int life, int x, int y, String desc){
		super(room_bind);
		//System.out.println("Ich bin ein Monster!"); //Offenbar nicht erreichbar
		setSpeed(speed);
		setPower(power);
		setHealth(life);
		this.desc = desc;
		this.hitbox = new Hitbox();
		setPosition(x+(hitbox.width/2),y+(hitbox.height/2));
	}
	
	
	// Getter Methoden
	
	public Hitbox getHitbox() {
		return this.hitbox;
	}
	
    //Methode überschrieben, prüft für Spieler zusätzlich Trigger und ob bereits ein anderer Spieler auf dem Feld steht
    public void move() {

        System.out.println("Setze Richtung auf "+this.getDirection());

        switch (this.getDirection()) {
            // TODO: Überprüfung ob Spieler aus der Map läuft nicht nötig, wenn Wände richtig gesetzt sind

            case LEFT:
            	if(getFieldPos().x > 0)
            		if (getRoom().roomFields[getFieldPos().x - 1][getFieldPos().y].walkable 
            		 && getRoom().roomFields[getFieldPos().x - 1][getFieldPos().y].entityType != 1) {
            			setPosition(getPosition().x - 32, getPosition().y);
              }
                break;

            case UP:
            	if(getFieldPos().y > 0)
            		if (getRoom().roomFields[getFieldPos().x][getFieldPos().y - 1].walkable 
              		 && getRoom().roomFields[getFieldPos().x][getFieldPos().y - 1].entityType != 1) {
            			setPosition(getPosition().x, getPosition().y - 32);
            		}
                break;

            case RIGHT:
            	if(getFieldPos().x < getRoom().roomFields.length - 1)
            		if (getRoom().roomFields[getFieldPos().x + 1][getFieldPos().y].walkable 
              		 && getRoom().roomFields[getFieldPos().x + 1][getFieldPos().y].entityType != 1) {
            			setPosition(getPosition().x + 32, getPosition().y);
            		}
                break;

            case DOWN:
            	if(getFieldPos().y < getRoom().roomFields[0].length - 1)
            		if (getRoom().roomFields[getFieldPos().x][getFieldPos().y + 1].walkable 
            		 && getRoom().roomFields[getFieldPos().x][getFieldPos().y + 1].entityType != 1) {
                        setPosition(getPosition().x, getPosition().y + 32);
            		}
                break;
            default:
                //nichts tun
        }

        /**
         * Die Entitites Liste soll durchlaufen werden, um zu überprüfen, ob an der Position xy des Spielers ein Monster ist.
         * TODO: Pixelkoordinatensystemkollisionsabfrage
         */

        // Links
/*        if(getRoom().roomFields[getFieldPos().x][getFieldPos().y].link != null){
    	
        	if (getRoom().roomFields[getFieldPos().x][getFieldPos().y].link.isActivated()) {
        		followLink(getRoom().roomFields[getFieldPos().x][getFieldPos().y].link);
        	}
        }
*/        
        // Trigger
        Trigger trigger = getRoom().roomFields[getPosition().toFieldPos().x][getPosition().toFieldPos().y].trigger;
        if (trigger != null) {
        	trigger.trigger();
    	}
    }
}
