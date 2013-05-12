package com.github.propra13.gruppeA3;

import java.io.IOException;

import com.github.propra13.gruppeA3.Exceptions.InvalidRoomLinkException;
import com.github.propra13.gruppeA3.Exceptions.MapFormatException;
import com.github.propra13.gruppeA3.Menu.Menu;
import com.github.propra13.gruppeA3.XMLParser.SAXCrawlerReader;
import com.github.propra13.gruppeA3.Room;

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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}