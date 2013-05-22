package com.github.propra13.gruppeA3.Menu;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;

import javax.swing.*;

import com.github.propra13.gruppeA3.Entities.Entities;
import com.github.propra13.gruppeA3.Entities.Monster;
import com.github.propra13.gruppeA3.Entities.Moveable;
import com.github.propra13.gruppeA3.Entities.Player;
import com.github.propra13.gruppeA3.Game;
import com.github.propra13.gruppeA3.Keys;
import com.github.propra13.gruppeA3.Map;
import com.github.propra13.gruppeA3.Exceptions.InvalidRoomLinkException;
import com.github.propra13.gruppeA3.Exceptions.MapFormatException;
import com.github.propra13.gruppeA3.Position;
import com.github.propra13.gruppeA3.XMLParser.CrawlerSAX;

@SuppressWarnings("serial")
public class MenuStart extends JFrame {

    //definiert die Fenstergrï¿½ï¿½e vom Spielfeld
    public int GameMinSizeX = 320;
    public int GameMinSizeY = 424;

    private Player player = null;
    public static Integer activeRoom = 0;

    protected Image monsterimg;
    protected Image wallimg;
    protected Image playerimg;

    protected Toolkit tool;
    protected String GamePath;
    protected Keys keyListener;
    
    public MenuStart(Map map) {
        // alle wichtigen Eigenschaften der Oberklasse Ã¼bernehmen.
    	super();
    	
        this.GamePath = System.getProperty("user.dir");
        this.tool = Toolkit.getDefaultToolkit();

        monsterimg = this.getImage(this.GamePath + "/data/images/Test_Monster.png");
        wallimg = this.getImage(this.GamePath + "/data/images/Test_Wand.png");
        playerimg = this.getImage(this.GamePath + "/data/images/Test_Player.png");



        // TODO: Um die neue den neuen Raum zu betreten den "activeRoom" Ã¤ndern bzw das Objekt neu bauen
        // Der oder die Spieler sollten ins XML File und im CrawlerSAX erzeugt werden.
        this.player = new Player(map.getMapRoom(activeRoom));
        //player ist auch ein entitiy.
        Map.mapRooms[activeRoom].entities.add(this.player);
        
        //wie bei menu wird das JFrame gezeichnet

        keyListener = new Keys(this.player, this);

        addKeyListener(keyListener);
        setLocationRelativeTo(this);
        setTitle(CrawlerSAX.title);
        setSize(GameMinSizeX, GameMinSizeY);
        setVisible(true);
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

        //try {
        // NICHT nochmal auslesen, sonst wird eine neue map erzeugt!!!
        // Map map = new Map("beispielmap");

        //Iteriert Ã¼ber Zeilen
        for (int i = 0; i < Map.mapRooms[activeRoom].roomFields.length; i++) {
            //Iteriert Ã¼ber Spalten
            for (int j = 0; j < Map.mapRooms[activeRoom].roomFields[i].length; j++) {
                System.out.printf("%c", Map.mapRooms[activeRoom].roomFields[i][j].charMap());
                    /*
                     * author: J.L
                     * +32 damit die zeile nicht abgeschnitten wird das gleiche bei Monster
                     */
                g2d.drawImage(walls[Map.mapRooms[activeRoom].roomFields[i][j].type], i*32, j*32+32, this);

                //Zeilenumbruch bei Zeilenende
                if (j == Map.mapRooms[activeRoom].roomFields[i].length - 1)
                    System.out.printf("%n");
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
        
        Monster monster;
        while (iter.hasNext()) {
            testEntity = iter.next();
            if (testEntity instanceof Monster) {
            	monster=(Monster)testEntity;
            	pm = monster.getPosition();
                g2d.drawImage(this.monsterimg, pm.x*32, pm.y*32+32, this);
                System.out.println("Monsterposition X:"+pm.x+" Y:"+pm.y +" activeRoom: "+activeRoom);

            }
        }
        
        /* @author CK
         * "pp.y*32 - 32" ist ein Workaround; das -32 setzt den Spieler ein Feld nach oben, da sonst
         * irgendwas verschoben ist
         * @author J.L 
         * hat sich erledigt 
         */ 
        g2d.drawImage(this.playerimg, pp.x*32, pp.y*32 , this);
        System.out.println("Playerposition X:"+pp.x+" Y:"+pp.y);

        //} catch (MapFormatException | IOException | InvalidRoomLinkException e) {
        // TODO Auto-generated catch block
        //	e.printStackTrace();
        //}


    }

}
