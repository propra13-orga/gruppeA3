/**
 * 
 */
package com.github.propra13.gruppeA3.Network;

import java.io.IOException;
import java.net.ServerSocket;

import com.github.propra13.gruppeA3.Entities.Player;
import com.github.propra13.gruppeA3.Exceptions.InvalidRoomLinkException;
import com.github.propra13.gruppeA3.Exceptions.MapFormatException;
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
	private Player[] player=null;
	
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
		loadGame("Story01", netstat.toString().toLowerCase());
	}
	
	public void loadGame(String mapName, String xmlName) {
		
	 	try {
	 		Map.initialize(mapName);
		} catch (MapFormatException | IOException
				| InvalidRoomLinkException e) {
			e.printStackTrace();
		}
	 	
		if(xmlName != null && !xmlName.equals("")) {
			SAXCrawlerReader reader=new SAXCrawlerReader();
		 	try {
		 		reader.read("data/levels/"+xmlName+"1.xml");
		 		this.playerCount = reader.getHandler().getPlayerCount();
		 		this.player = reader.getHandler().getPlayer();
		 	} catch (Exception e) {
		 			e.printStackTrace();
		 	}
		}

	}
	
	/**
	 * Die Runmethode, startet alle threads
	 */
	public void run() {
		try {
			threads = new ServerUpdater[playerCount];
			for(int i = 0; i < playerCount; i++){
				threads[i] = new ServerUpdater(server.accept(),  player, i);
				threads[i].setThreads(threads);
				threads[i].start();
				System.out.println("Spieler "+(i+1)+" connected");
			}
			System.out.println("Server gestartet");
		}catch ( IOException e ) {
		      e.printStackTrace();
	    }
	}
}
