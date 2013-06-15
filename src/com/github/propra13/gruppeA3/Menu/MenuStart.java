package com.github.propra13.gruppeA3.Menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Random;


import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.github.propra13.gruppeA3.GameWindow;
import com.github.propra13.gruppeA3.Keys;
import com.github.propra13.gruppeA3.Entities.Entities;
import com.github.propra13.gruppeA3.Entities.Item;
import com.github.propra13.gruppeA3.Entities.Monster;
import com.github.propra13.gruppeA3.Entities.Moveable.Direction;
import com.github.propra13.gruppeA3.Entities.PlasmaBall;
import com.github.propra13.gruppeA3.Entities.Player;
import com.github.propra13.gruppeA3.Entities.Projectile;
import com.github.propra13.gruppeA3.Map.Map;
import com.github.propra13.gruppeA3.Map.Position;
import com.github.propra13.gruppeA3.Map.Room;

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
    	private int buttonPosX;
    	private int buttonPosY;
       
	public static boolean ingame = false;
	public static boolean menu = false;
	public static boolean win = false;

	//score infoleiste
	public static int score;
	Font smallfont = new Font("Helvetica", Font.BOLD, 14);
	
    public MenuStart() {
    	setLayout(null);
    	setFocusable(true);
    	new GameWindow();  
    	setBackground(Color.BLACK);
        setSize(GameMinSizeX, GameMinSizeY);
        setDoubleBuffered(true);
        
        activeRoom = Map.getMapRoom(0);
 		player = new Player(activeRoom);
        addKeyListener(new Keys(player));
        
        randomgen = new Random(System.currentTimeMillis());
        		
        menu = true; //wichtig für den ersten Spiel aufruf
        timer = new Timer(delay, this);
        timer.start();
        
        
     // Menü vorbereiten
     	buttonstart = new JButton("Spiel starten");
     	buttonbeenden = new JButton("Beenden");
     	initMenu();
    }
    
 // Startet Spiel
 public void initGame(){
	// Buttons ausblenden
		buttonstart.setVisible(false);
		buttonbeenden.setVisible(false);
		
	 // Spielablaufparameter setzen
 		menu=false;
 		ingame = true;
 		win = false;
 		player.setLives(3);

 	}
    
    public void paint(Graphics g) { //Funktion zum Zeichnen von Grafiken
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        
        if(ingame)
        {
        	setBackground(Color.WHITE);
        //Iteriert über Spalten
        for (int i = 0; i < activeRoom.roomFields.length; i++) {
            //Iteriert über Zeilen
            for (int j = 0; j < activeRoom.roomFields[i].length; j++) {
               
            	/*
                 * author: J.L
                 * +32 damit die zeile nicht abgeschnitten wird das gleiche bei Monster
                 * [Map.mapRooms[activeRoom].roomFields[i][j].type]
                 */
            	
            	int walltype = activeRoom.roomFields[i][j].type;
            	
            	switch(walltype)
            	{
            	case 1:
            		 g2d.drawImage(GameWindow.backgroundimg_1, i*32, j*32, this);
            		 break;
            	
            	case 2:
            		g2d.drawImage(GameWindow.backgroundimg_2, i*32, j*32, this);
            		break;
            	case 3:
            		g2d.drawImage(GameWindow.backgroundimg_3, i*32, j*32, this);
            		break;
            	default:
            		g2d.drawImage(GameWindow.backgroundimg_4, i*32, j*32, this);
            		break;
            	}
            }
        }
        
        Position pp = player.getPosition().getDrawPosition(player.getHitbox());

        // Malt Entities
        LinkedList<Entities> tempEntities = activeRoom.entities;
        Iterator<Entities> iter = tempEntities.iterator();
        Entities testEntity;
        Position entityPos = new Position(0,0);
        
        // Durchläuft Liste
        Monster monster;
        Item item;
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
        		String msg;
        		msg="Spiel Gewonnen";
        		paintMessage(msg,g);
        	}
        	else if(ingame ==false && win == false && timer.isRunning())
        	{
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
	buttonbeenden.setBounds(buttonPosX,buttonPosY+40,200,30);
	// benenne Aktionen
	buttonbeenden.setActionCommand("beenden");
	buttonstart.setActionCommand("starten");
	// ActionListener hinzufügen
	buttonstart.addActionListener(this);
	buttonbeenden.addActionListener(this);
	// füge Buttons zum Panel hinzu
	add(buttonstart);
	add(buttonbeenden);
}

//Socre and set Leben
public void Score(Graphics2D g) {
    int i,s;

    g.setFont(smallfont);
    g.setColor(Color.BLACK);
    s = score;
  //  System.out.println(s);
    g.drawImage(GameWindow.coin, 720, 543, this);
    g.drawString(Integer.toString(s), 750, 563);
    g.drawImage(GameWindow.heart, 5, 542, this);
    g.drawString("x",35,562);
    g.drawString(Integer.toString(player.getLives()), 50,562);
    //auslesen der Spieler leben über die getter player.getLives()
   
   g.drawString("Health: ", 70, 563);
   g.drawString(Integer.toString(player.getHealth()),125,563);
}
public void paintMessage(String msg, Graphics g){
	// Mache Buttons wieder sichtbar
	buttonstart.setVisible(true);
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
		if(ingame){			
			player.move();
			moveEnemies();
			activeRoom.removeEntities();
			if (player.getBuff() != null)
				player.getBuff().tick();
			
			if(movecounter == 0)
				movecounter = 60;
			else
				movecounter--;
		}
		else if(ingame == false ){
				//Spiel Start
				String action = e.getActionCommand();
				if("starten".equals(action))
				{
				initGame();
				}
				else if("beenden".equals(action)) 
				{
					System.exit(0);	// Programm beenden
				}
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
			testent = iter.next();
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
		switch(rndnumber%6){
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
}
