package com.github.propra13.gruppeA3;

import java.awt.*;
import java.awt.event.*;

public class Keys implements KeyListener {
	
	Player player; //Referenz zum gesteuerten Charakter
	private Frame listener_frame;
		
	//Konstruktor
	public Keys(Player player_bind){
		player = player_bind;
		listener_frame = new Frame("");
		listener_frame.setSize(0,0);
		listener_frame.setVisible(false);
		listener_frame.addKeyListener(this);
	}
	
	public void keyTyped(KeyEvent e){}
	
	public void keyPressed(KeyEvent e){
		int pressed = e.getKeyCode();
		switch(pressed) {
			case 37: player.direct = LEFT;
					 break;
			case 38: player.direct = UP;
					 break;
			case 39: player.direct = RIGHT;
					 break;
			case 40: player.direct = DOWN;
					 break;
			default: player.direct = NONE;
					 break;
		}	
	}
	
	public void keyReleased(KeyEvent e){}
	
}
