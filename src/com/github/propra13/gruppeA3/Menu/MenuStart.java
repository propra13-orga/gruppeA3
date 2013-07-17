package com.github.propra13.gruppeA3.Menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.Timer;

import com.github.propra13.gruppeA3.Game;
import com.github.propra13.gruppeA3.GameWindow;
import com.github.propra13.gruppeA3.Keys;
import com.github.propra13.gruppeA3.Music;
import com.github.propra13.gruppeA3.Editor.Editor;
import com.github.propra13.gruppeA3.Entities.*;
import com.github.propra13.gruppeA3.Entities.Moveable.Direction;
import com.github.propra13.gruppeA3.Exceptions.InvalidRoomLinkException;
import com.github.propra13.gruppeA3.Exceptions.MapFormatException;
import com.github.propra13.gruppeA3.Map.Map;
import com.github.propra13.gruppeA3.Map.MapHeader;
import com.github.propra13.gruppeA3.Map.Position;
import com.github.propra13.gruppeA3.Map.Room;
import com.github.propra13.gruppeA3.Network.Client;
import com.github.propra13.gruppeA3.Network.Server;

/**
 * Klasse f�r Menu- und Spielablauf
 *
 */

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
    private boolean questFinished = false;
    
    public static final int delay = 17;
    public Graphics2D g2d;
    
    /**
     *  Menüelemente
     *  	buttonNewGame: Startet ein neues Spiel
     *  	buttonNextMap: Startet die nächste Karte
     *  	buttonNetwork: Öffnet die Netzwerk Modi
     *  	buttonBeenden: Beendet das Spiel
     *  	buttonHelp: Öffnet die Hilfe
     *  	buttonHelpOk: Ok-Button im Hilfe-Fenster
     *  	buttonEditor: Startet den Map-Editor
     *  	buttonBack: Sorgt dafür, dass man eine Einstellung zurück geht
     *  	buttonDeathmatch: Öffnet das Deathmatch menü
     *  	buttonCoop: Öffnet das Co-Op Menü
     *  	buttonJoin: Betritt ein offenes Netzwerk Spiel (Deathmatch, Co-Op)
     *  	buttonCreate: Öffnet ein neues Netzwerk Spiel (Deathmatch, Co-Op)
     *		buttonOptions: Öffnet ein Fenster mit Optionen für das Netzwerkspiel
     */
	private JButton buttonNewGame;
	private JButton buttonNextMap;
	private JButton buttonSingleplayer;
	private JButton buttonNetwork;
	private JButton buttonHelp;
	private JButton buttonHelpOk;
	private JButton buttonBeenden;
	private JButton buttonEditor;
	private JButton buttonBack;
	private JButton buttonDeathmatch;
	private JButton buttonCoop;
	private JButton buttonJoin;
	private JButton buttonCreate;
	private JButton buttonEinstellungen; //Netzwerkeinstellungen
	private JButton buttonOptionen; //Hauptmenüeinstellungen
	private int buttonPosX;
	private int buttonPosY;
	
	private JDialog helpDialog;
	
	private JLabel background;
    
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
    public static enum NetworkStatus {NONE, DEATHMATCH, COOP}
    private static GameStatus gameStatus;
    private static NetworkStatus netstat = NetworkStatus.NONE;
    
    /**Header der Map, die gerade gespielt wird oder zuletzt gespielt wurde*/
    MapHeader lastMap;


	//score infoleiste
	public static int score;
	Font smallfont = new Font("Helvetica", Font.BOLD, 14);
	
	/**
	 * Netzwerkinformationen
	 * 		name: Name des Spielers
	 * 		host: Host/IP des Creaters
	 * 		port: Der Port auf dem gelauscht wird
	 */
	private String name="Player1", host="localhost";
	private int port=1337;
	
	public static Music music=null;
	public static Client client;
	
	/**
	 * Konstruktor der Klasse MenuStart
	 * Initalisiert alle Komponenten, die fuer das Spiel notwendig sind
	 * Setzt das Layout des Panels fest
	 */
    public MenuStart() {
    			
    	setLayout(null);
    	setFocusable(true);
    	new GameWindow();  
    	setBackground(Color.BLACK);
        setSize(GameMinSizeX, GameMinSizeY);
        setDoubleBuffered(true);
        
        setGameStatus(GameStatus.MAINMENU); 

        /**
         * Initalisiert und startet den Timer mit timer.start
         */
        timer = new Timer(delay, this);
        timer.start();
        
        
        // Menü vorbereiten
        background = new JLabel(new ImageIcon(GameWindow.background));
        
     	buttonNewGame = new JButton("Neue Kampagne");
     	buttonNextMap = new JButton("Nächste Karte");
     	buttonSingleplayer = new JButton("Einzelspiel");
     	buttonNetwork = new JButton("Multiplayer");
     	buttonEditor = new JButton("Karteneditor");
     	buttonHelp = new JButton("Hilfe");
     	buttonHelpOk = new JButton("Ok");
     	buttonBack = new JButton("Zurück");
     	buttonBeenden = new JButton("Beenden");
     	buttonDeathmatch = new JButton("Deathmatch");
     	buttonCoop = new JButton("Co-Op");
     	buttonCreate = new JButton("Spiel erzeugen");
     	buttonJoin = new JButton("Spiel beitreten");
     	buttonEinstellungen = new JButton("Einstellungen");
     	buttonOptionen = new JButton("Optionen");
     	
     	// benenne Aktionen
    	buttonNewGame.setActionCommand("newgame");
    	buttonNextMap.setActionCommand("nextmap");
    	buttonSingleplayer.setActionCommand("singleplayer");
    	buttonNetwork.setActionCommand("network");
    	buttonEditor.setActionCommand("editor");
    	buttonBack.setActionCommand("back");
    	buttonBeenden.setActionCommand("exit");
    	buttonDeathmatch.setActionCommand("deathmatch");
    	buttonCoop.setActionCommand("coop");
    	buttonCreate.setActionCommand("create");
    	buttonJoin.setActionCommand("join");
    	buttonEinstellungen.setActionCommand("options");
    	
    	// ActionListener hinzufügen
    	buttonNewGame.addActionListener(this);
    	buttonNextMap.addActionListener(this);
    	buttonSingleplayer.addActionListener(this);
    	buttonNetwork.addActionListener(this);
    	buttonHelp.addActionListener(this);
    	buttonEditor.addActionListener(this);
    	buttonBack.addActionListener(this);
    	buttonBeenden.addActionListener(this);
    	buttonDeathmatch.addActionListener(this);
    	buttonCoop.addActionListener(this);
    	buttonCreate.addActionListener(this);
    	buttonJoin.addActionListener(this);
    	buttonEinstellungen.addActionListener(this);
    	buttonOptionen.addActionListener(this);
    	
    	// füge Buttons zum Panel hinzu
    	add(buttonNewGame);
    	add(buttonNextMap);
    	add(buttonSingleplayer);
    	add(buttonNetwork);
    	add(buttonHelp);
    	add(buttonEditor);
    	add(buttonBack);
    	add(buttonBeenden);
    	add(buttonDeathmatch);
    	add(buttonCoop);
    	add(buttonCreate);
    	add(buttonJoin);
    	add(buttonEinstellungen);
    	add(buttonOptionen);
    	//add(buttonEinstellungen);
     	initMenu();
     	music = new Music();
    }
    
    /**
     * Aktiven Raum setzen
     * @param room
     */
    public static void setActiveRoom(Room room){
    	activeRoom = room;
    }
    
    /**
     * Random setzen
     * @param rand
     */
    public void setRandom(Random rand){
    	this.randomgen = rand;
    }
    
    /**
     * Zeigt Dateiauswahldialog für Karten und startet ggf. den Editor
     */
    private void initEditor() {
    	setGameStatus(GameStatus.EDITOR);
 		
 		setVisible(false);
		Game.frame.add(new Editor());
		Game.frame.validate();
    }
    
    private void showOptions() {
    	setVisible(false);
		Game.frame.add(new MainOptions());
		Game.frame.validate();
    }
    
    public void setButtonVisible(boolean bground, boolean bNGame, boolean bNMap, boolean bSplayer, 
    							boolean bNetwork, boolean bOptionen, boolean bHelp, boolean bEditor, 
    							boolean bBeenden, boolean bDeathmatch, boolean bCoop, boolean bCreate,
    							boolean bJoin, boolean bNWOptions){
		background.setVisible(bground);
		buttonNewGame.setVisible(bNGame);
		buttonNextMap.setVisible(bNMap);
		buttonSingleplayer.setVisible(bSplayer);
		buttonNetwork.setVisible(bNetwork);
		buttonOptionen.setVisible(bOptionen);
		buttonHelp.setVisible(bHelp);
		buttonEditor.setVisible(bEditor);
		buttonBeenden.setVisible(bBeenden);
		buttonDeathmatch.setVisible(bDeathmatch);
		buttonCoop.setVisible(bCoop);
		buttonCreate.setVisible(bCreate);
		buttonJoin.setVisible(bJoin);
		buttonEinstellungen.setVisible(bNWOptions);
    }
    
    /**
     * Startet eine neue Karte.
     * @param header Header der zu startenden Karte.
     * @param playerID ID des Players (im Normalfall 0)
     */
    public void initMap(MapHeader header, int playerID) {

    	// Stellt Map auf
	 	try {
	 		Map.initialize(header);
	 	} catch (InvalidRoomLinkException | IOException | MapFormatException e) {
	 		e.printStackTrace();
	 	}
	 	
	 	randomgen = new Random(System.currentTimeMillis());
		activeRoom = Map.getRoom(0);
		
		//Falls bisher keine Map gespielt wurde oder die letzte Map nicht die letzte Storymap war
		if(		lastMap == null || 
				//oder die letzte Map nicht die letzte Storymap war
				! (lastMap.type == MapHeader.STORY_MAP && lastMap.storyID == Game.storyHeaders.size()))
			player = new Player(playerID);
		
		else
			player.initialize();
		addKeyListener(new Keys(player));
		
		
		// Menü-Buttons ausblenden, Status ändern
		setButtonVisible(false, false, false, false, false, false,
						 false, false, false, false, false, false,
						 false, false);
			
		lastMap = Map.header;
		setGameStatus(GameStatus.INGAME);
 	}
 
    /**
     * "Submethode" einer paint-Methode; zeichnet einen Raum.
     * @param g2d Graphics2D der aufrufenden paint-Methode.
     * @param room Raum, der gezeichnet werden soll.
     * @param panel JPanel, auf das der Raum gezeichnet werden soll.
     */
 	public static void paintRoom(Graphics2D g2d, Room room, JPanel panel) {
 		
 		/*
 		 * Felder malen
 		 */
 		//Iteriert über Spalten
        for (int i = 0; i < room.roomFields.length; i++) {
            //Iteriert über Zeilen
            for (int j = 0; j < room.roomFields[i].length; j++) {
            	
            	int fieldtype = room.roomFields[i][j].type;
            	
            	switch(fieldtype)
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
            		switch(room.roomFields[i][j].attribute1) {
            		case 0:
            			g2d.drawImage(GameWindow.riverimg_u, i*32, j*32, panel);
            			break;
            		case 1:
            			g2d.drawImage(GameWindow.riverimg_r, i*32, j*32, panel);
            			break;
            		case 2:
            			g2d.drawImage(GameWindow.riverimg_d, i*32, j*32, panel);
            			break;
            		case 3:
            			g2d.drawImage(GameWindow.riverimg_l, i*32, j*32, panel);
            			break;
            		}
            		break;
            	//Link
            	case 5:
            		g2d.drawImage(GameWindow.backgroundimg_1, i*32, j*32, panel);
            		break;
            	// Checkpoint-Link
            	case 6:
            		g2d.drawImage(GameWindow.riverimg_r, i*32, j*32, panel);
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
        
        /*
         * Entities malen
         */
        @SuppressWarnings("unchecked")
		LinkedList<Entities> tempEntities = (LinkedList<Entities>) room.entities.clone();
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
            if (testEntity instanceof Item) {
            	item = (Item)testEntity;
            	entityPos.setPosition(item.getPosition().getDrawPosition(item.getHitbox()));

        		//Entscheiden, welches Element aus Item-Arrays genommen werden soll nach Element
				int elementIndex = -1;
				switch(item.getElement()) {
				case PHYSICAL:
					elementIndex = 0;
					break;
				case FIRE:
					elementIndex = 1;
					break;
				case WATER:
					elementIndex = 2;
					break;
				case ICE:
					elementIndex = 3;
					break;
				}
				
            	switch (item.getType()) {
            		case 1:
        				g2d.drawImage(GameWindow.lifePosion, entityPos.x, entityPos.y, panel);
        				break;
            		case 2:
        				g2d.drawImage(GameWindow.deadlyPosion, entityPos.x, entityPos.y, panel);
        				break;
            		case 3:
            			g2d.drawImage(GameWindow.manaPosion, entityPos.x, entityPos.y, panel);
            			break;
            		case 4:
            			g2d.drawImage(GameWindow.swords[elementIndex], entityPos.x, entityPos.y, panel);
            			break;
            		case 5:
            			g2d.drawImage(GameWindow.shields[elementIndex], entityPos.x, entityPos.y, panel);
            			break;
            		case 6:
            			g2d.drawImage(GameWindow.drachenei, entityPos.x, entityPos.y, panel);
            			break;
            	}
            }
            else if (testEntity instanceof Coin){
            	coin = (Coin)testEntity;
            	entityPos.setPosition(coin.getPosition().x - (coin.getHitbox().width/2), coin.getPosition().y - (coin.getHitbox().height/2));
            	g2d.drawImage(GameWindow.coin, entityPos.x, entityPos.y, panel);
            }
            else if (testEntity instanceof Monster) {
            	monster=(Monster)testEntity;
            	entityPos.setPosition(monster.getPosition().x - (monster.getHitbox().width/2) , monster.getPosition().y - (monster.getHitbox().height/2));
            	//zeichnet Monster-Typen mit Blickrichtung 
               // g2d.drawImage(GameWindow.monsterimg_1left, entityPos.x, entityPos.y, this);
            	switch(monster.getType()){
            		case 1:{           	
            			switch(monster.getFaceDirection()){
            			case UP: 
            				g2d.drawImage(GameWindow.monsterimg_1up, entityPos.x, entityPos.y , panel);
            				break;
            			case DOWN:
            				g2d.drawImage(GameWindow.monsterimg_1down, entityPos.x, entityPos.y , panel);
            				break;
            			case LEFT:
            				g2d.drawImage(GameWindow.monsterimg_1left, entityPos.x, entityPos.y , panel);
            				break;
            			case RIGHT:
            				g2d.drawImage(GameWindow.monsterimg_1right, entityPos.x, entityPos.y , panel);
            				break;	
            			default:
            				g2d.drawImage(GameWindow.monsterimg_1down, entityPos.x, entityPos.y , panel);
            				break;
            			}
            			break;
            		}
            			
            		case 2:
            		{           	
            			switch(monster.getFaceDirection()){
            			case UP: 
            				g2d.drawImage(GameWindow.monsterimg_2up, entityPos.x, entityPos.y , panel);
            				break;
            			case DOWN:
            				g2d.drawImage(GameWindow.monsterimg_2down, entityPos.x, entityPos.y , panel);
            				break;
            			case LEFT:
            				g2d.drawImage(GameWindow.monsterimg_2left, entityPos.x, entityPos.y , panel);
            				break;
            			case RIGHT:
            				g2d.drawImage(GameWindow.monsterimg_2right, entityPos.x, entityPos.y , panel);
            				break;	
            			default:
            				g2d.drawImage(GameWindow.monsterimg_2down, entityPos.x, entityPos.y , panel);
            				break;
            			}
            			break;
            		}
            		case 3:
            		{           	
            			switch(monster.getFaceDirection()){
            			case UP: 
            				g2d.drawImage(GameWindow.monsterimg_3up, entityPos.x, entityPos.y , panel);
            				break;
            			case DOWN:
            				g2d.drawImage(GameWindow.monsterimg_3down, entityPos.x, entityPos.y , panel);
            				break;
            			case LEFT:
            				g2d.drawImage(GameWindow.monsterimg_3left, entityPos.x, entityPos.y , panel);
            				break;
            			case RIGHT:
            				g2d.drawImage(GameWindow.monsterimg_3right, entityPos.x, entityPos.y , panel);
            				break;	
            			default:
            				g2d.drawImage(GameWindow.monsterimg_3down, entityPos.x, entityPos.y , panel);
            				break;
            			}
            			break;
            		}
            		case 4:{
            			switch(monster.getFaceDirection()){
            			case UP: 
            				g2d.drawImage(GameWindow.monsterimg_4up, entityPos.x, entityPos.y , panel);
            				break;
            			case DOWN:
            				g2d.drawImage(GameWindow.monsterimg_4down, entityPos.x, entityPos.y , panel);
            				break;
            			case LEFT:
            				g2d.drawImage(GameWindow.monsterimg_4left, entityPos.x, entityPos.y , panel);
            				break;
            			case RIGHT:
            				g2d.drawImage(GameWindow.monsterimg_4right, entityPos.x, entityPos.y , panel);
            				break;	
            			default:
            				g2d.drawImage(GameWindow.monsterimg_4down, entityPos.x, entityPos.y , panel);
            				break;
            			}
            			break;
            		}
            		case 5:
            			
            			//Entscheiden, welches Element aus Boss-Arrays genommen werden soll nach Element
        				int elementIndex = -1;
        				switch(monster.getElement()) {
        				case PHYSICAL:
        					elementIndex = 0;
        					break;
        				case FIRE:
        					elementIndex = 1;
        					break;
        				case WATER:
        					elementIndex = 2;
        					break;
        				case ICE:
        					elementIndex = 3;
        					break;
        				}
        	          	
            			switch(monster.getFaceDirection()){
            			case UP: 
            				g2d.drawImage(GameWindow.bossImgs_up[elementIndex], entityPos.x, entityPos.y , panel);
            				break;
            			case DOWN:
            				g2d.drawImage(GameWindow.bossImgs_down[elementIndex], entityPos.x, entityPos.y , panel);
            				break;
            			case LEFT:
            				g2d.drawImage(GameWindow.bossImgs_left[elementIndex], entityPos.x, entityPos.y , panel);
            				break;
            			case RIGHT:
            				g2d.drawImage(GameWindow.bossImgs_right[elementIndex], entityPos.x, entityPos.y , panel);
            				break;	
            			default:
            				g2d.drawImage(GameWindow.bossImgs_down[elementIndex], entityPos.x, entityPos.y , panel);
        			
        			break;
            		}
            	}
            }
            
            else if (testEntity instanceof NPC){
            	npc = (NPC)testEntity;
            	entityPos.setPosition(npc.getPosition().getDrawPosition(npc.getHitbox()));
            	switch (npc.getType()){
            		case 1:
            			g2d.drawImage(GameWindow.npc3, entityPos.x, entityPos.y, panel);
            			break;
            		case 2:
            			g2d.drawImage(GameWindow.npc2, entityPos.x, entityPos.y, panel);
            			break;
            		case 3:
            			g2d.drawImage(GameWindow.npc4, entityPos.x, entityPos.y, panel);
            	}
            }
            else if (testEntity instanceof PlasmaBall) {
            	entityPos.setPosition(testEntity.getPosition().x - (testEntity.getHitbox().width/2) , testEntity.getPosition().y - (testEntity.getHitbox().height/2));
            	g2d.drawImage(GameWindow.plasma, entityPos.x, entityPos.y, panel);
            	
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
        	// Wenn der Multiplayer Button gedrückt wurde, dann soll
        	if (getGameStatus() == GameStatus.NETWORK){
        		// Co-Op erscheinen, wenn anschließend der Co-Op Button gedrückt wurde
        		if(getNetstat() == NetworkStatus.COOP)
        			paintMessage("Co-Op", g);
        		// Deathmatch erscheinen, wenn anschließend der Deatchmatch Button gedrückt wurde
        		else if(getNetstat() == NetworkStatus.DEATHMATCH)
        			paintMessage("Deathmatch", g);
        		// Multiplayer erscheinen, da wir im Multiplayer Modus sind
        		else
        			paintMessage("Multiplayer", g);
        	}
        	//Hauptmenü
        	else if(getGameStatus() == GameStatus.MAINMENU)
        	{
        		initMenu();
        		paintMessage("Hauptmenü",g);
        	}
        	// Spiel gewonnen
        	else if(getGameStatus() == GameStatus.GAMEWON && timer.isRunning())
        	{
        		initMenu();
        		paintMessage("Spiel gewonnen!",g);
        		
        	}
        	// Karte gewonnen, der Spieler kann in die nächste eintreten oder von vorn beginnen
        	else if(getGameStatus() == GameStatus.MAPWON && timer.isRunning()) {

        		initMenu();
        		paintMessage("Karte gemeistert!",g);
        	}
        	// Game Over, Spieler gestorben
        	else if(getGameStatus() == GameStatus.GAMEOVER && timer.isRunning())
        	{
        		initMenu();
        		paintMessage("Game Over",g);
        	}
        
        	Toolkit.getDefaultToolkit().sync();
        	g.dispose();
        } 
    }
    
    /**
     * Setzt die Menü-Button aufs JPanel, je nachdem welchen Zustand das Menü gerade hat
     */
	public void initMenu(){
		// Zeichne Menüelemente
		// Lege Standardpositionen für Buttons fest
		buttonPosX = GameMinSizeX/2-100;
		buttonPosY = GameMinSizeY/2-100;
		
		background.setBounds(0, 0, 800, 600);
		add(background);
		background.setVisible(true);
		// bestimme Position und Größe
		
		//Falls schon eine Karte gemeistert wurde, Nächste-Karte-Button anzeigen
		if(getGameStatus() == GameStatus.MAPWON) {
			
			background.setVisible(true);
			buttonNextMap.setVisible(true);
			buttonOptionen.setVisible(true);
			buttonHelp.setVisible(true);
			buttonBeenden.setVisible(true);
			buttonNextMap.setBounds(buttonPosX,buttonPosY,    200,30);
			buttonOptionen.setBounds(buttonPosX,buttonPosY+40, 200,30);
			buttonHelp.setBounds(buttonPosX,   buttonPosY+80, 200,30);
			buttonBeenden.setBounds(buttonPosX,buttonPosY+120,200,30);
		}
		// Ansonsten normales Hauptmenü anzeigen
		else {
			buttonNextMap.setVisible(false);
			buttonNewGame.setVisible(true);
			buttonNetwork.setVisible(true);
			buttonEditor.setVisible(true);
			buttonOptionen.setVisible(true);
			buttonHelp.setVisible(true);
			buttonBeenden.setVisible(true);
			buttonNewGame.setBounds(buttonPosX,buttonPosY,     200,30);
			buttonSingleplayer.setBounds(buttonPosX, buttonPosY+40, 200,30);
			buttonNetwork.setBounds(buttonPosX,buttonPosY+  80,200,30);
			buttonEditor.setBounds(buttonPosX, buttonPosY+ 120,200,30);
			buttonOptionen.setBounds(buttonPosX,buttonPosY+160,200,30);
			buttonHelp.setBounds(buttonPosX,   buttonPosY+ 200,200,30);
			buttonBeenden.setBounds(buttonPosX,buttonPosY+ 240,200,30);
		}
	}


	/**
	 * Gibt alle Spielrelevaten Informationen in der Infoleiste wieder
	 * Informationen f�r Leben, Coins etc. werden duch getter und setter Methoden aus dem Player aufgerufen
	 * @param g
	 */
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
		g.drawString(Integer.toString(player.getAttack()),245,563);
		g.drawImage(GameWindow.infoshield, 270, 543,this);
		g.drawString(Integer.toString(player.getArmour()),305,563);
		
	}
	
	/**
	 * Startet den Singleplayer-Modus, in dem eigene Karten
	 * (bzw. Nicht-Kampagnen-Karten) gespielt werden können.
	 */
	private void startSingleplayer() {
		new SingleplayerWindow();
	}

	/**
	 * Gibt eine Message aus, ob das Spiel gewonnen oder verloren wurde und erm�glicht den Neustart des Spiels
	 * @param msg
	 * @param g
	 */
	public void paintMessage(String msg, Graphics g){
		// Mache Buttons wieder sichtbar
		Font small = new Font("Arial", Font.BOLD, 20);
		FontMetrics metr = this.getFontMetrics(small);

		g.setColor(Color.WHITE);
		g.setFont(small);
		g.drawString(msg, (GameMinSizeX - metr.stringWidth(msg))/2, 80);

	}

	/**
	 * Pr�ft, ob das Spiel gestartet ist oder nicht
	 * Wenn das Spiel l�uft wird das JPanel neu gezeichnet
	 * Wird vom Timer gesteuert
	 * actionPerfomed m�ssen alle Aktionen �bergeben werden, die der Timer ausf�hren soll
	 */
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
			//Spielstart
			String action = e.getActionCommand();
			if("newgame".equals(action) || "nextmap".equals(action)) {
				MapHeader mapToStart = null;
				//Falls zuletzt keine oder die letzte Storymap gespielt wurde, erste starten
				if(lastMap == null || lastMap.type != MapHeader.STORY_MAP ||
						(lastMap.type == MapHeader.STORY_MAP && lastMap.storyID == Game.storyHeaders.size()))
					mapToStart = Game.storyHeaders.getFirst();
				//anderenfalls nächste Storymap starten
				else
					mapToStart = Game.storyHeaders.get(lastMap.storyID);
				
				initMap(mapToStart, 0);
			}
			
			else if(e.getSource() == buttonSingleplayer)
				startSingleplayer();
			
			//Hilfe
			else if(e.getSource() == buttonHelp)
				help();
			else if(e.getSource() == buttonHelpOk)
				helpDialog.dispose();
			
			//Optionen
			else if(e.getSource() == buttonOptionen)
				showOptions();
			
			//Netzwerk-Kram
			else if("network".equals(action)){
				initNetwork();
			}
			else if("coop".equals(action)){
				setNetstat(NetworkStatus.COOP);
				networkMenu();
			}
			else if ("deathmatch".equals(action)){
				setNetstat(NetworkStatus.DEATHMATCH);
				networkMenu();
			}
			else if("create".equals(action)){
				Server server = new Server(this.getPort(), MenuStart.getNetstat());
				server.start();
				setClient(new Client(this, MenuStart.getNetstat(), true));
			}
			else if("join".equals(action)){
				setClient(new Client(this, MenuStart.getNetstat(), false));
			}
			else if("back".equals(action)){
				backMenu();
			}
			else if("options".equals(action)){
				new NetworkOptions(this);
			}
			else if("editor".equals(action))
				initEditor();
			else if("exit".equals(action)) 
				System.exit(0);	// Programm beenden
		}
		repaint();
	}
	
	/**
	 * Provisorischer Hilfe-Dialog
	 * Wird aufgerufen, wenn "Hilfe" gedrückt wurde; erklärt die Steuerung.
	 */
	public void help() {
		helpDialog = new JDialog();
		helpDialog.setTitle("Hilfe");
		helpDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		helpDialog.setModal(true); //macht Hauptfenster unfokussierbar
		helpDialog.setSize(400,300);
		GridBagLayout layout = new GridBagLayout();
		helpDialog.setLayout(layout);
		helpDialog.setResizable(false);
		
		JLabel text1 = new JLabel("<html><body>" +
				"Hallo! Ich bin das provisorische Hilfe-Fenster.<br>" +
				"Die Steuerung funktioniert wie folgt:<br><br>");
		text1.setHorizontalAlignment(SwingConstants.CENTER);
		
		JLabel text2 = new JLabel ("<html><body>" +
				"Pfeiltasten:<br>" +
				"A:<br>" +
				"Tasten 1-4:<br>" +
				"E:<br><br>" +
				"</body></html>");
		
		JLabel text3 = new JLabel ("<html><body>" +
				"Bewegung<br>" +
				"Angreifen<br>" +
				"Zaubern<br>" +
				"NPCs ansprechen<br><br>" +
				"</body></html>");
		
		JLabel text4 = new JLabel ("Und nun viel Spaß!");
		text4.setHorizontalAlignment(SwingConstants.CENTER);
		
		GridBagConstraints text1Constraints = new GridBagConstraints();
		text1Constraints.gridx = 0;
		text1Constraints.gridy = 0;
		text1Constraints.gridheight = 1;
		text1Constraints.gridwidth = 2;
		text1Constraints.fill = GridBagConstraints.HORIZONTAL;
		text1Constraints.weightx = text1Constraints.weighty = 1;
		text1Constraints.insets = new Insets(8,8,8,8);
		
		GridBagConstraints text2Constraints = new GridBagConstraints();
		text2Constraints.gridx = 0;
		text2Constraints.gridy = 1;
		text2Constraints.gridheight = text2Constraints.gridwidth = 1;
		text2Constraints.fill = GridBagConstraints.HORIZONTAL;
		text2Constraints.weightx = text2Constraints.weighty = 1;
		text2Constraints.insets = new Insets(8,40,8,8);
		
		GridBagConstraints text3Constraints = new GridBagConstraints();
		text3Constraints.gridx = 1;
		text3Constraints.gridy = 1;
		text3Constraints.gridheight = text3Constraints.gridwidth = 1;
		text3Constraints.fill = GridBagConstraints.HORIZONTAL;
		text3Constraints.weightx = 2;
		text3Constraints.weighty = 1;
		text3Constraints.insets = new Insets(8,0,8,8);
		
		GridBagConstraints text4Constraints = new GridBagConstraints();
		text4Constraints.gridx = 0;
		text4Constraints.gridy = 2;
		text4Constraints.gridheight = 1;
		text4Constraints.gridwidth = 2;
		text4Constraints.fill = GridBagConstraints.HORIZONTAL;
		text4Constraints.weightx = text4Constraints.weighty = 1;
		text4Constraints.insets = new Insets(8,8,8,8);
		
		GridBagConstraints buttonOkConstr = new GridBagConstraints();
		buttonOkConstr.gridx = 0;
		buttonOkConstr.gridy = 3;
		buttonOkConstr.gridheight = 1;
		buttonOkConstr.gridwidth = 2;
		buttonOkConstr.weightx = buttonOkConstr.weighty = 1;
		buttonOkConstr.insets = new Insets(2,4,2,4);
		
		buttonHelpOk.addActionListener(this);
		helpDialog.add(buttonHelpOk, buttonOkConstr);
		helpDialog.add(text1, text1Constraints);
		helpDialog.add(text2, text2Constraints);
		helpDialog.add(text3, text3Constraints);
		helpDialog.add(text4, text4Constraints);
		
		helpDialog.setVisible(true);
	}
	
	public void initNetwork(){
		setGameStatus(GameStatus.NETWORK);
		buttonNewGame.setVisible(false);
		buttonNextMap.setVisible(false);
		buttonSingleplayer.setVisible(false);
		buttonNetwork.setVisible(false);
		buttonEditor.setVisible(false);
		buttonHelp.setVisible(false);
		buttonBeenden.setVisible(false);
		buttonBack.setVisible(true);
		buttonDeathmatch.setVisible(true);
		buttonCoop.setVisible(true);
		buttonEinstellungen.setVisible(true);
		buttonDeathmatch.setBounds(buttonPosX,buttonPosY,   200,30);
		buttonCoop.setBounds(buttonPosX,buttonPosY+40,	 200,30);
		buttonEinstellungen.setBounds(buttonPosX,buttonPosY+80,	 200,30);
		buttonBack.setBounds(buttonPosX,buttonPosY+120,200,30);
	}
	
	public void networkMenu(){
		buttonDeathmatch.setVisible(false);
		buttonCoop.setVisible(false);
		buttonEinstellungen.setVisible(false);
		buttonCreate.setBounds(buttonPosX,buttonPosY,   200,30);
		buttonJoin.setBounds(buttonPosX,buttonPosY+40,	 200,30);
		buttonBack.setBounds(buttonPosX,buttonPosY+80,200,30);
		buttonCreate.setVisible(true);
		buttonJoin.setVisible(true);
	}

	public void backMenu(){
		if((getNetstat() == NetworkStatus.COOP) || (getNetstat() == NetworkStatus.DEATHMATCH)){
			setNetstat(NetworkStatus.NONE);
			buttonBack.setVisible(true);
			buttonBack.setBounds(buttonPosX,buttonPosY+120,200,30);
			buttonDeathmatch.setVisible(true);
			buttonCoop.setVisible(true);
			buttonEinstellungen.setVisible(true);
			buttonCreate.setVisible(false);
			buttonJoin.setVisible(false);
		} else {
			setGameStatus(GameStatus.MAINMENU);
			buttonNewGame.setVisible(true);
			buttonNextMap.setVisible(false);
			buttonSingleplayer.setVisible(true);
			buttonNetwork.setVisible(true);
			buttonHelp.setVisible(true);
			buttonOptionen.setVisible(true);
			buttonEditor.setVisible(true);
			buttonBeenden.setVisible(true);
			buttonBack.setVisible(false);
			buttonDeathmatch.setVisible(false);
			buttonCoop.setVisible(false);
			buttonEinstellungen.setVisible(false);
		}
	}
	
	private void executeEnemyActions(){
		Entities testent = null;	//durch alle Entitys der Liste iterieren
		Monster testmonster = null;
		Projectile testproj = null;
		@SuppressWarnings("unchecked")
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
		int rndnumber = randomgen.nextInt(6);
		switch(rndnumber) {
			case 0:
				monster.setDirection(Direction.UP);
				monster.setFaceDirection(Direction.UP);
				Keys.upCtr++;
				break;
			case 1:
				monster.setDirection(Direction.DOWN);
				monster.setFaceDirection(Direction.DOWN);
				Keys.downCtr++;
				break;
			case 2:
				monster.setDirection(Direction.LEFT);
				monster.setFaceDirection(Direction.LEFT);
				Keys.leftCtr++;
				break;
			case 3:
				monster.setDirection(Direction.RIGHT);
				monster.setFaceDirection(Direction.RIGHT);
				Keys.rightCtr++;
				
			default:
				monster.setDirection(Direction.NONE);
		}
	}
	
	private void executePlayerAttacks(){
		if((player.getAttackCount() == 0) && (player.getIsAttacking() == true)){
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
		
		//Monster-Angriffsspeed-Limit (vermute ich, ck)
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
		Game.Menu.lastMap = null; // Reset des Spiels
	}
	
	/** Wird von Player aufgerufen, wenn der Spieler das Ziel der Map betritt
	 * Anhand von nextMap wird die nächste Karte ausgesucht
	 */
	public static void winMap() {
		//Falls die letzte Map eine Storymap war
		if(Game.Menu.lastMap.type == MapHeader.STORY_MAP) {
			if(Game.Menu.lastMap.storyID >= Game.storyHeaders.size())
				setGameStatus(GameStatus.GAMEWON);
			else
				setGameStatus(GameStatus.MAPWON);
		}
		//Falls keine Storymap, Spiel gewonnen
		else
			setGameStatus(GameStatus.GAMEWON);
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
			@SuppressWarnings("unchecked")
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
										
									case 3:
										if(questFinished == false){
											//System.out.println("eierzählen1");
											LinkedList<Entities> roomitems = (LinkedList<Entities>) player.getRoom().entities;
											Iterator<Entities> itemsiter = roomitems.iterator();
											int eggcount = 0;
											Item testitem = null;
											Entities testentitem = null;
											while(itemsiter.hasNext()){
												testentitem = itemsiter.next();
												if(testentitem instanceof Item){
													//System.out.println("eierzählen2");
													testitem = (Item)testentitem;
													if(testitem.getType() == 6){
														//System.out.println("ei gefunden");
														eggcount++;
														//System.out.println("eggcount:" + eggcount);
													}
												}
											}
											if(eggcount == 0){
												questFinished = true;
											}
										}
										if(questFinished == false){
											JOptionPane.showMessageDialog(null, npc.getText(), npc.getName(), JOptionPane.PLAIN_MESSAGE);
											talk = false;
										}
										else if(questFinished){
											if(npc.getItems().isEmpty()){
												JOptionPane.showMessageDialog(null, "Vielen Dank für deine Hilfe!", npc.getName(), JOptionPane.PLAIN_MESSAGE);
											}
											else{
												JOptionPane.showMessageDialog(null, "Danke für die Hilfe, hier hast du deine Belohnung" , npc.getName(), JOptionPane.PLAIN_MESSAGE);
												new Quest(this.player, npc);
											}
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
	 * Zentriert ein Fenster über Hauptspielfenster.
	 * @param dialog Zu zentrierender JDialog.
	 */
	public static void centerWindow(Window dialog) {
		Point framePos = Game.frame.getLocationOnScreen();
		Point newPos = new Point();
		Rectangle bounds = dialog.getBounds(); //Kantenlänge des Dialogs
		newPos.x = framePos.x + Game.MINWIDTH/2 - bounds.width/2;
		newPos.y = framePos.y + Game.MINHEIGHT/2 - bounds.height/2;
		dialog.setLocation(newPos);
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
	 * Liefert den aktuell genutzten Port
	 * @return port
	 */
	public int getPort(){
		return this.port;
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
	 * Liefert den aktuell genutzten Namen
	 * @return name
	 */
	public String getName(){
		return this.name;
	}
	
	/**
	 * Setzt die Server IP
	 * @param ip
	 */
	public void setHost(String ip)
	{
		this.host = ip;
	}
	
	/**
	 * Liefert die aktuell genutzte Server IP
	 * @return host
	 */
	public String getHost(){
		return this.host;
	}

	public static NetworkStatus getNetstat() {
		return netstat;
	}

	public static void setNetstat(NetworkStatus netstat) {
		MenuStart.netstat = netstat;
	}
	
	public void setPlayer(Player player){
		this.player = player;
	}

	public static Client getClient() {
		return client;
	}

	public static void setClient(Client client) {
		MenuStart.client = client;
	}
}
