package com.github.propra13.gruppeA3;

import com.github.propra13.gruppeA3.Field;
import com.github.propra13.gruppeA3.Position;
import com.github.propra13.gruppeA3.Entities.Entities;
import com.github.propra13.gruppeA3.Entities.Monster;
import com.github.propra13.gruppeA3.Exceptions.MapFormatException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class Room {
	
	public int ID;
	public Field[][] roomFields;	//roomFields[Spalte][Zeile]
	public List<Entities> entities = new LinkedList<Entities>();
	public Field[] spawns = null;
	final static int fieldBytes = 4;
	
	//Temporäre Sammellisten
	private List<Link> checkpointLinks = new LinkedList<Link>();
	private List<Field> checkpointsToBuild = new LinkedList<Field>(); //für buildCheckpoints()
	
	/* TODO:
	 * Metadatenzeile
	 * Links
	 * Room-ID (für Links, Listen und was nicht all)
	 * vernünftiges Dateiformat
	 * ( 6 Byte pro Field (neues Item-Format) )
	 */
	
	//Baut Map-Objekt aus Datei
	public Room(int roomID, String filename)
			throws IOException, MapFormatException {
		this.ID = roomID;
		System.out.println("Baue Raum "+ID);
		this.roomFields = readFile(filename);
		buildCheckpoints();
		System.out.println("Checkpoints gebaut");
		
		//Iteriert über alle gefundenen Checkpointtrigger
		Iterator<Field> iter = checkpointsToBuild.iterator();
		Field toBuild;
		while (iter.hasNext()) {
			toBuild = iter.next();
		}
	}
	
	
	/* Liest Raum aus Datei aus
	 * Vorgehensweise:
	 * Lädt alle Bytes in buffer-Array
	 * Zählt Zeilenendenmarker (FF bzw. 255)
	 * Iteriert über alle Felder in allen Zeilen
	 * Gibt roomFields[][] zurück (enthält alle Felder des Raums)
	 */
	private Field[][] readFile (String filename)
			throws FileNotFoundException, IOException, MapFormatException {
		
		
		/* Spielfelddatei -> Buffer-Array */
		File file = new File(filename);
		int[] buffer = new int[(int) file.length()];
		
		//Total hässlich, muss aber so (IO halt)
		if (! file.exists())
			throw new FileNotFoundException (filename);
		FileInputStream fis = new FileInputStream(filename);
			
		try {
			for (int i=0; i < file.length(); i++) {
				buffer[i] = fis.read(); //Liest byteweise Datei in buffer[]
			}
		} catch (IOException e) {
			throw new IOException (filename);
		} finally {
			try {
				fis.close();
			} catch (IOException e){}
		}
		
		
		/*Symmetriecheck (EOL: EndOfLine)*/
		//Durchläuft Buffer-Array und zählt Zeilenenden
		int EOL_counter=0;
		for (int i=0; i < buffer.length; i++) {
			if (buffer[i] == 255) {
				EOL_counter++;
			}
		}
		
		//Symmcheck
		//Durchläuft alle Zeilen und vergleicht Zeilenlänge
		//mit der ersten Zeile (in Bytes!)
		EOL_counter=0;		//Aktuelle Zeile
		int lineLen=0;		//Vergleichszeilenlänge
		int lineIterate=0;	//Zeilenbreitenzähler
		for (int i=0; i < buffer.length; i++) {
			
			if (buffer[i] == 255) {
				if (EOL_counter == 0) {
					//Setzt Vergleichszeilenlänge
					lineLen = lineIterate;
				}
				else if (lineLen != lineIterate) {
					//Eine Zeilenlänge war von Vergleichszeilenlänge verschieden
					throw new MapFormatException(filename + " ist nicht rechteckig.");
				}
				
				EOL_counter++; //Damit später die Zeilenzahl stimmt
				lineIterate = 0; //Setzt Zeilenbreitenzähler zurück
			}
			else {
				lineIterate++;
			}
		}
		
		
		/* Buffer -> Map-Array */
		Field room[][] = new Field[lineLen/fieldBytes][EOL_counter];
		
		//Durchläuft Zeilen
		int spawncounter = 0;
		spawns = new Field[2];
		spawns[0] = null;
		spawns[1] = null;
		
		int lineIndex = 0;   //Anfang der aktuellen Zeile
		int bufferIndex = 0; //Anfang des aktuellen Felds
		
		/* i: Zeilennummer
		 * lineIndex: Nummer des buffer-Arrayplatzes des ersten Bytes delineIndex r aktuellen Zeile
		 * 
		 * j: Feldnummer in aktueller Zeile
		 * bufferIndex: Nummer des buffer-Arrayplatzes des ersten Bytes des aktuellen Felds
		 * 
		 * k: Arrayplatz-Offset für Bestimmung der Feldattribute
		 */
		for (int i=0; i < EOL_counter; i++) {
			lineIndex = i*lineLen + i; //Zeile*Zeilenlänge + EOLs
			
			
			// Durchläuft Spalten (in Feldern)
			for (int j=0; j < lineLen/fieldBytes; j++) {
				
				//Nimmt erstes Feldbyte
				bufferIndex = lineIndex + j*fieldBytes;
				
				//Iteriert über alle sechs Feldbytes
				int texture = 255; //255: Eclipse meckert sonst
				int type = 255;
				int attr1 = 255;
				int attr2 = 255;
				
				for (int k=0; k < 4; k++) {
					switch (k) {
						case 0:
							type = buffer[bufferIndex + k];
							break;
						case 1:
							texture = buffer[bufferIndex + k];
							break;
						case 2:
							attr1 = buffer[bufferIndex + k];
							break;
						case 3:
							attr2 = buffer[bufferIndex + k];
							break;
					}
					Position pos=new Position(j, i);
					room[j][i] = new Field(
							this, type, texture, attr1, attr2, pos);
				}
				
				// Checkpoints, Trigger, Spawns und Links setzen
				switch (room[j][i].type) {
				
					//Spawn
					case 1:
						if (attr1 == 2) {
							
							// Um den Pfad vom Dateinamen einfach abhacken zu können
							File compare = new File(filename);
							
							
							if( !compare.getName().equals( "00." + Map.roomEnding) )
								throw new MapFormatException("Spawns dürfen nur in Raum 00 sein.");
							
							else if (spawncounter > 1 || attr2 > 1)
								throw new MapFormatException("Nicht mehr als zwei Spawns erlaubt");
							
							else if (spawns[spawncounter] != null)
								throw new MapFormatException("Zwei Spawns mit gleicher ID");
							
							else
								Map.addSpawn(room[j][i]);
							
							spawncounter++;
						}
						break;
						
					//Link
					case 5:
						if (attr2 != 254) 
							new Link(attr1, attr2, room[j][i].pos, false);
						else
						Map.setEnd(room[j][i], this);
						break;
						
					/* Checkpoint-Link
					 * Werden gesammelt für buildCheckpoints()
					 */
					case 6:
						System.out.println("Checkpoint-Link "+attr1+" gefunden");
						checkpointLinks.add(new Link(attr1, attr2, room[j][i].pos, true));
						break;
						
					// Trigger
					case 7:
						// Sucht Trigger-Subobjekt raus
						switch (attr2) {
							case 0:
								System.out.println("Checkpointtrigger gefunden auf "+j+":"+i);
								checkpointBuildLater(room[j][i]);
								break;
						}
						break;
						
				}
					
			}
		}
		return room;
	}
	
	private void checkpointBuildLater(Field location) {
		checkpointsToBuild.add(location);
	}
	
	// Baut alle Checkpoint-Trigger aus checkpointsToBuild
	private void buildCheckpoints() {
		Iterator<Field> iter = checkpointsToBuild.iterator();
		Field toBuild;

		//Iteriert über alle gefundenen Checkpointtrigger
		while (iter.hasNext()) {
			toBuild = iter.next();
			System.out.println("Suche Link für Checkpointtrigger "+toBuild.attribute1);
	
			Iterator<Link> iter2 = checkpointLinks.iterator();
			Link toCheck;
			//Iteriert über alle gefundenen Checkpoint-Links
			while (iter2.hasNext()) {
				toCheck = iter2.next();
				System.out.println("Prüfe Checkpoint-Link "+toCheck.ID);
				
				/* Falls die ID des Checkpoint-Links (aus checkpointLinks) mit der ID
				 * des Triggers (aus checkpointsToBuild übereinstimmt, wird der
				 * Checkpoint-Trigger erzeugt.
				 */
				if (toCheck.ID == toBuild.attribute1) {
					System.out.println("Setze Checkpoint");
					toBuild.trigger = new Checkpoint(toBuild, toCheck);
				}
			}
        }
	}

}
	
