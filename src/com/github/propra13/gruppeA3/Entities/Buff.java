package com.github.propra13.gruppeA3.Entities;

public abstract class Buff {

	/** Wird jeden Gametick aufgerufen
	 * Nutzung: Zeit herunterzählen oä
	 * @param time Geschwindigkeit des Timers an
	 */
	public abstract void tick();
	
	public abstract void terminate();
}
