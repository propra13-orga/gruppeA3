/**
 * 
 */
package com.github.propra13.gruppeA3.Network;

import java.io.IOException;
import java.net.ServerSocket;

/**
 * @author Majida Dere
 *
 */
public class Server extends Thread {

	private ServerSocket server = null;
	
	/**
	 * 
	 */
	public Server(int port) {
		System.out.println("Starte Server...");
		try {
			this.server = new ServerSocket(port); // Port zuerst uebergeben
			//System.out.println("Warte auf "+laenge+". Spieler...");
		}catch ( IOException e ) {
		      e.printStackTrace();
	    }
	}

}
