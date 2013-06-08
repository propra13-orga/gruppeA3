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
	
		//map muss an Menu weitergereicht werden, damit die map nicht nochmal ausgelesen wird.
		meinmenue = new Menu();
		meinmenue.setVisible(true);
		
	}
}
