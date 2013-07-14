package com.github.propra13.gruppeA3.Network;

import java.io.IOException;

import com.github.propra13.gruppeA3.Entities.Player;
import com.github.propra13.gruppeA3.Menu.MenuStart;

/**
 * @author Majida Dere
 *
 */
public class ClientUpdater extends Thread {

	MenuStart gui = null;
	Protocol protocol = null;
	Player[] players = null;
	int playerNumber;
	
	/**
	 * Setzt die noetigen Parameter
	 * @param start Der Frame der sich um das Spielfeld kuemmert
	 * @param protokoll Das Protokoll zur Kommunikation mit dem Server
	 * @param players Das Spielerarray mit allen Spielern
	 * @param playerNumber Die Spielernummer des lokalen Spielers
	 * @param spielfeld Das Spielfeld
	 */
	public ClientUpdater(MenuStart gui, Protocol protocol, Player[] players, 
			int playerNumber)
	{
		this.gui = gui;
		this.protocol = protocol;
		this.players = players;
		this.playerNumber = playerNumber;
	}
	
	public void run()
	{
		boolean running = true;
		String vergleich;
		while(running)
		{
			try
			{	
				if(!protocol.isInputShutdown()) 
					vergleich = protocol.receiveString();
				else {
					running = false;
					continue;
				}
				
				if(vergleich != null && vergleich.equalsIgnoreCase("spieler"))
				{
					
				}
				 if(vergleich.equalsIgnoreCase("eog"))
				{
					running = false;
				}				
			}catch(IOException ex){ex.printStackTrace();}//TODO bessere Exception
		}	
	}
}
