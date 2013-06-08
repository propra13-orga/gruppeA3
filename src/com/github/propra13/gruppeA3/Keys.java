package com.github.propra13.gruppeA3;

import java.awt.event.*;

import javax.swing.JPanel;

import com.github.propra13.gruppeA3.Entities.Player;
import com.github.propra13.gruppeA3.Entities.Moveable.direction;

public class Keys implements KeyListener {
	
	Player player; //Referenz zum gesteuerten Charakter
	boolean playerIsSet = true;

	//Konstruktor
	public Keys(Player player_bind) {
		player = player_bind;
	}
	
	public void keyTyped(KeyEvent e){}
	
	public void keyPressed(KeyEvent e){
        System.out.println("Key pressed: "+e.getKeyCode());

		int pressed = e.getKeyCode();
		switch(pressed) {
			case 37: player.setDirection(direction.LEFT);
					 break;
			case 38: player.setDirection(direction.UP);
					 break;
			case 39: player.setDirection(direction.RIGHT);
					 break;
			case 40: player.setDirection(direction.DOWN);
					 break;
		} 
		player.move(); //TODO: in den loop

	}
	
	public void keyReleased(KeyEvent e){
		player.setDirection(direction.NONE);
    }
	

	/*
	public void keyTyped(KeyEvent e) {
        System.out.println("keyTyped: "+e);
    }
    public void keyPressed(KeyEvent e) {
        System.out.println("keyPressed: "+e);
    }
    public void keyReleased(KeyEvent e) {
        System.out.println("keyReleased: "+e);
    }
    */
}
