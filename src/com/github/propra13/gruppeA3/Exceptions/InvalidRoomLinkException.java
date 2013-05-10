package com.github.propra13.gruppeA3.Exceptions;

public class InvalidRoomLinkException extends Exception {
	/**
	 * @author Christian Krüger
	 * Wird geworfen, falls ein fehlerhafter Room-Link gefunden wird.
	 */
	private static final long serialVersionUID = 1L;
	
	public InvalidRoomLinkException() {
	}

	public InvalidRoomLinkException( String s ) {
		super( s );
	}
}
