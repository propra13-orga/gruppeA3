package com.github.propra13.gruppeA3;

import java.io.IOException;

import com.github.propra13.gruppeA3.Entities.Player;
import com.github.propra13.gruppeA3.Exceptions.InvalidRoomLinkException;
import com.github.propra13.gruppeA3.Exceptions.MapFormatException;
import com.github.propra13.gruppeA3.Map;


public class Game {

	//Attribute: Mehrere globale Variablen f�r Schwierigkeitsgrad etc.
	public Map map;
	public Player[] players;
	public Graphics gui;
	
	
	public static void main(String[] args){
		
	}
	
	
	void startgame(int spieleranzahl)
			throws MapFormatException, IOException, InvalidRoomLinkException {
		/* TODO:
		 * Exceptions abfangen bzw. in GUI ausgeben
		 * Map-Verzeichnisname aus Men� abfragen(sp�ter)
		 */
		map = new Map("standardmap");
		spawnplayers(spieleranzahl);
		
	}
	
	void spawnplayers(int anzahl) {
		/* TODO:
		 * spawnfunktion einf�gen
		 */
	}
	
}
