package com.github.propra13.gruppeA3;

import java.awt.*;
import java.awt.event.*;
import com.github.propra13.gruppeA3.Entities.Player;
import com.github.propra13.gruppeA3.Entities.Moveable.direction;


public class Keys implements KeyListener {
	
	Player player; //Referenz zum gesteuerten Charakter
	private Frame listener_frame;

	//Konstruktor
	public Keys(Player player_bind, Frame listener_frame){
		player = player_bind;
        this.listener_frame =  listener_frame;
		listener_frame.setSize(0,0);
		listener_frame.setVisible(false);
	}
	
	public void keyTyped(KeyEvent e){}
	
	public void keyPressed(KeyEvent e){
        System.out.println("Key pressed: "+e.getKeyCode());

		int pressed = e.getKeyCode();
		switch(pressed) {
			case 37: player.direct = direction.LEFT;
					 player.move();
					 break;
			case 38: player.direct = direction.UP;
					 player.move();
					 break;
			case 39: player.direct = direction.RIGHT;
					 player.move();
					 break;
			case 40: player.direct = direction.DOWN;
					 player.move();
					 break;		 
			default: player.direct = direction.NONE;
					 break;
		}

        this.listener_frame.repaint();

	}
	
	public void keyReleased(KeyEvent e){

    }
}
