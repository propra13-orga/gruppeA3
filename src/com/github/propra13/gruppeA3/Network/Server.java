/**
 * 
 */
package com.github.propra13.gruppeA3.Network;

import java.io.IOException;
import java.net.ServerSocket;

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
	private ServerThread[] threads;
	
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
	}
	
	/**
	 * Die Runmethode, startet alle threads
	 */
	public void run() {
		try {
			// TODO: vorläufig 2 Clienten
			threads = new ServerThread[2];
			for(int i = 0; i < 2; i++){
				threads[i] = new ServerThread(server.accept());
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
