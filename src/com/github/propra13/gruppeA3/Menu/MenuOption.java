package com.github.propra13.gruppeA3.Menu;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Diese Klasse stellt das Optionsfenster dar in dem Server IP, 
 * Port und Spielername eingestellt werden koennen
 * @author Majida Dere
 */
class MenuOption extends JFrame implements ActionListener 
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 6820369579416978104L;
	private JTextField Name, IP ,Port;
	private MenuStart gui;
	
	/**
	 * Erstellt das Optionsfenster
	 */
	public MenuOption(MenuStart gui) 
	{
        super("Server Einstellungen");
        setVisible(true);
		this.gui = gui;
        	
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        JPanel ONorth = new JPanel();
        JPanel OSouth = new JPanel();
        
        //Buttons
        JButton OK = new JButton("OK");
        OK.addActionListener(this);
        OK.setActionCommand("OK");
        JButton Abbrechen = new JButton("Abbrechen");
        Abbrechen.addActionListener(this);
        Abbrechen.setActionCommand("Abbrechen");
        
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(ONorth, BorderLayout.CENTER);
        this.getContentPane().add(OSouth, BorderLayout.SOUTH);
        
        Name = new JTextField(gui.getName());
        IP = new JTextField(gui.getHost(),20);
        Port = new JTextField(Integer.toString(gui.getPort()),20);
				
		ONorth.setLayout(new GridLayout(3,2));
		ONorth.add( new JLabel("Name:"));
		ONorth.add(Name);
		ONorth.add( new JLabel("IP:") );
		ONorth.add(IP);
		ONorth.add(new JLabel("Port:"));
		ONorth.add(Port);
		
		OSouth.setLayout(new FlowLayout());
		OSouth.add(OK);
		OSouth.add(Abbrechen);
		
	    pack();
	    setLocationRelativeTo(null);
	    setResizable(false);
	}
	
	/**
	 * ActionListener der sich um die Verarbeitung der Eingaben kuemmert.
	 */
	public void actionPerformed(ActionEvent e) {
		if (e.getActionCommand().equals("Abbrechen")) {
			dispose();
		}
		if (e.getActionCommand().equals("OK")) {
			gui.setHost(IP.getText());
			gui.setName(Name.getText());
			gui.setPort(Integer.parseInt(Port.getText()));
			dispose();
		}
	}
}