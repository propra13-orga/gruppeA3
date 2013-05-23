package com.github.propra13.gruppeA3.Entities;

import java.util.Iterator;
import java.util.LinkedList;
import com.github.propra13.gruppeA3.Menu.GameEndWindow;
import com.github.propra13.gruppeA3.Menu.Menu;
import com.github.propra13.gruppeA3.Menu.MenuStart;
import com.github.propra13.gruppeA3.Link;
import com.github.propra13.gruppeA3.Map;

import com.github.propra13.gruppeA3.Room;

/**
 * @author Majida Dere
 *         Diese Klasse definiert einen Spieler und seine Eigenschaften und Methoden.
 */

public class Player extends Moveable {
   // Attribute / werden noch nicht benutzt
   // private String name;
   // private int score;


    // Konstruktoren
    public Player(Room room_bind) {
        super(room_bind);
        setPosition(1,1);
    }

    //Methode überschrieben, prüft für Spieler zusätzlich, ob bereits ein anderer Spieler auf dem Feld steht
    public void move() {

        System.out.println("Setze Richtung auf "+this.direct);

        switch (this.direct) {
            // TODO: Überprüfung ob Spieler aus der Map läuft nicht nötig, wenn Wände richtig gesetzt sind

            case LEFT:
                System.out.println("Ist das Feld begehbar?: "+currentroom.roomFields[pos.x - 1][pos.y].walkable);
              if (currentroom.roomFields[pos.x - 1][pos.y].walkable                      && currentroom.roomFields[pos.x - 1][pos.y].entityType != 1) {
                    setPosition(this.pos.x - 1, this.pos.y);
              }
                break;

            case UP:
                System.out.println("Ist das Feld begehbar?: "+currentroom.roomFields[pos.x][pos.y - 1].walkable);
               if (currentroom.roomFields[pos.x][pos.y - 1].walkable
                       && currentroom.roomFields[pos.x][pos.y - 1].entityType != 1) {
                    setPosition(this.pos.x, this.pos.y - 1);
                    }
                break;

            case RIGHT:
                System.out.println("Ist das Feld begehbar?: "+currentroom.roomFields[this.pos.x + 1][this.pos.y].walkable);
                if (currentroom.roomFields[this.pos.x + 1][this.pos.y].walkable
                        && currentroom.roomFields[pos.x + 1][pos.y].entityType != 1) {
                    setPosition(this.pos.x + 1, this.pos.y);
                }
                break;

            case DOWN:
                System.out.println("Ist das Feld begehbar?: "+currentroom.roomFields[this.pos.x][this.pos.y + 1].walkable);
                if (currentroom.roomFields[this.pos.x][this.pos.y + 1].walkable
                        && currentroom.roomFields[pos.x][pos.y + 1].entityType != 1) {
                    setPosition(this.pos.x, this.pos.y + 1);
                }
                break;
            default:
                System.out.println("Nicht bewegt!");
                //nichts tun
        }

        /**
         * @author Majida Dere
         * Feld muss neu gezeichnet werden. Der Spieler hat sich bewegt.
         *
         */

        // draw();
        /**
         * Die Entitites Liste soll durchlaufen werden, um zu überprüfen, ob an der Position xy des Spielers ein Monster ist.
         */
        LinkedList<Entities> tempEntities = this.currentroom.entities;
        Iterator<Entities> iter = tempEntities.iterator();

        Entities testEntity;
        while (iter.hasNext()) {
            testEntity = iter.next();
            System.out.println("Checking: Entity " + testEntity.getPosition().x + ":" + testEntity.getPosition().y);
            if ((testEntity instanceof Monster) && (this.pos.equals(testEntity.getPosition()))){
                // Dies wird später benötigt, wenn Spieler und auch Monster mehrere Leben haben:
                // this.setLife(this.getLife()-((Monster)testEntity).getPower());
            	System.out.println("Monster auf dem gleichen Feld wie Spieler!");
                this.death();
            }
        }

        if(currentroom.roomFields[pos.x][pos.y].link != null){
        	System.out.println("Hier ist ein Link!");
        	changeRooms(currentroom.roomFields[pos.x][pos.y].link);
        }
        
        if(currentroom.roomFields[pos.x][pos.y]== Map.end)
        	this.win();

    }

    private void death() {
    	GameEndWindow end = new GameEndWindow("Game Over");
    	Menu.closeWindow();

        //TODO: Eventuell Referenzen auf null setzten damit GC abräumt?
    }
    
    // @author CK - provisorisch
    private void win() {
    	GameEndWindow end = new GameEndWindow("Gewonnen! Fuck Yeah!");
    	Menu.closeWindow();
    }
    
    private void changeRooms(Link link){
    	if(this.currentroom == link.targetRooms[0]){
    		this.currentroom = link.targetRooms[1];	//currentroom auf neuen Raum setzten
    		this.setPosition(link.targetFields[1].pos.x, link.targetFields[1].pos.y);
    		MenuStart.activeRoom = currentroom.ID;
    	}
    	else{
    		this.currentroom = link.targetRooms[0];
    		this.setPosition(link.targetFields[0].pos.x, link.targetFields[0].pos.y);
    		MenuStart.activeRoom = currentroom.ID;
    	}
    }

    /* Noch nicht benutzt
    private void use() {
        //TODO: Item von Spieler benutzen lassen
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
	*/
}

