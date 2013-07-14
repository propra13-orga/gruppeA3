package com.github.propra13.gruppeA3;

import java.awt.event.*;
import com.github.propra13.gruppeA3.Editor.WarningWindow;
import com.github.propra13.gruppeA3.Entities.Player;
import com.github.propra13.gruppeA3.Entities.Moveable.Direction;
import com.github.propra13.gruppeA3.Menu.MenuStart;

public class Keys implements KeyListener {
	
	Player player; //Referenz zum gesteuerten Charakter
	boolean playerIsSet = true;

	//Debugging
	public static int upCtr=0;
	public static int leftCtr=0;
	public static int downCtr=0;
	public static int rightCtr=0;
	public static int noDirCtr=0;
	WarningWindow msgWindow = new WarningWindow();

	//Konstruktor
	public Keys(Player player_bind) {
		player = player_bind;
	}
	
	public void keyTyped(KeyEvent e){}
	
	public void keyPressed(KeyEvent e){
		
		int pressed = e.getKeyCode();
		if(MenuStart.getGameStatus() == MenuStart.GameStatus.INGAME)
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
					
				case KeyEvent.VK_E:
					MenuStart.talk = true;
					break;
					
				/* Test-Cheat-Kram
				 */
				//Setzt Mana wieder auf 100
				case KeyEvent.VK_M:
					player.setMana(100);
					break;
					
				//Debug-Info-Knopf
				case KeyEvent.VK_L:
					msgWindow.showWindow("<html><body>" +
							"Rauf (r%6 = 0): "+upCtr+"<br>" +
							"Links (r%6 = 2): "+leftCtr+"<br>" +
							"Runter (r%6 = 1): "+downCtr+"<br>" +
							"Rechts (r%6 = 3): "+rightCtr+"<br>" +
							"Keine (ansonsten) / 2: "+noDirCtr/2+"<br>" +
							"</body></html>");
					break;
			}
		}
	}
	
	public void keyReleased(KeyEvent e){
		player.setDirection(Direction.NONE);
    }
}
