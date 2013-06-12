package com.github.propra13.gruppeA3;

import java.awt.event.*;

import com.github.propra13.gruppeA3.Entities.Player;
import com.github.propra13.gruppeA3.Entities.Moveable.direction;
import com.github.propra13.gruppeA3.Menu.MenuStart;

public class Keys implements KeyListener {
	
	Player player; //Referenz zum gesteuerten Charakter
	boolean playerIsSet = true;

	//Konstruktor
	public Keys(Player player_bind) {
		player = player_bind;
	}
	
	public void keyTyped(KeyEvent e){}
	
	public void keyPressed(KeyEvent e){
		int pressed = e.getKeyCode();
		if(MenuStart.ingame)
		{
			switch(pressed){
				case KeyEvent.VK_LEFT:
					player.setDirection(direction.LEFT);
					player.move();
					break;
				
				case KeyEvent.VK_RIGHT:
					player.setDirection(direction.RIGHT);
					player.move();
					break;
					
				case KeyEvent.VK_UP:
					player.setDirection(direction.UP);
					player.move();
					break;
				
				case KeyEvent.VK_DOWN:
					player.setDirection(direction.DOWN);
					player.move();
					break;
					
				case  KeyEvent.VK_SPACE:
					//angriffsfunktion hier
					break;
					
				case KeyEvent.VK_1:
					//Zauber 1 hier
					break;
					
				case KeyEvent.VK_2:
					//Zauber 2 hier
					break;
					
				case KeyEvent.VK_3:
					//Zauber 3 hier
					break;
					
				case KeyEvent.VK_4:
					//Zauber 4 hier
					break;
			}
		}
	}
	
	public void keyReleased(KeyEvent e){
	
    }
}
