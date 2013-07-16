/**
 * 
 */
package com.github.propra13.gruppeA3.Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Iterator;

import com.github.propra13.gruppeA3.Game;
import com.github.propra13.gruppeA3.Entities.Player;
import com.github.propra13.gruppeA3.Exceptions.InvalidRoomLinkException;
import com.github.propra13.gruppeA3.Exceptions.MapFormatException;
import com.github.propra13.gruppeA3.Map.Map;
import com.github.propra13.gruppeA3.Map.MapHeader;
import com.github.propra13.gruppeA3.Menu.MenuStart;

/**
 * Diese Klasse erzeugt einen Server
 * Sie läuft so lange, bis sie beendet wird oder kein Spieler mehr da ist
 * @author Majida Dere
 *
 */
public class Server extends Thread{

	/**
	 * Attribute:
	 * 		server: Ein Serversocket wird für die Kommunikation verwendet
	 * 		threads: Ein ServerThread array zur Kommunikation mit allen Clienten.
	 * 				 Das Array ist so groß wie Clienten da sind, maximal 4.
	 */
	private ServerSocket server = null;
	private ServerUpdater[] threads;
	private int playerCount=1;
	private Player[] players=null;
	
	/**
	 * Erzeugt einen neuen Server
	 * @param port Der Port auf den der Server lauscht
	 */
	public Server(int port, MenuStart.NetworkStatus netstat) {
		System.out.println("Starte Server...");
		try {
			this.server = new ServerSocket(port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		get_started(netstat.toString());
	}
	
	public void get_started(String mapName) {
		
		Iterator<MapHeader> iter = Game.mapHeaders.iterator();
		MapHeader map=null;
		while(iter.hasNext()){
			map = iter.next();
			if(map.mapName.equalsIgnoreCase(mapName))
				break;
		}
		
	 	try {
	 		if(null != map){
	 			Map.initialize(map);
	 			this.playerCount = map.maxPlayers;
	 		}
		} catch (MapFormatException | IOException
				| InvalidRoomLinkException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Die Runmethode, startet alle threads
	 */
	public void run() {
		boolean started = true;
		this.players = new Player[playerCount];
		try {
			threads = new ServerUpdater[playerCount];
			for(int i = 0; i < playerCount; i++){
				players[i] = new Player(i);
				threads[i] = new ServerUpdater(server.accept(),  players, i);
				threads[i].setThreads(threads);
				threads[i].start();
				System.out.println("Spieler "+(i+1)+" connected");
			}
			System.out.println("Server gestartet");
			for(int i = 0; i < threads.length; i++)
				if(threads[i] != null)
					threads[i].sendNextGame();
		}catch ( IOException e ) {
		      e.printStackTrace();
	    }
	}
}
