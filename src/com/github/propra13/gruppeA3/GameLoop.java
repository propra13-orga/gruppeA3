package com.github.propra13.gruppeA3;

public class GameLoop {
	private boolean runflag = false;
	
	void run(double delta){
		//Loop vorbereiten: Flag auf true setzen, Systemzeit holen
		runflag = true;
		double nextTime  = (double)System.nanoTime() / 1000000000.0;
		
		while(runflag){
			//currTime auf momentane Zeit setzen
			double currTime = (double)System.nanoTime() / 1000000000.0;
			
			//Wenn der Zeitpunkt der naechsten Aktualisierung erreicht oder Ueberschritten wurde, Schleife ausfuehren
			if(currTime >= nextTime){
				//Zeitpunkt der naechsten Aktualisierung errechnen
				nextTime += delta;	
				//update();		//Spielfeld aktualisieren
				//draw();		//Fenster neuzeichnen
			}
			else{
				//Sleep-Zeit berechnen
				int sleepTime = (int)(1000.0 * (nextTime - currTime));
				//Thread schlafen lassen, wenn berechnete Zeit groesser 0
				if(sleepTime > 0){
					try{
						Thread.sleep(sleepTime);
					}
					catch(InterruptedException e){
						//nichts tun
					}
				}
			}
		}
	}
	
	//Mit stop()-Aufruf Gameloop beenden
	public void stop(){
		runflag = false;
	}
}
