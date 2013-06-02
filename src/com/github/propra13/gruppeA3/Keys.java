package com.github.propra13.gruppeA3;

import java.awt.event.*;
import com.github.propra13.gruppeA3.Entities.Player;
import com.github.propra13.gruppeA3.Entities.Moveable.direction;

public class Keys implements KeyListener {
	
	Player player; //Referenz zum gesteuerten Charakter

	//Konstruktor
	public Keys(Player player_bind){
		player = player_bind;
		/* nicht mehr funktionsfaehig im JPanel
        this.listener_frame =  listener_frame;
		listener_frame.setSize(0,0);
		listener_frame.setVisible(false);
		
		*/
	}
	
	public void keyTyped(KeyEvent e){}
	
	public void keyPressed(KeyEvent e){
        System.out.println("Key pressed: "+e.getKeyCode());

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
		}

	}
	
	public void keyReleased(KeyEvent e){
		player.direct = direction.NONE;
    }
}
