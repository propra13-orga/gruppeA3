/**
 * 
 */
package com.github.propra13.gruppeA3.Network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * Diese Klasse dient zur Netzwerkkommunikation und liefert 
 * Getter und Setter Methoden zum Senden und Empfangen von Spielelementen
 * @author Majida Dere
 *
 */
public class Protocol {

	/**
	 * Attribute:
	 * 			in: Der eingehende Stream, aus diesem werden alle zu emfangenden Nachrichten gelesen
	 * 			out: Der ausgehende Stream, in diesen werden alle zu sendenden Nachrichten geschrieben
	 * 			socket: Der Socket, der die Verbindungen aufrecht erhält.
	 */
	private DataInputStream in;
    private DataOutputStream out;
    private Socket socket = null;
    
    /**
     * Dieser Konstruktor erzeugt ein Protokoll für den Client/Server mit einem vorgegebenen Socket
     * @param socket Die Verbindung zwischen Server und Client
     */
	public Protocol(Socket socket) {
		this.socket = socket;
		try {
			this.in = new DataInputStream(socket.getInputStream());
			this.out = new DataOutputStream(socket.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Dieser Konstruktor erzeugt ein Protokoll für den Client/Server mit IP und Port
	 * @param ip Die IP des Clients
	 * @param port Der Port über den die Kommunikation laufen soll
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public Protocol(String ip, int port) 
			throws UnknownHostException, IOException{
    	this(new Socket(ip,port));
    }

}
