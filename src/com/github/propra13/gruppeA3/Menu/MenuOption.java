package com.github.propra13.gruppeA3.Menu;

import com.github.propra13.gruppeA3.Entities.GameOptions;

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

//wird noch bearbeitet. zur Zeit noch nicht in der Nutzung

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
	private String SaveName = "Player1"; 	//dummy
	private String SaveIP = "localhost"; 	//dummy
	private String TMPPort = "1337";
	
	/**
	 * Erstellt das Optionsfenster
	 */
	public MenuOption() 
	{
        //super("Optionen");
        	
        //setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        
        JPanel ONorth = new JPanel();
        JPanel OSouth = new JPanel();
        
        //Buttons
        JButton Abbrechen = new JButton("Abbrechen");
        Abbrechen.addActionListener(this);
        Abbrechen.setActionCommand("Abbrechen");
        JButton OK = new JButton("OK");
        OK.addActionListener(this);
        OK.setActionCommand("OK");
        
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(ONorth, BorderLayout.CENTER);
        this.getContentPane().add(OSouth, BorderLayout.SOUTH);
        
        Name = new JTextField(SaveName);
        IP = new JTextField(SaveIP,20);
        Port = new JTextField(TMPPort,20);
				
		ONorth.setLayout(new GridLayout(8,2));
		ONorth.add( new JLabel("Name:"));
		ONorth.add(Name);
		ONorth.add( new JLabel("IP:") );
		ONorth.add(IP);
		ONorth.add(new JLabel("Port:"));
		ONorth.add(Port);
		
		OSouth.setLayout(new FlowLayout());
		OSouth.add(Abbrechen);
		OSouth.add(OK);
		
	    pack();
	    setLocationRelativeTo(null);
	    setResizable(false);
	}
	
	/**
	 * ActionListener der sich um die Verarbeitung der Eingaben kuemmert.
	 */
	public void actionPerformed(ActionEvent e) 
	{
		if (e.getActionCommand().equals("Abbrechen")) 
		{
			dispose();
		}
		if (e.getActionCommand().equals("OK")) 
		{	
			dispose();
		}
	}
}