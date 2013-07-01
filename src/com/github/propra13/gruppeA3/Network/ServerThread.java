/**
 * 
 */
package com.github.propra13.gruppeA3.Network;

import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;

/**
 * ServerThread ist eine Hilfsklasse für die Klasse Server
 * Diese wird von Server erzeugt und existiert pro Client einmal
 * Jede ServerThread Klasse enthält ein Array aller ServerThread Instanzen
 * So können alle Clienten angesprochen werden
 * @author Majida Dere
 *
 */
public class ServerThread extends Thread {
	
	/**
	 * Attribute:
	 * 		protocol: Ist das Protokoll eines jeden ServerThreads und wird zur Kommunikation mit den Clienten verwendet
	 * 		started: Ein bool'scher Wert, wird dazu verwendet um zu deklarieren wie lange der Thread läuft
	 * 		threads: Array von ServerThreads, wo alle ServerThreads gespeichert werden, so kann man alle Clienten erreichen
	 */
	private Protocol protocol;
	private boolean started;
	private ServerThread[] threads;
	
	/**
	 * Erzeugt einen neuen ServerThread
	 * @param s Socket das die Verbindung zum Client darstellt
	 * @throws IOException Wird im Fehlerfall geworfen
	 */
	public ServerThread(Socket s) throws IOException {
		this.protocol = new Protocol(s);
	}
	
	/**
	 * Setzt ServerThread array threads
	 * @param threads Referenz zum Array
	 */
	public void setThreads(ServerThread[] threads){
		this.threads = threads;
	}

	/**
	 * Sendet eine Chatnachricht
	 * @param message Die zu sendende Nachricht
	 * @throws IOException die im Fehlerfall geworfene Eception
	 */
	public void sendMessage(String message) throws IOException{
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
				else vergleich = null;
				 
				if(null == vergleich)
					started = false;
				
				else if(vergleich.equalsIgnoreCase("chat"))
				{
					sendMessage(receiveMessage());
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
