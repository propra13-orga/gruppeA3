package com.github.propra13.gruppeA3;

/* @author CK
 * Superklasse für feste Schalter auf der Map, die eine
 * Aktion auslösen
 * Trigger-Referenz liegt in Field.trigger
 */
public abstract class Trigger {
	public Field field;
	
	public Trigger(Field field) {
		this.field = field;
	}
	
	// Löst Trigger aus
	public abstract void trigger();
	
	// Gibt Status des Triggers zurück
	public abstract boolean status();
}
