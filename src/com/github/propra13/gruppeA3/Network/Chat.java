/**
 * 
 */
package com.github.propra13.gruppeA3.Network;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.IOException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * Diese Klasse implementiert ein Chatfenster
 * @author Majida Dere
 *
 */
public class Chat extends JFrame {

	/**
	 * Attribute:
	 * 		serialVersionUID: Eine automatisch generierte User ID, bisher ohne Nutzen
	 * 		userText: Hier wird der zu Sendende Text eingetragen
	 * 		chatWindow: Die TextArea, welche für die Ausgabe des Chatverlaufs verwendet wird
	 * 		protocol: Wird verwendet um mit den anderen Clienten zu kommunizieren
	 * 		ct: Der Thread, der die Textnachrichten abfängt und ausgibt
	 */
	private static final long serialVersionUID = 1135932855613041682L;
    private JTextField userText=null;
    private JTextArea chatWindow=null;
    private Protocol protocol=null;
    private ChatThread ct;
    
    /**
     * Erzeugt ein Chatfenster
     * @param name Name des Spielers
     * @param p Das erzeugte Protokoll
     */
	public Chat(final String name, Protocol p) {
		super("Chatfenster von "+name);
		setProtocol(p);
		setType(Type.UTILITY);
		userText = new JTextField();
		userText.addActionListener( new ActionListener(){
    	   public void actionPerformed(ActionEvent event){
    	   }
		});
		
		userText.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
            	if(e.getSource()==userText){
            		if(e.getKeyCode() == KeyEvent.VK_ENTER){
            			try {
            				protocol.sendString("chat");
							protocol.sendString(name + ": " + userText.getText() + "\n");
						} catch (IOException ex) {
							// TODO Auto-generated catch block
							ex.printStackTrace();
						}
            			userText.setText("");
            		}
            	}
            }
        });
		
		getContentPane().add(userText,BorderLayout.SOUTH);
		chatWindow= new JTextArea();
		chatWindow.setEditable(false);
		getContentPane().add(new JScrollPane(chatWindow));
		setSize(300,250);
		ct = new ChatThread(this);
		ct.start();
	}

	/**
	 * Setzt die Protkoll Variable
	 * @param p
	 */
	public void setProtocol(Protocol p){
		this.protocol = p;
	}

	/**
	 * Liefert das aktuell genutzte Protokoll
	 * @return Protocol
	 */
	public Protocol getProtocol(){
		return this.protocol;
	}
	
	/**
	 * Wird dazu verwendet um eingehende Nachrichten auszugeben
	 * @param s Auszugebende String
	 */
	public void append(String s){
		chatWindow.append(s);
	}
}
