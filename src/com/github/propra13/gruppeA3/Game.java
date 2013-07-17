package com.github.propra13.gruppeA3;

/**
 * Main-Klasse des Spiels
 *Initalisiert das JFrame und setzt die Parameter f�rs Fenster fest
 */

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JFrame;

import com.github.propra13.gruppeA3.Exceptions.InvalidRoomLinkException;
import com.github.propra13.gruppeA3.Exceptions.MapFormatException;
import com.github.propra13.gruppeA3.Map.Map;
import com.github.propra13.gruppeA3.Map.MapHeader;
import com.github.propra13.gruppeA3.Menu.MenuStart;
import com.github.propra13.gruppeA3.XMLParser.SAXCrawlerReader;

@SuppressWarnings("unused")
public class Game extends JFrame{

	private static final long serialVersionUID = 1L;
	/**Höhe des Fensters*/
	public final static int MINHEIGHT = 600;
	/**Breite des Fensters*/
	public final static int MINWIDTH = 800;
	
	/**Statische Referenz auf das Vater-JFrame*/
	public static JFrame frame;
	/**Statische Referenz auf das MenuStart-Objekt*/
	public static MenuStart Menu;
	
	/**Die Header von allen verf�gbaren Maps*/
	public static LinkedList<MapHeader> mapHeaders = new LinkedList<MapHeader>();
	/**Die Header von allen verf�gbaren Kampagnenmaps*/
	public static LinkedList<MapHeader> storyHeaders = new LinkedList<MapHeader>();
	
	/**
	 * Startet das Fenster
	 */
	public Game() {
		setTitle("Dungeon Crawler");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//schließen button belegt
		setSize(MINWIDTH,MINHEIGHT);
		setResizable(false);
		
		// Zentrierung des Fensters
		// Auflösung
		final Dimension d = getToolkit().getScreenSize();
		// Zentriert
		setLocation((int)((d.getWidth() - this.getWidth()) / 2), (int)((d.getHeight() - this.getHeight()) / 2));

		//Game Start
		Map.updateMapList();
		Menu = new MenuStart();
		add(Menu);
		setVisible(true);
	}
	
	public static void main(String[] args){
		
		frame = new Game();
	}
}
