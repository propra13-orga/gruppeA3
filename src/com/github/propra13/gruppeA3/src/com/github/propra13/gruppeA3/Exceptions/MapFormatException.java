package com.github.propra13.gruppeA3.Exceptions;

public class MapFormatException extends Exception {
	/**
	 * @author Christian Krüger
	 * Wird geworfen, falls das Format der Map-Dateien nicht stimmt.
	 */
	private static final long serialVersionUID = 1L;
	public String cause;
	
	public MapFormatException() {
	}

	public MapFormatException( String s ) {
		super( s );
	}

}
