package com.github.propra13.gruppeA3;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JFrame;

import com.github.propra13.gruppeA3.Exceptions.InvalidRoomLinkException;
import com.github.propra13.gruppeA3.Exceptions.MapFormatException;
import com.github.propra13.gruppeA3.Menu.MenuStart;
import com.github.propra13.gruppeA3.XMLParser.SAXCrawlerReader;

@SuppressWarnings("unused")
public class Game extends JFrame{

	private final int MINHEIGHT = 630;
	private final int MINWIDTH = 800;
	
	public Game(){
		setTitle("Dungeon Crawler");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//schlie�en button belegt
		setSize(MINWIDTH,MINHEIGHT);
		setResizable(false);
		// Zentrierung des Fensters
		// Auflösung
		final Dimension d = getToolkit().getScreenSize();
		// Zentriert
		setLocation((int)((d.getWidth() - this.getWidth()) / 2), (int)((d.getHeight() - this.getHeight()) / 2));

		//Game Start
		MenuStart MS = new MenuStart();
		add(MS);
		setVisible(true);
	}
	
	public static void main(String[] args){
		try {
			Map.initialize("Map02");
		} catch (InvalidRoomLinkException | IOException | MapFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        
		//Übergebe map an XML Parser, sodass wir immer dieselbe map benutzen.
		SAXCrawlerReader reader=new SAXCrawlerReader();
		try {
			reader.read("data/levels/level1.xml");
			
			} catch (Exception e) {
				e.printStackTrace();
			}
		
		new Game();
	}
}
