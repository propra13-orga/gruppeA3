package com.github.propra13.gruppeA3.Entities;

import com.github.propra13.gruppeA3.Menu.MenuStart;

public class AttackBuff extends Buff {
	
	final public static int manaCost = 30;
	
	private double factor;
	private int tickCounter;
	Player player;
	
	/**
	 * Setzt einen Attackbuff für den Player.
	 * @param player Spieler, der den Buff bekommt
	 * @param factor Attack-Faktor, der gesetzt werden soll
	 * @param time Dauer, die der Attack-Buff halten soll
	 */
	public AttackBuff(Player player, double factor, int time) {
		this.player = player;
		tickCounter = (int)((double)(1000.0 / MenuStart.delay)) * time;
		
		this.factor = factor;
		player.setPower((int) ((double)player.getPower()*factor));
		player.setMana(player.getMana() - manaCost);
	}
	
	@Override
	public void tick() {
		tickCounter--;
		if(tickCounter == 0)
			terminate();
	}
	
	public void terminate() {
		player.setBuff(null);
		player.setPower((int)(Math.round((double)player.getPower()/factor)));
	}

}
