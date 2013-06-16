package com.github.propra13.gruppeA3.Menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ConcurrentModificationException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.github.propra13.gruppeA3.Game;
import com.github.propra13.gruppeA3.GameWindow;
import com.github.propra13.gruppeA3.Keys;
import com.github.propra13.gruppeA3.Editor.Editor;
import com.github.propra13.gruppeA3.Entities.*;
import com.github.propra13.gruppeA3.Entities.Moveable.Direction;
import com.github.propra13.gruppeA3.Exceptions.InvalidRoomLinkException;
import com.github.propra13.gruppeA3.Exceptions.MapFormatException;
import com.github.propra13.gruppeA3.Map.FieldNotifier;
import com.github.propra13.gruppeA3.Map.Map;
import com.github.propra13.gruppeA3.Map.Position;
import com.github.propra13.gruppeA3.Map.Room;
import com.github.propra13.gruppeA3.XMLParser.SAXCrawlerReader;

@SuppressWarnings("serial")
public class MenuStart extends JPanel implements ActionListener {

    //definiert die Fenstergröße vom Spielfeld
    final static public int GameMinSizeX = 800; 
    final static public int GameMinSizeY = 600;

	public static Timer timer;
    public Player player;
    public static Room activeRoom;
    protected Toolkit tool;
    private Random randomgen;
    private int movecounter = 0;
    
    public static final int delay = 17;
    public Graphics2D g2d;
    
    // Menüelemente
    	private JButton buttonstart;
    	private JButton buttonbeenden;
    	private JButton buttoneditor;
    	private int buttonPosX;
    	private int buttonPosY;
       
	public static boolean ingame = false;
	public static boolean menu = false;
	public static boolean win = false;
	public static boolean editor = false;

	//score infoleiste
	public static int score;
	Font smallfont = new Font("Helvetica", Font.BOLD, 14);
	
    public MenuStart() {
    	// Lese Map
    	try {
    		Map.initialize("Map02", null);
    	} catch (InvalidRoomLinkException | IOException | MapFormatException e) {
    		e.printStackTrace();
    	}
    	
    	SAXCrawlerReader reader=new SAXCrawlerReader();
    	try {
    		reader.read("data/levels/level1.xml");
    		
    	} catch (Exception e) {
    			e.printStackTrace();
    	}
    			
    	setLayout(null);
    	setFocusable(true);
    	new GameWindow();  
    	setBackground(Color.BLACK);
        setSize(GameMinSizeX, GameMinSizeY);
        setDoubleBuffered(true);
        
        randomgen = new Random(System.currentTimeMillis());
        	
        activeRoom = Map.getMapRoom(0);
 		player = new Player(activeRoom);

 		
        addKeyListener(new Keys(player));
        
        menu = true; //wichtig für den ersten Spiel aufruf
        timer = new Timer(delay, this);
        timer.start();
        
        
        // Menü vorbereiten
     	buttonstart = new JButton("Spiel starten");
     	buttoneditor = new JButton("Karteneditor");
     	buttonbeenden = new JButton("Beenden");
     	initMenu();
    }
    
    private void initEditor() {
    	editor = true;
 		menu=false;
 		ingame = false;
 		win = false;
 		
 		setVisible(false);
 	    
		//zu bearbeitende Map wählen
 		String mapName = "Map02";
 		
		JFileChooser fc = new JFileChooser();
		fc.setCurrentDirectory(new java.io.File("./data/maps"));
		fc.setDialogTitle("Karte wählen");
		fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		fc.setAcceptAllFileFilterUsed(false);
		add(fc);
		
		// Falls Map ausgewählt wurde, wirf Editor an
		if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) { 
			mapName = fc.getSelectedFile().getName();
			System.out.println("Karte: "+mapName);
			Game.frame.add(new Editor(mapName));
 			Game.frame.validate();
		}
		
