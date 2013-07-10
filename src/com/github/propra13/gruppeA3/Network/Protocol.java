package com.github.propra13.gruppeA3.Network;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import com.github.propra13.gruppeA3.Entities.Coin;
import com.github.propra13.gruppeA3.Entities.Hitbox;
import com.github.propra13.gruppeA3.Entities.Item;
import com.github.propra13.gruppeA3.Entities.Monster;
import com.github.propra13.gruppeA3.Entities.Player;
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
     * @param item Item im Raum oder beim Spieler
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
    
    /**
     * Item empfangen
     * @return Item im Raum oder beim Spieler
     * @throws IOException
     */
    public Item receiveItem() throws IOException{
    	int x = this.in.readInt();
    	int y = this.in.readInt();
    	int damage = this.in.readInt();
    	int type = this.in.readInt();
    	String desc = receiveString();
    	String name = receiveString();
    	int value = this.in.readInt();
    	//TODO: statt null Raum eintragen
		return new Item(damage, type, x, y, desc, name, value);
    }
    
    /**
     * Coin senden
     * @param coin
     * @throws IOException
     */
    public void sendCoin(Coin coin) throws IOException{
    	sendString("coin");
    	this.out.writeInt(coin.getType());
    	this.out.writeInt(coin.getValue());
    	sendPosition(coin.getPosition());
    	sendHitbox(coin.getHitbox());
    }

    /**
     * Coin empfangen
     * @return
     * @throws IOException
     */
    public Coin receiveCoin() throws IOException{
    	int type = this.in.readInt();
    	int value = this.in.readInt();
    	Position pos = receivePosition();
    	Hitbox hitbox = receiveHitbox();
    	Coin coin = new Coin(value, type, pos);
    	coin.setHitbox(hitbox);
		return coin;
    }
    
    /**
     * Monster senden
     * @param monster
     * @throws IOException
     */
    public void sendMonster(Monster monster) throws IOException{
    	sendString("monster");
    	this.out.writeInt(monster.getRoomID());
    	this.out.writeDouble(monster.getSpeed());
    	this.out.writeInt(monster.getPower());
    	this.out.writeInt(monster.getType());
    	this.out.writeInt(monster.getHealth());
    	sendPosition(monster.getPosition());
    	sendString(monster.getDesc());
    	this.out.writeInt(monster.getCoin().getValue());
    	this.out.writeInt(monster.getCoin().getType());
    	this.out.writeInt(monster.getArmour());
    	this.out.writeBoolean(monster.isBoss());
    }
    
    /**
     * Monster empfangen
     * @return
     * @throws IOException
     */
    public Monster receiveMonster() throws IOException{
    	int roomID = this.in.readInt();
    	Double speed = this.in.readDouble();
    	int power = this.in.readInt();
    	int type = this.in.readInt();
    	int life = this.in.readInt();
    	Position pos = receivePosition();
    	String desc = receiveString();
    	int coinValue = this.in.readInt();
    	int coinType = this.in.readInt();
    	int armour = this.in.readInt();
    	boolean isBoss = this.in.readBoolean();
    	return new Monster(roomID, speed, power, type, life, pos.x, pos.y, desc, coinValue, coinType, armour, isBoss);
    }
    
    /**
     * Spieler senden
     * @param player
     * @throws IOException
     */
    public void sendPlayer(Player player) throws IOException{
    	sendString("player");
    	this.out.writeInt(player.getRoomID());
    	this.out.writeInt(player.getPlayerID());
    	this.out.writeInt(player.getLives());
    	this.out.writeInt(player.getHealth());
    	this.out.writeDouble(player.getSpeed());
    	this.out.writeInt(player.getMana());
    	this.out.writeInt(player.getDirection().ordinal());
    	this.out.writeInt(player.getFaceDirection().ordinal());
    	sendPosition(player.getPosition());
    	this.out.writeInt(player.getArmour());
    	this.out.writeInt(player.getAttack());
    	this.out.writeInt(player.getAttackCount());
    }
    
    /**
     * Spieler empfangen
     * @return
     * @throws IOException
     */
    public Player receivePlayer() throws IOException{
    	int roomID = this.in.readInt();
    	int playerID = this.in.readInt();
    	int lives = this.in.readInt();
    	int health = this.in.readInt();
    	double speed = this.in.readDouble(); 
    	int mana = this.in.readInt();
    	int direct = this.in.readInt();
    	int face = this.in.readInt();
    	Position pos = receivePosition();
    	int armour = this.in.readInt();
    	int attack = this.in.readInt();
    	int attackCount = this.in.readInt();

		return new Player(roomID, playerID, lives, health, speed, mana, 
						  direct, face, pos, armour, attack, attackCount);
    }
    
    /**
     * PlayerID senden
     * @param playerID
     * @throws IOException
     */
    public void sendPlayerID(int playerID) throws IOException{
    	sendString("playerID");
    	this.out.writeInt(playerID);
    }
    
    /**
     * PlayerID empfangen
     * @return
     * @throws IOException
     */
    public int receivePlayerID() throws IOException{
    	return this.in.readInt();
    }
}