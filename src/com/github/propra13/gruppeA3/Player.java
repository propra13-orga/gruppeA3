package com.github.propra13.gruppeA3;

public class Player extends Moveable {
// Attribute
	int position; //Position auf dem Feld
	int life;//Lebensstärke?
	int power;//Figurstärke
	direction direct= direction.NONE; //Richtung
		
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
		
		//Charakter-Spawn finden und Charakterposition auf Spawnkoordinaten setzen
		void spawn(Field[][] map){
			for(int i = 0; i+1 < map[0].length; i++){
				for(int j = 0; j+1 < map.length; j++){ //Spielfeld durchlaufen
					if(map[j][i].attribute == 5){ //wenn Spawn gefunden, position setzen
						this.pos.x=j;
						this.pos.y=i;
						j = map.length;			//schleifen beenden
						i = map[0].length;
					}
				}
			}
		}
		
}
