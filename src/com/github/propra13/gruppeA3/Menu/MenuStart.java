package com.github.propra13.gruppeA3.Menu;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.*;

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

    //definiert die Fenstergr��e vom Spielfeld
    public int GameMinSizeX = 400;
    public int GameMinSizeY = 300;

    private Map map = null;
    private Player player = null;
    private Integer activeRoom = 0;

    protected Image monsterimg;
    protected Image wallimg;
    protected Image playerimg;

    protected Toolkit tool;
    protected String GamePath;
    protected Keys keyListener;

    public MenuStart(Map map) {
        // alle wichtigen Eigenschaften der Oberklasse übernehmen.
        super();

        this.GamePath = System.getProperty("user.dir");
        this.tool = Toolkit.getDefaultToolkit();

        monsterimg = this.getImage(this.GamePath + "/data/images/Test_Monster.png");
        wallimg = this.getImage(this.GamePath + "/data/images/Test_Wand.png");
        playerimg = this.getImage(this.GamePath + "/data/images/Test_Player.png");

        //übergebene Map zuweisen
        this.map = map;

        // TODO: Um die neue den neuen Raum zu betreten den "activeRoom" ändern bzw das Objekt neu bauen
        this.player = new Player(map.getMapRoom(this.activeRoom));

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

        //Iteriert über Zeilen
        for (int i = 0; i < map.mapRooms[0].roomFields.length; i++) {
            //Iteriert über Spalten
            for (int j = 0; j < map.mapRooms[0].roomFields[i].length; j++) {
                System.out.printf("%c", map.mapRooms[0].roomFields[i][j].charMap());

                int x = i * 32;
                int y = j * 32;
                g2d.drawImage(walls[map.mapRooms[0].roomFields[i][j].type], x, y, this);

                //Zeilenumbruch bei Zeilenende
                if (j == map.mapRooms[0].roomFields[i].length - 1)
                    System.out.printf("%n");
            }
        }

        Position pp = this.player.getPosition();
        g2d.drawImage(this.playerimg, pp.x*32, pp.y*32, this);
        System.out.println("Playerposition X:"+pp.x+" Y:"+pp.y);

        //} catch (MapFormatException | IOException | InvalidRoomLinkException e) {
        // TODO Auto-generated catch block
        //	e.printStackTrace();
        //}


    }

}
