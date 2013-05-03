package com.github.propra13.gruppeA3;

import com.github.propra13.gruppeA3.Field;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Map {
	
	Field[][] mapFields;	//mapFields[Zeile][Spalte]
	Field spawn = new Field();
	
	//Baut Map-Objekt; Argument: Dateiname
	public Map(String filename) throws IOException {
		this.mapFields = readFile(filename);
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
		Field map[][] = new Field[EOL_counter][lineLen/3];
		
		//Durchläuft Zeilen
		for (int i=0; i < EOL_counter; i++) {
			int lineIndex = i*lineLen + i;
			
			// Durchläuft Spalten (in Feldern)
			for (int j=0; j < lineLen/3; j++) {
				//Nimmt erstes Feldbyte
				int bufferIndex = lineIndex + j*3;
				map[i][j] = new Field();
				
				//Iteriert über alle drei Feldbytes
				for (int k=0; k < 3; k++) {
					switch (k) {
					case 0:
						map[i][j].type = buffer[bufferIndex + k];
					case 1:
						map[i][j].attribute = buffer[bufferIndex + k];
					case 2:
						map[i][j].item = buffer[bufferIndex + k];
						map[i][j].x = i;
						map[i][j].y = j;
					}
				}
				if (map[i][j].mapType() == "Spawn")
					//TODO: keine zwei Objekte für Spawn und map[i][j]
					this.spawn = map[i][j];
				
			}
		}
		return map;
	}
}
	
