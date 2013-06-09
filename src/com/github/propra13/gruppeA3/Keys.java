package com.github.propra13.gruppeA3;

import java.awt.event.*;

import javax.swing.JPanel;

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
		int keys = e.getKeyCode();
		if(MenuStart.ingame)
		{
			
			if(keys == KeyEvent.VK_LEFT)
    		{
				player.setDirection(direction.LEFT);
				player.move();
    		}
			else if(keys == KeyEvent.VK_RIGHT)
			{
				player.setDirection(direction.RIGHT);
				player.move();
			}
			else if(keys == KeyEvent.VK_UP)
			{
				player.setDirection(direction.UP);
				player.move();
			}
			else if(keys == KeyEvent.VK_DOWN)
			{
				player.setDirection(direction.DOWN);
				player.move();
			}
		}
	}
	
	public void keyReleased(KeyEvent e){
	
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
