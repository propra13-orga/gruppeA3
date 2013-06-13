package com.github.propra13.gruppeA3.Entities;

import com.github.propra13.gruppeA3.Menu.MenuStart;

public class SpeedBuff extends Buff{
	
	private int tickCounter;
	final public static int manaCost = 15;
	Player player;
	
	/**
	 * Setzt einen Speedbuff für den Player.
	 * @param player Spieler, der den Buff bekommt
	 * @param factor Speed-Faktor, der gesetzt werden soll
	 * @param time Dauer, die der Speed-Buff halten soll
	 */
	public SpeedBuff(Player player, double factor, int time) {
		this.player = player;
		player.setSpeed(factor);
		tickCounter = (int)((double)(1000.0 / MenuStart.delay)) * time;
		player.setMana(player.getMana() - manaCost);
	}
	
	@Override
	public void tick() {
		tickCounter--;
		if(tickCounter == 0) {
			player.buff = null;
			player.setSpeed(1);
		}
	}
}
