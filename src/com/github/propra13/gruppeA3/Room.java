package com.github.propra13.gruppeA3;

import com.github.propra13.gruppeA3.Field;
import com.github.propra13.gruppeA3.Position;
import com.github.propra13.gruppeA3.Exceptions.MapFormatException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

public class Room {
	
	public int ID;
	public Field[][] roomFields;	//roomFields[Zeile][Spalte]
	public LinkedList entities = new LinkedList();
	public Field[] spawns = null;
	
	/* TODO:
	 * Metadatenzeile
	 * Links
	 * Room-ID (für Links, Listen und wat nich all)
	 * vernünftiges Dateiformat
	 * ( 6 Byte pro Field (neues Item-Format) )
	 */
	
	//Baut Map-Objekt aus Datei
	public Room(int roomID, String filename)
			throws IOException, MapFormatException {
		this.ID = roomID;
		this.roomFields = readFile(filename);
	}
	
	
	
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
		System.out.println("Kartengröße: " + EOL_counter + "x" + lineLen/3);
		
		
		/* Buffer -> Map-Array */
		Field room[][] = new Field[EOL_counter][lineLen/3];
		
		//Durchläuft Zeilen
		int spawncounter = -1;
		spawns = new Field[2];
		spawns[0] = null;
		spawns[1] = null;
		
		int lineIndex = 0;
		int bufferIndex = 0;
		
		for (int i=0; i < EOL_counter; i++) {
			lineIndex = i*lineLen + i;
			
			// Durchläuft Spalten (in Feldern)
			for (int j=0; j < lineLen/6; j++) {
				//Nimmt erstes Feldbyte
				bufferIndex = lineIndex + j*3;
				
				//Iteriert über alle sechs Feldbytes
				int texture = 255; //255: Eclipse meckert sonst
				int type = 255;
				int attr1 = 255;
				int attr2 = 255;
				int entityType = 255;
				int entityAttr = 255;
				Position pos = new Position();
				
				for (int k=0; k < 6; k++) {
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
						case 4:
							entityType = buffer[bufferIndex + k];
							break;
						case 5:
							entityAttr = buffer[bufferIndex + k];
							break;
					}
					pos.x = i;
					pos.y = j;
					room[i][j] = new Field(
							type, texture, attr1, attr2, entityType, entityAttr, pos);
					
				}
				
				//Spawn setzen
				if (attr1 == 2) {
					
					if(filename != "00." + Map.roomEnding)
						throw new MapFormatException("Spawns dürfen nur in Raum 00 sein.");
					
					else if (spawncounter > 1 || attr2 > 1)
						throw new MapFormatException("Nicht mehr als zwei Spawns erlaubt");
					
					else if (spawns[spawncounter] != null)
						throw new MapFormatException("Zwei Spawns mit gleicher ID");
					
					else
						spawns[spawncounter] = room[i][j];
					
					spawncounter++;
						
					
				}
				
			}
		}
		return room;
	}

}
	
