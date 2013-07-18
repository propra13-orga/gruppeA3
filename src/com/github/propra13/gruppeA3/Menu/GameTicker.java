package com.github.propra13.gruppeA3.Menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import com.github.propra13.gruppeA3.Entities.Player;
import com.github.propra13.gruppeA3.Menu.MenuStart.GameStatus;

public class GameTicker implements ActionListener {
	
	MenuStart gui;
	
	GameTicker(MenuStart gui) {
		this.gui = gui;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		tick();

	}
	
	/**
	 * Der Game-Tick - updatet einmal die GUI.
	 */
	public void tick() {
		if(MenuStart.getGameStatus() == GameStatus.INGAME) {
			gui.player.move(this);
			gui.executeEnemyActions();
			MenuStart.activeRoom.removeEntities();
			gui.executeTalk();
			gui.executePlayerAttacks();
			gui.tickCounters();
		}
		
		gui.repaint();
	}
	
	/**
	 * Der Game-Tick - updatet einmal die GUI; bewegt alle Spieler des Spieler-Arrays.
	 */
	public void tick(Player[] players) {
		Player thisPlayer = gui.player;
		MenuStart.activeRoom.removeEntities();
		for(int i=0; i < players.length; i++) {
			if(MenuStart.getGameStatus() == GameStatus.INGAME) {
				gui.player = players[i];
				gui.player.move(this);
				gui.executeEnemyActions();
				gui.executeTalk();
				gui.executePlayerAttacks();
				gui.tickCounters();
			}
		}
		
		gui.player = thisPlayer;
		gui.repaint();
	}

}
