package com.github.propra13.gruppeA3;

import java.awt.event.*;

import com.github.propra13.gruppeA3.Editor.WarningWindow;
import com.github.propra13.gruppeA3.Entities.Player;
import com.github.propra13.gruppeA3.Entities.Moveable.Direction;
import com.github.propra13.gruppeA3.Menu.MenuStart;

/**
* Dies ist die Klasse für den Keylistener, welche die Tastatureingaben abfängt, sie benötigt eine Referenz zum gesteuerten Spieler. 
* Von den von KeyListener implementierten Methoden wird nur keyTyped() verwendet.
*/

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
		/**
		 * Konstruktor für den KeyListener
		 * @param player_bind Spieler an die die Steuerung gebunden werden soll
		 */

	public Keys(Player player_bind) {
		player = player_bind;
	}
	
	public void keyTyped(KeyEvent e){}
		/**
		 * Diese Methode ruft die jeweiligen Methoden des Spielers auf, je nachdem welche Taste gedrückt wird
	 	 * 
		 */

	public void keyPressed(KeyEvent e){
		System.out.println("event im SP-keylistener");
		int pressed = e.getKeyCode();
		if(MenuStart.getGameStatus() == MenuStart.GameStatus.INGAME)
		{
			//Zwei Switches, damit Bewegung nicht unterbrochen wird
			switch(pressed) {
			case KeyEvent.VK_9:
				player.switchSwordEl();
				break;
			
			case KeyEvent.VK_0:
				player.switchShieldEl();
				break;
				
			case KeyEvent.VK_A:
				player.setAttack(true);
				Music.soundattach(); //Soundeffekt hit
				break;
				
			case KeyEvent.VK_1:
				player.setCast("SpeedBuff");
				break;
				
			case KeyEvent.VK_2:
				player.setCast("AttackBuff");
				break;
				
			case KeyEvent.VK_3:
				if(Game.Menu.player.level >=2){
					player.setCast("firePlasma");
					Music.soundmagic(); //Soundeffekt magic
				}
				break;
				
			case KeyEvent.VK_4:
				if(Game.Menu.player.level >=3){
					player.setCast("fireAOEPlasma");
					Music.soundmagic(); //Soundeffekt magic
				}
				break;
				
			case KeyEvent.VK_E:
				MenuStart.talk = true;
				break;
			}
			
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
					
				case KeyEvent.VK_T:
					MenuStart.client.getChat().setVisible(true);
					break;
					
				/* Test-Cheat-Kram
				 */
				//Setzt Mana wieder auf 100
				case KeyEvent.VK_M:
					player.setMana(100);
					break;
					
				//Debug-Knöpfe
				case KeyEvent.VK_L:
					msgWindow.showWindow("<html><body>" +
							"Rauf (r%6 = 0): "+upCtr+"<br>" +
							"Links (r%6 = 2): "+leftCtr+"<br>" +
							"Runter (r%6 = 1): "+downCtr+"<br>" +
							"Rechts (r%6 = 3): "+rightCtr+"<br>" +
							"Keine (ansonsten) / 2: "+noDirCtr/2+"<br>" +
							"</body></html>");
					break;
					
				case KeyEvent.VK_P:
					player.win();
			}
		}
	}
	
	public void keyReleased(KeyEvent e){
		player.setDirection(Direction.NONE);
    }
}
