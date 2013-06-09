package com.github.propra13.gruppeA3.Menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Iterator;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.Timer;

import com.github.propra13.gruppeA3.GameWindow;
import com.github.propra13.gruppeA3.Keys;
import com.github.propra13.gruppeA3.Map;
import com.github.propra13.gruppeA3.Position;
import com.github.propra13.gruppeA3.Entities.Entities;
import com.github.propra13.gruppeA3.Entities.Item;
import com.github.propra13.gruppeA3.Entities.Monster;
import com.github.propra13.gruppeA3.Entities.Moveable;
import com.github.propra13.gruppeA3.Entities.Player;
import com.github.propra13.gruppeA3.Entities.Walls;

@SuppressWarnings("serial")
public class MenuStart extends JPanel implements ActionListener {

    //definiert die Fenstergröße vom Spielfeld
    final static public int GameMinSizeX = 800; 
    final static public int GameMinSizeY = 600;

	public static Timer timer;
    public Player player;
    public static Integer activeRoom;
    protected Toolkit tool;
    
    private int fps = 60;
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
    	setFocusable(true);
    	new GameWindow();  
    	setBackground(Color.WHITE);
        setSize(GameMinSizeX, GameMinSizeY);
        setDoubleBuffered(true);
        
        menu = true; //wichtig für den ersten Spiel aufruf
        
        timer = new Timer(fps, this);
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
 		score = 0;
 		activeRoom = 0;
 		Map.mapRooms[activeRoom].entities.add(player); 
 		 player = new Player(Map.getMapRoom(activeRoom));
         addKeyListener(new Keys(player));
 		timer.start();
 	}
    
    public void paint(Graphics g) { //Funktion zum Zeichnen von Grafiken
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        
        if(ingame)
        {

        //Iteriert über Spalten
        for (int i = 0; i < Map.mapRooms[activeRoom].roomFields.length; i++) {
            //Iteriert über Zeilen
            for (int j = 0; j < Map.mapRooms[activeRoom].roomFields[i].length; j++) {
               
            	/*
                 * author: J.L
                 * +32 damit die zeile nicht abgeschnitten wird das gleiche bei Monster
                 * [Map.mapRooms[activeRoom].roomFields[i][j].type]
                 */
            	
            	int walltype = Map.mapRooms[activeRoom].roomFields[i][j].type;
            	
            	switch(walltype)
            	{
            	case 1:
            		 g2d.drawImage(GameWindow.wallimg_1_32, i*32, j*32, this);
            		 break;
            	
            	case 2:
            		g2d.drawImage(GameWindow.wallimg_2_32, i*32, j*32, this);
            		break;
            	case 3:
            		g2d.drawImage(GameWindow.wallimg_3_32, i*32, j*32, this);
            		break;
            	default:
            		g2d.drawImage(GameWindow.backgroundimg, i*32, j*32, this);
            		break;
            	}
            }
        }
        
        Position pp = player.getPosition().drawPosition(Player.hitbox);

        // Malt Entities
        LinkedList<Entities> tempEntities = Map.mapRooms[activeRoom].entities;
        Iterator<Entities> iter = tempEntities.iterator();
        Entities testEntity;
        Position entityPos;
        
        // Durchläuft Liste
        Monster monster;
        Item item;
        while (iter.hasNext()) {
            testEntity = iter.next();
            if (testEntity instanceof Monster) {
            	monster=(Monster)testEntity;
            	entityPos= monster.getPosition();
                g2d.drawImage(GameWindow.monsterimg, entityPos.x*32, entityPos.y*32, this);
            }
            else if (testEntity instanceof Item) {
            	item = (Item)testEntity;
            	entityPos = item.getPosition();
            	switch (item.getType()){
            		case 4:
            			g2d.drawImage(GameWindow.sword, entityPos.x*32, entityPos.y*32, this);
            			break;
            		case 5:
            			g2d.drawImage(GameWindow.shield, entityPos.x*32, entityPos.y*32, this);
            			break;
            	}
            }
        }

        // Malt Spieler
        g2d.drawImage(GameWindow.playerimg, pp.x, pp.y-32 , this);
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
        	else if(ingame ==false && win == true)
        	{
        		String msg;
        		msg="Spiel Gewonnen";
        		paintMessage(msg,g);
        	}
        	else
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
	buttonPosY = GameMinSizeY/2-40;
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

//score + life
public void Score(Graphics2D g) {
    int i,x;
    String s;

    g.setFont(smallfont);
    g.setColor(Color.GREEN);
    s = "Score: " + score;
    g.drawString(s, 700, 590); //Zeile 32 spalte 25
   for (i = 0; i < player.getLives(); i++) {
	   x = i * 32 + 8 ;
	   System.out.println(x);
        g.drawImage(GameWindow.heart, x, 573, this);
    }
   
 //   g.drawImage(GameWindow.heart, 250, 573, this);
}

public void paintMessage(String msg, Graphics g){
	// Mache Buttons wieder sichtbar
	buttonstart.setVisible(true);
	buttonbeenden.setVisible(true);
	Font small = new Font("Arial", Font.BOLD, 20);
	FontMetrics metr = this.getFontMetrics(small);

	g.setColor(Color.RED);
	g.setFont(small);
	g.drawString(msg, (GameMinSizeX - metr.stringWidth(msg))/2, 80);

}
	@Override
	public void actionPerformed(ActionEvent e) {
		
		if(ingame){			
		//	player.move();		
		}
		else if(ingame == false && menu == true){
			// im Menü - Aufruf bei button
			String action = e.getActionCommand();
			//Spiel Start
			if(action.equals("starten")) initGame();
			else if(action.equals("beenden")) System.exit(0);	// Programm beenden
		}
		repaint();
	}


}
