package com.github.propra13.gruppeA3.Network;

import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import com.github.propra13.gruppeA3.Game;
import com.github.propra13.gruppeA3.Keys;
import com.github.propra13.gruppeA3.Entities.Entities;
import com.github.propra13.gruppeA3.Entities.Player;
import com.github.propra13.gruppeA3.Exceptions.InvalidRoomLinkException;
import com.github.propra13.gruppeA3.Exceptions.MapFormatException;
import com.github.propra13.gruppeA3.Map.Map;
import com.github.propra13.gruppeA3.Map.MapHeader;
import com.github.propra13.gruppeA3.Menu.MenuStart;
import com.github.propra13.gruppeA3.Menu.MenuStart.GameStatus;
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
	private ClientUpdater clientUpdater;
	private Player[] players;
	private int playerID;

	/**
	 * Erzeugt einen neuen Clienten
	 * @param gui Das Menü
	 * @param netstat Wird verwendet um zwischen Deathmatch und Co-Op zu unterscheiden
	 * @param serverStarted Läuft der Client auf dem selben Rechner wie der Server?
	 */
	public Client(MenuStart gui, NetworkStatus netstat, boolean serverStarted) {
		this.gui = gui;
		if (!serverStarted){
			Iterator<MapHeader> iter = Game.mapHeaders.iterator();
			MapHeader map=null;
			while(iter.hasNext()){
				map = iter.next();
				if(map.mapName.equalsIgnoreCase(netstat.toString()))
					break;
			}
				
		 	try {
		 		if(null != map){
		 			Map.initialize(map);
		 		}
			} catch (MapFormatException | IOException
					| InvalidRoomLinkException e) {
				e.printStackTrace();
			}
		 }
		System.out.println("Client " + netstat.toString() + " started");
		if(connect(gui.getHost(), gui.getPort())){
			System.out.println("Verbindung erfolgreich");
			chat = new Chat(gui.getName(), this.protocol);
			clientUpdater = new ClientUpdater(gui, chat, protocol, players, playerID);
			clientUpdater.start();
		}else{
			System.out.println("Fehlgeschlagen");
			return;
		}
		
		gui.setPlayer(players[playerID]);
		gui.addKeyListener(new Keys(players[playerID]));
		
		for(int i = 0; i < players.length; i++)
			Map.getRoom(0).entities.add(players[i]);
	 	
		gui.setRandom(new Random(System.currentTimeMillis()));
		MenuStart.setActiveRoom(Map.getRoom(0));
		
		
		// Menü-Buttons ausblenden, Status ändern
		gui.setButtonVisible(false, false, false, false, false, false, false, false,
						 false, false, false, false, false, false, false, false);
			
		MenuStart.setGameStatus(GameStatus.INGAME);
		System.out.println("entitieslist "+Map.getRoom(0).entities.size());
		Iterator<Entities> iter = Map.getRoom(0).entities.iterator();
		Entities entity;
		Player player;
		while(iter.hasNext()){
			entity = iter.next();
			if(entity instanceof Player){
				player = (Player)entity;
				System.out.println("playerIDD "+player.getPlayerID());
			}
		}
	}
	
	/**
	 * 
	 * @throws IOException
	 */
	private void loadNextGame() throws IOException {
		String vergleich = protocol.receiveString();
		vergleich = protocol.receiveString();
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
				this.loadNextGame();
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

	public Chat getChat() {
		return chat;
	}
}