		// Falls keine Map ausgewählt wurde, mach Menü wieder an
		else {
			editor = false;
			menu = true;
			setVisible(true);
		}
    }
    
 // Startet Spiel
 public void initGame(){
	// Buttons ausblenden
		buttonstart.setVisible(false);
		buttoneditor.setVisible(false);
		buttonbeenden.setVisible(false);
		
	 // Spielablaufparameter setzen
		editor = false;
 		menu=false;
 		ingame = true;
 		win = false;
 		player.setLives(3);
 		
		


 	}
 
 	public static void paintRoom(Graphics2D g2d, Room room, JPanel panel) {
 		//Iteriert über Spalten
        for (int i = 0; i < room.roomFields.length; i++) {
            //Iteriert über Zeilen
            for (int j = 0; j < room.roomFields[i].length; j++) {
            	
            	int walltype = room.roomFields[i][j].type;
            	
            	switch(walltype)
            	{
            	case 1:
            		 g2d.drawImage(GameWindow.backgroundimg_1, i*32, j*32, panel);
            		 break;
            	
            	case 2:
            		g2d.drawImage(GameWindow.backgroundimg_2, i*32, j*32, panel);
            		break;
            	case 3:
            		g2d.drawImage(GameWindow.backgroundimg_3, i*32, j*32, panel);
            		break;
            	default:
            		g2d.drawImage(GameWindow.backgroundimg_4, i*32, j*32, panel);
            		break;
            	}
            }
        }        
 	}
    
    public void paint(Graphics g) { //Funktion zum Zeichnen von Grafiken
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        
        if(ingame)
        {
        	
        paintRoom(g2d, activeRoom, this);
        
        setBackground(Color.WHITE);
        
        Position pp = player.getPosition().getDrawPosition(player.getHitbox());

        // Malt Entities
        LinkedList<Entities> tempEntities = (LinkedList<Entities>) activeRoom.entities.clone();
        Iterator<Entities> iter = tempEntities.iterator();
        Entities testEntity;
        Position entityPos = new Position(0,0);
        
        // Durchläuft Liste
        Monster monster;
        Item item;
        Coin coin;
        NPC npc;
        while (iter.hasNext()) {
            testEntity = iter.next();
            if (testEntity instanceof Monster) {
            	monster=(Monster)testEntity;
            	entityPos.setPosition(monster.getPosition().x - (monster.getHitbox().width/2) , monster.getPosition().y - (monster.getHitbox().height/2));
                g2d.drawImage(GameWindow.monsterimg, entityPos.x, entityPos.y, this);
            }
            else if (testEntity instanceof Item) {
            	item = (Item)testEntity;
            	entityPos.setPosition(item.getPosition().x - (item.getHitbox().width/2), item.getPosition().y - (item.getHitbox().height/2));
            	switch (item.getType()){
            		case 4:
            			g2d.drawImage(GameWindow.sword, entityPos.x, entityPos.y, this);
            			break;
            		case 5:
            			g2d.drawImage(GameWindow.shield, entityPos.x, entityPos.y, this);
            			break;
            	}
            }
            else if (testEntity instanceof Coin){
            	coin = (Coin)testEntity;
            	entityPos.setPosition(coin.getPosition().x - (coin.getHitbox().width/2), coin.getPosition().y - (coin.getHitbox().height/2));
            	g2d.drawImage(GameWindow.coin, entityPos.x, entityPos.y, this);
            }
            else if (testEntity instanceof NPC){
            	npc = (NPC)testEntity;
            	entityPos.setPosition(npc.getPosition().x - (npc.getHitbox().width/2), npc.getPosition().y - (npc.getHitbox().height/2));
            	switch (npc.getType()){
            		case 1:
            			g2d.drawImage(GameWindow.npc3, entityPos.x, entityPos.y, this);
            			break;
            		case 2:
            			g2d.drawImage(GameWindow.npc2, entityPos.x, entityPos.y, this);
            			break;
            	}
            }
            else if (testEntity instanceof PlasmaBall) {
            	entityPos.setPosition(testEntity.getPosition().x - (testEntity.getHitbox().width/2) , testEntity.getPosition().y - (testEntity.getHitbox().height/2));
            	g2d.drawImage(GameWindow.plasma, entityPos.x, entityPos.y, this);
            	
            }
        }

        // Malt Spieler
        switch(player.getFaceDirection()){
        case UP: 
        	g2d.drawImage(GameWindow.playerimg_up1, pp.x, pp.y , this);
        	break;
        case DOWN:
        	g2d.drawImage(GameWindow.playerimg_down1, pp.x, pp.y , this);
        	break;
        case LEFT:
        	g2d.drawImage(GameWindow.playerimg_left1, pp.x, pp.y , this);
        	break;
        case RIGHT:
        	g2d.drawImage(GameWindow.playerimg_right1, pp.x, pp.y , this);
        	break;	
        default:
        	g2d.drawImage(GameWindow.playerimg_down1, pp.x, pp.y , this);
        	break;
        }
        
        Score(g2d);
        }
        else
        {
        	if(ingame == false && menu == true)
        	{
        		String msg;
        		msg = "Hauptmenü";
        		paintMessage(msg,g);
        	}
        	else if(ingame ==false && win == true && timer.isRunning())
        	{
        		setBackground(Color.BLACK);
        		String msg;
        		msg="Spiel Gewonnen";
        		paintMessage(msg,g);
        	}
        	else if(ingame ==false && win == false && timer.isRunning())
        	{
        		setBackground(Color.BLACK);
        		String msg;
        		msg="Game Over";
        		paintMessage(msg,g);
        	}
        
        	Toolkit.getDefaultToolkit().sync();
        	g.dispose();
        } 
    }
