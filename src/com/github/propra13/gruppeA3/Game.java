package com.github.propra13.gruppeA3;

import java.io.IOException;

import com.github.propra13.gruppeA3.Exceptions.InvalidRoomLinkException;
import com.github.propra13.gruppeA3.Exceptions.MapFormatException;
import com.github.propra13.gruppeA3.Menu.Menu;
import com.github.propra13.gruppeA3.XMLParser.SAXCrawlerReader;
import com.github.propra13.gruppeA3.Map;

public class Game {
	public static Menu meinmenue;
	
	public static void main(String[] args){

		Map map = null;
		
		try {
			map = new Map("beispielmap");
		} catch (InvalidRoomLinkException | IOException | MapFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//Übergebe map an XML Parser, sodass wir immer dieselbe map benutzen.
		SAXCrawlerReader reader=new SAXCrawlerReader(map);
		try {
			reader.read("data/levels/level1.xml");
			
		} catch (Exception e) {
		}
		
		/* Test-Konsolenoutput
		 * @author Christian Krüger
		 */
		
		//Iteriert über Räume
		for (int k=0; k < Map.mapRooms.length; k++) {
			System.out.println("Raum: " + Map.mapRooms[k].ID);
			
			//Iteriert über Zeilen TODO: ans laufen kriegen

			for (int i=0; i < Map.mapRooms[k].roomFields.length; i++) {
				//Iteriert über Spalten
				for (int j=0; j < Map.mapRooms[k].roomFields[i].length; j++) {
					System.out.printf("%c", Map.mapRooms[k].roomFields[i][j].charMap());
					//Zeilenumbruch bei Zeilenende
					if(j==Map.mapRooms[k].roomFields[i].length - 1)
						System.out.printf("%n");
				}
			}
			
			System.out.printf("%n%n");
			
		}
		
		//map muss an Menu weitergereicht werden, damit die map nicht nochmal ausgelesen wird.
		meinmenue = new Menu(map);
		meinmenue.setVisible(true);
		
	}
}
