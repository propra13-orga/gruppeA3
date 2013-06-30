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
public class ChatThread extends Thread {
	
	/**
	 * Attribute:
	 * 		chat: Das Chatfenster, dient zur Ausgabe
	 */
	private Chat chat;

	/**
	 * Erzeugt ein Thread welcher im Hintergrund läuft
	 * @param chat Das Chatfenster
	 */
	public ChatThread(Chat chat) {
		this.chat = chat;
		// TODO Auto-generated constructor stub
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
				if(!chat.getProtocol().isInputShutdown())
					vergleich = chat.getProtocol().receiveString();
				else vergleich = null;
				
				if(null == vergleich)
					running = false;
				
				else if(vergleich.equalsIgnoreCase("chat")){
					String s = chat.getProtocol().receiveString();
					chat.append(s);
					//System.out.println(s);
				}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
}
