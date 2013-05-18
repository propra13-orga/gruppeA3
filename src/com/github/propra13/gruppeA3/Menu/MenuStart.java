package com.github.propra13.gruppeA3.Menu;

import java.awt.Graphics;
import java.awt.Image;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JDialog;

import com.github.propra13.gruppeA3.Map;
import com.github.propra13.gruppeA3.Exceptions.InvalidRoomLinkException;
import com.github.propra13.gruppeA3.Exceptions.MapFormatException;
import com.github.propra13.gruppeA3.XMLParser.CrawlerSAX;

@SuppressWarnings("serial")
public class MenuStart extends JDialog {

	//definiert die Fenstergröße vom Spielfeld
	public int GameMinSize = 400; 
	public int GameMaxSize = 300;
	
	public MenuStart() {
		//Spielstart fenster aus menu.java kopiert
	setLocationRelativeTo(this);
	setTitle(CrawlerSAX.title);
	setSize(GameMinSize,GameMaxSize); //vorher wurde hier 8*32 gerechnet
	setVisible(true);
	}
	
	public void paint(Graphics g) {
		//super.paint(g);
		ImageIcon tempImage;
		// Array von Bildern aller Wandtypen
		Image[] walls = new Image[256];
		for (int x = 0; x < 5 ; x++) {
			tempImage = new ImageIcon("data/images/wall_"+x+"_32.png");
			walls[x] = tempImage.getImage();
		}
		
		try {
			Map map = new Map("beispielmap");
			
			//Iteriert Ã¼ber Zeilen
			for (int i=0; i < map.mapRooms[0].roomFields.length; i++) {
				//Iteriert Ã¼ber Spalten
				for (int j=0; j < map.mapRooms[0].roomFields[i].length; j++) {
					g.drawImage(walls[ map.mapRooms[0].roomFields[i][j].type ],i*32,j*32,this);
				}
			}
			
		} catch (MapFormatException | IOException
				| InvalidRoomLinkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}

}
