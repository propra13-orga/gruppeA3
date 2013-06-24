package com.github.propra13.gruppeA3.Editor;

import com.github.propra13.gruppeA3.Map.Field;

/**
 * Falls der nächste Klick ein Auswahlklick ist, wird dem Listener,
 * dessen isListening-Flag gesetzt ist, mit chooseClick() das ausgewählte
 * Feld mitgeteilt.
 * @author Christian Krüger
 */
public interface ChooseClickListener {
	
	/**
	 * Wird aufgerufen, wenn der Auswahlklick erfolgt ist.
	 * @param affectedField Ausgewähltes Feld
	 */
	public void chooseClick(Field affectedField);
	
	/**
	 * Falls der Listener aktiv ist, wird der Auswahlklick mitgeteilt.
	 * Wichtig: In der Implementierung von chooseClick() sollte das
	 * Flag wieder zurückgesetzt werden.
	 * @return
	 */
	public boolean isListening();
}
