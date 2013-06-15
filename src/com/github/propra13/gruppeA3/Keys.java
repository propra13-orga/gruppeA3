package com.github.propra13.gruppeA3;

import java.awt.event.*;

import com.github.propra13.gruppeA3.Entities.Player;
import com.github.propra13.gruppeA3.Entities.Moveable.Direction;
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
					player.setDirection(Direction.LEFT);
					player.setFaceDirection(Direction.LEFT);
					//player.move();
					break;
				
				case KeyEvent.VK_RIGHT:
					player.setDirection(Direction.RIGHT);
					player.setFaceDirection(Direction.RIGHT);
					//player.move();
					break;
					
				case KeyEvent.VK_UP:
					player.setDirection(Direction.UP);
					player.setFaceDirection(Direction.UP);
					//player.move();
					break;
				
				case KeyEvent.VK_DOWN:
					player.setDirection(Direction.DOWN);
					player.setFaceDirection(Direction.DOWN);
					//player.move();
					break;
					
				case  KeyEvent.VK_A:
					player.attack();
					break;
					
				case KeyEvent.VK_1:
					player.setSpeedBuff();
					break;
					
				case KeyEvent.VK_2:
					player.setAttackBuff();
					break;
					
				case KeyEvent.VK_3:
					player.firePlasma();
					break;
					
				case KeyEvent.VK_4:
					player.fireAOEPlasma();
					break;
					
				// Setzt Mana wieder auf 100 (zum Testen)
				case KeyEvent.VK_M:
					player.setMana(100);
					break;
			}
		}
	}
	
	public void keyReleased(KeyEvent e){
		player.setDirection(Direction.NONE);
    }
}
