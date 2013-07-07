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

	/**
	 * Erzeigt ein neues Music objekt, was direkt die Titelmelodie abspielt
	 */
	public Music() {
		play("title");
	}
	
	/**
	 * Spielt die Melodie zum übergebenen Track
	 * @param track
	 */
	public void play(String track){
		File file = new File(System.getProperty("user.dir")+"/data/sound/"+track+".mid");
		try {
			sound = Applet.newAudioClip( file.toURI().toURL() );
			sound.loop();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
	}
	
	/**
	 * Stopt den aktuell gespielten Track um später einen neuen zu starten
	 */
	public void stop(){
		sound.stop();
	}
}

