package com.github.propra13.gruppeA3.Entities;

import com.github.propra13.gruppeA3.Room;
/**
 *  
 * @author Majida Dere
 * Diese Klasse definiert einen Spieler und seine Eigenschaften und Methoden.
 * 
 */

public class Player extends Moveable {
    // Attribute
	public int playerid;
	private String name = null;


	// Konstruktoren
	public Player(Room room_bind){
		super(room_bind);
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