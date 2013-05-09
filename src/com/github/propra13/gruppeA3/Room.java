package com.github.propra13.gruppeA3;

import com.github.propra13.gruppeA3.Field;
import com.github.propra13.gruppeA3.Position;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.LinkedList;

public class Room {
	
	public int ID;
	public Field[][] roomFields;	//mapFields[Zeile][Spalte]
	public LinkedList entities = new LinkedList();
	
	/* TODO:
	 * Metadatenzeile
	 * Links
	 * Room-ID (für Links, Listen und wat nich all)
	 * vernünftiges Dateiformat
	 * ( 6 Byte pro Field (neues Item-Format) )
	 */
	
	//Baut Map-Objekt aus Datei
	public Room(int roomID, String filename) throws FileNotFoundException, IOException{
		this.ID = roomID;
		this.roomFields = readFile(filename);
	}
	
	private Field[][] readFile (String filename) throws FileNotFoundException, IOException {
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
					System.out.println("Fehler: Die Eingelesene Karte ist nicht rechteckig.");
					System.exit(0);
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
		for (int i=0; i < EOL_counter; i++) {
			int lineIndex = i*lineLen + i;
			
			// Durchläuft Spalten (in Feldern)
			for (int j=0; j < lineLen/3; j++) {
				//Nimmt erstes Feldbyte
				int bufferIndex = lineIndex + j*3;
				
				//Iteriert über alle sechs Feldbytes
				int[] byteParts;
				int texture;
				int type;
				int attr1;
				int attr2;
				int entityType;
				int entityAttr;
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
				
				/* TODO:
				 * Spawn und End sind keine Typen mehr
				 */
				if (room[i][j].fieldType() == "Spawn")
					super.setSpawn(room[i][j]);
				else if (room[i][j].fieldType() == "End")
					super.setEnd(room[i][j]);
				
			}
		}
		return room;
	}

}
	
