package com.github.propra13.gruppeA3.Menu;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.List;
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
import com.github.propra13.gruppeA3.Entities.Monster;
import com.github.propra13.gruppeA3.Entities.Player;
import com.github.propra13.gruppeA3.Exceptions.InvalidRoomLinkException;
import com.github.propra13.gruppeA3.Exceptions.MapFormatException;

@SuppressWarnings("serial")
public class MenuStart extends JPanel implements ActionListener{

    //definiert die Fenstergröße vom Spielfeld
    final public int GameMinSizeX = 800; 
    final public int GameMinSizeY = 600;

	public static Timer timer;
	
    private Player player = null;
    public static Integer activeRoom = 0;

    protected Image monsterimg;
    protected Image wallimg;
    protected Image playerimg;

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
        //timer = new Timer(10000/60, this);
        //timer.start();


        // TODO: Um die neue den neuen Raum zu betreten den "activeRoom" Ã¤ndern bzw das Objekt neu bauen
        // Der oder die Spieler sollten ins XML File und im CrawlerSAX erzeugt werden.
        this.player = new Player(Map.getMapRoom(activeRoom));
        //player ist auch ein entitiy.
        Map.mapRooms[activeRoom].entities.add(this.player);
        
        //wie bei menu wird das JFrame gezeichnet
        addKeyListener(new Keys(this.player));
        setSize(GameMinSizeX, GameMinSizeY);
        requestFocus();
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

        
        
        Position pp = this.player.getPosition();
        
        /**
         * Gehe die Entites Liste solange durch, bis du ein Monster findest und zeichne dieses dann auf die Map.
         */
        LinkedList<Entities> tempEntities = Map.mapRooms[activeRoom].entities;
        Iterator<Entities> iter = tempEntities.iterator();
        Entities testEntity;
        Position pm;
        
        /**
         * monster: temporäre Variable, wird dazu verwendet um die Position des Monsters zu bestimmen
         * gehe diese Liste bis zum Ende durch, und zeichne alle gefundenen Monster ins Frame ein.
         * Die if Abfrage überprüft hier, ob das Entity Element eine Instanz von Monster ist. (Wenn es eine ist, wird es getypcastet.)
         * Wird später um Player und Item erweitert werden. 
         * 
         */
        
        Monster monster;
        while (iter.hasNext()) {
            testEntity = iter.next();
            if (testEntity instanceof Monster) {
            	monster=(Monster)testEntity;
            	pm = monster.getPosition();
                g2d.drawImage(this.monsterimg, pm.x*32, pm.y*32, this);
                System.out.println("Monsterposition X:"+pm.x+" Y:"+pm.y +" activeRoom: "+activeRoom);

            }
        }
        
        g2d.drawImage(this.playerimg, pp.x*32, pp.y*32 , this);
        System.out.println("Playerposition X:"+pp.x+" Y:"+pp.y);

        //} catch (MapFormatException | IOException | InvalidRoomLinkException e) {
        // TODO Auto-generated catch block
        //	e.printStackTrace();
        //}


    }
    
    /* test umbau für jpanel
    public void death(Graphics g) {
    	String msg = "Game Over";
    	Font small = new Font("Helvetica", Font.BOLD, 25);
    	FontMetrics metr = this.getFontMetrics(small);
    	g.setColor(Color.WHITE);
    	g.setFont(small);
    	g.drawString(msg, (WIDTH - metr.stringWidth(msg)) / 2, HEIGHT /2);
    }
    */
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		repaint();
	}

}
