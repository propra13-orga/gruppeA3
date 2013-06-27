package com.github.propra13.gruppeA3.Menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
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
    public static boolean talk = false;
    
    public static final int delay = 17;
    public Graphics2D g2d;
    
    /**
     *  Menüelemente
     *  	buttonNewGame:
     *  	buttonNextMap:
     *  	buttonNetwork: Öffnet die Netzwerk Modi
     *  	buttonBeenden:
     *  	buttonEditor:
     *  	buttonBack: Sorgt dafür, dass man eine Einstellung zurück geht
     *  	buttonDeathmatch: Öffnet das Deathmatch menü
     *  	buttonCoop: Öffnet das Co-Op Menü
     *  	buttonJoin: Betritt ein offenes Netzwerk Spiel (Deathmatch, Co-Op)
     *  	buttonCreate: öffnet ein neues Netzwerk Spiel (Deathmatch, Co-Op)
     */
	private JButton buttonNewGame;
	private JButton buttonNextMap;
	private JButton buttonNetwork;
	private JButton buttonBeenden;
	private JButton buttonEditor;
	private JButton buttonBack;
	private JButton buttonDeathmatch;
	private JButton buttonCoop;
	private JButton buttonJoin;
	private JButton buttonCreate;
	private int buttonPosX;
	private int buttonPosY;
    
	/**
	 *  Status in denen sich das Spiel/Menü befindet
	 *  	GameStatus:
	 *  	NetworkStatus: Aufzählungstyp für die Netzwerk Modi
	 *  	gameStatus:
	 *  	netstat: Kann einen Wert von NetworkStatus annehmen, wird für den Back Button benötigt 
	 *  			 und um zu entscheiden, welcher Spielmodus gerade geöffnet wurde
	 *
	 */
    public static enum GameStatus {INGAME, MAINMENU, EDITOR, GAMEWON, GAMEOVER, MAPWON, NETWORK}
    public static enum NetworkStatus {NONE, DEATH, COOP}
    private static GameStatus gameStatus;
    private static NetworkStatus netstat = NetworkStatus.NONE;
	
    // Zähler, welche Map die jeweils nächste ist
	private static int nextMap = 1;


	//score infoleiste
	public static int score;
	Font smallfont = new Font("Helvetica", Font.BOLD, 14);
	
	/**
	 * Netzwerkinformationen
	 * 		name: Name des Spielers
	 * 		host: Host/IP des Creaters
	 * 		port: Der Port auf dem gelauscht wird
	 */
	private String name, host;
	private int port;
	
	/**
	 * 
	 */
    public MenuStart() {
    			
    	setLayout(null);
    	setFocusable(true);
    	new GameWindow();  
    	setBackground(Color.BLACK);
        setSize(GameMinSizeX, GameMinSizeY);
        setDoubleBuffered(true);
        
        setGameStatus(GameStatus.MAINMENU); //wichtig für den ersten Spiel aufruf
        timer = new Timer(delay, this);
        timer.start();
        
        
        // Menü vorbereiten
     	buttonNewGame = new JButton("Neues Spiel");
     	buttonNextMap = new JButton("Nächste Karte");
     	buttonNetwork = new JButton("Multiplayer");
     	buttonEditor = new JButton("Karteneditor");
     	buttonBack = new JButton("Zurück");
     	buttonBeenden = new JButton("Beenden");
     	buttonDeathmatch = new JButton("Deathmatch");
     	buttonCoop = new JButton("Co-Op");
     	buttonCreate = new JButton("Spiel erzeugen");
     	buttonJoin = new JButton("Spiel beitreten");
     	
     	// benenne Aktionen
    	buttonNewGame.setActionCommand("newgame");
    	buttonNextMap.setActionCommand("nextmap");
    	buttonNetwork.setActionCommand("network");
    	buttonEditor.setActionCommand("editor");
    	buttonBack.setActionCommand("back");
    	buttonBeenden.setActionCommand("exit");
    	buttonDeathmatch.setActionCommand("deathmatch");
    	buttonCoop.setActionCommand("coop");
    	buttonCreate.setActionCommand("create");
    	buttonJoin.setActionCommand("join");
    	
    	// ActionListener hinzufügen
    	buttonNewGame.addActionListener(this);
    	buttonNextMap.addActionListener(this);
    	buttonNetwork.addActionListener(this);
    	buttonEditor.addActionListener(this);
    	buttonBack.addActionListener(this);
    	buttonBeenden.addActionListener(this);
    	buttonDeathmatch.addActionListener(this);
    	buttonCoop.addActionListener(this);
    	buttonCreate.addActionListener(this);
    	buttonJoin.addActionListener(this);
    	
    	// füge Buttons zum Panel hinzu
    	add(buttonNewGame);
    	add(buttonNextMap);
    	add(buttonNetwork);
    	add(buttonEditor);
    	add(buttonBack);
    	add(buttonBeenden);
    	add(buttonDeathmatch);
    	add(buttonCoop);
    	add(buttonCreate);
    	add(buttonJoin);
     	initMenu();
    }
    
    /**
     * Zeigt Dateiauswahldialog für Karten und startet ggf. den Editor
     */
    private void initEditor() {
    	setGameStatus(GameStatus.EDITOR);
 		
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
			setGameStatus(GameStatus.MAINMENU);
			setVisible(true);
		}
    }
    
    // Startet Spiel
    public void initGame(String mapName, String xmlName){

    	// Stellt Map auf
	 	try {
	 		Map.initialize(mapName);
	 	} catch (InvalidRoomLinkException | IOException | MapFormatException e) {
	 		e.printStackTrace();
	 	}
	 	
	 	SAXCrawlerReader reader=new SAXCrawlerReader();
	 	try {
	 		reader.read("data/levels/"+xmlName+".xml");
	 		
	 	} catch (Exception e) {
	 			e.printStackTrace();
	 	}
	 	
	 	randomgen = new Random(System.currentTimeMillis());
		activeRoom = Map.getRoom(0);
		if(nextMap == 1)
			player = new Player(activeRoom);
		else
			player.initialize();
		addKeyListener(new Keys(player));
		
		
		// Menü-Buttons ausblenden, Status ändern
		buttonNewGame.setVisible(false);
		buttonNextMap.setVisible(false);
		buttonNetwork.setVisible(false);
		buttonEditor.setVisible(false);
		buttonBeenden.setVisible(false);
		buttonDeathmatch.setVisible(false);
		buttonCoop.setVisible(false);
		buttonCreate.setVisible(false);
		buttonJoin.setVisible(false);
			
		setGameStatus(GameStatus.INGAME);
 	}
 
 	public static void paintRoom(Graphics2D g2d, Room room, JPanel panel) {
 		//Iteriert über Spalten
        for (int i = 0; i < room.roomFields.length; i++) {
            //Iteriert über Zeilen
            for (int j = 0; j < room.roomFields[i].length; j++) {
            	
            	int walltype = room.roomFields[i][j].type;
            	
            	switch(walltype)
            	{
            	//Boden
            	case 1:
            		 g2d.drawImage(GameWindow.backgroundimg_1, i*32, j*32, panel);
            		 break;
            	//Wand
            	case 2:
            		g2d.drawImage(GameWindow.backgroundimg_2, i*32, j*32, panel);
            		break;
            	//Wasser
            	case 3:
            		g2d.drawImage(GameWindow.backgroundimg_3, i*32, j*32, panel);
            		break;
            	//Link
            	case 5:
            		g2d.drawImage(GameWindow.backgroundimg_1, i*32, j*32, panel);
            		break;
            	// Checkpoint-Link
            	case 6:
            		g2d.drawImage(GameWindow.backgroundimg_3, i*32, j*32, panel);
            		break;
            	//Trigger
            	case 7:
            		g2d.drawImage(GameWindow.backgroundimg_1, i*32, j*32, panel);
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
        
        if(getGameStatus() == GameStatus.INGAME)
        {
        	
        paintRoom(g2d, activeRoom, this);
        
        setBackground(Color.GRAY);
        
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
            	//zeichnet Monster-Typen mit Blickrichtung 
               // g2d.drawImage(GameWindow.monsterimg_1left, entityPos.x, entityPos.y, this);
            	switch(monster.getType()){
            		case 1:{           	
            			switch(monster.getFaceDirection()){
            			case UP: 
            				g2d.drawImage(GameWindow.monsterimg_1up, entityPos.x, entityPos.y , this);
            				break;
            			case DOWN:
            				g2d.drawImage(GameWindow.monsterimg_1down, entityPos.x, entityPos.y , this);
            				break;
            			case LEFT:
            				g2d.drawImage(GameWindow.monsterimg_1left, entityPos.x, entityPos.y , this);
            				break;
            			case RIGHT:
            				g2d.drawImage(GameWindow.monsterimg_1right, entityPos.x, entityPos.y , this);
            				break;	
            			default:
            				g2d.drawImage(GameWindow.monsterimg_1down, entityPos.x, entityPos.y , this);
            				break;
            			}
            			break;
            		}
            			
            		case 2:
            		{           	
            			switch(monster.getFaceDirection()){
            			case UP: 
            				g2d.drawImage(GameWindow.monsterimg_2up, entityPos.x, entityPos.y , this);
            				break;
            			case DOWN:
            				g2d.drawImage(GameWindow.monsterimg_2down, entityPos.x, entityPos.y , this);
            				break;
            			case LEFT:
            				g2d.drawImage(GameWindow.monsterimg_2left, entityPos.x, entityPos.y , this);
            				break;
            			case RIGHT:
            				g2d.drawImage(GameWindow.monsterimg_2right, entityPos.x, entityPos.y , this);
            				break;	
            			default:
            				g2d.drawImage(GameWindow.monsterimg_2down, entityPos.x, entityPos.y , this);
            				break;
            			}
            			break;
            		}
            		case 3:
            		{           	
            			switch(monster.getFaceDirection()){
            			case UP: 
            				g2d.drawImage(GameWindow.monsterimg_3up, entityPos.x, entityPos.y , this);
            				break;
            			case DOWN:
            				g2d.drawImage(GameWindow.monsterimg_3down, entityPos.x, entityPos.y , this);
            				break;
            			case LEFT:
            				g2d.drawImage(GameWindow.monsterimg_3left, entityPos.x, entityPos.y , this);
            				break;
            			case RIGHT:
            				g2d.drawImage(GameWindow.monsterimg_3right, entityPos.x, entityPos.y , this);
            				break;	
            			default:
            				g2d.drawImage(GameWindow.monsterimg_3down, entityPos.x, entityPos.y , this);
            				break;
            			}
            			break;
            		}
            		case 4:{           	
            			switch(monster.getFaceDirection()){
            			case UP: 
            				g2d.drawImage(GameWindow.monsterimg_4up, entityPos.x, entityPos.y , this);
            				break;
            			case DOWN:
            				g2d.drawImage(GameWindow.monsterimg_4down, entityPos.x, entityPos.y , this);
            				break;
            			case LEFT:
            				g2d.drawImage(GameWindow.monsterimg_4left, entityPos.x, entityPos.y , this);
            				break;
            			case RIGHT:
            				g2d.drawImage(GameWindow.monsterimg_4right, entityPos.x, entityPos.y , this);
            				break;	
            			default:
            				g2d.drawImage(GameWindow.monsterimg_4down, entityPos.x, entityPos.y , this);
            				break;
            			}
            			break;
            		}
            		case 5:{
            	          	
                			switch(monster.getFaceDirection()){
                			case UP: 
                				g2d.drawImage(GameWindow.bossimg_1up, entityPos.x, entityPos.y , this);
                				break;
                			case DOWN:
                				g2d.drawImage(GameWindow.bossimg_1down, entityPos.x, entityPos.y , this);
                				break;
                			case LEFT:
                				g2d.drawImage(GameWindow.bossimg_1left, entityPos.x, entityPos.y , this);
                				break;
                			case RIGHT:
                				g2d.drawImage(GameWindow.bossimg_1right, entityPos.x, entityPos.y , this);
                				break;	
                			default:
                				g2d.drawImage(GameWindow.bossimg_1down, entityPos.x, entityPos.y , this);
            			
            			break;
                			}
            		}
            	}
            }
                	
            else if (testEntity instanceof Item) {
            	item = (Item)testEntity;
            	entityPos.setPosition(item.getPosition().x - (item.getHitbox().width/2), item.getPosition().y - (item.getHitbox().height/2));
            	switch (item.getType()){
            		case 1:
        				g2d.drawImage(GameWindow.lifePosion, entityPos.x, entityPos.y, this);
        				break;
            		case 2:
        				g2d.drawImage(GameWindow.deadlyPosion, entityPos.x, entityPos.y, this);
        				break;
            		case 3:
            			g2d.drawImage(GameWindow.manaPosion, entityPos.x, entityPos.y, this);
            			break;
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
        	//Macht Menü sichtbar, positioniert Buttons je nach Spielzustand
        	initMenu();
        	// Wenn der Multiplayer Button gedrückt wurde, dann soll
        	if (getGameStatus() == GameStatus.NETWORK){
        		// Co-Op erscheinen, wenn anschließend der Co-Op Button gedrückt wurde
        		if(netstat == NetworkStatus.COOP)
        			paintMessage("Co-Op", g);
        		// Deathmatch erscheinen, wenn anschließend der Deatchmatch Button gedrückt wurde
        		else if(netstat == NetworkStatus.DEATH)
        			paintMessage("Deathmatch", g);
        		// Multiplayer erscheinen, da wir im Multiplayer Modus sind
        		else
        			paintMessage("Multiplayer", g);
        	}
        	//Hauptmenü
        	else if(getGameStatus() == GameStatus.MAINMENU)
        	{
        		paintMessage("Hauptmenü",g);
        	}
        	// Spiel gewonnen
        	else if(getGameStatus() == GameStatus.GAMEWON && timer.isRunning())
        	{
        		setBackground(Color.BLACK);
        		paintMessage("Spiel gewonnen!",g);
        		
        	}
        	// Karte gewonnen, der Spieler kann in die nächste eintreten oder von vorn beginnen
        	else if(getGameStatus() == GameStatus.MAPWON && timer.isRunning()) {
        		setBackground(Color.BLACK);
        		paintMessage("Karte gemeistert!",g);
        	}
        	// Game Over, Spieler gestorben
        	else if(getGameStatus() == GameStatus.GAMEOVER && timer.isRunning())
        	{
        		setBackground(Color.BLACK);
        		paintMessage("Game Over",g);
        	}
        
        	Toolkit.getDefaultToolkit().sync();
        	g.dispose();
        } 
    }
    
public void initMenu(){
	// Zeichne Menüelemente
	// Lege Standardpositionen für Buttons fest
	buttonPosX = GameMinSizeX/2-100;
	buttonPosY = GameMinSizeY/2-100;
	
	// bestimme Position und Größe
	
	//Falls schon eine Karte gemeistert wurde, Nächste-Karte-Button anzeigen
	if(nextMap > 1 && nextMap < 4) {
		buttonNextMap.setVisible(true);
		buttonNextMap.setBounds(buttonPosX,buttonPosY,    200,30);
		buttonNewGame.setBounds(buttonPosX,buttonPosY+40, 200,30);
		buttonEditor.setBounds(buttonPosX, buttonPosY+80, 200,30);
		buttonBeenden.setBounds(buttonPosX,buttonPosY+120,200,30);
	}
	// Ansonsten normales Hauptmenü anzeigen
	else {
		buttonNextMap.setVisible(false);
		buttonNewGame.setBounds(buttonPosX,buttonPosY,   200,30);
		buttonNetwork.setBounds(buttonPosX, buttonPosY+40,200,30);
		buttonEditor.setBounds(buttonPosX, buttonPosY+80,200,30);
		buttonBeenden.setBounds(buttonPosX,buttonPosY+120,200,30);
	}
}


//Score and set Leben
public void Score(Graphics2D g) {
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
	g.drawString(Integer.toString(player.getPower()),245,563);
	g.drawImage(GameWindow.infoshield, 270, 543,this);
	g.drawString(Integer.toString(player.getArmour()),305,563);
	
}

	public void paintMessage(String msg, Graphics g){
		// Mache Buttons wieder sichtbar
		Font small = new Font("Arial", Font.BOLD, 20);
		FontMetrics metr = this.getFontMetrics(small);

		g.setColor(Color.WHITE);
		g.setFont(small);
		g.drawString(msg, (GameMinSizeX - metr.stringWidth(msg))/2, 80);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		// Tasks für Timer in dieser if-condition eintragen
		if(getGameStatus() == GameStatus.INGAME) {
			player.move();
			executeEnemyActions();
			activeRoom.removeEntities();
			executeTalk();
			executePlayerAttacks();
			tickCounters();

		}
		else {
			//Spiel Start
			String action = e.getActionCommand();
			if("newgame".equals(action)) {
				nextMap = 1;
				initGame("Story01", "level1");
			}
			else if("nextmap".equals(action)) {
				if (nextMap < 10)
					initGame("Story0"+nextMap, "level"+nextMap);
				else
					initGame("Story"+nextMap, "level"+nextMap);
			}
			else if("network".equals(action)){
				initNetwork();
			}
			else if("coop".equals(action)){
				netstat = NetworkStatus.COOP;
				networkMenu();
			}
			else if ("deathmatch".equals(action)){
				netstat = NetworkStatus.DEATH;
				networkMenu();
			}
			else if("create".equals(action)){
				// TODO
			}
			else if("join".equals(action)){
				// TODO
			}
			else if("back".equals(action)){
				backMenu();
			}
			else if("editor".equals(action))
				initEditor();
			else if("exit".equals(action)) 
				System.exit(0);	// Programm beenden
		}
		repaint();
	}
	
	public void initNetwork(){
		setGameStatus(GameStatus.NETWORK);
		buttonNewGame.setVisible(false);
		buttonNextMap.setVisible(false);
		buttonNetwork.setVisible(false);
		buttonEditor.setVisible(false);
		buttonBeenden.setVisible(false);
		buttonBack.setVisible(true);
		buttonDeathmatch.setVisible(true);
		buttonCoop.setVisible(true);
		buttonDeathmatch.setBounds(buttonPosX,buttonPosY,   200,30);
		buttonCoop.setBounds(buttonPosX,buttonPosY+40,	 200,30);
		buttonBack.setBounds(buttonPosX,buttonPosY+80,	 200,30);
	}
	
	public void networkMenu(){
		buttonDeathmatch.setVisible(false);
		buttonCoop.setVisible(false);
		buttonCreate.setBounds(buttonPosX,buttonPosY,   200,30);
		buttonJoin.setBounds(buttonPosX,buttonPosY+40,	 200,30);
		buttonCreate.setVisible(true);
		buttonJoin.setVisible(true);
	}

	public void backMenu(){
		if((netstat == NetworkStatus.COOP) || (netstat == NetworkStatus.DEATH)){
			netstat = NetworkStatus.NONE;
			buttonBack.setVisible(true);
			buttonDeathmatch.setVisible(true);
			buttonCoop.setVisible(true);
			buttonCreate.setVisible(false);
			buttonJoin.setVisible(false);
		} else {
			setGameStatus(GameStatus.MAINMENU);
			buttonNewGame.setVisible(true);
			buttonNextMap.setVisible(true);
			buttonNetwork.setVisible(true);
			buttonEditor.setVisible(true);
			buttonBeenden.setVisible(true);
			buttonBack.setVisible(false);
			buttonDeathmatch.setVisible(false);
			buttonCoop.setVisible(false);
		}
	}
	
	
	
	private void executeEnemyActions(){
		Entities testent = null;	//durch alle Entitys der Liste iterieren
		Monster testmonster = null;
		Projectile testproj = null;
		LinkedList<Entities> tempEntities = (LinkedList<Entities>) player.getRoom().entities.clone();
	    Iterator<Entities> iter = tempEntities.iterator();
		while(iter.hasNext()){
			testent = iter.next();
			if(testent instanceof Monster) {
				testmonster = (Monster) testent;
				if(testmonster != null){
					if(movecounter == 0){
						generateDirection(testmonster);
					}
					testmonster.attack();
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
				monster.setFaceDirection(Direction.UP);
				break;
			case 1:
				monster.setDirection(Direction.DOWN);
				monster.setFaceDirection(Direction.DOWN);
				break;
			case 2:
				monster.setDirection(Direction.LEFT);
				monster.setFaceDirection(Direction.LEFT);
				break;
			case 3:
				monster.setDirection(Direction.RIGHT);
				monster.setFaceDirection(Direction.RIGHT);
				
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
		
		Monster monster = null;
		Entities testent = null;	//durch alle Entitys der Liste iterieren
		@SuppressWarnings("unchecked")
		LinkedList<Entities> tempEntities = (LinkedList<Entities>) player.getRoom().entities.clone();
	    Iterator<Entities> iter = tempEntities.iterator();
	    while(iter.hasNext()){
	    	testent = iter.next();
	    	if(testent instanceof Monster){
	    		monster = (Monster) testent;
	    		if(monster != null){
	    			if(monster.getAttackCount() > 0){
	    				monster.setAttackCount(monster.getAttackCount() - 1);
	    			}
	    		}
	    	}
	    }
		
	}
	
	/** Wird von Player aufgerufen, wenn der Spieler stirbt
	 */
	public static void death() {
		setGameStatus(GameStatus.GAMEOVER);
		nextMap = 1; // Reset des Spiels
	}
	
	/** Wird von Player aufgerufen, wenn der Spieler das Ziel der Map betritt
	 * Anhand von nextMap wird die nächste Karte ausgesucht
	 */
	public static void winMap() {
		nextMap++; 
		if (nextMap > 3) //3: Anzahl der Maps, die wir derzeit haben
			setGameStatus(GameStatus.GAMEWON);
		else
			setGameStatus(GameStatus.MAPWON);
	}
	
	private void executeTalk(){
		if(talk == true){
			Position temp = new Position(player.getPosition().x,player.getPosition().y);
			switch(player.getFaceDirection()){
			case UP:
				temp.setPosition(temp.x , temp.y -6);
				break;
			case DOWN:
				temp.setPosition(temp.x , temp.y +6);
				break;
			case LEFT:
				temp.setPosition(temp.x - 6 , temp.y);
				break;
			case RIGHT:
				temp.setPosition(temp.x + 6 , temp.y);
				break;
			default:
				break;
			}
			int xdelta;
			int ydelta;
			Entities testent = null;	//durch alle Entitys der Liste iterieren
			LinkedList<Entities> tempEntities = (LinkedList<Entities>) player.getRoom().entities.clone();
		    Iterator<Entities> iter = tempEntities.iterator();
		    NPC npc = null;
			while(iter.hasNext()){
				testent = iter.next();
				if(testent instanceof NPC){	
					xdelta = temp.x - testent.getPosition().x; //x-Abstand der Mittelpunkte bestimmen
					if(xdelta < 0)
						xdelta = xdelta * (-1);
					ydelta = temp.y - testent.getPosition().y; //y-Abstand der Mittelpunkte bestimmen
					if(ydelta < 0)
						ydelta = ydelta * (-1);
					if(Math.sqrt(xdelta*xdelta + ydelta*ydelta) < 50){	//Wenn wurzel(x^2 + y^2) < 50 ist, auf hitboxkollision prüfen
						if(player.hitboxCheck(temp, testent) == false){
							 if(testent instanceof NPC){
								npc = (NPC)testent;
								switch(npc.getType()){
									case 1:
										JOptionPane.showMessageDialog(null, npc.getText(), npc.getName(), JOptionPane.PLAIN_MESSAGE);
										talk = false;
										break;
									case 2:
										if(npc.getItems().isEmpty()){
											JOptionPane.showMessageDialog(null, "Ich besitze keine Ware mehr", npc.getName(), JOptionPane.PLAIN_MESSAGE);
										} else{
											JOptionPane.showMessageDialog(null, npc.getText(), npc.getName(), JOptionPane.PLAIN_MESSAGE);
											new Shop(this.player, npc);
										}
										talk = false;
										break;
								}
							}
						}
					}
				}
			}
		}
	}

	public static GameStatus getGameStatus() {
		return gameStatus;
	}

	public static void setGameStatus(GameStatus gameStatus) {
		MenuStart.gameStatus = gameStatus;
	}
	
	/**
	 * Setzt den Port
	 * @param port
	 */
	public void setPort(int port)
	{
		this.port = port;
	}
	
	/**
	 * Setzt den Namen
	 * @param name
	 */
	public void setName(String name)
	{
		this.name = name;
	}
	
	/**
	 * Setzt die Server IP
	 * @param ip
	 */
	public void setHost(String ip)
	{
		this.host = ip;
	}
}
