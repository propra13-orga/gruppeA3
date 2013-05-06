package com.github.propra13.gruppeA3.Entities;

import com.github.propra13.gruppeA3.Map;
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
	Position pos;
	Map map;
	int life;
	int power;
	double speed; 

	/** 
	 * Diese Methode bewegt ein bewegbares Objekt im Raum
	 **/
	public void move(){
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

	/**
	 * Diese Methode überprüft ob die linke Position begehbar ist
	 * @param x X-Achse
	 * @param y Y-AChse
	 * @return true, wenn begehbar, sonst false
	 */
	public boolean leftFree(int x, int y){
		return true;
	}
	
	/**
	 * Diese Methode überprüft ob die rechte Position begehbar ist
	 * @param x X-Achse
	 * @param y Y-AChse
	 * @return true, wenn begehbar, sonst false
	 */
	public boolean rightFree(int x, int y){
		return true;
	}
	
	/**
	 * Diese Methode überprüft ob die obere Position begehbar ist
	 * @param x X-Achse
	 * @param y Y-AChse
	 * @return true, wenn begehbar, sonst false
	 */
	public boolean upFree(int x, int y){
		return true;
	}
	
	/**
	 * Diese Methode überprüft ob die untere Position begehbar ist
	 * @param x X-Achse
	 * @param y Y-AChse
	 * @return true, wenn begehbar, sonst false
	 */
	public boolean downFree(int x, int y){
		return true;
	}

	@Override
	void setPosition(Position pos) {
		setPosition(pos.x,pos.y);
	}
}