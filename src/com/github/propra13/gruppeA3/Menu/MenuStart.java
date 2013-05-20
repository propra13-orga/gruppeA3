package com.github.propra13.gruppeA3.Menu;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.io.IOException;

import javax.swing.JFrame;

import com.github.propra13.gruppeA3.Map;
import com.github.propra13.gruppeA3.Exceptions.InvalidRoomLinkException;
import com.github.propra13.gruppeA3.Exceptions.MapFormatException;
import com.github.propra13.gruppeA3.XMLParser.CrawlerSAX;

@SuppressWarnings("serial")
public class MenuStart extends JFrame {

	//definiert die Fenstergröße vom Spielfeld
	public int GameMinSizeX = 400; 
	public int GameMinSizeY = 300;

	public MenuStart() {
		
	//wie bei menu wird das JFrame gezeichnet
	setLocationRelativeTo(this);
	setTitle(CrawlerSAX.title);
	setSize(GameMinSizeX,GameMinSizeY); 
    setVisible(true);
	}
	
	public void paint(Graphics g) { //Funktion zum Zeichnen von Grafiken
		super.paint(g);
		Graphics2D g2d = (Graphics2D) g;
		//Bilder laden und ausgeben 
		Toolkit tool = Toolkit.getDefaultToolkit();

		// Array von Bildern aller Wandtypen
		Image[] walls = new Image[256];
		for (int x = 0; x < 5 ; x++) {
			walls[x] = tool.getImage("data/images/wall_"+x+"_32.png");
		}
		
		try {
			Map map = new Map("beispielmap");
			
			//Iteriert über Zeilen
			for (int i=0; i < map.mapRooms[0].roomFields.length; i++) {
				//Iteriert über Spalten
				for (int j=0; j < map.mapRooms[0].roomFields[i].length; j++) {
					int x = i*32;
					int y = j*32;
					g2d.drawImage(walls[ map.mapRooms[0].roomFields[i][j].type ],x,y,this);
				}
			}
			
		} catch (MapFormatException | IOException | InvalidRoomLinkException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}



}
