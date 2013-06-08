package com.github.propra13.gruppeA3.Menu;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.Timer;

import com.github.propra13.gruppeA3.Keys;
import com.github.propra13.gruppeA3.Map;
import com.github.propra13.gruppeA3.Position;
import com.github.propra13.gruppeA3.Entities.Entities;
import com.github.propra13.gruppeA3.Entities.Item;
import com.github.propra13.gruppeA3.Entities.Monster;
import com.github.propra13.gruppeA3.Entities.Player;
import com.github.propra13.gruppeA3.Exceptions.InvalidRoomLinkException;
import com.github.propra13.gruppeA3.Exceptions.MapFormatException;

@SuppressWarnings("serial")
public class MenuStart extends JPanel implements ActionListener {

    //definiert die Fenstergröße vom Spielfeld
    final static public int GameMinSizeX = 800; 
    final static public int GameMinSizeY = 600;

	public static Timer timer;
	
    static Player player = null;
    public static Integer activeRoom = 0;

    protected Image monsterimg;
    protected Image wallimg;
    protected Image playerimg;
    
    // Item images
    protected Image lifePosion;
    protected Image deadlyPosion;
    protected Image manaPosion;
    protected Image sword;
    protected Image shield;

    protected Toolkit tool;
    protected String GamePath;
    
    
    
    public MenuStart() {
        // alle wichtigen Eigenschaften der Oberklasse übernehmen.
    	super();
    	setFocusable(true);
    	
        this.GamePath = System.getProperty("user.dir");
        this.tool = Toolkit.getDefaultToolkit();
        
        try {
			Map.initialize("Map02");
		} catch (InvalidRoomLinkException | IOException | MapFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

        monsterimg = this.getImage(this.GamePath + "/data/images/Test_Monster.png");
        wallimg = this.getImage(this.GamePath + "/data/images/Test_Wand.png");
        playerimg = this.getImage(this.GamePath + "/data/images/Test_Player.png");
        sword = this.getImage(this.GamePath + "/data/images/sword.png");
        shield = this.getImage(this.GamePath + "/data/images/shield.png");
        timer = new Timer(10000/60, this);
        timer.start();
        
        player = new Player(Map.getMapRoom(activeRoom));
        addKeyListener(new Keys(player));

        Map.mapRooms[activeRoom].entities.add(player);
        setSize(GameMinSizeX, GameMinSizeY);
    }
    

    protected Image getImage(String path) {
        System.out.println("Loading Image: "+path);
        Image img = null;

        try {
            img = tool.getImage(path);
        } catch (NullPointerException npe) {
            System.out.println("Bild wurde nicht gefunden!");
        }

        return img;
    }

    public void paint(Graphics g) { //Funktion zum Zeichnen von Grafiken
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        // Malt Felder
        // Array von Bildern aller Wandtypen
        Image[] walls = new Image[256];
        for (int x = 0; x < 5; x++) {
            walls[x] = tool.getImage("data/images/wall_" + x + "_32.png");
        }

        //Iteriert über Spalten
        for (int i = 0; i < Map.mapRooms[activeRoom].roomFields.length; i++) {
            //Iteriert über Zeilen
            for (int j = 0; j < Map.mapRooms[activeRoom].roomFields[i].length; j++) {
                /*
                 * author: J.L
                 * +32 damit die zeile nicht abgeschnitten wird das gleiche bei Monster
                 */
                g2d.drawImage(walls[Map.mapRooms[activeRoom].roomFields[i][j].type], i*32, j*32, this);
            }
        }
        

        Position pp = player.getPosition().drawPosition(Player.hitbox);
        

        // Malt Entities
        System.out.println("Ich mal mal den Kram");
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
                g2d.drawImage(this.monsterimg, entityPos.x*32, entityPos.y*32, this);
                System.out.println("Monsterposition X:"+entityPos.x+" Y:"+entityPos.y +" activeRoom: "+activeRoom);
            }
            else if (testEntity instanceof Item) {
            	item = (Item)testEntity;
            	entityPos = item.getPosition();
            	switch (item.getType()){
            		case 4:
            			g2d.drawImage(this.sword, entityPos.x*32, entityPos.y*32, this);
            			System.out.println("Schwert X:"+entityPos.x+" Y:"+entityPos.y +" activeRoom: "+activeRoom);
            			break;
            		case 5:
            			g2d.drawImage(this.shield, entityPos.x*32, entityPos.y*32, this);
            			System.out.println("Schild X:"+entityPos.x+" Y:"+entityPos.y +" activeRoom: "+activeRoom);
            			break;
            	}
            }
        }

        // Malt Spieler
        
        g2d.drawImage(this.playerimg, pp.x+32, pp.y , this);
        System.out.println("Playerposition X:"+pp.x+" Y:"+pp.y);

    }
    
    
	@Override
	public void actionPerformed(ActionEvent e) {
		repaint();
	}

}
