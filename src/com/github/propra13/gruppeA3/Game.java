package com.github.propra13.gruppeA3;

import java.io.IOException;
import com.github.propra13.gruppeA3.Exceptions.MapFormatException;
import com.github.propra13.gruppeA3.Menu.Menu;
import com.github.propra13.gruppeA3.Room;
import com.github.propra13.gruppeA3.XMLParser.SAXCrawlerReader;

public class Game {
	public static void main(String[] args){
		
		Menu meinmenue = new Menu();
		meinmenue.setVisible(true);
		
		/** Room room = null;
		try {
			room = new Room(0,"beispielmap/00.room");
		} catch (IOException | MapFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} **/
		
		SAXCrawlerReader reader=new SAXCrawlerReader();
		try {
			reader.read("data/levels/level1.xml");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
}
