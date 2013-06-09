package com.github.propra13.gruppeA3.Menu;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import com.github.propra13.gruppeA3.*;


public class GameEndWindow extends JFrame{
/*
	
	//Komponenten
	private JButton buttonbeenden;
	private JButton buttonmenue;
	private JLabel endmessage;
	private String stringbuttonmenue;
	private String stringbuttonbeenden;

	private JPanel panelButton;
	
	//Konstruktor
	public GameEndWindow(String s){
		setTitle("Spiel zu Ende");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(400,200);
		setSize(400,200);
		getContentPane().setLayout(new GridBagLayout());
		

		//Button Texte
		stringbuttonmenue = "Hauptmenu";
		stringbuttonbeenden = "Beenden";
		
		
		//Button erzeugen
		buttonbeenden = new JButton(stringbuttonbeenden);
		buttonmenue = new JButton(stringbuttonmenue);
		endmessage = new JLabel(s);
		
		panelButton = new JPanel(new GridBagLayout());
		
		GridBagConstraints gbc_buttonbeenden = new GridBagConstraints();
		gbc_buttonbeenden.gridx = 0;
		gbc_buttonbeenden.gridy = 1;
		gbc_buttonbeenden.gridwidth = 1;
		panelButton.add(buttonbeenden, gbc_buttonbeenden);
		
		GridBagConstraints gbc_buttonmenue = new GridBagConstraints();
		gbc_buttonmenue.gridx = 1;
		gbc_buttonmenue.gridy = 1;
		gbc_buttonmenue.gridwidth = 1;
		panelButton.add(buttonmenue, gbc_buttonmenue);
		
		GridBagConstraints gbc_endmessage = new GridBagConstraints();
		gbc_endmessage.gridx = 0;
		gbc_endmessage.gridy = 0;
		gbc_endmessage.gridwidth = 2;
		panelButton.add(endmessage, gbc_endmessage);
		
		addButtonListener(buttonbeenden);
		addButtonListener(buttonmenue);
		
		//Positonierung der Button auf BorderLayout
				GridBagConstraints gbc_panelButton = new GridBagConstraints();
				gbc_panelButton.gridy = 0;
				getContentPane().add(panelButton, gbc_panelButton);
				setVisible(true);
		
		this.setAlwaysOnTop(true);
		this.setVisible(true);
	}
	
	//Listener fuer Button
	private void addButtonListener(JButton b){
		b.addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent ae){
					eingabe(ae.getActionCommand());
				}
		});
	}
	
	//Action fuer Button
	private void eingabe(String a){
		if(a == stringbuttonbeenden)
			{
				System.exit(1);
			}
	
	
		if(a == stringbuttonmenue){
			Game.meinmenue.setVisible(true);
			this.dispose();
		}
	}
*/
}
