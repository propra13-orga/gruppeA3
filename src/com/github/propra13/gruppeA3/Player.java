package com.github.propra13.gruppeA3;

public class Player extends Moveable {
// Attribute
	int position; //Position auf dem Feld
	int life;//Lebensstärke?
	int power;//Figurstärke
	direction direct= direction.NONE; //Richtung
	
	public Player() {
		//Player auf Spawn setzen
		this.pos.x=map.spawn.x;
		this.pos.y=map.spawn.y;
	}
		
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
