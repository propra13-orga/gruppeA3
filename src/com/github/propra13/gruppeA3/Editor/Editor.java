package com.github.propra13.gruppeA3.Editor;

import java.awt.Component;
import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import com.github.propra13.gruppeA3.Game;
import com.github.propra13.gruppeA3.Exceptions.InvalidRoomLinkException;
import com.github.propra13.gruppeA3.Exceptions.MapFormatException;
import com.github.propra13.gruppeA3.Map.Link;
import com.github.propra13.gruppeA3.Map.Map;
import com.github.propra13.gruppeA3.Map.MapHeader;
import com.github.propra13.gruppeA3.Map.Room;
import com.github.propra13.gruppeA3.Map.Trigger;
import com.github.propra13.gruppeA3.Menu.MenuStart;
import com.github.propra13.gruppeA3.Menu.MenuStart.GameStatus;

/** @author CK
 * Hauptklasse des Map-Editors
 */
public class Editor extends JTabbedPane implements ChangeListener {
  
	private static final long serialVersionUID = 1L;
	
	public static Editor editor;
	
	//Subfenster
	protected LinkWindow linkEditor;
	protected WarningWindow warning;
	protected TriggerWindow triggerEditor;
	protected MonsterWindow monsterEditor;
	protected ItemWindow itemEditor;
	protected NPCWindow NPCEditor;
	
	private Menu menuPanel = new Menu();
	
	public boolean mapIsOpened = false;
	
	/** Falls der nächste Klick ein aus einem Dialog hervorgegangener
	 *  spezieller Auswahlklick ist (zB für den Link-Dialog)
	 */
	public enum ChooseClickType {LINK,CHECKPOINTFIELD,CHECKPOINTLINK,RIVER,NONE}
	public ChooseClickType chooseClick=ChooseClickType.NONE;
	
	protected String saveAsName; //Name, unter der die Karte gespeichert werden soll
	public LinkedList<Link> mapLinks = new LinkedList<Link>();
	public LinkedList<Trigger> mapTriggers = new LinkedList<Trigger>();
	
	private int activeTab = 0; //ID des Tabs wird gespeichert, um Änderungen mitteilen zu können

	/**
	 * Initialisiert Map und baut Tab-Fenster auf.
	 * @param mapName Verzeichnisname der Map, die bearbeitet werden soll
	 */
	public Editor() {
		super(JTabbedPane.TOP);
		
		editor = this;
		
		linkEditor = new LinkWindow();
		warning = new WarningWindow();
		triggerEditor = new TriggerWindow();
		monsterEditor = new MonsterWindow();
		itemEditor = new ItemWindow();
		NPCEditor = new NPCWindow();
		
		//Menü-Tab
		addTab("Menü", menuPanel);
		
	    addChangeListener(this);
		repaint();
		setVisible(true);
	}
	
	/**
	 * Öffnet eine Karte im Editor.
	 * @param header MapHeader der zu öffnenden Map.
	 */
	public void openMap(MapHeader header) {
		
		try {
			Map.initialize(header);
		} catch (MapFormatException | IOException | InvalidRoomLinkException e) {
			e.printStackTrace();
		}
		
		updateTabs();
	}
	
	/**
	 * Aktualisiert die Raum-Tabs.
	 */
	protected void updateTabs() {
		//Alte Raum-Tabs entfernen
		this.removeAll();
		add("Menü", menuPanel);
		
		//Raum-Tabs
		for(int i=0; i < Map.mapRooms.length; i++) {
			mapIsOpened = true;
			RoomTab roomTab = new RoomTab(Map.getRoom(i));
			add(roomTab);
		}
	}
	
	/**
	 * Schreibt die aktuelle Karte.
	 * TODO: Sanity-Check
	 */
	public void write() {
		//Header updaten
		Map.header.maxPlayers = Map.spawns.size();
		
		boolean error = false;
		try {
			Map.writeMap();
		} catch (TransformerException | ParserConfigurationException
				| IOException e) {
			e.printStackTrace();
			error = true;
			warning.showWindow("Ein Fehler ist aufgetreten.");
		}
		if(!error) {
			System.out.println("gespeichert");
			warning.showWindow("Gespeichert.");
		}
	}
	
	/** Beendet den Editor und holt Hauptmenü wieder aus der Versenkung
	 */
	public void exit() {
		editor = null;
		MenuStart.setGameStatus(GameStatus.MAINMENU);
		setVisible(false);
		Game.Menu.remove(this);
		Game.Menu.setVisible(true);
	}
	
