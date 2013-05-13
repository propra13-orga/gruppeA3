package com.github.propra13.gruppeA3;

import java.io.IOException;

import com.github.propra13.gruppeA3.Exceptions.InvalidRoomLinkException;
import com.github.propra13.gruppeA3.Exceptions.MapFormatException;
import com.github.propra13.gruppeA3.Menu.Menu;
import com.github.propra13.gruppeA3.XMLParser.SAXCrawlerReader;
import com.github.propra13.gruppeA3.Map;

public class Game {
	public static void main(String[] args){

		Menu meinmenue = new Menu();
		meinmenue.setVisible(true);
		Map map = null;
		
		try {
			map = new Map("beispielmap");
		} catch (InvalidRoomLinkException | IOException | MapFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		SAXCrawlerReader reader=new SAXCrawlerReader();
		try {
			reader.read("data/levels/level1.xml");
		} catch (Exception e) {

		}
		
		/* Test-Konsolenoutput
		 * @author Christian Krüger
		 */
		
		//Iteriert über Räume
		for (int k=0; k < map.mapRooms.length; k++) {
			System.out.println("Raum: " + map.mapRooms[k].ID);
			
			//Iteriert über Zeilen
			for (int i=0; i < map.mapRooms[0].roomFields.length; i++) {
				//Iteriert über Spalten
				for (int j=0; j < map.mapRooms[0].roomFields[i].length; j++) {
					System.out.printf("%c", map.mapRooms[0].roomFields[i][j].charMap());
					//Zeilenumbruch bei Zeilenende
					if(j==map.mapRooms[0].roomFields[i].length - 1)
						System.out.printf("%n");
				}
			}
			
			System.out.printf("%n%n");
			
		}
		
	}
}
