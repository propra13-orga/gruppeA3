/**
 * 
 */
package com.github.propra13.gruppeA3.Network;

import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

import com.github.propra13.gruppeA3.Entities.Player;
import com.github.propra13.gruppeA3.Entities.Moveable.Direction;

/**
 * ServerUpdater ist eine Hilfsklasse für die Klasse Server
 * Diese wird von Server erzeugt und existiert auf der Serverseite pro Client einmal
 * Jede ServerUpdater Klasse enthält ein Array aller ServerUpdater Instanzen
 * So können alle Clienten angesprochen werden
 * @author Majida Dere
 *
 */
public class ServerUpdater extends Thread {
	
	/**
	 * Attribute:
	 * 		protocol: Ist das Protokoll eines jeden ServerUpdaters und wird zur Kommunikation mit den Clienten verwendet
	 * 		started: Ein bool'scher Wert, wird dazu verwendet um zu deklarieren wie lange der Thread läuft
	 * 		threads: Array von ServerUpdaters, wo alle ServerUpdaters gespeichert werden, so kann man alle Clienten erreichen
	 */
	private Protocol protocol;
	private boolean started;
	private ServerUpdater[] threads;
	private int playerID;
	private Player[] players;
	private int key;
	
	/**
	 * Erzeugt einen neuen ServerUpdater
	 * @param s Socket das die Verbindung zum Client darstellt
	 * @throws IOException Wird im Fehlerfall geworfen
	 */
	public ServerUpdater(Socket s, Player[] players, int playerID) throws IOException {
		this.protocol = new Protocol(s);
		this.players = players;
		this.playerID = playerID;
	}
	
	/**
	 * Setzt ServerUpdater array threads
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
	public synchronized void sendMessage(String message) throws IOException {
		for(int i = 0; i < threads.length; i++){
			if(null != threads[i]){
				threads[i].getProtocol().sendString("chat");
				threads[i].getProtocol().sendString(message);
			}
		}
	}
	
	/**
	 * Liefert das Protokoll des aktuellen ServerUpdaters
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
		this.protocol.sendPlayers(players);
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
				else {
					started = false;
					continue;
				}
				
				if(vergleich.equalsIgnoreCase("chat"))
				{
					sendMessage(receiveMessage());
				} else if(vergleich.equalsIgnoreCase("key")){
					receiveKeys();
					move();
					sendKeys(key, playerID);
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
	
	private synchronized void receiveKeys() throws IOException{
		int keys[] = this.protocol.receiveKey();
		key = keys[0];
		playerID = keys[1];
	}
	
	private synchronized void sendKeys(int key, int playerID) throws IOException{
		for (int i = 0; i < threads.length; i++)
			if(null != threads[i])
				threads[i].protocol.sendKey(key, playerID);
	}
	
	public synchronized void sendTick() {
		try {
			this.protocol.sendString("tick");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private synchronized void move() {
		if(players[playerID].getLives() == 0)
			return;
		switch(key){
			case KeyEvent.VK_LEFT:
				players[playerID].setDirection(Direction.LEFT);
				players[playerID].setFaceDirection(Direction.LEFT);
				break;
			
			case KeyEvent.VK_RIGHT:
				players[playerID].setDirection(Direction.RIGHT);
				players[playerID].setFaceDirection(Direction.RIGHT);
				break;
				
			case KeyEvent.VK_UP:
				players[playerID].setDirection(Direction.UP);
				players[playerID].setFaceDirection(Direction.UP);
				break;
			
			case KeyEvent.VK_DOWN:
				players[playerID].setDirection(Direction.DOWN);
				players[playerID].setFaceDirection(Direction.DOWN);
				break;
			case KeyEvent.VK_A:
				players[playerID].setAttack(true);
				break;
		}
	}
}
