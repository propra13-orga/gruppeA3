package com.github.propra13.gruppeA3;

import com.github.propra13.gruppeA3.Field;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Room {
	
	Field[][] roomFields;	//mapFields[Zeile][Spalte]
	Field spawn;			//wird bald umgebaut; Map-Klasse muss Spawns wissen
	
	/* TODO:
	 * Metadatenzeile
	 * Links
	 * Room-ID (für Links, Listen und wat nich all)
	 * vernünftiges Dateiformat
	 * ( 4Byte pro Field (neues Item-Format) )
	 */
	
	//Baut Map-Objekt aus Datei
	public Room(String filename) throws IOException {
		this.roomFields = readFile(filename);
	}
	
	private Field[][] readFile (String filename) throws IOException {
		/* Spielfelddatei -> Buffer-Array */
		//TODO: vernünftiges Exception-Handling
		File file = new File(filename);
		int[] buffer = new int[(int) file.length()];
		FileInputStream fis = new FileInputStream(filename);
		for (int i=0; i < file.length(); i++) {
			buffer[i] = fis.read(); //Liest byteweise Datei in buffer[]
		}
		fis.close();
		
		
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
				room[i][j] = new Field();
				
				//Iteriert über alle drei Feldbytes
				int[] byteParts;
				for (int k=0; k < 3; k++) {
					switch (k) {
						case 0:
							byteParts = splitByte(buffer[bufferIndex + k]);
							room[i][j].texture = byteParts[1];
							room[i][j].type = byteParts[2];
							break;
						
						case 1:
							byteParts = splitByte(buffer[bufferIndex + k]);
							room[i][j].attribute1 = byteParts[1];
							room[i][j].attribute2 = byteParts[2];
							break;
						
						case 2:
							room[i][j].item = buffer[bufferIndex + k];
							room[i][j].x = i;
							room[i][j].y = j;
							break;
					}
				}
				if (room[i][j].fieldType() == "Spawn")
					this.spawn = room[i][j];
				
			}
		}
		return room;
	}

	//Splits a given byte to single hex numbers (4 bit)
	private int[] splitByte(int toSplit) {
		int partA=0;
		while(toSplit > 15) {
			partA++;
			toSplit = toSplit - 16;
		}
		int[] returnVal = {partA, toSplit};
		return returnVal;
	}

}
	
