package com.github.propra13.gruppeA3.Entities;

import com.github.propra13.gruppeA3.Room;
import com.github.propra13.gruppeA3.Position;

/**
 * @author Majida Dere 
 * Diese Klasse dient als Vorlage für alle bewegbaren Objekte im Spiel.
 *
 */
public class Moveable extends Entities {
	
	/**
	 * Attribute:
	 * 		direction Aufzähltyp wird für Richtungsorientierung des bewegbaren Objektes benötigt
	 * 		pos Position des bewegbaren Objektes im Raum
	 * 		life Lebensstärke eines bewegbaren Objektes
	 * 		power Figurstärke
	 * 		speed Geschwindigkeit
	 **/
	public enum direction{LEFT,RIGHT,UP,DOWN,NONE}
	public direction direct= direction.NONE; //Richtung
	Position pos;
	Room currentroom;
	int life;
	int power;
	double speed; 

	/** 
	 * Diese Methode bewegt ein bewegbares Objekt im Raum
	 * Begehbarkeit des Feldes pr�fen
	 * Wenn begehbar, setposition anwenden
	 **/
	public void move(){
		switch(this.direct){
		case LEFT: 
			if(currentroom.roomFields[this.pos.x-1][this.pos.y].walkable == true){
				setPosition(this.pos.x-1,this.pos.y);
			}
					break;
					
		case UP:
			if(currentroom.roomFields[this.pos.x][this.pos.y+1].walkable == true){
				setPosition(this.pos.x,this.pos.y+1);
			}
					break;
					
		case RIGHT:
			if(currentroom.roomFields[this.pos.x+1][this.pos.y].walkable == true){
				setPosition(this.pos.x+1,this.pos.y);
			}
					break;
					
		case DOWN:
			if(currentroom.roomFields[this.pos.x][this.pos.y+1].walkable == true){
				setPosition(this.pos.x,this.pos.y+1);
			}
					break;
					
		default: ;	//nichts tun
		}
				
	}
	
	/**
	 * Diese Methode liefert die aktuelle Position im Raum
	 * @return liefert die Position im Raum
	 */
	public Position getPosition(){
		return pos;
	}
	
	/**
	 * Diese Methode liefert den aktuellen Life Status eines bewegbaren Objektes
	 * @return liefert ein int Leben
	 */
	public int getLife(){	
		return 0;
	}

	/**
	 * Diese Methode ändert die aktuelle Position im Raum
	 * @param x X-Achse
	 * @param y Y-Achse
	 */
	public void setPosition(int x,int y){
//		map[x][y] = player;
//		map[pos.x][pos.y] = NONE; 
		pos.x=x;
		pos.y=y;
	}
	
	/**
	 * Diese Methode setzt den aktuellen Life Status eines bewegbaren Objektes
	 * @param life
	 */
	public void setLife(int life){
	}

	

	@Override
	void setPosition(Position pos) {
		setPosition(pos.x,pos.y);
	}
}