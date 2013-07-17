/**
 * 
 */
package com.github.propra13.gruppeA3.Network;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import com.github.propra13.gruppeA3.Entities.Player;

/**
 * ServerThread ist eine Hilfsklasse für die Klasse Server
 * Diese wird von Server erzeugt und existiert pro Client einmal
 * Jede ServerThread Klasse enthält ein Array aller ServerThread Instanzen
 * So können alle Clienten angesprochen werden
 * @author Majida Dere
 *
 */
public class ServerUpdater extends Thread {
	
	/**
	 * Attribute:
	 * 		protocol: Ist das Protokoll eines jeden ServerThreads und wird zur Kommunikation mit den Clienten verwendet
	 * 		started: Ein bool'scher Wert, wird dazu verwendet um zu deklarieren wie lange der Thread läuft
	 * 		threads: Array von ServerThreads, wo alle ServerThreads gespeichert werden, so kann man alle Clienten erreichen
	 */
	private Protocol protocol;
	private boolean started;
	private ServerUpdater[] threads;
	private int playerID;
	private Player[] player;
	private int key;
	
	/**
	 * Erzeugt einen neuen ServerThread
	 * @param s Socket das die Verbindung zum Client darstellt
	 * @throws IOException Wird im Fehlerfall geworfen
	 */
	public ServerUpdater(Socket s, Player[] player, int playerID) throws IOException {
		this.protocol = new Protocol(s);
		this.player = player;
		this.playerID = playerID;
	}
	
	/**
	 * Setzt ServerThread array threads
	 * @param threads Referenz zum Array
	 */
	public void setThreads(ServerUpdater[] threads){
		this.threads = threads;
	}

	/**
	 * Sendet eine Chatnachricht
	 * @param message Die zu sendende Nachricht
	 * @throws IOException die im Fehlerfall geworfene Eception
	 */
	public synchronized void sendMessage(String message) throws IOException{
		for(int i = 0; i < threads.length; i++){
			if(null != threads[i]){
				threads[i].getProtocol().sendString("chat");
				threads[i].getProtocol().sendString(message);
			}
		}
	}
	
	/**
	 * Liefert das Protokoll des aktuellen ServerThreads
	 * @return Protocol
	 */
	private Protocol getProtocol() {
		return this.protocol;
	}

	/**
	 * Liefert eine Chatnachricht
	 * @return Chatnachricht
	 * @throws IOException
	 */
	public String receiveMessage() throws IOException{
		return this.protocol.receiveString();
	}
	/**
	 * Wird vom Server pro Level aufgerufen
	 * @throws IOException
	 */
	public void sendNextGame() throws IOException
	{
		this.protocol.sendString("start");
		this.protocol.sendPlayers(player);
		this.protocol.sendPlayerID(playerID);
	}
	
	/**
	 * Die Hauptthreadfunktion
	 * Hier werden Einkommende Inputs empfangen und ausgewertet
	 */
	public void run()
	{
		started = true;
		String vergleich;
		while(started)
		{
			try
			{
				if(!this.protocol.isInputShutdown())
					vergleich = protocol.receiveString();
				else{
					started = false;
					continue;
				}
				
				if(vergleich.equalsIgnoreCase("chat"))
				{
					sendMessage(receiveMessage());
				} else if(vergleich.equalsIgnoreCase("key")){
					int keys[] = this.protocol.receiveKey();
					key = keys[0];
					playerID = keys[1];
					// Key auswerten
					// Ausertung an alle Spieler schicken
				}
			}
			catch(SocketException ex)
			{
				ex.printStackTrace();
			}		
			catch(IOException ex)
			{
				ex.printStackTrace();//TODO bessere Exception
			}
		}
	}
}
