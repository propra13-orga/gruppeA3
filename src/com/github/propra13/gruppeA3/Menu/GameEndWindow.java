package com.github.propra13.gruppeA3.Menu;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


public class GameEndWindow extends JFrame{
	//Komponenten
	private JButton buttonbeenden;
	private JButton buttonmenue;
	private JLabel endmessage;

	private JPanel panelButton;
	
	//Konstruktor
	public GameEndWindow(String s){
		setTitle("Spiel zu Ende");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocation(400,200);
		setSize(200,140);
		getContentPane().setLayout(new GridBagLayout());
		
		buttonbeenden = new JButton("Spiel Beenden");
		buttonmenue = new JButton("Hauptmenü");
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
		
	}
}
