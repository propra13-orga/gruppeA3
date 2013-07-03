package com.github.propra13.gruppeA3.Editor;

import java.util.LinkedList;

/**
 * @author Christian
 * Interface, das eine Liste für zu zeichnende Feldhighlights beinhaltet.
 * Wird für paintRoom-Methode benötigt.
 */
public interface HighlightContainer {
	
	public abstract LinkedList<FieldHighlight> getHighlights();

}