	/**
	 * Fügt einen neuen Raum zur Karte hinzu.
	 * @param index Index des neuen Raums. Alle bestehenden Räume werden daran angepasst.
	 */
	public void addRoom(int index) {
		if(index == 0 && ! Map.spawns.isEmpty()) {
			warning.showWindow("Spawns dürfen nur in Raum 0 sein.");
			return;
		}
		
		Room newRoom = new Room(index);
		Room[] newMapRooms = new Room[Map.mapRooms.length + 1];
		
		//Räume in neues Array kopieren
		for(int i=0; i < newMapRooms.length; i++) {
			//Räume vor neuem Raum
			if(i < index)
				newMapRooms[i] = Map.mapRooms[i];
			//Neuer Raum
			else if(i == index)
				newMapRooms[i] = newRoom;
			//Räume hinter neuem Raum
			else if(i > index) {
				newMapRooms[i] = Map.mapRooms[i - 1];
				newMapRooms[i].ID = i;
			}
		}
		
		Map.mapRooms = newMapRooms;
		updateTabs();
	}
	
	/**
	 * Verschiebt einen Raum.
	 * @param roomToMove Raum, der verschoben wird
	 * @param newIndex Neue Stelle des Raums
	 */
	public void moveRoom(int indexToMove, int newIndex) {
		if( (indexToMove==0 || newIndex==0) && ! Map.spawns.isEmpty() ) {
			warning.showWindow("Spawns dürfen nur in Raum 0 sein.");
			return;
		}
		
		Room roomToMove = Map.mapRooms[indexToMove];
		
		//Verschieben
		for(int i=0; i < Map.mapRooms.length; i++) {
			//Verschiebung nach rechts; alle im Zwischenraum einen nach links schieben
			if(indexToMove < newIndex && i > indexToMove && i <= newIndex) {
				Map.mapRooms[i - 1] = Map.mapRooms[i];
				Map.mapRooms[i-1].ID = i-1;
			}
			//Verschiebung nach links; alle im Zwischenraum einen nach rechts schieben
			else if(indexToMove > newIndex && i > newIndex && i <= indexToMove) {
				Map.mapRooms[i + 1] = Map.mapRooms[i];
				Map.mapRooms[i+1].ID = i+1;
			}
		}
		
		Map.mapRooms[newIndex] = roomToMove;
		Map.mapRooms[newIndex].ID = newIndex;
		updateTabs();
	}
	
	/**
	 * Löscht einen Raum.
	 * @param indexToDelete Raum, der gelöscht werden soll.
	 */
	public void deleteRoom(int indexToDelete) {
		//ggf. Spawns und Ende in Map löschen
		if(indexToDelete == 0)
			Map.spawns.clear();
		if(Map.end != null && Map.end.getRoom() == Map.getRoom(indexToDelete)) {
			Map.end = null;
		}
		
		//Raum löschen
		Room[] newMapRooms = new Room[Map.mapRooms.length - 1];
		for(int i=0; i < newMapRooms.length; i++) {
			if(i < indexToDelete)
				newMapRooms[i] = Map.mapRooms[i];
			else {
				newMapRooms[i] = Map.mapRooms[i+1];
				newMapRooms[i].ID = i;
			}
		}
		
		Map.mapRooms = newMapRooms;
		updateTabs();
	}

	/**
	 * Wird von Map.initialize() bei jedem neuen Link aufgerufen
	 * @param link Link, der mitgeteilt wird
	 */
	public void notify(Link link) {
		mapLinks.add(link);
	}
	
	/**
	 * Wird von Map.initialize() bei jedem neuen Trigger aufgerufen
	 * @param trigger Trigger, der mitgeteilt wird
	 */
	public void notify(Trigger trigger) {
		mapTriggers.add(trigger);
	}

	/**
	 * Event-Listener: Tab gewechselt
	 * Teilt den Raum-Tabs mit, wenn sie den Fokus verlieren oder bekommen.
	 */
	@Override
	public void stateChanged(ChangeEvent e) {
		
		//Alten Tab benachrichtigen
		if(activeTab >= 0) {
			Component tab = getComponentAt(activeTab);
			if(tab instanceof RoomTab) {
				RoomTab roomTab = (RoomTab)tab;
				roomTab.focusLost();
			}
		}
			
		//Neuen Tab benachrichtigen
		activeTab = getSelectedIndex();
		if(activeTab >= 0) {
			Component tab = getComponentAt(activeTab);
			if(tab instanceof RoomTab) {
				RoomTab roomTab = (RoomTab)tab;
				roomTab.focusGained();
			}
		}
	}
} 