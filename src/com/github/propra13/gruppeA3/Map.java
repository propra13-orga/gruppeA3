package com.github.propra13.gruppeA3;

import com.github.propra13.gruppeA3.Field;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Map {
	
	Field[][] map;
	
	public Map(String filename) throws IOException {
		this.map = readFile(filename);
	}
	
	
	private Field[][] readFile (String filename) throws IOException {
		/* Spielfelddatei -> Buffer-Array */
		System.out.println("Lese ein: " + filename);
		File file = new File(filename);
		int[] buffer = new int[(int) file.length()];
		FileInputStream fis = new FileInputStream(filename);
		for (int i=0; i < file.length(); i++) {
			buffer[i] = fis.read();
		}
		fis.close();
		
		/*Symmetriecheck (EOL: EndOfLine)*/
		//Zeilenzählung
		int EOL_counter=0;
		for (int i=0; i < buffer.length; i++) {
			if (buffer[i] == 255) {
				EOL_counter++;
			}
		}
		
		//Symmcheck
		EOL_counter=0;
		int lineLen=0;
		int lineIterate=0;
		for (int i=0; i < buffer.length; i++) {
			
			if (buffer[i] == 255) {
				if (EOL_counter == 0) {
					lineLen = lineIterate;
				}
				else if (lineLen != lineIterate) {
					System.out.println("Fehler: Die Eingelesene Karte ist nicht rechteckig.");
					System.exit(0);
				}
				
				EOL_counter++;
				lineIterate = 0;
			}
			else {
				lineIterate++;
			}
		}
		System.out.println("Kartengröße: " + EOL_counter + "x" + lineLen/3);
		
		/* Buffer -> Map-Array */
		Field map[][] = new Field[EOL_counter][lineLen/3];
		for (int i=0; i < EOL_counter; i++) {
			int lineIndex = i*lineLen + i;
			
			for (int j=0; j < lineLen/3; j++) {
				int bufferIndex = lineIndex + j*3;
				map[i][j] = new Field();
				
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
				
			}
		}
		return map;
	}
}
	
