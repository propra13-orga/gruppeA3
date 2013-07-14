/**
 * 
 */
package com.github.propra13.gruppeA3.Network;

import java.io.IOException;

/**
 * Diese Klasse ist eine Hilfsklasse und dient dazu um eingehende Nachrichten abzufangen
 * @author Majida Dere
 *
 */
public class ChatUpdater extends Thread {
	
	/**
	 * Attribute:
	 * 		chat: Das Chatfenster, dient zur Ausgabe
	 */
	private Chat chat;

	/**
	 * Erzeugt ein Thread welcher im Hintergrund läuft
	 * @param chat Das Chatfenster
	 */
	public ChatUpdater(Chat chat) {
		this.chat = chat;
	}
	
	/**
	 * Die Run Methode, hier werden die Nachrichten abgefangen
	 */
	public void run() {
		System.out.println("run...");
		boolean running = true;
		String vergleich;
		while(running){
			try {
				/**
				 *  Ist die Verbindung noch offen
				 *  Wenn ja, dann hole den Eingehenden String
				 */
				if(!chat.getProtocol().isInputShutdown())
					vergleich = chat.getProtocol().receiveString();
				/**
				 * Wenn nein, setze running auf false und beende die Schleife
				 */
				else {
						running = false;
						continue;
				}
				/**
				 * Ist der Eingegangene String "chat", dann lese den nächsten String ein, welcher die Nachricht darstellt
				 * und füge diese hinten an chatWindow dran.
				 */
				if(vergleich.equalsIgnoreCase("chat")){
					String s = chat.getProtocol().receiveString();
					chat.append(s);
				}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
}
