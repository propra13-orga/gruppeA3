/**
 * 
 */
package com.github.propra13.gruppeA3;

import java.applet.Applet;
import java.applet.AudioClip;
import java.io.File;
import java.net.MalformedURLException;
/**
 * Klasse zum Abspielen von midi Dateien
 * @author Majida Dere
 *
 */
public class Music {

	/**
	 * Attribute:
	 * 		sound: AudioClip Datei zum abspielen einer midi datei
	 */
	private AudioClip sound;
	String track;
	boolean isRunning;

	/**
	 * Erzeigt ein neues Music objekt, was direkt die Titelmelodie abspielt
	 */
	public Music() {
		track = "menu";
		File file = new File(System.getProperty("user.dir")+"/data/sound/"+track+".mid");
		
		try {
			sound = Applet.newAudioClip(file.toURI().toURL());
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		
		play();
	}
	
	/**
	 * Spielt die Melodie zum übergebenen Track
	 */
	public void play() {
		sound.loop();
		isRunning = true;
	}
	
	/**
	 * Stopt den aktuell gespielten Track um später einen neuen zu starten
	 */
	public void stop() {
		sound.stop();
		isRunning = false;
	}
	
	/**
	 * @return Gibt zurück, ob die Musik gerade abgespielt wird
	 */
	public boolean isRunning() {
		return isRunning;
	}
}

