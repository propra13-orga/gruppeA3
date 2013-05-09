package com.github.propra13.gruppeA3;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class Menu extends JFrame implements ComponentListener{
//schützen der Button und Buttonbezeichnung
	private JButton buttonstart;
	private JButton buttonbeenden;
	private JButton buttonoptionen;
	private String stringbuttonstart;
	private String stringbuttonbeenden;
	private String stringbuttonoptionen;
	
	private JPanel panelButton;
	
	private final int MINWIDTH = 400;
	private final int MINHEIGHT = 300;
	
	public Menu(){
	super("Fenster");	
	setTitle("Dungeon Crawler");
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//schließen button belegt
	setLocation(400,200);	//fensterpostion auf bildschirm festlegen
	setSize(MINWIDTH,MINHEIGHT);
	getContentPane().setLayout(new BorderLayout(5,5));	//unterteilung des fensters
	
	//Fenster überwachung auf Mindestgröße erweiterung der Classe um implements ComponentListener
	addComponentListener(this);
	
	
	//Button Texte
	stringbuttonstart = "Spiel Starten";
	stringbuttonbeenden = "Beenden";
	stringbuttonoptionen = "Optionen";
	
	//Button erzeugen
	buttonstart = new JButton(stringbuttonstart);
	buttonbeenden = new JButton(stringbuttonbeenden);
	buttonoptionen = new JButton(stringbuttonoptionen);
	
	panelButton = new JPanel(new FlowLayout());	//button anpassen und anordnen
	//Button anordnen
	panelButton.add(buttonstart);
	panelButton.add(buttonbeenden);
	panelButton.add(buttonoptionen);
	
	//listener für button
	addButtonListener(buttonstart);
	addButtonListener(buttonbeenden);
	addButtonListener(buttonoptionen);
	
	//positonierung der button auf BorderLayout
	getContentPane().add(BorderLayout.CENTER, panelButton);
	setVisible(true);	
	}

	@SuppressWarnings("unused")
	public static void main(String[] args){
	Menu m = new Menu();	//initialisierung des Fensters
	}	
	
	//Listener für Button
	private void addButtonListener(JButton b){
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae)
			{
				eingabe(ae.getActionCommand());
			}
		});
	}

	//Action für button
	private void eingabe(String a){
		if(a == stringbuttonbeenden)
		{
			System.exit(1);
		}
	}


	public void componentResized(ComponentEvent e){
		Dimension dim = this.getSize();
		dim.width = (dim.width < MINWIDTH) ? MINWIDTH: dim.width;
		dim.height = (dim.height < MINHEIGHT) ? MINHEIGHT: dim.height;
		this.setSize(dim);
	}

	@Override
	public void componentHidden(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentMoved(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void componentShown(ComponentEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