public void initMenu(){
	// Zeichne Menüelemente
	// Lege Standartpositionen für Buttons fest
	buttonPosX = GameMinSizeX/2-100;
	buttonPosY = GameMinSizeY/2-100;
	// bestimme Position und Größe
	buttonstart.setBounds(buttonPosX,buttonPosY,200,30);
	buttoneditor.setBounds(buttonPosX,buttonPosY+40,200,30);
	buttonbeenden.setBounds(buttonPosX,buttonPosY+80,200,30);
	// benenne Aktionen
	buttonbeenden.setActionCommand("beenden");
	buttoneditor.setActionCommand("editor");
	buttonstart.setActionCommand("starten");
	// ActionListener hinzufügen
	buttonstart.addActionListener(this);
	buttoneditor.addActionListener(this);
	buttonbeenden.addActionListener(this);
	// füge Buttons zum Panel hinzu
	add(buttonstart);
	add(buttoneditor);
	add(buttonbeenden);
}


//Score and set Leben
public void Score(Graphics2D g) {
	// int s nur so lange keine get funktion vorhanden ist für schwert und schild
	int s = 0;
	g.setFont(smallfont);
	g.setColor(Color.BLACK);
	g.drawImage(GameWindow.coin, 720, 543, this);
	g.drawString(Integer.toString(player.getMoney()), 750, 563);
	g.drawImage(GameWindow.heart, 5, 542, this);
	g.drawString("x",35,562);
	g.drawString(Integer.toString(player.getLives()), 50,562);
	//auslesen der Spieler leben über die getter player.getLives()
	g.drawString("Health: ", 70, 563);
	g.drawString(Integer.toString(player.getHealth()),125,563);
	g.drawImage(GameWindow.mana, 160, 543,this);
	g.drawString(Integer.toString(player.getMana()),185,563);
	g.drawImage(GameWindow.infosword, 220, 543,this);
	g.drawString(Integer.toString(s),245,563);
	g.drawImage(GameWindow.infoshield, 270, 543,this);
	g.drawString(Integer.toString(s),305,563);
	
}

