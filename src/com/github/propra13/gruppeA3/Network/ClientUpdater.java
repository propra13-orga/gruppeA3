package com.github.propra13.gruppeA3.Network;

import java.io.IOException;

import com.github.propra13.gruppeA3.Keys;
import com.github.propra13.gruppeA3.Entities.Player;
import com.github.propra13.gruppeA3.Menu.MenuStart;

/**
 * @author Majida Dere
 *
 */
public class ClientUpdater extends Thread {

	private MenuStart gui = null;
	private Protocol protocol = null;
	private Player[] players = null;
	private int playerID;
	private Chat chat;
	
	/**
	 * Setzt die noetigen Parameter
	 * @param gui Der Frame der sich um das Spielfeld kuemmert
	 * @param protocol Das Protokoll zur Kommunikation mit dem Server
	 * @param players Das Spielerarray mit allen Spielern
	 * @param playerID Die Spielernummer des lokalen Spielers
	 */
	public ClientUpdater(MenuStart gui, Chat chat, Protocol protocol,
						Player[] players, int playerID){
		this.gui = gui;
		this.chat = chat;
		this.protocol = protocol;
		this.players = players;
		this.playerID = playerID;
		//gui.setPlayer(players[playerID]);
		//gui.addKeyListener(new Keys(players[playerID]));
		
	}
	
	/**
	 * Spiel laden
	 * @param players 
	 * @throws IOException Falls es einen Fehler in der Uebertragung gab
	 */
	private void loadNextGame() throws IOException {
		String vergleich = protocol.receiveString();
    	if(vergleich.equals("player"))
    	{
    		players = protocol.receivePlayers();
    	}
    	vergleich = protocol.receiveString();
    	if(vergleich.equals("playerID"))
    	{
    		playerID = protocol.receivePlayerID();
    	}
    	
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
				
				
				if(vergleich.equals("chat")){
					String s = protocol.receiveString();
					chat.append(s);
				}
				else if(vergleich.equals("start"))
					loadNextGame();
				else if (vergleich.equalsIgnoreCase("player"))
					players = this.protocol.receivePlayers();
				else if(vergleich.equalsIgnoreCase("eog")){
					running = false;
				}			
			}catch(IOException ex){ex.printStackTrace();}//TODO bessere Exception
		}	
	}
}
