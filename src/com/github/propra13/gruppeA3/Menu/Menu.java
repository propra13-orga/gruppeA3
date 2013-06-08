package com.github.propra13.gruppeA3.Menu;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import com.github.propra13.gruppeA3.Keys;


public class Menu extends JFrame implements ComponentListener{
//Initialisieren der Button und Buttonbezeichnung
	
	/* workaround: static-Ref auf das aktuelle MenuStart-Objekt,
	 *             damits von überall abgeschossen werden kann
	 */
	private static MenuStart activeGameWindow;
	
	private JButton buttonstart;
	private JButton buttonbeenden;
	private JButton buttonoptionen;
	private String stringbuttonstart;
	private String stringbuttonbeenden;
	private String stringbuttonoptionen;
	
	private JPanel panelButton;		// Button einem JPanel zuweisen
	
	private final int MINWIDTH = 800;
	private final int MINHEIGHT = 600;
	
	public Menu(){	//JFrame zeichnen
		addKeyListener(new Keys(null));

		setTitle("Dungeon Crawler");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	//schlie�en button belegt
	//auslesen der Auflösung
		final Dimension d = this.getToolkit().getScreenSize(); 
	//Umrechnen der Auflösung und schreiben der Werte als integer
		setLocation((int) ((d.getWidth() - this.getWidth()) / 2), (int) ((d.getHeight() - this.getHeight()) / 2));
		/* @author CK
		 * TODO: 
		 * if(d.getHeight == 600 || d.getWidth == 800)
		 * 		Vollbild
		 * else if(d.getHeight < 600 || d.getWidth < 800)
		 * 		Nicht mehr unterstützte Auflösung, irgendwas halbwegs sinnvolles tun
		 * 
		 * Damit es nicht zu hässlichen Konflikten mit Taskleisten oder anderem Kram kommt.
		 * Benötigt zwangsläufig auch ESC-Knopfreaktion, damit man auch aus dem Spiel wieder raus kommt.
		 */
	    		
		setSize(MINWIDTH,MINHEIGHT);
		setResizable(false);
		//wichtig für die Fenster Zentrierung soll nach setSize kommen.
		setLocationRelativeTo(null); 
		getContentPane().setLayout(new GridBagLayout());	//unterteilung des fensters
	
		//ComponentListener zuständig für Überwachung der Mindestfenstergröße
		addComponentListener(this);
	
		//Button Texte
		stringbuttonstart = "Spiel Starten";
		stringbuttonbeenden = "Beenden";
		stringbuttonoptionen = "Optionen";
	
		//Button erzeugen
		buttonstart = new JButton(stringbuttonstart);
		buttonbeenden = new JButton(stringbuttonbeenden);
		buttonoptionen = new JButton(stringbuttonoptionen);
	
		panelButton = new JPanel(new GridBagLayout());	//Unterteilung des Fensters in Zeilen und Blöcke
	
		//Button anordnen
		GridBagConstraints gbc_buttonstart = new GridBagConstraints();
		gbc_buttonstart.insets = new Insets(0, 0, 5, 5);
		gbc_buttonstart.gridx = 1;
		gbc_buttonstart.gridy = 0;
		gbc_buttonstart.gridwidth = 1;
		panelButton.add(buttonstart, gbc_buttonstart);
		GridBagConstraints gbc_buttonbeenden = new GridBagConstraints();
		gbc_buttonbeenden.insets = new Insets(0, 0, 5, 5);
		gbc_buttonbeenden.gridx = 3;
		gbc_buttonbeenden.gridy = 0;
		gbc_buttonbeenden.gridwidth = 2;
		panelButton.add(buttonbeenden, gbc_buttonbeenden);
		GridBagConstraints gbc_buttonoptionen = new GridBagConstraints();
		gbc_buttonoptionen.insets = new Insets(0, 0, 5, 5);
		gbc_buttonoptionen.gridx = 1;
		gbc_buttonoptionen.gridy = 1;
		gbc_buttonoptionen.gridwidth = 4;
		panelButton.add(buttonoptionen, gbc_buttonoptionen);
	
		//listener fuer Button
		addButtonListener(buttonstart);
		addButtonListener(buttonbeenden);
		addButtonListener(buttonoptionen);
	
		//Positonierung der Button auf BorderLayout
		GridBagConstraints gbc_panelButton = new GridBagConstraints();
		gbc_panelButton.gridy = 0;
		getContentPane().add(panelButton, gbc_panelButton);
		setVisible(true);
	
	}

	/*@SuppressWarnings("unused")
	public static void main(String[] args){
	Menu m = new Menu();	//initialisierung des Fensters
	}*/	
	

	//Listener fuer Button
	private void addButtonListener(JButton b){
		b.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent ae)
					{
						eingabe(ae.getActionCommand());
					}
				});
			}

	//Action fuer button
		private void eingabe(String a){
			if(a == stringbuttonbeenden)
				{
					System.exit(1);
				}
	
			if(a == stringbuttonoptionen)
			{
				MenuOption dialog = new MenuOption();
				dialog.setLocationRelativeTo(this);
				dialog.setVisible(true);
			}	
			
			if(a == stringbuttonstart)
			{
			//JPanel-Übergabe und alten JPanel schließen
				Component start;
				remove(panelButton);
				validate();
				start = add(new MenuStart());
				update(start.getGraphics());
			}	
					
	}

	//falls fenster kleiner gemacht wird, wieder auf mindestgröße zurück setzen
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
