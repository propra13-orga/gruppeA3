package com.github.propra13.gruppeA3;

import java.awt.*;

import com.github.propra13.gruppeA3.Entities.Player;
import com.github.propra13.gruppeA3.Entities.Moveable.direction;
import java.awt.event.*;

public class Keys implements KeyListener {
	
	Player player; //Referenz zum gesteuerten Charakter
	private Frame listener_frame;
	private boolean loop_continue;
		
	//Konstruktor
	public Keys(Player player_bind){
		player = player_bind;
		listener_frame = new Frame("");
		listener_frame.setSize(0,0);
		listener_frame.setVisible(false);
		listener_frame.addKeyListener(this);
		loop_continue = false;
	}
	
	public void keyTyped(KeyEvent e){}
	
	public void keyPressed(KeyEvent e){
		int pressed = e.getKeyCode();
		switch(pressed) {
			case 37: player.direct = direction.LEFT;
					 break;
			case 38: player.direct = direction.UP;
					 break;
			case 39: player.direct = direction.RIGHT;
					 break;
			case 40: player.direct = direction.DOWN;
					 break;
			default: player.direct = direction.NONE;
					 break;
		}
		loop_continue = true;
		move_loop();
	}
	
	public void keyReleased(KeyEvent e){
		loop_continue = false;
	}
	
	void move_loop(){
		while(loop_continue == true){
			player.move();
		}
	}
	
}
