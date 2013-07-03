package com.github.propra13.gruppeA3.Map;


/** @author CK
 * Superklasse für feste Schalter auf der Map, die eine Aktion auslösen
 * Trigger-Referenz liegt in Field.trigger
 */
public abstract class Trigger {
	protected Field field;
	
	public Trigger(Field field) {
		this.field = field;
	}
	
	/**
	 * Löst Trigger aus
	 */
	public abstract void trigger();
	
	/**
	 * Gibt Status des Triggers zurück
	 * @return true falls ausgelöst, false falls nicht (im Zweifel subclassabhängig)
	 */
	public abstract boolean status();
	
	public Field getField() {
		return field;
	}
	
	/**
	 * Ändert die Link-Attribute
	 * @param link Link, dessen Attribute übernommen werden sollen
	 */
	public abstract void edit (Trigger trigger);
	
	/**
	 * Klont den Trigger
	 * @return Gibt eine Kopie des Triggers zurück
	 */
	public abstract Trigger clone();
}
