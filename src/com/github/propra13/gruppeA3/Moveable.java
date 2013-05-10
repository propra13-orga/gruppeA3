package com.github.propra13.gruppeA3;

import com.github.propra13.gruppeA3.Map;
import com.github.propra13.gruppeA3.Position;

/**
 * @author Majida Dere 
 * Diese Klasse dient als Vorlage für alle bewegbaren Objekte im Spiel.
 *
 */
public class Moveable {
	
	/**
	 * Attribute:
	 * 				direction Aufzähltyp wird für Richtungsorientierung des bewegbaren Objektes benötigt
	 * 				pos Position des bewegbaren Objektes im Raum
	 **/
	enum direction{LEFT,RIGHT,UP,DOWN,NONE}
	Position pos;
	Map map;

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
}