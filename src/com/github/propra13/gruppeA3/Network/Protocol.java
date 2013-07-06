package com.github.propra13.gruppeA3.Network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.github.propra13.gruppeA3.Entities.Hitbox;
import com.github.propra13.gruppeA3.Entities.Item;
import com.github.propra13.gruppeA3.Map.Position;

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
	public Protocol(Socket socket) throws IOException {
		System.out.println(socket.toString());
		
		this.in = new DataInputStream(socket.getInputStream());
		this.out = new DataOutputStream(socket.getOutputStream());
		this.socket = socket;
		
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
	
    /**
     * Offene Verbindungen schließen
     * @throws IOException
     */
    public void close() throws IOException
    {
       this.in.close();
       this.out.close();
    }
    
    /**
     * Prüft ob die eingehende Verbindung noch offen ist
     * @return true, wenn Die Verbindung geschlossen ist
     * 		   false, wenn sie noch offen ist.
     */
    public boolean isInputShutdown()
    {
    	return this.socket.isInputShutdown();
    }
    
    /**
     * Prüft ob die ausgehende Verbindung noch offen ist
     * @return true, wenn Die Verbindung geschlossen ist
     * 		   false, wenn sie noch offen ist.
     */
    public boolean isOutputShutdown()
    {
    	return this.socket.isOutputShutdown();
    }
    
    /**
     * Text senden
     * @param str Der zu sendende String
     * @throws IOException
     */
    public void sendString(String str) throws IOException
    {
    	this.out.writeInt(str.length());
    	this.out.writeChars(str);
    }
    
    /**
     * Text empfangen
     * @return der zu emfangende String
     * @throws IOException
     */
    public String receiveString() throws IOException
    {
    	int laenge = this.in.readInt();
    	char[] chars = new char[laenge];
		for (int i = 0; i < laenge; i++)
			chars[i] = in.readChar();
		return new String(chars);
    }
    
    /**
     * Neue Position senden
     * @param pos Die aktuelle Position des Objekts
     * @throws IOException
     */
    public void sendPosition(Position pos) throws IOException{
    	sendString("position");
    	this.out.writeInt(pos.x);
    	this.out.writeInt(pos.y);
    }
    
    /**
     * Neue Position empfangen
     * @return die neue Position des Objektes
     * @throws IOException
     */
    public Position receivePosition() throws IOException{
    	int x = this.in.readInt();
    	int y = this.in.readInt();
		return new Position(x,y);
    }
    
    /**
     * Hitbox senden
     * @param Hitbox Die Hitbox des Objektes
     * @throws IOException
     */
    public void sendHitbox(Hitbox hitbox) throws IOException{
    	sendString("hitbox");
    	this.out.writeInt(hitbox.width);
    	this.out.writeInt(hitbox.height);
    }
    
    /**
     * Hitbox empfangen
     * @return Hitbox des Objektes
     * @throws IOException
     */
    public Hitbox receiveHitbox() throws IOException{
    	int width = this.in.readInt();
    	int height = this.in.readInt();
    	return new Hitbox(width, height);
    }
    
    /**
     * Item senden
     * @param item Item im Raum
     * @throws IOException
     */
    public void sendItem(Item item) throws IOException{
    	sendString("item");
    	this.out.writeInt(item.getPosition().x);
    	this.out.writeInt(item.getPosition().y);
    	this.out.writeInt(item.getDamage());
    	this.out.writeInt(item.getType());
    	sendString(item.getDesc());
    	sendString(item.getName());
    	this.out.writeInt(item.getValue());
    }
    
    public Item receiveItem() throws IOException{
    	int x = this.in.readInt();
    	int y = this.in.readInt();
    	int damage = this.in.readInt();
    	int type = this.in.readInt();
    	String desc = receiveString();
    	String name = receiveString();
    	int value = this.in.readInt();
    	//TODO: statt null Raum eintragen
		return new Item(null, damage, type, x, y, desc, name, value);
    }
}