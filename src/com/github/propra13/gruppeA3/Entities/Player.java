package com.github.propra13.gruppeA3.Entities;


/**
 *  
 * @author Majida Dere
 * Diese Klasse definiert einen Spieler und seine Eigenschaften und Methoden.
 * 
 */

public class Player extends Moveable {
// Attribute
	
	public direction direct= direction.NONE; //Richtung
	
	// Methode zum Bewegen von Objekten
		public void move(){
			switch(direct){
				case LEFT: if(leftFree(pos.x-1,pos.y)){
					setPosition(pos.x-1,pos.y);
					//feldNeuZeichnen();
					//zusätzliche Monsteraktionen/Zufallserscheininungen von Gegenständen
				}break;
				case RIGHT: if(rightFree(pos.x+1,pos.y)){
					setPosition(pos.x+1,pos.y);
					//feldNeuZeichnen();
					//zusätzliche Monsteraktionen/Zufallserscheininungen von Gegenständen
				}break;
				case UP: if (upFree(pos.x,pos.y+1)){
					setPosition(pos.x,pos.y+1);
					//feldNeuZeichnen();
					//zusätzliche Monsteraktionen/Zufallserscheininungen von Gegenständen
				}break;
				case DOWN:if (downFree(pos.x,pos.y-1)){
					setPosition(pos.x,pos.y+1);
					//feldNeuZeichnen();
					//zusätzliche Monsteraktionen/Zufallserscheininungen von Gegenständen
				}break;
				case NONE: //zusätzliche Monsteraktionen/Zufallserscheininungen von Gegenständen
					
			}
		}	
		
		
}