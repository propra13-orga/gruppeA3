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
					break;
				
				case KeyEvent.VK_RIGHT:
					player.setDirection(Direction.RIGHT);
					player.setFaceDirection(Direction.RIGHT);
					break;
					
				case KeyEvent.VK_UP:
					player.setDirection(Direction.UP);
					player.setFaceDirection(Direction.UP);
					break;
				
				case KeyEvent.VK_DOWN:
					player.setDirection(Direction.DOWN);
					player.setFaceDirection(Direction.DOWN);
					break;
					
				case  KeyEvent.VK_A:
					player.setAttack(true);
					break;
					
				case KeyEvent.VK_1:
					player.setCast("SpeedBuff");
					break;
					
				case KeyEvent.VK_2:
					player.setCast("AttackBuff");
					break;
					
				case KeyEvent.VK_3:
					player.setCast("firePlasma");
					break;
					
				case KeyEvent.VK_4:
					player.setCast("fireAOEPlasma");
					break;
					
				// Setzt Mana wieder auf 100 (zum Testen)
				case KeyEvent.VK_M:
					player.setMana(100);
					break;
					
				case KeyEvent.VK_E:
					MenuStart.talk = true;
					break;
			}
		}
	}
	
	public void keyReleased(KeyEvent e){
		player.setDirection(Direction.NONE);
    }
}
