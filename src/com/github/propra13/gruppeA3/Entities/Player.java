package com.github.propra13.gruppeA3.Entities;

import java.util.Iterator;
import java.util.LinkedList;

import com.github.propra13.gruppeA3.Room;

/**
 * @author Majida Dere
 *         Diese Klasse definiert einen Spieler und seine Eigenschaften und Methoden.
 */

public class Player extends Moveable {
    // Attribute
    private String name = null;


    // Konstruktoren
    public Player(Room room_bind) {
        super(room_bind);
        setPosition(1,1);
    }

    //Methode überschrieben, prüft für Spieler zusätzlich, ob bereits ein anderer Spieler auf dem Feld steht
    public void move() {

        System.out.println("Setze Richtung auf "+this.direct);

        switch (this.direct) {
            // TODO: Vorher prüfen ob die Bewegung ausserhalb der Map liegt
            // TODO: Spieler bewegt sich nicht
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
            if ((testEntity instanceof Monster) && (this.pos == testEntity.getPosition())) {
                // Dies wird später benötigt, wenn Spieler und auch Monster mehrere Leben haben:
                // this.setLife(this.getLife()-((Monster)testEntity).getPower());
                this.death();
            } else if ((testEntity instanceof Enemy) && (this.pos == testEntity.getPosition())) {
                // Dies wird später benötigt, wenn Spieler und auch Gegner mehrere Leben haben:
                // this.setLife(this.getLife()-((Enemy)testEntity).getLife());
                this.death();
            }
        }


        /** Wird nicht genutzt!
         switch(this.currentroom.roomFields[this.pos.x][this.pos.y].entityType){
         case 0: break;
         case 2: this.death(); //Spieler stirbt bei betreten eines Feldes mit Gegner
         case 3: this.use();   //Spieler hebt Item auf / benutzt Item
         default: break;		  //nichts tun
         }
         */

    }

    private void death() {


        //TODO: Spieler sterben lassen
    }

    private void use() {
        //TODO: Item von Spieler benutzen lassen
    }

    /**
     * @return name liefert den Namen des Spielers zurück
     */
    public String getName() {
        return name;
    }

    /**
     * @param name legt den Namen eines Spielers fest
     */
    public void setName(String name) {
        this.name = name;
    }

    //Punktestand
    private int score = 0;

    /**
     * @return liefert den Punktestand
     */
    public int getScore() {
        return score;
    }

    /**
     * @ param score setzt den Punktestand, anfangs ist dieser auf 0
     */
    public void setScore(int score) {
        this.score = score;
    }

}

// Methode zum Bewegen von Objekten (w�rde methode der oberklasse �berschreiben evtl sp�ter um animationen zu realisieren?)
    /*	public void move(){
			switch(direct){
				case LEFT: if(leftFree(pos.x-1,pos.y)){
					setPosition(pos.x-1,pos.y);
					//feldNeuZeichnen();
					//zusätzliche Monsteraktionen/Zufallserscheininungen von Gegenständen
				}break;
				case RIGHT: if(rightFree(pos.x+1,pos.y)){
					setPosition(pos.x+1,pos.y);
					//feldNeuZeichnen();
					//zusätzliche Monsteraktionen/Zufallserscheininungen von Gegenständen
				}break;
				case UP: if (upFree(pos.x,pos.y+1)){
					setPosition(pos.x,pos.y+1);
					//feldNeuZeichnen();
					//zusätzliche Monsteraktionen/Zufallserscheininungen von Gegenständen
				}break;
				case DOWN:if (downFree(pos.x,pos.y-1)){
					setPosition(pos.x,pos.y+1);
					//feldNeuZeichnen();
					//zusätzliche Monsteraktionen/Zufallserscheininungen von Gegenständen
				}break;
				case NONE: //zusätzliche Monsteraktionen/Zufallserscheininungen von Gegenständen
					
			}
		}*/	