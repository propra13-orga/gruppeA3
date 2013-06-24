package com.github.propra13.gruppeA3.Editor;

import java.io.IOException;
import java.util.LinkedList;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import com.github.propra13.gruppeA3.Game;
import com.github.propra13.gruppeA3.Exceptions.InvalidRoomLinkException;
import com.github.propra13.gruppeA3.Exceptions.MapFormatException;
import com.github.propra13.gruppeA3.Map.Link;
import com.github.propra13.gruppeA3.Map.Map;
import com.github.propra13.gruppeA3.Map.Trigger;
import com.github.propra13.gruppeA3.Menu.MenuStart;
import com.github.propra13.gruppeA3.Menu.MenuStart.GameStatus;

/** @author CK
 * Hauptklasse des Map-Editors
 */
public class Editor extends JTabbedPane {
  
	private static final long serialVersionUID = 1L;
	
	public static Editor editor;
	
	protected LinkWindow linkEditor;
	
	/** Falls der nächste Klick ein aus einem Dialog hervorgegangener
	 *  spezieller Auswahlklick ist (zB für den Link-Dialog)
	 */
	public enum ChooseClickType {LINK,CHECKPOINTFIELD,CHECKPOINTLINK,NONE}
	public ChooseClickType chooseClick;
	
	private String mapName;
	@SuppressWarnings("unused")
	private LinkedList<JPanel> roomTabs = new LinkedList<JPanel>();
	public LinkedList<Link> mapLinks = new LinkedList<Link>();
	public LinkedList<Trigger> mapTriggers = new LinkedList<Trigger>();

	/**
	 * Initialisiert Map und baut Tab-Fenster auf.
	 * @param mapName Verzeichnisname der Map, die bearbeitet werden soll
	 */
	public Editor(String mapName) {
		super(JTabbedPane.TOP);
		
		editor = this;
		this.mapName = mapName;
		linkEditor = new LinkWindow();
		
		// init Map
		try {
			Map.initialize(this.mapName);
		} catch (MapFormatException | IOException | InvalidRoomLinkException e) {
			e.printStackTrace();
		}
		
		//Menü-Tab
		addTab("Menü", new Menu(this));
		
		//Raum-Tabs
		for(int i=0; i < Map.mapRooms.length; i++) {
			String title = ""+i;
			addTab(title, new RoomTab(Map.getRoom(i)));
		}
		setSelectedIndex(1);
		repaint();
		setVisible(true);
	}
	
	/** Beendet den Editor und holt Hauptmenü wieder aus der Versenkung
	 */
	public void exit() {
		editor = null;
		MenuStart.gameStatus = GameStatus.MAINMENU;
		setVisible(false);
		Game.Menu.remove(this);
		Game.Menu.setVisible(true);
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
} 