package com.github.propra13.gruppeA3;
/**
 * @author Majida Dere 
 *
 */
public class Moveable {
	
	enum direction{LEFT,RIGHT,UP,DOWN,NONE}
	Position pos;

	// Methode zum Bewegen von Objekten
	public void move(){
	}
	//Methoden zum Liefern bzw. Setzen der Position
	public Position getPosition(){
		return pos;
	}
	public void setPosition(int x,int y){
		pos.x=x;
		pos.y=y;
	}
	//Methoden zur Richtungsbestimmung
	public boolean leftFree(int x, int y){
		return true;
	}
	public boolean rightFree(int x, int y){
		return true;
	}
	public boolean upFree(int x, int y){
		return true;
	}
	public boolean downFree(int x, int y){
		return true;
	}
}

