package com.github.propra13.gruppeA3;

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
	public final static int MINHEIGHT = 600;
	public final static int MINWIDTH = 800;
	
	public static JFrame frame;
	public static MenuStart Menu;
	
	public static LinkedList<MapHeader> mapHeaders = new LinkedList<MapHeader>();
	
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
