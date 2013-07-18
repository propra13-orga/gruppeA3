package com.github.propra13.gruppeA3.Network;

import java.awt.event.KeyEvent;
import java.io.IOException;

import com.github.propra13.gruppeA3.Keys;
import com.github.propra13.gruppeA3.Music;
import com.github.propra13.gruppeA3.Entities.Player;
import com.github.propra13.gruppeA3.Entities.Moveable.Direction;
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
	private int key;
	private int id;
	
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
				else if(vergleich.equalsIgnoreCase("key")){
					receiveKeys();
					move();
					players[id].move();
				}
				else if(vergleich.equalsIgnoreCase("eog")){
					running = false;
				}			
			}catch(IOException ex){ex.printStackTrace();}//TODO bessere Exception
		}	
	}
	
	private synchronized void receiveKeys() throws IOException{
		int keys[] = this.protocol.receiveKey();
		key = keys[0];
		id = keys[1];
	}
	
	private synchronized void sendKeys() throws IOException{
		protocol.sendKey(key, id);
	}
	
	private synchronized void move() {
		if(players[id].getLives() == 0)
			return;
		switch(key){
			case KeyEvent.VK_LEFT:
				players[id].setDirection(Direction.LEFT);
				players[id].setFaceDirection(Direction.LEFT);
				break;
			
			case KeyEvent.VK_RIGHT:
				players[id].setDirection(Direction.RIGHT);
				players[id].setFaceDirection(Direction.RIGHT);
				break;
				
			case KeyEvent.VK_UP:
				players[id].setDirection(Direction.UP);
				players[id].setFaceDirection(Direction.UP);
				break;
			
			case KeyEvent.VK_DOWN:
				players[id].setDirection(Direction.DOWN);
				players[id].setFaceDirection(Direction.DOWN);
				break;
			case KeyEvent.VK_A:
				players[id].setAttack(true);
				Music.soundattach(); //Soundeffekt hit
				break;
			case KeyEvent.VK_T:
				chat.setVisible(true);
				break;
			case KeyEvent.VK_E:
				MenuStart.talk = true;
				break;
		}
	}
}