public void paintMessage(String msg, Graphics g){
	// Mache Buttons wieder sichtbar
	buttonstart.setVisible(true);
	buttoneditor.setVisible(true);
	buttonbeenden.setVisible(true);
	Font small = new Font("Arial", Font.BOLD, 20);
	FontMetrics metr = this.getFontMetrics(small);

	g.setColor(Color.WHITE);
	g.setFont(small);
	g.drawString(msg, (GameMinSizeX - metr.stringWidth(msg))/2, 80);

}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		// Tasks für Timer in dieser if-condition eintragen
		if(ingame) {
			player.move();
			moveEnemies();
			activeRoom.removeEntities();
			executePlayerAttacks();
			tickCounters();

		}
		else if(ingame == false ) {
			//Spiel Start
			String action = e.getActionCommand();
			if("starten".equals(action))
				initGame();
			else if("editor".equals(action))
				/*
				 * Hier kannst du gerne deinen eigenen Editor 
				 * aufrufen, mit einem Jframe. Aber lass die Finger
				 * von der MenuStart mit der JPANEL TECHNIK hier hat 
				 * kein JFRAME ETWAS VERLOREN!!
				 * 
				 * Chill mal.
				 */
				initEditor();
			else if("beenden".equals(action)) 
				System.exit(0);	// Programm beenden
		}
		repaint();
	}

	private void moveEnemies(){
		Entities testent = null;	//durch alle Entitys der Liste iterieren
		Monster testmonster = null;
		Projectile testproj = null;
		LinkedList<Entities> tempEntities = player.getRoom().entities;
	    Iterator<Entities> iter = tempEntities.iterator();
		while(iter.hasNext()){
			try {
				testent = iter.next();
			} catch (ConcurrentModificationException e) {
				e.printStackTrace();
			}
			if(testent instanceof Monster) {
				testmonster = (Monster) testent;
				if(testmonster != null){
					if(movecounter == 0){
						generateDirection(testmonster);
					}
					testmonster.move();

				}
			}
			else if(testent instanceof Projectile) {
				testproj = (PlasmaBall) testent;
				testproj.tick();
			}
		}
	}
	
	private void generateDirection(Monster monster){
		int rndnumber = randomgen.nextInt();
		switch(rndnumber%6) {
			case 0:
				monster.setDirection(Direction.UP);
				break;
			case 1:
				monster.setDirection(Direction.DOWN);
				break;
			case 2:
				monster.setDirection(Direction.LEFT);
				break;
			case 3:
				monster.setDirection(Direction.RIGHT);
				
			default:
				monster.setDirection(Direction.NONE);
		}
	}
	
	
	private void executePlayerAttacks(){
		if((player.getAttackCount() == 0) && (player.getAttack() == true)){
			player.attack();
			player.setAttackCount(30);
			player.setAttack(false);
		}
		else if(player.getCastCount() == 0){
			switch(player.getCast()){
				case "SpeedBuff":
					player.setSpeedBuff();
					player.setCastCount(30);
					player.setCast("");
					break;
					
				case "AttackBuff":
					player.setAttackBuff();
					player.setCastCount(30);
					player.setCast("");
					break;
					
				case "firePlasma":
					player.firePlasma();
					player.setCastCount(30);
					player.setCast("");
					break;
					
				case "fireAOEPlasma":
					player.fireAOEPlasma();
					player.setCastCount(30);
					player.setCast("");
					break;
					
				default:
					break;
			}
		}
	}
	
	private void tickCounters(){
		if (player.getBuff() != null){
			player.getBuff().tick();
		}
		
		if(movecounter == 0){
			movecounter = 60;
		}
		
		else{
			movecounter--;
		}
		
		if(player.getAttackCount() > 0){
			player.setAttackCount(player.getAttackCount()-1);
		}
		
		if(player.getCastCount() > 0){
			player.setCastCount(player.getCastCount()-1);
		}
		
	}
	
	
}
