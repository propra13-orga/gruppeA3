package com.github.propra13.gruppeA3;

/**
 * Klasse für GUI: Spielgrafik
 * @autor Jenny Lenz
 */

import com.github.propra13.gruppeA3.Entities.GameOptions;
import com.github.propra13.gruppeA3.Entities.Player;
import com.github.propra13.gruppeA3.Exceptions.InvalidRoomLinkException;
import com.github.propra13.gruppeA3.Exceptions.MapFormatException;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;

public class GameWindow extends JFrame{

    private GameOptions options;
    private java.util.Map<String, Player> players;

    public GameWindow(GameOptions options) {
        super();

        this.options = options;
        this.players = this.options.getAllPlayers();

        if(this.players.size() < 1) {
            System.out.println("Bitte stellen Sie den Namen von mindestens einem Spieler ein!");
            return;
        }
    }

    public void paint(Graphics g) {

        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;

        Toolkit tool = Toolkit.getDefaultToolkit();
        

        // Allgemeine Karte zeichnen
        // Player Zeichnen

        while(true) {
            // Bewegungen abfangen

            // Auf Bewegungen reagieren
            // ggf. Aktionenen ausführen (Bewegen)
            // nach der Aktion das betroffene Objekt neu zeichnen
        }

    }

    public void startGame() {

    }
    
    public void endGame() {
    	super.dispose();
    }
}