package com.github.propra13.gruppeA3.Map;


public interface FieldNotifier {

	/**
	 * Wird aufgerufen, falls bei einem Map-Lesevorgang ein Feld gefunden wurde
	 * Wird genutzt um raumübergreifende Strukturen zu erkennen, zB Links
	 * @param field das Feld, das kommuniziert wird
	 */
	public void notify(Field field);
}
