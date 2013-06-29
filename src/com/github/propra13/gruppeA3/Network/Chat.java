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
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

/**
 * @author Majida Dere
 *
 */
public class Chat extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1135932855613041682L;
    private JTextField userText=null;
    private JTextArea chatWindow=null;
    private Protocol protocol=null;
    private String name=null;
	/**
	 * 
	 */
	public Chat(final String name, Protocol p) {
		this.name = name;
		this.protocol = p;
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
							protocol.sendString(name + ": " + userText.getText() + "\n");
							String s = protocol.receiveString();
	            			chatWindow.append(s);
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
	}

	public void setProtocol(Protocol p){
		this.protocol = p;
	}
	
	public static void main(String[] args) throws UnknownHostException, IOException{
		ServerSocket server = new ServerSocket(1337);
		new Chat("Maj1", new Protocol("127.0.0.1", 1337));
		Protocol p1 = new Protocol(server.accept());
		new Chat("Maj2", new Protocol("127.0.0.1", 1337));
		Protocol p2 = new Protocol(server.accept());
	}
}
