package com.github.propra13.gruppeA3.Network;

import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.github.propra13.gruppeA3.Menu.MenuStart;
import com.github.propra13.gruppeA3.Menu.MenuStart.NetworkStatus;

/**
 * Diese Klasse erzeugt einen Clienten
 * @author Majida Dere
 *
 */
public class Client extends JFrame {

	/**
	 * Attribute:
	 * 		serialVersionUID: Eine automatisch generierte User ID, bisher ohne Nutzen
	 * 		protocol: Wird verwendet um mit dem Server zu kommunizieren
	 * 		gui: Das Menü, welches alle wichtigen Funktionen benihaltet zur Darstellung
	 * 		chat: Das Chat, jeder Client besitzt einen.
	 */
	private static final long serialVersionUID = 8970545556976842124L;
	private Protocol protocol=null;
	private MenuStart gui;
	private Chat chat;

	/**
	 * Erzeugt einen neuen Clienten
	 * @param gui Das Menü
	 * @param netstat Wird verwendet um zwischen Deathmatch und Co-Op zu unterscheiden
	 */
	public Client(MenuStart gui, NetworkStatus netstat) {
		this.gui = gui;
		System.out.println("Client " + netstat.toString() + " started");
		if(connect(gui.getHost(), gui.getPort())){
			System.out.println("Verbindung erfolgreich");
			chat = new Chat(gui.getName(), this.protocol);
			chat.setVisible(false);
		}else{
			System.out.println("Fehlgeschlagen");
		}
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * Diese Methode wird beim Verbinden zum Server aufgerufen.
	 * @param serverName IP des Servers
	 * @param port Port des Servers
	 * @return Verbindungsversuch erfolgreich? Ja/Nein
	 */
	private boolean connect(String serverName, int port)
	{
		try
		{
			this.protocol = new Protocol(serverName,port);
			try {
				this.protocol.sendString("chat");
				this.protocol.sendString(this.gui.getName() + " hat den Raum betreten\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
			return true;
		}
		catch(IOException ex)
		{
			JOptionPane.showMessageDialog(this, 
					"Läuft der Server schon?", "Connection Problem",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
	}
}