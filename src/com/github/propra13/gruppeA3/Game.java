package com.github.propra13.gruppeA3;

import com.github.propra13.gruppeA3.Entities.Player;
import com.github.propra13.gruppeA3.Map;


public class Game {

	//Attribute: Mehrere globale Variablen für Schwierigkeitsgrad etc.
	public Map map;
	public Player[] players;
	public Graphics gui;
	
	
	public static void main(String[] args){
		
	}
	
	void startgame(int spieleranzahl){
		/* TODO:
		 * Map-Verzeichnisname aus Menü abfragen(später)
		 */
		map = new Map("standardmap");
		spawnplayers(spieleranzahl);
		
	}
	
	void spawnplayers(int anzahl){
		/* TODO:
		 * spawnfunktion einfügen
		 */
	}
	
}
