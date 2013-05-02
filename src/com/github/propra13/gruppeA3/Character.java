package com.github.propra13.gruppeA3;

public class Character {
	
	int x;
	int y;
	
	//Konstruktor (still to do)
	//Character(){
	//}
	
	//Charakter-Spawn finden und Charakterposition auf Spawnkoordinaten setzen
	void spawn(Field[][] map){
		for(int i = 0; i+1 < map[0].length; i++){
			for(int j = 0; j+1 < map.length; j++){
				if(map[j][i].attribute == 5){
					this.x=j;
					this.y=i;
					j = map.length;
					i = map[0].length;
				}
			}
		}
	}
	
	void setposition(Field[][] map,int newx, int newy){
		map[this.x][this.y].item = 0; //charakter von altem Feld entfernen
		this.x = newx;
		this.y = newy;
		map[this.x][this.y].item = 1; //charakter auf neues Feld setzen
	}
	
}
