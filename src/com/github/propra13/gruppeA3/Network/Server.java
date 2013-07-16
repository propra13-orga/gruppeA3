/**
 * 
 */
package com.github.propra13.gruppeA3.Network;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.Iterator;

import com.github.propra13.gruppeA3.Entities.Player;
import com.github.propra13.gruppeA3.Exceptions.InvalidRoomLinkException;
import com.github.propra13.gruppeA3.Exceptions.MapFormatException;
import com.github.propra13.gruppeA3.Map.Field;
import com.github.propra13.gruppeA3.Map.Map;
import com.github.propra13.gruppeA3.Menu.MenuStart;
import com.github.propra13.gruppeA3.XMLParser.SAXCrawlerReader;

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
		get_started("Story01", netstat.toString().toLowerCase());
	}
	
	public void get_started(String mapName, String xmlName) {
		
	 	/*try {
	 		Map.initialize(mapName);
		} catch (MapFormatException | IOException
				| InvalidRoomLinkException e) {
			e.printStackTrace();
		}*/
	 	
		if(xmlName != null && !xmlName.equals("")) {
			SAXCrawlerReader reader=new SAXCrawlerReader();
		 	try {
		 		reader.read("data/levels/"+xmlName+"1.xml");
		 		this.playerCount = reader.getHandler().getPlayerCount();
		 		this.players = reader.getHandler().getPlayer();
		 	} catch (Exception e) {
		 			e.printStackTrace();
		 	}
		}
		
		//Spawns setzen
		
	    if(Map.spawns.size() < players.length)
	    	System.out.println("nicht genug Spawns für alle Spieler");
	    else {
	        Iterator<Field> iter = (Iterator<Field>) Map.spawns.iterator();
	        Field spawn;
	        for(int i=0; i < players.length; i++) {
	            spawn = iter.next();
	            players[i].setPosition(spawn.pos.toPosition());
	            for(int j=0; j < players.length; j++)
	            	players[i].getRoom().entities.add(players[j]);
	        }
	    }
	}
	
	/**
	 * Die Runmethode, startet alle threads
	 */
	public void run() {
		boolean started = true;
		try {
			threads = new ServerUpdater[playerCount];
			for(int i = 0; i < playerCount; i++){
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
