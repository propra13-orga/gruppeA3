package com.github.propra13.gruppeA3;

import java.awt.Color;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.JFrame;

import com.github.propra13.gruppeA3.Exceptions.InvalidRoomLinkException;
import com.github.propra13.gruppeA3.Exceptions.MapFormatException;
import com.github.propra13.gruppeA3.Map.Map;
import com.github.propra13.gruppeA3.Menu.MenuStart;
import com.github.propra13.gruppeA3.XMLParser.SAXCrawlerReader;

@SuppressWarnings("unused")
public class Game extends JFrame{

	private static final long serialVersionUID = 1L;
	private final int MINHEIGHT = 600;
	private final int MINWIDTH = 800;
	
	public static JFrame frame;
	
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
		
		frame = new Game();
	}
}
